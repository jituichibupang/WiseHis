package com.cclinux.framework.core.platform.service;

import com.cclinux.framework.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Notes: 逻辑基类
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/11 5:06
 * @Ver: ccminicloud-framework 3.2.1
 */

public class BaseService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    protected void appError(String msg) {
        throw new AppException(msg);
    }
}
