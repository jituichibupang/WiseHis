package com.cclinux.framework.helper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Date;

import java.util.Map;

/**
 * @Notes: 时间处理 时间戳均为毫秒
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/12 21:39
 * @Ver: ccminicloud-framework 3.2.1
 */


public class TimeHelper {

    public static String getNowTime(String fmt) {
        Date currentDate = DateUtil.date();
        if (StrUtil.isEmpty(fmt)) fmt = "yyyyMMddHHmmss";
        return DateUtil.format(currentDate, fmt);
    }

    // 获取当前时间戳 , step:时间步长 (秒)
    public static long timestamp(long step) {
        return System.currentTimeMillis() + step * 1000;
    }

    public static long timestamp() {
        return timestamp(0);
    }

    // 获取当前时间字符串, step:时间步长 (秒)
    // 解析日期时间字符串，格式支持：
    //  yyyy-MM-dd HH:mm:ss
    //  yyyy/MM/dd HH:mm:ss
    //  yyyy.MM.dd HH:mm:ss
    public static String time(String fmt, long step) {
        return DateUtil.format(DateUtil.date(System.currentTimeMillis() + step * 1000), fmt);
    }

    public static String time(String fmt) {
        return time(fmt, 0);
    }

    // 时间戳转时间 （按格式）
    public static String timestamp2Time(long timestamp, String fmt) {
        return DateUtil.format(DateUtil.date(timestamp), fmt);
    }

    // 时间戳转时间 （默认格式）
    public static String timestamp2Time(long timestamp) {
        return timestamp2Time(timestamp, "yyyy-MM-dd HH:mm:ss");
    }


    // 时间转时间戳
    public static long time2Timestamp(String time) {
        return DateUtil.parse(time).getTime();
    }

    public static void db2Time(Map<String, Object> record, String field) {
        db2Time(record, field, "yyyy-MM-dd HH:mm:ss");
    }

    // 对时间格式处理
    public static String fmtTime(String time, String fmt) {
        long timestamp = time2Timestamp(time);
        return timestamp2Time(timestamp, fmt);
    }

    // 对时间格式处理
    public static void fmtDBTime(Map<String, Object> record, String field, String fmt) {
        if (ObjectUtil.isEmpty(record)) return;
        if (!record.containsKey(field)) return;

        String time = MapUtil.getStr(record, field, field);

        record.replace(field, fmtTime(time, fmt));
    }

    // 数据列表里的时间转换
    public static void db2Time(Map<String, Object> record, String field, String fmt) {
        if (ObjectUtil.isEmpty(record)) return;

        if (!record.containsKey(field)) return;

        long timestamp = MapUtil.getLong(record, field, -1L);
        if (ObjectUtil.isNull(timestamp)) return;
        if (NumberUtil.equals(timestamp, -1L)) return;

        record.replace(field, timestamp2Time(timestamp, fmt));
    }

    //  根据时间戳获取那天的零点
    public static long getDayFirstTimestamp(long timestamp) {
        if (NumberUtil.equals(timestamp, 0)) timestamp = timestamp();
        return time2Timestamp(timestamp2Time(timestamp, "yyyy-MM-dd"));
    }

    public static long getDayFirstTimestamp() {
        return getDayFirstTimestamp(0);
    }

    // 获取某天所在某月第一天时间戳
    public static long getMonthFirstTimestamp(long timestamp) {
        if (NumberUtil.equals(timestamp, 0)) timestamp = timestamp();

        Date d = DateUtil.parse(timestamp2Time(timestamp, "yyyy-MM-dd"));
        Date firstDayOfCurrentMonth = DateUtil.beginOfMonth(d);

        return firstDayOfCurrentMonth.getTime();
    }

    public static long getMonthFirstTimestamp() {
        return getMonthFirstTimestamp(0);
    }


    // 获取某天所在某月最后一天时间戳
    public static long getMonthLastTimestamp(long timestamp) {
        if (NumberUtil.equals(timestamp, 0)) timestamp = timestamp();

        Date d = DateUtil.parse(timestamp2Time(timestamp, "yyyy-MM-dd"));
        Date lastDayOfCurrentMonth = DateUtil.endOfMonth(d);

        return lastDayOfCurrentMonth.getTime();

    }

    public static long getMonthLastTimestamp() {
        return getMonthLastTimestamp(0);
    }
}
