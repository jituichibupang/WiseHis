 /**
 * Notes: UGC内容校验
 * Ver : CCMiniCloud Framework 2.4.1 ALL RIGHTS RESERVED BY cclinux0730 (wechat)
 * Date: 2020-11-14 07:48:00 
 */

const cloudHelper = require('../helper/cloud_helper.js');
const pageHelper = require('../helper/page_helper.js');
const setting = require('../setting/setting.js');

/**
 * 图片类型校验
 * @param {*} fileName 
 * @param {*} type 
 */
function imgTypeCheck(path, type = ['jpg', 'jpeg', 'png','JPG','JPEG','PNG']) {
	let fmt = path.split(".")[(path.split(".")).length - 1];
	if (type.indexOf(fmt) > -1)
		return true;
	else
		return false;
}

/**
 * 图片大小校验
 * @param {*} size 
 * @param {*} maxSize 
 */
function imgSizeCheck(size, maxSize) {
	return size < maxSize;
}
 

module.exports = {  
	imgTypeCheck,
	imgSizeCheck
}