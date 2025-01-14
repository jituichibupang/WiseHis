package com.cclinux.projects.meethis.controller.cust;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cclinux.framework.annotation.LoginIgnore;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.framework.validate.DataCheck;
import com.cclinux.projects.meethis.service.cust.MeetService;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Notes: 资讯模块
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/7 5:57
 * @Ver: ccminicloud-framework 3.2.1
 */


@RestController("MeetHisMeetController")
public class MeetController extends BaseMyCustController {

    @Resource(name = "MeetHisMeetService")
    MeetService meetService;

    @LoginIgnore
    @RequestMapping(value = "/meet/view")
    public ApiResult view(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {

        // 数据校验
        String[] RULES = {
                "id:must|id|name=id"
        };
        DataCheck.check(input, RULES);


        // 业务
        long id = MapUtil.getLong(input, "id");
        Map<String, Object> ret = meetService.view(id, userId);


        if (ObjectUtil.isNotEmpty(ret)) {

            ret.remove("meetForms");

        }


        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/meet/detail/for/join")
    public ApiResult detailForMeetJoin(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {

        // 数据校验
        String[] RULES = {
                "meetId:must|id|name=id",
                "day:must|string|name=日期",
                "time:must|string|name=时段"
        };
        DataCheck.check(input, RULES);


        // 业务
        long meetId = MapUtil.getLong(input, "meetId");
        String day = MapUtil.getStr(input, "day");
        String time = MapUtil.getStr(input, "time");

        Map<String, Object> ret = meetService.detailForMeetJoin(userId, meetId, day, time);

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/meet/join")
    public ApiResult meetJoin(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {

        // 数据校验
        String[] RULES = {
                "meetId:must|id|name=id",
                "day:must|string|name=日期",
                "time:must|string|name=时段",
                "forms:string|name=表单",
                "obj:string|name=表单",
        };
        DataCheck.check(input, RULES);


        // 业务
        long meetId = MapUtil.getLong(input, "meetId");
        String day = MapUtil.getStr(input, "day");
        String time = MapUtil.getStr(input, "time");
        String forms = MapUtil.getStr(input, "forms");
        String obj = MapUtil.getStr(input, "obj");

        meetService.meetJoin(userId, meetId, day, time, forms, obj);

        return ApiResult.success();
    }

    @RequestMapping(value = "/meet/my/join/cancel")
    public ApiResult cancelMyMeetJoin(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {

        // 数据校验
        String[] RULES = {
                "meetJoinId:must|id|name=id"
        };
        DataCheck.check(input, RULES);


        // 业务
        long meetJoinId = MapUtil.getLong(input, "meetJoinId");

        meetService.cancelMyMeetJoin(userId, meetJoinId);

        return ApiResult.success();
    }


    @LoginIgnore
    @RequestMapping(value = "/meet/list")
    public ApiResult getMeetList(@RequestBody Map<String, Object> input) {
        // 数据校验
        String[] RULES = {
                "cateId:int",
                "search:string|name=搜索条件",
                "page:must|int|default=1",
                "size:must|int|size|default=10",
                "isTotal:bool",
                "sortType:string|name=搜索类型",
                "sortVal:string|name=搜索类型值",
                "orderBy:string|name=排序",
                "whereEx:string|name=附加查询条件",
                "oldTotal:int"
        };
        DataCheck.check(input, RULES);

        // 业务
        PageParams pageParams = new PageParams(input);
        PageResult ret = meetService.getMeetList(pageParams);

        List<Map<String, Object>> list = ret.getList();

        for (Map<String, Object> record : list) {
            record.put("meetDays", meetService.calcDays(Convert.toStr(record.get("meetDays"))));
            FormHelper.fmtDBObj(record, "meetObj", "cover,tag");

        }

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/meet/my/join/list")
    public ApiResult getMyMeetJoinList(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {
        // 数据校验
        String[] RULES = PageParams.PAGE_CHECK_RULE;
        DataCheck.check(input, PageParams.PAGE_CHECK_RULE);

        // 业务
        PageParams pageParams = new PageParams(input);
        PageResult ret = meetService.getMyMeetJoinList(userId, pageParams);

        List<Map<String, Object>> list = ret.getList();

        for (Map<String, Object> record : list) {
            TimeHelper.db2Time(record, "meetStart", "yyyy-MM-dd HH:mm");
            TimeHelper.db2Time(record, "meetEnd", "yyyy-MM-dd HH:mm");
        }

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/meet/my/join/detail")
    public ApiResult getMyMeetJoinDetail(@RequestBody Map<String, Object> input, @RequestAttribute long userId) {
        // 数据校验
        String[] RULES = {
                "meetJoinId:must|id|name=id"
        };
        DataCheck.check(input, RULES);

        // 业务
        long meetJoinId = MapUtil.getLong(input, "meetJoinId");
        Map<String, Object> ret = meetService.getMyMeetJoinDetail(userId, meetJoinId);

        if (ObjectUtil.isNotEmpty(ret)) {

            TimeHelper.db2Time(ret, "meetJoinCheckTime", "yyyy-MM-dd HH:mm:ss");
        }

        return ApiResult.success(ret);
    }

    @LoginIgnore
    @RequestMapping(value = "/meet/list/by/day")
    public ApiResult getMeetListByDay(@RequestBody Map<String, Object> input) {
        // 数据校验
        String[] RULES = {
                "day:must|string|name=日期"
        };
        DataCheck.check(input, RULES);

        // 业务
        String day = MapUtil.getStr(input, "day");
        Object ret = meetService.getMeetListByDay(day);

        return ApiResult.success(ret);
    }

    @LoginIgnore
    @RequestMapping(value = "/meet/list/has/day")
    public ApiResult getMeetHasDaysFromDay(@RequestBody Map<String, Object> input) {
        // 数据校验
        String[] RULES = {
                "fromDay:must|string|name=日期"
        };
        DataCheck.check(input, RULES);

        // 业务
        String fromDay = MapUtil.getStr(input, "fromDay");
        Object ret = meetService.getMeetHasDaysFromDay(fromDay);

        return ApiResult.success(ret);
    }
}
