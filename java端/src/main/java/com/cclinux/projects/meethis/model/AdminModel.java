package com.cclinux.projects.meethis.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cclinux.framework.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Notes: 管理员实体
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/2/16 9:35
 * @Ver: ccminicloud-framework 3.2.1
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("meethis_admin")
public class AdminModel extends BaseModel {

    /**
     * 状态
     */
    public static final class STATUS {
        public static final int STOP = 0; //非正常
        public static final int NORMAL = 1; // 正常
    }

    /**
     * 类型
     */
    public static final class TYPE {
        public static final int COMM = 0; //普通管理员
        public static final int SUPER = 1; // 超级管理员
        public static final int OTHTER = 9; // 其他管理人员
    }

    @TableId(value = "ADMIN_ID", type = IdType.AUTO)
    private Long adminId;


    @TableField("ADMIN_STATUS")
    private int adminStatus;

    @TableField("ADMIN_NAME")
    private String adminName;

    @TableField("ADMIN_DESC")
    private String adminDesc;

    @TableField("ADMIN_PHONE")
    private String adminPhone;

    @TableField("ADMIN_PASSWORD")
    private String adminPassword;

    @TableField("ADMIN_TYPE")
    private int adminType;

    @TableField("ADMIN_LOGIN_CNT")
    private long adminLoginCnt;

    @TableField("ADMIN_LOGIN_TIME")
    private long adminLoginTime;

}
