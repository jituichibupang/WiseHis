package com.cclinux.projects.meethis.controller.admin;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cclinux.framework.annotation.DemoShow;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.validate.DataCheck;
import com.cclinux.projects.meethis.service.admin.AdminUserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController("MeetHisAdminUserController")
public class AdminUserController extends BaseMyAdminController {

    @Resource(name = "MeetHisAdminUserService")
    private AdminUserService adminUserService;


    @RequestMapping(value = "/admin/user/list")
    public ApiResult getAdminUserList(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = PageParams.PAGE_CHECK_RULE;
        DataCheck.check(input, RULES);

        // 业务
        PageParams pageParams = new PageParams(input);
        PageResult ret = adminUserService.getAdminUserList(pageParams);

        List<Map<String, Object>> list = ret.getList();

        for (Map<String, Object> record : list) {
            FormHelper.removeField(record, "userPassword");
        }

        return ApiResult.success(ret);
    }

    @DemoShow
    @RequestMapping(value = "/admin/user/del")
    public ApiResult delUser(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long"
        };
        DataCheck.check(input, RULES);

        // 业务
        long userId = MapUtil.getLong(input, "id");
        adminUserService.delUser(userId);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/user/status")
    public ApiResult statusUser(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "status:must|int|name=状态"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int status = MapUtil.getInt(input, "status");
        adminUserService.statusUser(id, status);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/user/pwd")
    public ApiResult editUserPwd(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "pwd:must|string|name=新密码"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        String pwd = MapUtil.getStr(input, "pwd");
        adminUserService.editUserPwd(id, pwd);

        return ApiResult.success();
    }

    @RequestMapping(value = "/admin/user/detail")
    public ApiResult getUserDetail(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|id|name=id"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        Map<String, Object> ret = adminUserService.getUserDetail(id);
        if (ObjectUtil.isNotEmpty(ret)) {
            FormHelper.removeField(ret, "userPassword");
        }


        return ApiResult.success(ret);
    }

    @RequestMapping(value = "/admin/user/export")
    public ApiResult exportUserJoinExcel(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "fmt:must|string|name=格式"
        };
        DataCheck.check(input, RULES);

        // 业务
        String fmt = MapUtil.getStr(input, "fmt");
        Map<String, Object> ret = adminUserService.exportUserJoinExcel(fmt);

        return ApiResult.success(ret);
    }


}
