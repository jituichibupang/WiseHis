package com.cclinux.framework.core.platform.controller;
import com.cclinux.framework.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    protected HttpServletRequest request;

    protected final Logger logger = LoggerFactory.getLogger(getClass());


}
