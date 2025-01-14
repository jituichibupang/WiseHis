package com.cclinux.framework.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cclinux.framework.config.AppConfig;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Notes: 自定义表单对象处理
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/2 15:41
 * @Ver: ccminicloud-framework 3.2.1
 */


public class FormHelper {

    // 数据库取出某一数组字段转换为Form表单数组
    public static void db2Forms(Map<String, Object> record, String field) {
        if (ObjectUtil.isEmpty(record)) return;

        Object obj = record.get(field);
        if (ObjectUtil.isEmpty(obj)) return;

        String jsonStr = obj.toString();

        // 数据处记录处理
        record.replace(field, jsonStr2Obj(jsonStr));
    }


    // json字符串转为 JSON对象
    public static JSON jsonStr2Obj(String jsonStr) {
        return JSONUtil.parse(jsonStr);
    }


    //  前端对象，数组(包括对象数组)转为json字符串
    public static String obj2JsonStr(Object obj) {

        String jsonStr = JSONUtil.toJsonStr(obj);

        return jsonStr;
    }

    // form表单转为对象的json字符串
    public static String form2ObjJsonStr(Object obj) {
        JSONArray jsonArr = JSONUtil.parseArray(obj);

        JSONObject json = new JSONObject();

        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject jsonObject = jsonArr.getJSONObject(i);
            String name = jsonObject.getStr("mark");
            Object val = jsonObject.get("val");
            json.set(name, val);
        }

        String jsonStr = JSONUtil.toJsonStr(json);

        return jsonStr;

    }

    public static String form2ObjJsonStr(String jsonStr) {
        Object obj = jsonStr2Obj(jsonStr);
        JSONArray jsonArr = JSONUtil.parseArray(obj);

        JSONObject json = new JSONObject();

        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject jsonObject = jsonArr.getJSONObject(i);
            String name = jsonObject.getStr("mark");
            Object val = jsonObject.get("val");
            json.set(name, val);
        }

        return JSONUtil.toJsonStr(json);

    }

    // 删除某个字段
    public static void removeField(Map<String, Object> record, String field) {
        MapUtil.removeAny(record, field);
    }

    // 格式化对象结点,移除某个对象字段里的某些json节点
    public static void fmtDBObjExclude(Map<String, Object> record, String objField, String delChilds) {
        Object obj = record.get(objField);
        if (ObjectUtil.isEmpty(obj)) return;

        JSONObject json = JSONUtil.parseObj(obj);

        String[] arr = delChilds.split(",");
        for (String child : arr) {
            json.remove(child);
        }

        record.replace(objField, json);
    }

    // 格式化对象结点
    public static void fmtDBObj(Map<String, Object> record, String objField, String includeFields
    ) {
        Object obj = record.get(objField);
        if (ObjectUtil.isEmpty(obj)) return;

        JSONObject json = JSONUtil.parseObj(obj);

        if (includeFields.equals("*")) {
            record.replace(objField, json);
            return;
        }

        String[] includeArr = includeFields.split(",");


        Set<String> keys = json.keySet();
        Set<String> copiedSet = new HashSet<>(keys); // 深拷贝

        for (String key : copiedSet) {
            if (!ArrayUtil.contains(includeArr, key)) {
                json.remove(key);
            }
        }
        ;

        record.replace(objField, json);
    }


    public static void fmtDBObj(Map<String, Object> record, String objField) {
        fmtDBObj(record, objField, "*");
    }


    // 计算分页的页数
    public static  int calcPageCount(long total, int size) {
        int ret = (int) Math.ceil(Convert.toDouble(total) / size);
        return ret;
    }
}
