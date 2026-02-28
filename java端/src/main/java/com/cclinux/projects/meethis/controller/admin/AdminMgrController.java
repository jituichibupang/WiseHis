package com.cclinux.projects.meethis.controller.admin;


import cn.hutool.core.map.MapUtil;
import com.cclinux.framework.annotation.DemoShow;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.framework.validate.DataCheck;
import com.cclinux.projects.meethis.model.AdminModel;
import com.cclinux.projects.meethis.service.admin.AdminMgrService;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController("MeetHisAdminMgrController")
public class AdminMgrController extends BaseMyAdminController {

    @Resource(name = "MeetHisAdminMgrService")
    private AdminMgrService adminMgrService;


    @RequestMapping(value = "/admin/mgr/list")
    public ApiResult getMgrList(@RequestBody Map<String, Object> input, @RequestAttribute long adminId) {
        // 数据校验
        String[] RULES = PageParams.PAGE_CHECK_RULE;
        DataCheck.check(input, RULES);

        // 业务
        PageParams pageParams = new PageParams(input);
        PageResult ret = adminMgrService.getMgrList(adminId, pageParams);

        // 数据格式化
        List<Map<String, Object>> list = ret.getList();
        for (Map<String, Object> node : list) {
            TimeHelper.db2Time(node, "adminAddTime");
            TimeHelper.db2Time(node, "adminLoginTime");
        }

        return ApiResult.success(ret);
    }

    @DemoShow
    @RequestMapping(value = "/admin/mgr/del")
    public ApiResult delMgr(@RequestBody Map<String, Object> input, @RequestAttribute long adminId) {

        // 数据校验
        String[] RULES = {
                "id:must|long"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        adminMgrService.delMgr(id, adminId);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/mgr/status")
    public ApiResult statusMgr(@RequestBody Map<String, Object> input, @RequestAttribute long adminId) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "status:must|int|name=状态"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int status = MapUtil.getInt(input, "status");
        adminMgrService.statusMgr(id, status, adminId);


        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/mgr/insert")
    public ApiResult insertMgr(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "type:must|int|name=类型",
                "name:must|string|min=5|max=30|name=账号",
                "desc:must|string|max=30|name=姓名",
                "phone:mobile|name=手机",
                "password:must|string|min=6|max=30|name=密码",
        };
        DataCheck.check(input, RULES);

        // 业务
        AdminModel admin = this.inputToAdmin(input);

        adminMgrService.insertMgr(admin);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/mgr/edit")
    public ApiResult editMgr(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long|name=id",
                "type:must|int|name=类型",
                "name:must|string|min=5|max=30|name=账号",
                "desc:must|string|max=30|name=姓名",
                "phone:mobile|name=手机",
                "password:string|min=6|max=30|name=密码",
        };
        DataCheck.check(input, RULES);

        // 业务
        AdminModel admin = this.inputToAdmin(input);

        adminMgrService.editMgr(admin);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/mgr/pwd")
    public ApiResult pwdMgr(@RequestBody Map<String, Object> input, @RequestAttribute long adminId) {

        // 数据校验
        String[] RULES = {
                "oldPassword:must|string|min=6|max=30|name=旧密码",
                "password:must|string|min=6|max=30|name=新密码",
        };
        DataCheck.check(input, RULES);

        // 业务
        String oldPassword = MapUtil.getStr(input, "oldPassword");
        String password = MapUtil.getStr(input, "password");

        adminMgrService.pwdMgr(adminId, oldPassword, password);

        return ApiResult.success();
    }

    @RequestMapping(value = "/admin/mgr/detail")
    public ApiResult getMgrDetail(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long|name=id"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");

        return ApiResult.success(adminMgrService.getMgrDetail(id));
    }

    private AdminModel inputToAdmin(Map<String, Object> input) {
        AdminModel admin = new AdminModel();

        admin.setAdminId(MapUtil.getLong(input, "id"));

        admin.setAdminType(MapUtil.getInt(input, "type"));
        admin.setAdminName(MapUtil.getStr(input, "name"));
        admin.setAdminDesc(MapUtil.getStr(input, "desc"));
        admin.setAdminPhone(MapUtil.getStr(input, "phone"));
        admin.setAdminPassword(MapUtil.getStr(input, "password"));

        return admin;
    }
}
