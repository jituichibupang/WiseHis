package com.cclinux.projects.meethis.service.cust;

import cn.hutool.core.convert.Convert;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.projects.meethis.mapper.MeetMapper;
import com.cclinux.projects.meethis.mapper.NewsMapper;
import com.cclinux.projects.meethis.model.MeetModel;
import com.cclinux.projects.meethis.model.NewsModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Notes: 首页
 * @Author: cclinux0730 (weixin)
 * @Ver: ccminicloud-framework 3.2.1
 */

@Service("MeetHisHomeService")
public class HomeService extends BaseMyCustService {

    @Resource(name = "MeetHisNewsMapper")
    private NewsMapper newsMapper;

    @Resource(name = "MeetHisMeetMapper")
    private MeetMapper meetMapper;

    @Resource(name = "MeetHisMeetService")
    private MeetService meetService;

    /**
     * 首页列表
     */
    public Map<String, Object> getHomeList() {
        Map<String, Object> ret = new HashMap<>();

        Where<NewsModel> whereNews = new Where<>();
        whereNews.eq("NEWS_VOUCH", 1);
        whereNews.orderByAsc("NEWS_ORDER");
        whereNews.orderByDesc("NEWS_ID");
        List<Map<String, Object>> newsList = newsMapper.getAllListMap(whereNews, "NEWS_TITLE,NEWS_OBJ,NEWS_ID," +
                "NEWS_CATE_NAME,ADD_TIME");

        for (Map<String, Object> record : newsList) {
            FormHelper.fmtDBObj(record, "newsObj", "cover");
            TimeHelper.fmtDBTime(record, "addTime", "yyyy-MM-dd");
        }

        ret.put("newsList", newsList);

        Where<MeetModel> whereMeet = new Where<>();
        whereMeet.eq("MEET_VOUCH", 1);
        whereMeet.orderByAsc("MEET_ORDER");
        whereMeet.orderByDesc("MEET_ID");
        List<Map<String, Object>> meetList = meetMapper.getAllListMap(whereMeet, "MEET_TITLE," +
                "MEET_OBJ," +
                "MEET_DAYS," +
                "MEET_ID," +
                "MEET_CATE_NAME, ADD_TIME");

        for (Map<String, Object> record : meetList) {
            record.put("meetDays", meetService.calcDays(Convert.toStr(record.get("meetDays"))));
            FormHelper.fmtDBObj(record, "meetObj", "cover");
        }

        ret.put("meetList", meetList);

        return ret;
    }

}
