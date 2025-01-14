package com.cclinux.projects.meethis.service.admin;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclinux.framework.core.domain.PageParams;
import com.cclinux.framework.core.domain.PageResult;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.helper.FileHelper;
import com.cclinux.framework.helper.FormHelper;
import com.cclinux.projects.meethis.comm.ProjectConfig;
import com.cclinux.projects.meethis.mapper.UserMapper;
import com.cclinux.projects.meethis.model.UserModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Notes: 用户管理业务逻辑
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/12/15 12:10
 * @Ver: ccminicloud-framework 3.2.1
 */


@Service("MeetHisAdminUserService")
public class AdminUserService extends BaseMyAdminService {


    @Resource(name = "MeetHisUserMapper")
    private UserMapper userMapper;


    /**
     * 用户列表
     */
    public PageResult getAdminUserList(PageParams pageRequest) {

        Where<UserModel> where = new Where<>();

        // 关键字查询
        String search = pageRequest.getSearch();
        if (StrUtil.isNotEmpty(search)) {
            where.and(wrapper -> {
                wrapper.or().like("USER_NAME", search);
                wrapper.or().like("USER_ACCOUNT", search);
            });
        }

        // 条件查询
        String sortType = pageRequest.getSortType();
        String sortVal = pageRequest.getSortVal();
        if (StrUtil.isNotEmpty(sortType) && StrUtil.isNotEmpty(sortVal)) {
            switch (sortType) {
                case "status": {
                    where.eq("USER_STATUS", Convert.toInt(sortVal));
                    break;
                }
                case "sort": {
                    where.fmtOrderBySort(sortVal, "");
                    break;
                }
            }

        }

        // 排序
        where.orderByDesc("USER_ID");


        Page page = new Page(pageRequest.getPage(), pageRequest.getSize());
        return userMapper.getPageList(page, where, "*");
    }

    /**
     * 删除用户
     */
    public void delUser(long id) {
        userMapper.delete(id);
    }


    /**
     * 获取单个用户
     */
    public Map<String, Object> getUserDetail(long id) {
        return userMapper.getOneMap(id);
    }

    /**
     * 修改用户状态
     */
    public void statusUser(long id, int status) {
        UpdateWhere<UserModel> uw = new UpdateWhere<>();
        uw.eq("USER_ID", id);

        uw.set("USER_STATUS", status);
        userMapper.edit(uw);
    }


    /**
     * 重置用户密码
     */
    public void editUserPwd(long id, String pwd) {
        UpdateWhere<UserModel> uw = new UpdateWhere<>();
        uw.eq("USER_ID", id);

        uw.set("USER_PASSWORD", DigestUtil.md5Hex(pwd));
        userMapper.edit(uw);
    }


    /**
     * 导出名单
     */
    public Map<String, Object> exportUserJoinExcel(String fmt) {
        String pid = ProjectConfig.PID;

        String savePath = FileHelper.getFileSavePath(pid, "xlsx", "data");

        JSONArray fmtArr = JSONUtil.parseArray(fmt);

        ExcelWriter writer = ExcelUtil.getWriter(savePath);
        writer.renameSheet("用户数据");

        String[] titles = {"序号", "账号", "姓名", "状态", "注册时间"};
        for (int i = 0; i < fmtArr.size(); i++) {
            JSONObject jsonObject = fmtArr.getJSONObject(i);
            titles = (String[]) ArrayUtil.append(titles, jsonObject.get("title"));
        }

        writer.writeHeadRow(Arrays.asList(titles));
        writer.setColumnWidth(0, 10);
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 20);
        writer.setColumnWidth(3, 15);
        writer.setColumnWidth(4, 30);
        for (int i = 1; i <= fmtArr.size(); i++) {
            writer.setColumnWidth(4 + i, 30);
        }


        Where<UserModel> where = new Where<>();

        long total = userMapper.count(where);

        int size = 100;
        int pageCount = FormHelper.calcPageCount(total, size);

        long no = 0;// 序号
        for (int i = 1; i <= pageCount; i++) {
            Page page = new Page(i, size, false);
            PageResult ret = userMapper.getPageList(page, where);

            List<Map<String, Object>> retList = ret.getList();
            for (Map<String, Object> node : ret.getList()) {
                no++;

                ArrayList<String> arr = new ArrayList<>();

                arr.add(Convert.toStr(no));

                arr.add(MapUtil.getStr(node, "userAccount"));
                arr.add(MapUtil.getStr(node, "userName"));

                int status = MapUtil.getInt(node, "userStatus");
                arr.add(status == 1 ? "正常" : "禁用");

                arr.add(MapUtil.getStr(node, "addTime"));

                JSONArray forms = JSONUtil.parseArray(MapUtil.getStr(node, "userForms"));
                for (int j = 0; j < forms.size(); j++) {
                    JSONObject jsonObject = forms.getJSONObject(j);
                    arr.add(Convert.toStr(jsonObject.get("val")));
                }


                writer.writeRow(arr);

            }

        }

        writer.close();


        String url = FileHelper.getFileSaveURL(savePath);
        Map<String, Object> ret = new HashMap<>();
        ret.put("url", url);
        ret.put("total", total);

        return ret;
    }


}
