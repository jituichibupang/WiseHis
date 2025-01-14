package com.cclinux.projects.meethis.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cclinux.framework.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Notes: 设置实体
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/12/10 9:35
 * @Ver: ccminicloud-framework 3.2.1
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("meethis_setup")
public class SetupModel extends BaseModel {

    @TableId(value = "SETUP_ID", type = IdType.AUTO)
    private Long setupId;

    @TableField("SETUP_TYPE")
    private String setupType;

    @TableField("SETUP_KEY")
    private String setupKey;

    @TableField("SETUP_VALUE")
    private String setupValue;


}
