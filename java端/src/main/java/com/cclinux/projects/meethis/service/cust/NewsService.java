package com.cclinux.projects.meethis.service.cust;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclinux.framework.annotation.LoginIgnore;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.projects.meethis.mapper.NewsMapper;
import com.cclinux.projects.meethis.model.NewsModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Notes: 资讯模块
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/7 5:41
 * @Ver: ccminicloud-framework 3.2.1
 */

@Service("MeetHisNewsService")
public class NewsService extends BaseMyCustService {

    @Resource(name = "MeetHisNewsMapper")
    private NewsMapper newsMapper;

    /**
     * 列表
     */
    @LoginIgnore
    public PageResult getNewsList(PageParams pageRequest) {

        Where<NewsModel> where = new Where<>();
        where.eq("NEWS_STATUS", NewsModel.STATUS.NORMAL);

        long cateId = pageRequest.getParamLong("cateId");
        if (NumberUtil.compare(cateId, 0) > 0)
            where.eq("NEWS_CATE_ID", cateId);

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            where.and(
                    wrapper -> {
                        wrapper.or().like("NEWS_TITLE", search);
                        //wrapper.or().like("NEWS_OBJ", search);
                    }
            );
        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "cateId": {
                    where.eq("NEWS_CATE_ID", Convert.toLong(sortVal));
                    break;
                }
                case "sort": {
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByAsc("NEWS_ORDER");
        where.orderByDesc("NEWS_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return newsMapper.getPageList(page, where, "NEWS_VIEW_CNT,NEWS_ID,NEWS_TITLE," +
                "NEWS_CATE_NAME,NEWS_OBJ,ADD_TIME");
    }


    /**
     * 单个浏览
     */
    @LoginIgnore
    public Map<String, Object> view(long id) {

        // PV
        UpdateWhere<NewsModel> uw = new UpdateWhere<>();
        uw.eq("NEWS_ID", id);
        newsMapper.inc(uw, "NEWS_VIEW_CNT");

        Where<NewsModel> where = new Where<>();
        where.eq("NEWS_ID", id);
        where.eq("NEWS_STATUS", NewsModel.STATUS.NORMAL);

        return newsMapper.getOneMap(where, "*");
    }
}
