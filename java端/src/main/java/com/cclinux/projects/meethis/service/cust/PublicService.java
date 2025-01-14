package com.cclinux.projects.meethis.service.cust;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.cclinux.framework.core.mapper.UpdateWhere;
import com.cclinux.framework.core.mapper.Where;
import com.cclinux.framework.helper.FileHelper;
import com.cclinux.framework.helper.TimeHelper;
import com.cclinux.projects.meethis.comm.ProjectConfig;
import com.cclinux.projects.meethis.mapper.SetupMapper;
import com.cclinux.projects.meethis.model.SetupModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Notes: 文件上传逻辑
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/2/18 7:44
 * @Ver: ccminicloud-framework 3.2.1
 */

@Service("MeetHisPublicService")
public class PublicService extends BaseMyCustService {

    @Resource(name = "MeetHisSetupMapper")
    private SetupMapper setupMapper;

    /**
     * 文件上传
     */
    public String upload(MultipartFile file) {

        // 先保存至临时文件夹
        return FileHelper.saveUpLoadFile(file, ProjectConfig.PID);
    }


    /**
     * 获得当前导出链接
     */
    public Map<String, Object> getExportDataURL(String key) {
        String url = "";
        String time = "";

        Where<SetupModel> where = new Where<>();
        where.eq("SETUP_KEY", key);
        SetupModel setup = setupMapper.getOne(where);
        if (ObjectUtil.isNotEmpty(setup)) {
            url = setup.getSetupValue() + "?rd=" + TimeHelper.timestamp();
            time = TimeHelper.timestamp2Time(setup.getAddTime());
        }

        Map<String, Object> ret = new HashMap<>();
        ret.put("url", url);
        ret.put("time", time);

        return ret;
    }

    /**
     * 删除数据文件
     */
    public void deleteDataExcel(String key) {
        Where<SetupModel> where = new Where<>();
        where.eq("SETUP_KEY", key);
        SetupModel setup = setupMapper.getOne(where);
        if (ObjectUtil.isNull(setup)) return;

        FileUtil.del(setup.getSetupValue());
    }

    /**
     * 入导出表
     */
    public void exportDataExcel(String key, String url) {

        // 删除导出表
        Where<SetupModel> where = new Where<>();
        where.eq("SETUP_KEY", key);
        setupMapper.delete(where);

        // 入导出表
        SetupModel setup = new SetupModel();
        setup.setSetupKey(key);
        setup.setSetupValue(url);
    }


    /**
     * 设置系统配置
     */
    public void setSetup(String key, String val, String type) {
        if (StrUtil.isEmpty((key))) return;

        Where<SetupModel> where = new Where<>();
        where.eq("SETUP_KEY", key);
        if (setupMapper.exists(where)) {
            UpdateWhere<SetupModel> uw = new UpdateWhere<>();
            uw.eq("SETUP_KEY", key);
            uw.set("SETUP_KEY", key);
            uw.set("SETUP_VALUE", val);
            setupMapper.edit(uw);
            return;
        }

        SetupModel setup = new SetupModel();
        setup.setSetupType(type);
        setup.setSetupKey(key);
        setup.setSetupValue(val);
        setupMapper.add(setup);

    }

    /**
     * 获取系统配置
     */
    public String getSetup(String key) {
        Where<SetupModel> where = new Where<>();
        where.eq("SETUP_KEY", key);
        SetupModel setup = setupMapper.getOne(where);

        if (ObjectUtil.isEmpty(setup)) return null;

        return setup.getSetupValue();
    }

    /**
     * 删除系统配置
     */
    public void removeSetup(String key) {
        Where<SetupModel> where = new Where<>();
        where.eq("SETUP_KEY", key);
        setupMapper.delete(where);
    }

}
