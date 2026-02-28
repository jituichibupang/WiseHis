package com.cclinux.projects.meethis.controller.admin;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.cclinux.framework.annotation.DemoShow;
import com.cclinux.framework.core.domain.ApiResult;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.validate.DataCheck;
import com.cclinux.projects.meethis.model.NewsModel;
import com.cclinux.projects.meethis.service.admin.AdminNewsService;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController("MeetHisAdminNewsController")
public class AdminNewsController extends BaseMyAdminController {

    @Resource(name = "MeetHisAdminNewsService")
    private AdminNewsService adminNewsService;


    @RequestMapping(value = "/admin/news/list")
    public ApiResult getAdminNewsList(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = PageParams.PAGE_CHECK_RULE;
        DataCheck.check(input, RULES);

        // 业务处理
        PageParams pageParams = new PageParams(input);
        PageResult ret = adminNewsService.getAdminNewsList(pageParams);

        List<Map<String, Object>> list = ret.getList();

        for (Map<String, Object> record : list) {
            // 数据格式处理
            FormHelper.removeField(record, "newsForms");
            FormHelper.fmtDBObj(record, "newsObj", "desc");
        }

        return ApiResult.success(ret);
    }

    @DemoShow
    @RequestMapping(value = "/admin/news/del")
    public ApiResult delNews(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long"
        };
        DataCheck.check(input, RULES);

        // 业务
        long newsId = MapUtil.getLong(input, "id");
        adminNewsService.delNews(newsId);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/news/status")
    public ApiResult statusNews(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "status:must|int|name=状态"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int status = MapUtil.getInt(input, "status");
        adminNewsService.statusNews(id, status);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/news/order")
    public ApiResult orderNews(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "order:must|long|name=排序号"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int order = MapUtil.getInt(input, "order");
        adminNewsService.orderNews(id, order);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/news/vouch")
    public ApiResult vouchNews(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "vouch:must|int"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        int vouch = MapUtil.getInt(input, "vouch");
        adminNewsService.vouchNews(id, vouch);

        return ApiResult.success();
    }

    @DemoShow
    @RequestMapping(value = "/admin/news/insert")
    public ApiResult insertNews(@RequestBody Map<String, Object> input, @RequestAttribute long adminId) {

        // 数据校验
        String[] RULES = {
                "title:must|string|min=4|max=50|name=标题",
                "cateId:must|long|name=分类",
                "cateName:must|string|name=分类名",
                "order:must|int|min=0|max=9999|name=排序号",
                "forms:must|string|name=表单",
                "obj:must|string|name=表单",
        };
        DataCheck.check(input, RULES);

        // 业务
        NewsModel news = this.inputToNews(input);

        long id = adminNewsService.insertNews(news);


        return ApiResult.success(MapUtil.of("id", id));
    }

    @DemoShow
    @RequestMapping(value = "/admin/news/edit")
    public ApiResult editNews(@RequestBody Map<String, Object> input, @RequestAttribute long adminId) {

        // 数据校验
        String[] RULES = {
                "id:must|long",
                "title:must|string|min=4|max=50|name=标题",
                "cateId:must|long|name=分类",
                "cateName:must|string|name=分类名",
                "order:must|long|min=0|max=9999|name=排序号",
                "forms:must|string|name=表单",
                "obj:must|string|name=表单",
        };
        DataCheck.check(input, RULES);

        // 业务
        NewsModel news = this.inputToNews(input);

        adminNewsService.editNews(news);


        return ApiResult.success();
    }


    @RequestMapping(value = "/admin/news/detail")
    public ApiResult getNewsDetail(@RequestBody Map<String, Object> input) {

        // 数据校验
        String[] RULES = {
                "id:must|id|name=id"
        };
        DataCheck.check(input, RULES);

        // 业务
        long id = MapUtil.getLong(input, "id");
        Map<String, Object> ret = adminNewsService.getNewsDetail(id);

        if (ObjectUtil.isNotEmpty(ret)) {

        }

        return ApiResult.success(ret);
    }

    private NewsModel inputToNews(Map<String, Object> input) {
        NewsModel news = new NewsModel();

        news.setNewsId(MapUtil.getLong(input, "id"));

        news.setNewsTitle(MapUtil.getStr(input, "title"));
        news.setNewsCateId(MapUtil.getLong(input, "cateId"));
        news.setNewsCateName(MapUtil.getStr(input, "cateName"));
        news.setNewsOrder(MapUtil.getInt(input, "order"));

        news.setNewsForms(MapUtil.getStr(input, "forms"));
        news.setNewsObj(MapUtil.getStr(input, "obj"));

        return news;
    }
}
