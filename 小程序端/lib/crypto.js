/*!
 * Crypto-JS v1.1.0
 * http://code.google.com/p/crypto-js/
 * Copyright (c) 2009, Jeff Mott. All rights reserved.
 * http://code.google.com/p/crypto-js/wiki/License
 */

var base64map = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

// Global Crypto object
const Crypto = {};

// Crypto utilities
Crypto.util = {
  // Bit-wise rotate left
  rotl: function(n, b) {
    return (n << b) | (n >>> (32 - b));
  },

  // Bit-wise rotate right
  rotr: function(n, b) {
    return (n << (32 - b)) | (n >>> b);
  },

  // Swap big-endian to little-endian and vice versa
  endian: function(n) {

    // If number given, swap endian
    if (n.constructor == Number) {
      return util.rotl(n, 8) & 0x00FF00FF |
        util.rotl(n, 24) & 0xFF00FF00;
    }

    // Else, assume array and swap all items
    for (var i = 0; i < n.length; i++)
      n[i] = util.endian(n[i]);
    return n;

  },

  // Generate an array of any length of random bytes
  randomBytes: function(n) {
    for (var bytes = []; n > 0; n--)
      bytes.push(Math.floor(Math.random() * 256));
    return bytes;
  },

  // Convert a string to a byte array
  stringToBytes: function(str) {
    var bytes = [];
    for (var i = 0; i < str.length; i++)
      bytes.push(str.charCodeAt(i));
    return bytes;
  },

  // Convert a byte array to a string
  bytesToString: function(bytes) {
    var str = [];
    for (var i = 0; i < bytes.length; i++)
      str.push(String.fromCharCode(bytes[i]));
    return str.join("");
  },

  // Convert a string to big-endian 32-bit words
  stringToWords: function(str) {
    var words = [];
    for (var c = 0, b = 0; c < str.length; c++, b += 8)
      words[b >>> 5] |= str.charCodeAt(c) << (24 - b % 32);
    return words;
  },

  // Convert a byte array to big-endian 32-bits words
  bytesToWords: function(bytes) {
    var words = [];
    for (var i = 0, b = 0; i < bytes.length; i++, b += 8)
      words[b >>> 5] |= bytes[i] << (24 - b % 32);
    return words;
  },

  // Convert big-endian 32-bit words to a byte array
  wordsToBytes: function(words) {
    var bytes = [];
    for (var b = 0; b < words.length * 32; b += 8)
      bytes.push((words[b >>> 5] >>> (24 - b % 32)) & 0xFF);
    return bytes;
  },

  // Convert a byte array to a hex string
  bytesToHex: function(bytes) {
    var hex = [];
    for (var i = 0; i < bytes.length; i++) {
      hex.push((bytes[i] >>> 4).toString(16));
      hex.push((bytes[i] & 0xF).toString(16));
    }
    return hex.join("");
  },

  // Convert a hex string to a byte array
  hexToBytes: function(hex) {
    var bytes = [];
    for (var c = 0; c < hex.length; c += 2)
      bytes.push(parseInt(hex.substr(c, 2), 16));
    return bytes;
  },

  // Convert a byte array to a base-64 string
  bytesToBase64: function(bytes) {

    // Use browser-native function if it exists
    if (typeof btoa == "function") return btoa(util.bytesToString(bytes));

    var base64 = [],
      overflow;

    for (var i = 0; i < bytes.length; i++) {
      switch (i % 3) {
        case 0:
          base64.push(base64map.charAt(bytes[i] >>> 2));
          overflow = (bytes[i] & 0x3) << 4;
          break;
        case 1:
          base64.push(base64map.charAt(overflow | (bytes[i] >>> 4)));
          overflow = (bytes[i] & 0xF) << 2;
          break;
        case 2:
          base64.push(base64map.charAt(overflow | (bytes[i] >>> 6)));
          base64.push(base64map.charAt(bytes[i] & 0x3F));
          overflow = -1;
      }
    }

    // Encode overflow bits, if there are any
    if (overflow != undefined && overflow != -1)
      base64.push(base64map.charAt(overflow));

    // Add padding
    while (base64.length % 4 != 0) base64.push("=");

    return base64.join("");

  },
  // Convert a base-64 string to a byte array
  base64ToBytes: function(base64) {

    // Use browser-native function if it exists
    if (typeof atob == "function") return util.stringToBytes(atob(base64));

    // Remove non-base-64 characters
    base64 = base64.replace(/[^A-Z0-9+\/]/ig, "");

    var bytes = [];

    for (var i = 0; i < base64.length; i++) {
      switch (i % 4) {
        case 1:
          bytes.push((base64map.indexOf(base64.charAt(i - 1)) << 2) |
            (base64map.indexOf(base64.charAt(i)) >>> 4));
          break;
        case 2:
          bytes.push(((base64map.indexOf(base64.charAt(i - 1)) & 0xF) << 4) |
            (base64map.indexOf(base64.charAt(i)) >>> 2));
          break;
        case 3:
          bytes.push(((base64map.indexOf(base64.charAt(i - 1)) & 0x3) << 6) |
            (base64map.indexOf(base64.charAt(i))));
          break;
      }
    }

    return bytes;

  },
  HMAC: function(hasher, message, key, options) {

    // Allow arbitrary length keys
    key = key.length > 16 * 4 ?
      hasher(key, {
        asBytes: true
      }) :
      Crypto.util.stringToBytes(key);

    // XOR keys with pad constants
    var okey = key,
      ikey = key.slice(0);
    for (var i = 0; i < 16 * 4; i++) {
      okey[i] ^= 0x5C;
      ikey[i] ^= 0x36;
    }

    var hmacbytes = hasher(Crypto.util.bytesToString(okey) +
      hasher(Crypto.util.bytesToString(ikey) + message, {
        asString: true
      }), {
        asBytes: true
      });
    return options && options.asBytes ? hmacbytes :
      options && options.asString ? Crypto.util.bytesToString(hmacbytes) :
      Crypto.util.bytesToHex(hmacbytes);

  },
  sha11: function(k) {
    var u = Crypto.util.stringToWords(k),
      v = k.length * 8,
      o = [],
      q = 1732584193,
      p = -271733879,
      h = -1732584194,
      g = 271733878,
      f = -1009589776;
    u[v >> 5] |= 128 << (24 - v % 32);
    u[((v + 64 >>> 9) << 4) + 15] = v;
    for (var y = 0; y < u.length; y += 16) {
      var D = q,
        C = p,
        B = h,
        A = g,
        z = f;
      for (var x = 0; x < 80; x++) {
        if (x < 16) {
          o[x] = u[y + x]
        } else {
          var s = o[x - 3] ^ o[x - 8] ^ o[x - 14] ^ o[x - 16];
          o[x] = (s << 1) | (s >>> 31)
        }
        var r = ((q << 5) | (q >>> 27)) + f + (o[x] >>> 0) + (x < 20 ? (p & h | ~p & g) + 1518500249 : x < 40 ? (p ^ h ^ g) + 1859775393 : x < 60 ? (p & h | p & g | h & g) - 1894007588 : (p ^ h ^ g) - 899497514);
        f = g;
        g = h;
        h = (p << 30) | (p >>> 2);
        p = q;
        q = r
      }
      q += D;
      p += C;
      h += B;
      g += A;
      f += z
    }
    return [q, p, h, g, f]
  },
  SHA1: function(e, c) {
    var d = Crypto.util.wordsToBytes(Crypto.util.sha11(e));
    return c && c.asBytes ? d : c && c.asString ? Crypto.util.bytesToString(d) : Crypto.util.bytesToHex(d)
  }

};

// Crypto mode namespace
Crypto.mode = {};

module.exports = Crypto;