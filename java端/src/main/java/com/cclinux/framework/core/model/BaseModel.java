package com.cclinux.framework.core.model;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cclinux.framework.helper.TimeHelper;
import lombok.Data;

import java.util.Date;

@Data
public class BaseModel {
    private static final long serialVersionUID = 1L;


    @TableField("ADD_TIME")
    private Long addTime;

    @TableField("EDIT_TIME")
    private Long editTime;


    /*
    public Long getAddTime() { // 新入库自动填充
        if (ObjectUtil.isNull(id) && ObjectUtil.isNull(addTime)) return TimeHelper.time();
        else
            return this.addTime;
    }

    public Long getEditTime() { // 新入库自动填充
        if (ObjectUtil.isNull(id) && ObjectUtil.isNull(editTime)) return TimeHelper.time();
        else
            return this.editTime;
    }*/

}

