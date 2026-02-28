package com.cclinux.framework.helper;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ObjHelper {


    /***
     * 将对象转成Map
     */
    public static Map<String, Object> toMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) return map;

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();     // 获取类里面的所有的成员变量
        for (Field field : fields) {
            field.setAccessible(true);                  // 临时取消成员变量权限修饰符
            String fieldName = field.getName();         // 获取成员变量名
            Object fieldValue;                          // 定义成员变量值接收对象
            try {
                fieldValue = field.get(obj);            // 接收成员变量
            } catch (Exception e) {
                fieldValue = new Object();              // 接受失败将接受对象重置
            }
            map.put(fieldName, fieldValue);
        }
        return map;
    }

    /***
     * Map的Key值为注解TableField的值
     */
    public static Map<String, Object> objToMapByDbField(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) return map;

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();     // 获取类里面的所有的成员变量
        for (Field field : fields) {
            field.setAccessible(true);                  // 临时取消成员变量权限修饰符
            TableField annotation = field.getAnnotation(TableField.class);      // 属性的TableField注解
            String fieldName = ObjectUtil.isEmpty(annotation) ? field.getName() : annotation.value();   // 有注解用注解，否则使用变量名

            Object fieldValue;                          // 定义成员变量值接收对象
            try {
                fieldValue = field.get(obj);            // 接收成员变量
            } catch (Exception e) {
                fieldValue = new Object();              // 接受失败将接受对象重置
            }
            map.put(fieldName, fieldValue);
        }
        return map;
    }


    /***
     * 把对象的某些字段取出生成MAP
     */
    public static Map<String, Object> objToMapByFields(Object obj, String getFields) {
        String[] filter = {"serialVersionUID"};

        if (ObjectUtil.isNull(obj)) return null;

        boolean isGetFields = true;
        if (StrUtil.isEmpty(getFields)) {
            isGetFields = false;
            getFields = "";

        }
        String[] getFieldsArr = getFields.split(",");
        for (int k = 0; k < getFieldsArr.length; k++) {
            getFieldsArr[k] = getFieldsArr[k].trim();
        }

        Map<String, Object> map = new HashMap<>();

        // 本类
        Class<?> clazz = obj.getClass();
        Field[] objFields = clazz.getDeclaredFields();     // 获取类里面的所有的成员变量
        for (Field objField : objFields) {
            objField.setAccessible(true);                  // 临时取消成员变量权限修饰符
            TableField annotation = objField.getAnnotation(TableField.class);      // 属性的TableField注解

            if (annotation == null || !annotation.exist()) continue;

            String fieldName = ObjectUtil.isEmpty(annotation) ? objField.getName() : annotation.value();   // 有注解用注解，否则使用变量名
            String varName = objField.getName();

            Object fieldValue;                          // 定义成员变量值接收对象
            try {
                fieldValue = objField.get(obj);            // 接收成员变量
            } catch (Exception e) {
                fieldValue = new Object();              // 接受失败将接受对象重置
            }

            if (isGetFields && getFieldsArr.length > 0) {
                for (String str : getFieldsArr) {
                    if (str.equals(fieldName)) {
                        map.put(varName, fieldValue);
                        break;
                    }
                }
            } else {
                map.put(varName, fieldValue);
            }


        }

/*
        // 父类
        Class<?> parent = clazz.getSuperclass();
        if (parent != null) {
            Field[] objFieldsParent = parent.getDeclaredFields();     // 获取类里面的所有的成员变量
            for (Field objField : objFieldsParent) {
                objField.setAccessible(true);                  // 临时取消成员变量权限修饰符
                TableField annotation = objField.getAnnotation(TableField.class);      // 属性的TableField注解
                String fieldName = ObjectUtil.isEmpty(annotation) ? objField.getName() : annotation.value();   // 有注解用注解，否则使用变量名
                String varName = objField.getName();

                Object fieldValue;                          // 定义成员变量值接收对象
                try {
                    fieldValue = objField.get(obj);            // 接收成员变量
                } catch (Exception e) {
                    fieldValue = new Object();              // 接受失败将接受对象重置
                }

                if (isGetFields && getFieldsArr.length > 0) {
                    for (String str : getFieldsArr) {
                        if (str.equals(fieldName)) {
                            map.put(varName, fieldValue);
                            break;
                        }
                    }
                } else {
                    if (!Arrays.asList(filter).contains(varName))
                        map.put(varName, fieldValue);
                }

            }

        }
*/
        if (MapUtil.isNotEmpty(map))
            return map;
        else
            return null;
    }

}
