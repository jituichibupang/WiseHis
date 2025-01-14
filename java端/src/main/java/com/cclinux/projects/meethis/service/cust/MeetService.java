package com.cclinux.projects.meethis.service.cust;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclinux.framework.annotation.LoginIgnore;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.core.mapper.WhereJoin;
import com.cclinux.framework.exception.AppException;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.projects.meethis.mapper.MeetJoinMapper;
import com.cclinux.projects.meethis.mapper.MeetMapper;
import com.cclinux.projects.meethis.model.MeetJoinModel;
import com.cclinux.projects.meethis.model.MeetModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Notes: 预约模块业务逻辑
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/7 5:41
 * @Ver: ccminicloud-framework 3.2.1
 */

@Service("MeetHisMeetService")
public class MeetService extends BaseMyCustService {

    @Resource(name = "MeetHisMeetMapper")
    private MeetMapper meetMapper;

    @Resource(name = "MeetHisMeetJoinMapper")
    private MeetJoinMapper meetJoinMapper;

    /**
     * 找出未过期的日期段
     */
    public List<String> calcDays(String days) {
        List<String> dayList = new ArrayList();

        String today = TimeHelper.time("yyyy-MM-dd");

        JSONArray daysArr = JSONUtil.parseArray(days);
        for (Object t : daysArr) {
            JSONObject object = new JSONObject();

            String day = Convert.toStr(t);
            if (day.compareTo(today) < 0) continue;

            dayList.add(day);
        }

        return dayList;
    }

    /**
     * 预约列表
     */
    @LoginIgnore
    public PageResult getMeetList(PageParams pageRequest) {

        Where<MeetModel> where = new Where<>();
        where.eq("MEET_STATUS", MeetModel.STATUS.NORMAL);

        long cateId = pageRequest.getParamLong("cateId");
        if (NumberUtil.compare(cateId, 0) > 0)
            where.eq("MEET_CATE_ID", cateId);

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            where.and(wrapper -> {
                wrapper.or().like("MEET_TITLE", search);
            });
        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "cateId": {
                    where.eq("MEET_CATE_ID", Convert.toLong(sortVal));
                    break;
                }
                case "sort": {
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByAsc("MEET_ORDER");
        where.orderByDesc("MEET_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return meetMapper.getPageList(page, where, "MEET_DAYS,MEET_STATUS,MEET_ID,MEET_TITLE,MEET_OBJ," +
                " MEET_CATE_NAME,MEET_MAX_CNT");
    }

    /**
     * 取得我的预约详情
     */
    public Map<String, Object> getMyMeetJoinDetail(long userId, long meetJoinId) {
        Where<MeetJoinModel> where = new Where<>();
        where.eq("MEET_JOIN_ID", meetJoinId);
        where.eq("MEET_JOIN_USER_ID", userId);
        Map<String, Object> ret = meetJoinMapper.getOneMap(where);
        if (ObjectUtil.isEmpty(ret)) return ret;

        long meetId = MapUtil.getLong(ret, "meetJoinMeetId");
        ret.put("meet", meetMapper.getOneMap(meetId, "MEET_TITLE,MEET_START,MEET_END"));

        return ret;
    }


    /**
     * 我的预约预约列表
     */
    @LoginIgnore
    public PageResult getMyMeetJoinList(long userId, PageParams pageRequest) {

        WhereJoin<MeetJoinModel> where = new WhereJoin<>();
        where.leftJoin(MeetModel.class, MeetModel::getMeetId, MeetJoinModel::getMeetJoinMeetId);

        where.eq("t.MEET_JOIN_USER_ID", userId);

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            where.and(wrapper -> {
                wrapper.or().like("t.MEET_JOIN_CODE", search);
                wrapper.or().like("t1.MEET_TITLE", search);
            });
        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "sort": {
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByDesc("MEET_JOIN_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return meetJoinMapper.getPageJoinList(page, where, "t.*,t1.MEET_TITLE");
    }

    /**
     * 单个浏览
     */
    @LoginIgnore
    public Map<String, Object> view(long id, long userId) {

        // PV
        UpdateWhere<MeetModel> uw = new UpdateWhere<>();
        uw.eq("MEET_ID", id);
        meetMapper.inc(uw, "MEET_VIEW_CNT");

        Where<MeetModel> where = new Where<>();
        where.eq("MEET_ID", id);
        where.eq("MEET_STATUS", MeetModel.STATUS.NORMAL);

        Map<String, Object> ret = meetMapper.getOneMap(where);
        if (ObjectUtil.isNull((ret))) return null;


        String today = TimeHelper.time("yyyy-MM-dd");

        // 获得预约统计数据
        Where<MeetJoinModel> whereGroup = new Where<MeetJoinModel>();
        whereGroup.ge("MEET_JOIN_DAY", today);
        whereGroup.select("MEET_JOIN_TIME,MEET_JOIN_DAY,count(0) as total ");
        whereGroup.groupBy("MEET_JOIN_TIME,MEET_JOIN_DAY");

        List<Map<String, Object>> listGroup = meetJoinMapper.selectMaps(whereGroup);
        Map<String, Integer> statMap = new HashMap<>();
        for (Map<String, Object> t : listGroup) {
            String key = DigestUtil.md5Hex(t.get("MEET_JOIN_DAY").toString() + t.get("MEET_JOIN_TIME").toString());
            statMap.put(key, Convert.toInt(t.get("total")));
        }

        JSONArray daysArr = JSONUtil.parseArray(ret.get("meetDays"));
        JSONObject obj = JSONUtil.parseObj(ret.get("meetObj"));

        JSONArray timesArr = obj.getJSONArray("time");

        // 取得时段
        List<String> timesList = new ArrayList();
        for (Object t : timesArr) {
            JSONObject tt = JSONUtil.parseObj(t);
            timesList.add(tt.getStr("title"));
        }

        List<Map<String, Object>> list = new ArrayList<>();
        for (Object t : daysArr) {
            JSONObject object = new JSONObject();

            String day = Convert.toStr(t);
            if (day.compareTo(today) < 0) continue;

            object.set("label", day);

            JSONArray timesObjArr = new JSONArray();
            for (Object tx : timesList) {

                JSONObject timeObj = new JSONObject();
                timeObj.set("label", Convert.toStr(tx));
                String key = DigestUtil.md5Hex(Convert.toStr(t) + Convert.toStr(tx));
                if (statMap.containsKey(key))
                    timeObj.set("cnt", Convert.toInt(statMap.get(key)));
                else
                    timeObj.set("cnt", 0);

                timesObjArr.add(timeObj);
            }

            object.set("times", timesObjArr);
            list.add(object);
        }
        ret.put("meetDays", list);


        return ret;

    }

    /**
     * 预约前获取关键信息
     */
    public Map<String, Object> detailForMeetJoin(long userId, long meetId, String day, String time) {

        // this.checkRules(userId, meetId, day, time);

        Where<MeetModel> where = new Where<>();
        where.eq("MEET_ID", meetId);
        where.eq("MEET_STATUS", MeetModel.STATUS.NORMAL);
        Map<String, Object> ret = meetMapper.getOneMap(where, "MEET_TITLE");
        if (ObjectUtil.isEmpty(ret)) throw new AppException("该预约项目不存在");
        logger.info(ret.toString());

        // 取出本人最近一次的填写表单
        Where<MeetJoinModel> whereJoin = new Where<>();
        whereJoin.eq("MEET_JOIN_USER_ID", userId);
        whereJoin.orderByDesc("MEET_JOIN_ID");
        Map<String, Object> retJoin = meetJoinMapper.getOneMap(whereJoin, "MEET_JOIN_FORMS");

        if (ObjectUtil.isEmpty(retJoin)) {
            ret.put("myForms", null);
        } else {
            ret.put("myForms", retJoin.get("meetJoinForms"));

        }

        return ret;
    }

    public void checkRules(long userId, long meetId, String day, String time) {
        Where<MeetModel> where = new Where<>();
        where.eq("MEET_ID", meetId);
        where.eq("MEET_STATUS", MeetModel.STATUS.NORMAL);
        MeetModel meet = meetMapper.getOne(where);
        if (ObjectUtil.isEmpty(meet))
            throw new AppException("该预约项目不存在或者已经停止");

        // 判断是否已经约满
        Where<MeetJoinModel> whereCnt = new Where<>();
        whereCnt.eq("MEET_JOIN_MEET_ID", meetId);
        whereCnt.eq("MEET_JOIN_DAY", day);
        whereCnt.eq("MEET_JOIN_TIME", time);
        long cnt = meetJoinMapper.count(whereCnt);
        if (cnt >= meet.getMeetMaxCnt())
            throw new AppException("该时段已经约满，请选择其他~");

        // 自己是否预约
        Where<MeetJoinModel> whereJoin = new Where<>();
        whereJoin.eq("MEET_JOIN_USER_ID", userId);
        whereJoin.eq("MEET_JOIN_MEET_ID", meetId);
        whereJoin.eq("MEET_JOIN_DAY", day);
        whereJoin.eq("MEET_JOIN_TIME", time);
        if (meetJoinMapper.exists(whereJoin))
            throw new AppException("您已经预约本时段，无须重复预约~");
    }

    /**
     * 预约
     */
    public long meetJoin(long userId, long meetId, String day, String time, String forms, String obj) {


        return 0;
    }

    /**
     * 取消我的预约,取消即为删除记录
     */
    public void cancelMyMeetJoin(long userId, long meetJoinId) {
        Where<MeetJoinModel> whereJoin = new Where<>();
        whereJoin.eq("MEET_JOIN_ID", meetJoinId);
        whereJoin.eq("MEET_JOIN_STATUS", MeetJoinModel.STATUS.NORMAL);
        MeetJoinModel meetJoin = meetJoinMapper.getOne(whereJoin);

        if (ObjectUtil.isEmpty(meetJoin))
            throw new AppException("未找到可取消的预约记录");

        if (NumberUtil.equals(meetJoin.getMeetJoinIsCheck(), 1))
            throw new AppException("该预约已经签到，无法取消");

        MeetModel meet = meetMapper.getOne(meetJoin.getMeetJoinMeetId());
        if (ObjectUtil.isEmpty(meet))
            throw new AppException("该预约不存在，无法取消");


        meetJoinMapper.delete(meetJoinId);

    }


    /**
     * 按天获取预约项目
     */
    public List<Map<String, Object>> getMeetListByDay(String day) {

        Where<MeetModel> where = new Where<>();
        where.eq("MEET_STATUS", MeetModel.STATUS.NORMAL);
        where.like("MEET_DAYS", day);

        where.orderByAsc("MEET_ORDER");
        where.orderByDesc("MEET_ID");

        String fields = "MEET_ID,MEET_TITLE,MEET_OBJ";

        List<Map<String, Object>> list = meetMapper.getAllListMap(where, fields);

        for (Map<String, Object> record : list) {
            FormHelper.fmtDBObj(record, "meetObj", "cover,time");
        }

        return list;
    }

    /**
     * 获取从某天开始可预约的日期
     *
     * @param {*} fromDay  日期 Y-M-D
     */
    public JSONArray  getMeetHasDaysFromDay(String fromDay) {
        long start = TimeHelper.time2Timestamp(fromDay + " 00:00:00");


        Where<MeetModel> where = new Where<>();
        where.eq("MEET_STATUS", MeetModel.STATUS.NORMAL);


        String fields = "MEET_DAYS";

        Map<String, String> ret = new HashMap<>();
       JSONArray arr = new JSONArray();

        List<MeetModel> list = meetMapper.getAllList(where, fields);

        String today = TimeHelper.time("yyyy-MM-dd");

        for (MeetModel meet : list) {
            JSONArray daysArr = JSONUtil.parseArray(meet.getMeetDays());
            for (Object t : daysArr) {
                JSONObject object = new JSONObject();

                String day = Convert.toStr(t);

                if (day.compareTo(today) < 0) continue;

                if (!ret.containsKey(day)) {
                    ret.put(day, day);
                    arr.add(day);
                }

            }
        }


        return arr;
    }
}
