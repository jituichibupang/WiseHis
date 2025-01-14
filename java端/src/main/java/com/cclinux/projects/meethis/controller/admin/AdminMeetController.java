package com.cclinux.projects.meethis.controller.admin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cclinux.framework.annotation.DemoShow;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.framework.validate.DataCheck;
import com.cclinux.projects.meethis.model.MeetModel;
import com.cclinux.projects.meethis.service.admin.AdminMeetService;
import com.cclinux.projects.meethis.service.cust.MeetService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("MeetHisAdminMeetController")
public class AdminMeetController extends BaseMyAdminController {

    @Resource(name = "MeetHisAdminMeetService")
    private AdminMeetService adminMeetService;

    @Resource(name = "MeetHisMeetService")
    private MeetService meetService;


    @RequestMapping(value = "/admin/meet/list")
    public ApiResult getAdminMeetList(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = PageParams.PAGE_CHECK_RULE;
        DataCheck.check(input, RULES);

        // 业务
        PageParams pageParams = new PageParams(input);
        PageResult ret = adminMeetService.getAdminMeetList(pageParams);

        List<Map<String, Object>> list = ret.getList();

        for (Map<String, Object> record : list) {

            //  FormHelper.db2Forms(record, "meetDays");

            record.put("meetDays", meetService.calcDays(Convert.toStr(record.get("meetDays"))));

            FormHelper.removeField(record, "meetForms");
            FormHelper.removeField(record, "meetObj");

        }

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/admin/meet/join/list")
    public ApiResult getAdminMeetJoinList(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "meetId:must|int",
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
        PageResult ret = adminMeetService.getAdminMeetJoinList(pageParams);

        List<Map<String, Object>> list = ret.getList();

        for (Map<String, Object> record : list) {

            TimeHelper.db2Time(record, "meetJoinCheckTime");
            FormHelper.db2Forms(record, "meetJoinForms");
            FormHelper.removeField(record, "meetJoinObj");
        }

        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/admin/meet/join/check")
    public ApiResult checkinMeetJoin(@RequestBody Map<String, Object> input) {

        // 数据校验
        // 数据校验
        String[] RULES = {
                "meetJoinId:must|long",
                "flag:must|int"
        };
        DataCheck.check(input, RULES);

        // 业务
        long meetId = MapUtil.getLong(input, "meetJoinId");
        int flag = MapUtil.getInt(input, "flag");
        adminMeetService.checkinMeetJoin(meetId, flag);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/meet/del")
    public ApiResult delMeet(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long"
        };
        DataCheck.check(input, RULES);

        // 业务
        long meetId = MapUtil.getLong(input, "id");
        adminMeetService.delMeet(meetId);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/meet/join/del")
    public ApiResult delMeetJoin(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "meetJoinId:must|long"
        };
        DataCheck.check(input, RULES);

        // 业务
        long meetJoinId = MapUtil.getLong(input, "meetJoinId");
        adminMeetService.delMeetJoin(meetJoinId);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/meet/status")
    public ApiResult statusMeet(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "status:must|int|name=状态"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int status = MapUtil.getInt(input, "status");
        adminMeetService.statusMeet(id, status);

        Map<String, Object> ret = new HashMap<>();

        return ApiResult.success(ret);
    }

    @DemoShow
    @RequestMapping(value = "/admin/meet/order")
    public ApiResult orderMeet(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "order:must|int|name=排序号"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int order = MapUtil.getInt(input, "order");
        adminMeetService.orderMeet(id, order);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/meet/vouch")
    public ApiResult vouchMeet(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "vouch:must|int"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int vouch = MapUtil.getInt(input, "vouch");
        adminMeetService.vouchMeet(id, vouch);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/meet/insert")
    public ApiResult insertMeet(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "title:must|string|min=4|max=50|name=标题",
                "cateId:must|long|name=分类",
                "cateName:must|string|name=分类名",
                "order:must|int|min=0|max=9999|name=排序号",
                "maxCnt:must|int|name=每时段人数上限",
                "forms:must|string|name=表单",
                "obj:must|string|name=表单",
        };
        DataCheck.check(input, RULES);

        // 业务
        MeetModel meet = this.inputToMeet(input);

        long id = adminMeetService.insertMeet(meet);


        return ApiResult.success(MapUtil.of("id", id));
    }

    @DemoShow
    @RequestMapping(value = "/admin/meet/edit")
    public ApiResult editMeet(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "title:must|string|min=4|max=50|name=标题",
                "cateId:must|long|name=分类",
                "cateName:must|string|name=分类名",
                "order:must|int|min=0|max=9999|name=排序号",
                "maxCnt:must|int|name=每时段人数上限",
                "forms:must|string|name=表单",
                "obj:must|string|name=表单",
        };
        DataCheck.check(input, RULES);

        // 业务
        MeetModel meet = this.inputToMeet(input);

        adminMeetService.editMeet(meet);


        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/meet/set/days")
    public ApiResult setMeetDays(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "meetId:must|long",
                "days:must|string|name=日期",
        };
        DataCheck.check(input, RULES);

        // 业务
        long meetId = MapUtil.getLong(input, "meetId");
        String days = MapUtil.getStr(input, "days");
        adminMeetService.setMeetDays(meetId, days);


        return ApiResult.success();
    }


    @RequestMapping(value = "/admin/meet/detail")
    public ApiResult getMeetDetail(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|id|name=id"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        Map<String, Object> ret = adminMeetService.getMeetDetail(id);
        if (ObjectUtil.isNotEmpty(ret)) {


        }


        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/admin/meet/join/export")
    public ApiResult exportMeetJoinExcel(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "meetId:must|id|name=id",
                "start:must|date|name=开始日期",
                "end:must|date|name=结束日期"
        };
        DataCheck.check(input, RULES);

        // 业务
        long meetId = MapUtil.getLong(input, "meetId");
        String start = MapUtil.getStr(input, "start");
        String end = MapUtil.getStr(input, "end");
        Map<String, Object> ret = adminMeetService.exportMeetJoinExcel(meetId, start, end);

        return ApiResult.success(ret);
    }

    @DemoShow
    @RequestMapping(value = "/admin/meet/join/clear")
    public ApiResult clearMeetAll(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "meetId:must|id|name=id"
        };
        DataCheck.check(input, RULES);

        // 业务
        long meetId = MapUtil.getLong(input, "meetId");
        adminMeetService.clearMeetAll(meetId);

        return ApiResult.success();
    }

    @RequestMapping(value = "/admin/meet/join/scan")
    public ApiResult scanMeetJoin(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "meetId:must|id|name=id",
                "code:must|string|name=核销码"
        };
        DataCheck.check(input, RULES);

        // 业务
        long meetId = MapUtil.getLong(input, "meetId");
        String code = MapUtil.getStr(input, "code");
        adminMeetService.scanMeetJoin(meetId, code);

        return ApiResult.success();
    }

    private MeetModel inputToMeet(Map<String, Object> input) {
        MeetModel meet = new MeetModel();

        meet.setMeetId(MapUtil.getLong(input, "id"));

        meet.setMeetTitle(MapUtil.getStr(input, "title"));
        meet.setMeetCateId(MapUtil.getLong(input, "cateId"));
        meet.setMeetCateName(MapUtil.getStr(input, "cateName"));
        meet.setMeetOrder(MapUtil.getInt(input, "order"));


        meet.setMeetMaxCnt(MapUtil.getLong(input, "maxCnt"));


        meet.setMeetForms(MapUtil.getStr(input, "forms"));
        meet.setMeetObj(MapUtil.getStr(input, "obj"));

        return meet;
    }

}
