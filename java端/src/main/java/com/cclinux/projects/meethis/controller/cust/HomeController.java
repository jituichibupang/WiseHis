package com.cclinux.projects.meethis.controller.cust;

import cn.hutool.core.map.MapUtil;
import com.cclinux.framework.annotation.LoginIgnore;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.validate.DataCheck;
import com.cclinux.projects.meethis.service.cust.HomeService;
import com.cclinux.projects.meethis.service.cust.PublicService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController("MeetHisHomeController")
public class HomeController extends BaseMyCustController {

    @Resource(name = "MeetHisPublicService")
    PublicService publicService;

    @Resource(name = "MeetHisHomeService")
    HomeService homeService;

    @LoginIgnore
    @RequestMapping(value = "/setup/get")
    public ApiResult getSetup(@RequestBody Map<String, Object> input) {
        // 数据校验
        String[] RULES = {
                "key:must|string"
        };
        DataCheck.check(input, RULES);

        // 业务
        String key = MapUtil.getStr(input, "key");
        String ret = publicService.getSetup(key);


        return ApiResult.success(ret);
    }


    @LoginIgnore
    @RequestMapping(value = "/home/list")
    public ApiResult getHomeList(@RequestBody Map<String, Object> input) {
        // 数据校验
        String[] RULES = {
        };
        DataCheck.check(input, RULES);

        // 业务
        Map<String, Object> ret = homeService.getHomeList();


        return ApiResult.success(ret);
    }

}
