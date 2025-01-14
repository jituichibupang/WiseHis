package com.cclinux.framework.exception;

import com.cclinux.framework.constants.AppCode;
import com.cclinux.framework.core.domain.ApiResult;
import org.apache.ibatis.javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice(basePackages = "com.cclinux.projects")
@RestControllerAdvice
public class AppExceptionHandler {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义异常
     */
    @ExceptionHandler(AppException.class)
    public ApiResult handleAppException(AppException e) {
        ApiResult result = new ApiResult();
        result.put("code", e.getCode());
        result.put("msg", e.getMessage());
        result.put("data", null);

        return result;
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e) {
        logger.error("运行时异常:", e);
        return ApiResult.error(AppCode.ERROR_SVR, "服务运行异常，请稍后再试");
    }


    @ExceptionHandler(Exception.class)
    public ApiResult handleOtherException(Exception e) {
        logger.error("未知异常:", e);
        return ApiResult.error(AppCode.ERROR_SVR, "服务未知异常，请稍后再试");
    }


    @ExceptionHandler(NotFoundException.class)
    public ApiResult handleNotFoundException(NotFoundException e) {

        logger.error("页面未找到:", e);
        return ApiResult.error(AppCode.ERROR_SVR, "route not found");
    }

}
