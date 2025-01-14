package com.cclinux.framework.core.domain;

import cn.hutool.core.util.ObjectUtil;
import com.cclinux.framework.constants.AppCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

public class ApiResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;


    public ApiResult() {
        put("code", AppCode.SUCC);
        put("msg", "success");
        put("data", null);
    }


    public ApiResult(int code, String msg) {
        put("code", code);
        put("msg", msg);
        put("data", null);
    }


    public ApiResult(int code, String msg, Object data) {
        put("code", code);
        put("msg", msg);
        if (ObjectUtil.isNotNull(data)) {
            put("data", data);
        } else
            put("data", null);
    }

    public static ApiResult success() {
        return new ApiResult();
    }

    public static ApiResult success(String data) {
        ApiResult ret = new ApiResult();
        ret.put("code", AppCode.SUCC);
        ret.put("msg", "success");
        ret.put("data", data);
        return ret;
    }

    public static ApiResult success(Object data) {
        return ApiResult.success("success", data);
    }

    public static ApiResult success(String msg, Object data) {
        return new ApiResult(AppCode.SUCC, msg, data);
    }


    public static ApiResult success(Map<String, Object> map) {
        ApiResult ret = new ApiResult();

        if (ObjectUtil.isNotNull(map)) {
            ret.put("data", map);
        } else
            ret.put("data", null);

        return ret;
    }

    public static ApiResult error() {
        return error(AppCode.ERROR_SVR, "系统故障，请稍后再试");
    }

    public static ApiResult error(String msg) {
        return error(AppCode.ERROR_SVR, msg);
    }

    public static ApiResult error(int code, String msg) {
        ApiResult ret = new ApiResult();
        ret.put("code", code);
        ret.put("msg", msg);
        ret.put("data", null);
        return ret;
    }


    public ApiResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
