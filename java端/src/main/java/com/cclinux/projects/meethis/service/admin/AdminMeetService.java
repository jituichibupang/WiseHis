package com.cclinux.projects.meethis.service.admin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.exception.AppException;
import com.cclinux.framework.helper.FileHelper;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.projects.meethis.comm.ProjectConfig;
import com.cclinux.projects.meethis.mapper.MeetJoinMapper;
import com.cclinux.projects.meethis.mapper.MeetMapper;
import com.cclinux.projects.meethis.model.MeetJoinModel;
import com.cclinux.projects.meethis.model.MeetModel;
import com.cclinux.projects.meethis.service.cust.MeetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Notes: 活动业务逻辑
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/7/15 12:10
 * @Ver: ccminicloud-framework 3.2.1
 */


@Service("MeetHisAdminMeetService")
public class AdminMeetService extends BaseMyAdminService {


    @Resource(name = "MeetHisMeetMapper")
    private MeetMapper meetMapper;

    @Resource(name = "MeetHisMeetJoinMapper")
    private MeetJoinMapper meetJoinMapper;

    @Resource(name = "MeetHisMeetService")
    private MeetService meetService;


    /**
     * 添加活动
     */
    public long insertMeet(MeetModel meet) {
        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");

        return 0;
    }

    /**
     * 修改活动
     */
    public void editMeet(MeetModel meet) {
        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");

    }

    /**
     * 设置预约天数
     */
    public void setMeetDays(long meetId, String days) {

        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");

    }


    /**
     * 活动列表
     */
    public PageResult getAdminMeetList(PageParams pageRequest) {

        Where<MeetModel> where = new Where<>();

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
                case "status": {
                    where.eq("MEET_STATUS", Convert.toInt(sortVal));
                    break;
                }
                case "vouch": {
                    where.eq("MEET_VOUCH", 1);
                    break;
                }
                case "top": {
                    where.eq("MEET_ORDER", 0);
                    break;
                }
                case "sort": {
                    logger.info("SortVal" + sortVal);
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByAsc("MEET_ORDER");
        where.orderByDesc("MEET_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return meetMapper.getPageList(page, where, "*");
    }

    /**
     * 活动报名名单
     */
    public PageResult getAdminMeetJoinList(PageParams pageRequest) {

        Where<MeetJoinModel> where = new Where<>();


        long meetId = pageRequest.getParamLong("meetId");
        where.eq("MEET_JOIN_MEET_ID", meetId);

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            if (search.length() == 21 && search.contains("#")) {
                // 日期查询
                String[] arr = search.split("#");
                where.between("MEET_JOIN_DAY", arr[0], arr[1]);
            } else {
                where.and(wrapper -> {
                    wrapper.or().like("MEET_JOIN_OBJ", search);
                });
            }

        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "check": {
                    where.eq("MEET_JOIN_IS_CHECK", Convert.toInt(sortVal));
                    break;
                }
                case "status": {
                    where.eq("MEET_JOIN_STATUS", Convert.toInt(sortVal));
                    break;
                }
                case "sort": {
                    logger.info("SortVal" + sortVal);
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByDesc("MEET_JOIN_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return meetJoinMapper.getPageList(page, where, "*");
    }

    /**
     * 删除活动
     */
    public void delMeet(long id) {
        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");

    }

    /**
     * 删除活动报名
     */
    public void delMeetJoin(long meetJoinId) {

        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");

    }


    /**
     * 获取单个预约
     */
    public Map<String, Object> getMeetDetail(long id) {
        return meetMapper.getOneMap(id);
    }

    /**
     * 修改预约状态
     */
    public void statusMeet(long id, int status) {
        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }


    /**
     * 管理员按钮核销
     */
    public void checkinMeetJoin(long meetJoinId, int flag) {
        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }

    /**
     * 修改排序
     */
    public void orderMeet(long id, int order) {
        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }


    /**
     * 首页设定
     */
    public void vouchMeet(long id, int vouch) {
        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }

    /**
     * 导出名单
     */
    public Map<String, Object> exportMeetJoinExcel(long meetId, String start, String end) {
        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");
        return null;
    }

    /**
     * 清空名单
     */
    public void clearMeetAll(long meetId) {
        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }

    /**
     * 管理员扫码核销
     */
    public void scanMeetJoin(long meetId, String code) {
        appError("{陪诊服务}该功能暂不开放，如有需要请加作者微信：cclinux0730");
    }

}
