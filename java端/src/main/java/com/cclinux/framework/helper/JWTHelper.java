package com.cclinux.framework.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.cclinux.framework.config.AppConfig;
import com.cclinux.framework.core.domain.LoginDTO;

import java.util.Date;
import java.util.Map;

public class JWTHelper {


    /**
     * 根据 map 生成 token 默认：HS265(HmacSHA256)算法, expire单位毫秒
     */
    public static String genToken(long id, String name, String sub, Long expire) {
        JWT jwt = JWT.create();

        // 设置携带数据
        jwt.setPayload("id", id);
        jwt.setPayload("name", name);

        jwt.setSubject(sub);

        // 设置密钥
        jwt.setKey(AppConfig.JWT_SECERT.getBytes());

        // 设置过期时间
        jwt.setExpiresAt(new Date(System.currentTimeMillis() + expire * 1000));

        return jwt.sign();
    }

    public static String genToken(Map<String, Object> map, String sub, Long expire) {
        JWT jwt = JWT.create();


        // 设置携带数据
        map.forEach(jwt::setPayload);

        jwt.setSubject(sub);

        // 设置密钥
        jwt.setKey(AppConfig.JWT_SECERT.getBytes());

        // 设置过期时间
        jwt.setExpiresAt(new Date(System.currentTimeMillis() + expire * 1000));

        return jwt.sign();
    }


    public static boolean check(String token) {
        if (StrUtil.isEmpty(token)) return false;
        try {
            return JWT.of(token).setKey(AppConfig.JWT_SECERT.getBytes()).validate(0);
        } catch (Exception ex) {
            return false;
        }
    }

    public static JSONObject getTokenData(String token) {

        JWT jwt = JWTUtil.parseToken(token).setKey(AppConfig.JWT_SECERT.getBytes());


        JSONObject payloads = jwt.getPayloads();


        return payloads;
    }

    public static LoginDTO getToken(String token) {

        JWT jwt = JWTUtil.parseToken(token).setKey(AppConfig.JWT_SECERT.getBytes());


        JSONObject payloads = jwt.getPayloads();

        LoginDTO dto = new LoginDTO();
        dto.setId(payloads.get("id", Long.class));
        dto.setName(payloads.get("name", String.class));

        return dto;
    }

    public static String getSub(String token) {
        JSONObject ret = getTokenData(token);
        if (ObjectUtil.isEmpty(ret) || !ret.containsKey("sub")) return "";

        String sub = (String) ret.get("sub");
        if (StrUtil.isEmpty(sub)) return "";
        return sub;
    }
}
