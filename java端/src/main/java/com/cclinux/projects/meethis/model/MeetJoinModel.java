package com.cclinux.projects.meethis.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cclinux.framework.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Notes: 活动报名实体
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/11/30 7:18
 * @Ver: ccminicloud-framework 3.2.1
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("meethis_meet_join")
public class MeetJoinModel extends BaseModel {
    /**
     * 状态
     */
    public static final class STATUS {
        public static final int STOP = 0; //非正常
        public static final int NORMAL = 1; // 正常
    }

    @TableId(value = "MEET_JOIN_ID", type = IdType.AUTO)
    private Long meetJoinId;

    @TableField("MEET_JOIN_MEET_ID")
    private long meetJoinMeetId;

    @TableField("MEET_JOIN_CODE")
    private String meetJoinCode;

    @TableField("MEET_JOIN_IS_CHECK")
    private int meetJoinIsCheck;

    @TableField("MEET_JOIN_CHECK_TIME")
    private long meetJoinCheckTime;

    @TableField("MEET_JOIN_USER_ID")
    private long meetJoinUserId;

    @TableField("MEET_JOIN_FORMS")
    private String meetJoinForms;

    @TableField("MEET_JOIN_OBJ")
    private String meetJoinObj;

    @TableField("MEET_JOIN_STATUS")
    private int meetJoinStatus;

    @TableField("MEET_JOIN_MEET_TITLE")
    private String meetJoinMeetTitle;

    @TableField("MEET_JOIN_DAY")
    private String meetJoinDay;

    @TableField("MEET_JOIN_TIME")
    private String meetJoinTime;


}
