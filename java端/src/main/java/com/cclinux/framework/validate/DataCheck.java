package com.cclinux.framework.validate;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.cclinux.framework.constants.AppCode;
import com.cclinux.framework.exception.AppException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Notes: 数据校验
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/2/18 7:32
 * @Ver: ccminicloud-framework 3.2.1
 * @Desc: 数字型支持前端传入字符型，后端转换为数字;
 * Money为字符型，支持前端数字转为字符；
 * 其他类型暂不支持
 */


public class DataCheck {

    static public void checkMust(Object value, String desc) {
        if (ObjectUtil.isEmpty(value)) throw new AppException(desc + "不能为空", AppCode.ERROR_DATA);
    }

    static public void checkStr(Object value, String desc) {
        if (value.getClass() != String.class) throw new AppException(desc + "类型错误(str)", AppCode.ERROR_DATA);
    }

    static public void checkBool(Object value, String desc) {
        if (value.getClass() != Boolean.class) throw new AppException(desc + "类型错误(bool)", AppCode.ERROR_DATA);
    }


    // 小数
    static public void checkDouble(Object value, String desc) {
        if (NumberUtil.isNumber(value.toString()))
            value = Convert.toDouble(value);

        if (value.getClass() != Double.class)
            throw new AppException(desc + "必须为整数或者小数格式"
                    , AppCode.ERROR_DATA);

    }


    // 正整数
    static public void checkLong(Object value, String desc) {
        if (NumberUtil.isLong(value.toString()))
            value = Convert.toLong(value);

        if (value.getClass() != Long.class)
            throw new AppException(desc + "必须为长整数"
                    , AppCode.ERROR_DATA);

    }


    // 正整数，<=2147483647
    static public void checkInt(Object value, String desc) {
        if (NumberUtil.isInteger(value.toString()))
            value = Convert.toInt(value);

        if (value.getClass() != Integer.class)
            throw new AppException(desc + "必须为正整数"
                    , AppCode.ERROR_DATA);

        Integer v = Convert.toInt(value);
        if (v < 0) throw new AppException(desc + "必须为正整数"
                , AppCode.ERROR_DATA);
    }

    static public void checkArr(Object value, String desc) {
        if (value.getClass() != ArrayList.class) throw new AppException(desc + "类型错误(arr)", AppCode.ERROR_DATA);
    }

    static public void checkObj(Object value, String desc) {
        if (value.getClass() != Object.class) throw new AppException(desc + "类型错误(obj)", AppCode.ERROR_DATA);
    }


    static public void checkMin(Object value, int min, String desc) {
        if (ObjectUtil.isEmpty(value)) return;

        if (value.getClass() == String.class && ObjectUtil.length(value) < min)
            throw new AppException(desc + "不能小于" + min + "位",
                    AppCode.ERROR_DATA);
        else if (value.getClass() == ArrayList.class && ObjectUtil.length(value) < min)
            throw new AppException(desc + "不能小于" + min + "项",
                    AppCode.ERROR_DATA);
        else if (value.getClass() == Integer.class && Convert.toInt(value) < min)
            throw new AppException(desc + "不能小于" + min,
                    AppCode.ERROR_DATA);
        else if (value.getClass() == Long.class && Convert.toLong(value) < min)
            throw new AppException(desc + "不能小于" + min,
                    AppCode.ERROR_DATA);
    }

    static public void checkMax(Object value, int max, String desc) {
        if (ObjectUtil.isEmpty(value)) return;

        if (value.getClass() == String.class && ObjectUtil.length(value) > max)
            throw new AppException(desc + "不能大于" + max + "位",
                    AppCode.ERROR_DATA);
        else if (value.getClass() == ArrayList.class && ObjectUtil.length(value) > max)
            throw new AppException(desc + "不能大于" + max + "项",
                    AppCode.ERROR_DATA);
        else if (value.getClass() == Integer.class && Convert.toInt(value) > max)
            throw new AppException(desc + "不能大于" + max,
                    AppCode.ERROR_DATA);
        else if (value.getClass() == Long.class && Convert.toLong(value) > max)
            throw new AppException(desc + "不能大于" + max,
                    AppCode.ERROR_DATA);
    }

    static public void checkLen(Object value, int len, String desc) {
        if (ObjectUtil.isEmpty(value)) return;

        if (value.getClass() == String.class && ObjectUtil.length(value) != len)
            throw new AppException(desc + "必须为" + len + "位",
                    AppCode.ERROR_DATA);
        else if (value.getClass() == ArrayList.class && ObjectUtil.length(value) != len)
            throw new AppException(desc + "必须为" + len + "项",
                    AppCode.ERROR_DATA);
        else if (value.getClass() == Integer.class && Convert.toInt(value) != len)
            throw new AppException(desc + "必须为" + len,
                    AppCode.ERROR_DATA);
        else if (value.getClass() == Long.class && Convert.toLong(value) != len)
            throw new AppException(desc + "必须为" + len,
                    AppCode.ERROR_DATA);
    }

    // 金额暂时作为Str
    static public void checkMoney(Object value, String desc) {
        if (ObjectUtil.isNull(value)) return;

        String val = Convert.toStr(value);
        if (!Validator.isMatchRegex("(^[1-9]([0-9]+)?(\\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\\.[0-9]([0-9])?$)", val))
            throw new AppException(desc + "必须为金额格式(示例2.01)", AppCode.ERROR_DATA);
    }

    static public void checkEmail(Object value, String desc) {
        if (ObjectUtil.isNull(value)) return;

        String val = Convert.toStr(value);
        if (StrUtil.isEmpty(val)) return;

        if (!Validator.isEmail(val)) throw new AppException(desc + "必须为邮箱格式", AppCode.ERROR_DATA);
    }

    static public void checkMobile(Object value, String desc) {
        if (ObjectUtil.isNull(value)) return;

        String val = Convert.toStr(value);
        if (StrUtil.isEmpty(val)) return;

        if (!Validator.isMatchRegex("(^1[1|2|3|4|5|6|7|8|9][0-9]{9}$)", val))
            throw new AppException(desc + "格式不正确",
                    AppCode.ERROR_DATA);
    }

    static public void checkLetter(Object value, String desc) {
        if (ObjectUtil.isNull(value)) return;

        String val = Convert.toStr(value);
        if (StrUtil.isEmpty(val)) return;

        if (!Validator.isMatchRegex("^[A-Za-z]+$", val)) throw new AppException(desc + "必须为字母",
                AppCode.ERROR_DATA);
    }

    static public void checkLetterNum(Object value, String desc) {
        if (ObjectUtil.isNull(value)) return;

        String val = Convert.toStr(value);
        if (StrUtil.isEmpty(val)) return;

        if (!Validator.isMatchRegex("^\\w+$", val)) throw new AppException(desc + "必须为字母，数字和下划线",
                AppCode.ERROR_DATA);
    }

    static public void checkDate(Object value, String desc) {
        if (ObjectUtil.isNull(value)) return;

        String val = Convert.toStr(value);
        if (StrUtil.isEmpty(val)) return;

        if (!Validator.isMatchRegex("^[1-9]\\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[12]\\d|3[01])$", val))
            throw new AppException(desc + "格式错误" +
                    "(示例2022-01-12)",
                    AppCode.ERROR_DATA);
    }

    // 短时间(时分)
    static public void checkHourMinute(Object value, String desc) {
        if (ObjectUtil.isNull(value)) return;

        String val = Convert.toStr(value);
        if (StrUtil.isEmpty(val)) return;

        if (!Validator.isMatchRegex("^([01]\\d|2[0-3]):([0-5]\\d)$", val))
            throw new AppException(desc + "格式错误" +
                    "(示例13:03)",
                    AppCode.ERROR_DATA);
    }


    // 时间(时分秒)
    static public void checkTime(Object value, String desc) {
        if (ObjectUtil.isNull(value)) return;

        String val = Convert.toStr(value);
        if (StrUtil.isEmpty(val)) return;

        if (!Validator.isMatchRegex("^([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$", val))
            throw new AppException(desc + "格式错误" +
                    "(示例13:03:59)",
                    AppCode.ERROR_DATA);
    }

    // 日期+时间
    static public void checkDateTime(Object value, String desc) {
        if (ObjectUtil.isNull(value)) return;

        String val = Convert.toStr(value);
        if (StrUtil.isEmpty(val)) return;

        if (!Validator.isMatchRegex("^[1-9]\\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[12]\\d|3[01])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$", val))
            throw new AppException(desc + "格式错误" +
                    "(示例2008-07-22 13:04:06)",
                    AppCode.ERROR_DATA);
    }


    static public void check(Map<String, Object> data, String[] rules) {
        Map<String, Object> returnData = new HashMap<>();


        for (String line : rules) {
            if (!line.contains(":")) throw new AppException("数据校验未定义KEY : " + line, AppCode.ERROR_DATA);

            String[] arr = line.split(":", 2);

            String formName = arr[0].trim();  // 表单名
            if (formName.isEmpty()) throw new AppException("数据校验未定义formName: " + line, AppCode.ERROR_DATA);


            String[] arrRule = arr[1].split("\\|");

            String desc = arr[0].trim();  //表单说明
            Object defVal = null; //缺省值
            String dataType = "";

            // 小循环获取规则
            for (String node : arrRule) {


                // 数据项说明
                if (node.startsWith("name=")) {
                    desc = "「" + node.replace("name=", "") + "」";
                    continue;
                }

                // 缺省值
                if (node.startsWith("default=")) {
                    defVal = node.replace("default=", "").trim();
                    continue;
                }

                String firstName = node.split(":")[0].toLowerCase(); //规则名

                // 缺省值数据类型转换
                switch (firstName) {

                    case "integer":
                    case "int": {
                        dataType = "Integer";
                        break;
                    }
                    case "double": {
                        dataType = "Double";
                        break;
                    }
                    case "id":
                    case "long": {
                        dataType = "Long";
                        break;
                    }
                    case "time":
                    case "datetime":
                    case "hourtime":
                    case "date":
                    case "mobile":
                    case "email":
                    case "letter":
                    case "letter_num":
                    case "money":
                    case "str":
                    case "string": {
                        dataType = "String";
                        break;
                    }
                    case "bool":
                    case "boolean": {
                        dataType = "Boolean";
                        break;
                    }
                    case "arr":
                    case "array": {
                        dataType = "Array";
                        break;
                    }
                    case "obj":
                    case "object": {
                        dataType = "Object";
                        break;
                    }
                }
            }

            if (StrUtil.isEmpty(dataType)) throw new AppException("数据校验未定义type : " + line, AppCode.ERROR_DATA);

            // 取值
            Object val = null;
            if (data.containsKey(formName)) val = data.get(formName);

            try {
                // 缺省值数据类型转换
                switch (dataType) {
                    case "Double": { // 小数
                        if (defVal != null) defVal = Convert.toDouble(defVal);
                        break;
                    }
                    case "Long": { // 长整数
                        if (defVal != null) defVal = Convert.toLong(defVal);
                        break;
                    }
                    case "Integer": { // 正整数
                        if (defVal != null) defVal = Convert.toInt(defVal);
                        break;
                    }
                    case "String": {
                        if (defVal != null) defVal = Convert.toStr(defVal);

                        // 数值型作为字符型处理
                        if (val != null && (val.getClass() == Integer.class || val.getClass() == Long.class || val.getClass() == Double.class))
                            val = Convert.toStr(val);

                        break;
                    }
                    case "Boolean": {
                        if (defVal != null && defVal.equals("true")) defVal = true;
                        else if (defVal != null && defVal.equals("false"))
                            defVal = false;
                        else
                            defVal = null;
                        break;
                    }
                    case "Array": {
                        if (defVal != null) defVal = Convert.convert(ArrayList.class, defVal);
                        break;
                    }
                    case "Object": {
                        if (defVal != null) defVal = Convert.convert(Object.class, defVal);
                        break;
                    }
                }
            } catch (Exception ex) {
                throw new AppException("数据校验缺省值错误 : " + line, AppCode.ERROR_DATA);
            }


            // 空且有缺省值
            if (val == null && defVal != null) val = defVal;

            // *** 校验
            for (String node : arrRule) {

                String[] detailArr = node.split("="); //min,max,len的附加参数
                String ruleName = detailArr[0].toLowerCase(); //规则名


                // 空且非必填的 不校验
                if (!ruleName.equals("must") && val == null) continue;

                switch (ruleName) {
                    case "must": {
                        checkMust(val, desc);
                        break;
                    }
                    case "str":
                    case "string": {
                        checkStr(val, desc);
                        break;
                    }
                    case "arr":
                    case "array": {
                        checkArr(val, desc);
                        break;
                    }
                    case "bool":
                    case "boolean": {
                        checkBool(val, desc);
                        break;
                    }
                    case "double": {
                        checkDouble(val, desc);
                        val = Convert.toDouble(val);
                        break;
                    }
                    case "integer":
                    case "int": {
                        checkInt(val, desc);
                        val = Convert.toInt(val);
                        break;
                    }
                    case "id":
                    case "long": {
                        checkLong(val, desc);
                        val = Convert.toLong(val);
                        break;
                    }
                    case "min": {
                        checkMin(val, Convert.toInt(detailArr[1]), desc);
                        break;
                    }
                    case "max": {
                        checkMax(val, Convert.toInt(detailArr[1]), desc);
                        break;
                    }
                    case "len": {
                        checkLen(val, Convert.toInt(detailArr[1]), desc);
                        break;
                    }
                    case "money": {
                        checkMoney(val, desc);
                        break;
                    }
                    case "mobile": {
                        checkMobile(val, desc);
                        break;
                    }
                    case "email": {
                        checkEmail(val, desc);
                        break;
                    }
                    case "letter": {
                        checkLetter(val, desc);
                        break;
                    }
                    case "letter_num": {
                        checkLetterNum(val, desc);
                        break;
                    }
                    case "time": {
                        checkTime(val, desc);
                        break;
                    }
                    case "hourtime": {
                        checkHourMinute(val, desc);
                        break;
                    }
                    case "datetime": {
                        checkDateTime(val, desc);
                        break;
                    }
                    case "date": {
                        checkDate(val, desc);
                        break;
                    }


                }

                // 数值回填
                returnData.put(formName, val);

            }
        }


        for (String line : rules) {
            if (!line.contains(":")) throw new AppException("数据校验未定义KEY : " + line, AppCode.ERROR_DATA);

            String[] arr = line.split(":", 2);

            String formName = arr[0].trim();  // 表单名
            if (formName.isEmpty()) throw new AppException("数据校验未定义formName: " + line, AppCode.ERROR_DATA);


            String[] arrRule = arr[1].split("\\|");

            String desc = arr[0].trim();  //表单说明
            Object defVal = null; //缺省值
            String dataType = "";

            // 小循环获取规则
            for (String node : arrRule) {


                // 数据项说明
                if (node.startsWith("name=")) {
                    desc = "「" + node.replace("name=", "") + "」";
                    continue;
                }

                // 缺省值
                if (node.startsWith("default=")) {
                    defVal = node.replace("default=", "").trim();
                    continue;
                }

                String firstName = node.split(":")[0].toLowerCase(); //规则名

                // 缺省值数据类型转换
                switch (firstName) {

                    case "integer":
                    case "int": {
                        dataType = "Integer";
                        break;
                    }
                    case "double": {
                        dataType = "Double";
                        break;
                    }
                    case "id":
                    case "long": {
                        dataType = "Long";
                        break;
                    }
                    case "time":
                    case "datetime":
                    case "hourtime":
                    case "date":
                    case "mobile":
                    case "email":
                    case "letter":
                    case "letter_num":
                    case "money":
                    case "str":
                    case "string": {
                        dataType = "String";
                        break;
                    }
                    case "bool":
                    case "boolean": {
                        dataType = "Boolean";
                        break;
                    }
                    case "arr":
                    case "array": {
                        dataType = "Array";
                        break;
                    }
                    case "obj":
                    case "object": {
                        dataType = "Object";
                        break;
                    }
                }
            }

            if (StrUtil.isEmpty(dataType)) throw new AppException("数据校验未定义type : " + line, AppCode.ERROR_DATA);

            // 取值
            Object val = null;
            if (data.containsKey(formName)) val = data.get(formName);

            try {
                // 缺省值数据类型转换
                switch (dataType) {
                    case "Double": { // 小数
                        if (defVal != null) defVal = Convert.toDouble(defVal);
                        break;
                    }
                    case "Long": { // 长整数
                        if (defVal != null) defVal = Convert.toLong(defVal);
                        break;
                    }
                    case "Integer": { // 正整数
                        if (defVal != null) defVal = Convert.toInt(defVal);
                        break;
                    }
                    case "String": {
                        if (defVal != null) defVal = Convert.toStr(defVal);

                        // 数值型作为字符型处理
                        if (val != null && (val.getClass() == Integer.class || val.getClass() == Long.class || val.getClass() == Double.class))
                            val = Convert.toStr(val);

                        break;
                    }
                    case "Boolean": {
                        if (defVal != null && defVal.equals("true")) defVal = true;
                        else if (defVal != null && defVal.equals("false"))
                            defVal = false;
                        else
                            defVal = null;
                        break;
                    }
                    case "Array": {
                        if (defVal != null) defVal = Convert.convert(ArrayList.class, defVal);
                        break;
                    }
                    case "Object": {
                        if (defVal != null) defVal = Convert.convert(Object.class, defVal);
                        break;
                    }
                }
            } catch (Exception ex) {
                throw new AppException("数据校验缺省值错误 : " + line, AppCode.ERROR_DATA);
            }


            // 空且有缺省值
            if (val == null && defVal != null) val = defVal;

            // *** 校验
            for (String node : arrRule) {

                String[] detailArr = node.split("="); //min,max,len的附加参数
                String ruleName = detailArr[0].toLowerCase(); //规则名


                // 空且非必填的 不校验
                if (!ruleName.equals("must") && val == null) continue;

                switch (ruleName) {
                    case "must": {
                        checkMust(val, desc);
                        break;
                    }
                    case "str":
                    case "string": {
                        checkStr(val, desc);
                        break;
                    }
                    case "arr":
                    case "array": {
                        checkArr(val, desc);
                        break;
                    }
                    case "bool":
                    case "boolean": {
                        checkBool(val, desc);
                        break;
                    }
                    case "double": {
                        checkDouble(val, desc);
                        val = Convert.toDouble(val);
                        break;
                    }
                    case "integer":
                    case "int": {
                        checkInt(val, desc);
                        val = Convert.toInt(val);
                        break;
                    }
                    case "id":
                    case "long": {
                        checkLong(val, desc);
                        val = Convert.toLong(val);
                        break;
                    }
                    case "min": {
                        checkMin(val, Convert.toInt(detailArr[1]), desc);
                        break;
                    }
                    case "max": {
                        checkMax(val, Convert.toInt(detailArr[1]), desc);
                        break;
                    }
                    case "len": {
                        checkLen(val, Convert.toInt(detailArr[1]), desc);
                        break;
                    }
                    case "money": {
                        checkMoney(val, desc);
                        break;
                    }
                    case "mobile": {
                        checkMobile(val, desc);
                        break;
                    }
                    case "email": {
                        checkEmail(val, desc);
                        break;
                    }
                    case "letter": {
                        checkLetter(val, desc);
                        break;
                    }
                    case "letter_num": {
                        checkLetterNum(val, desc);
                        break;
                    }
                    case "time": {
                        checkTime(val, desc);
                        break;
                    }
                    case "hourtime": {
                        checkHourMinute(val, desc);
                        break;
                    }
                    case "datetime": {
                        checkDateTime(val, desc);
                        break;
                    }
                    case "date": {
                        checkDate(val, desc);
                        break;
                    }


                }


                returnData.put(formName, val);

            }
        }

        // 数据回填
        data.clear();
        for (String key : returnData.keySet()) {
            data.put(key, returnData.get(key));
        }

    }
}
