package com.cclinux.projects.meethis.controller.admin;

import cn.hutool.core.map.MapUtil;
import com.cclinux.framework.annotation.DemoShow;
import com.cclinux.framework.annotation.LoginIgnore;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.validate.DataCheck;
import com.cclinux.projects.meethis.service.admin.AdminHomeService;
import com.cclinux.projects.meethis.service.cust.PublicService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Notes: 后台首页控制器
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/2/16 9:42
 * @Ver: ccminicloud-framework 3.2.1
 */

@RestController("MeetHisAdminHomeController")
public class AdminHomeController extends BaseMyAdminController {

    @Resource(name = "MeetHisAdminHomeService")
    private AdminHomeService adminHomeService;

    @Resource(name = "MeetHisPublicService")
    private PublicService publicService;

    @RequestMapping(value = "/admin/home")
    public ApiResult adminHome() {

        ArrayList list = adminHomeService.adminHome();
        return ApiResult.success(list);
    }

    @LoginIgnore
    @RequestMapping(value = "/admin/login")
    public ApiResult adminLogin(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "name:must|string|min=5|max=30|name=账号",
                "password:must|string|min=5|max=30|name=密码"
        };
        DataCheck.check(input, RULES);


        // 业务
        String name = MapUtil.getStr(input, "name");
        String password = MapUtil.getStr(input, "password");


        Map<String, Object> ret = adminHomeService.adminLogin(name, password);

        return ApiResult.success(ret);
    }

    @DemoShow
    @RequestMapping(value = "/admin/setup/set")
    public ApiResult setSetup(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "key:must|string",
                "content:must|string|name=内容",
        };
        DataCheck.check(input, RULES);


        // 业务
        String key = MapUtil.getStr(input, "key");
        String content = MapUtil.getStr(input, "content");


        publicService.setSetup(key, content, "");

        return ApiResult.success();
    }

}
