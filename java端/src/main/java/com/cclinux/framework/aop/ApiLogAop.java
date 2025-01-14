package com.cclinux.framework.aop;

import cn.hutool.json.JSONUtil;
import com.cclinux.framework.exception.AppException;
import com.cclinux.framework.exception.AppExceptionHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ApiLogAop {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 换行符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 连接点
     */
    @Pointcut("execution(public * com.cclinux.projects.*.controller..*.*(..))")
    public void aop() {
    }


    /**
     * 在切点之前织入
     */
    @Before("aop()")
    public void doBefore(JoinPoint joinPoint) {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();


        String url = request.getRequestURL().toString();

        // 打印请求相关参数
        logger.info("========================================== Start ==========================================");
        logger.info("URL            : {}", url);
        logger.info("Header(token)  : {}", request.getHeader("token"));
        logger.info("Header(sub)    : {}", request.getHeader("sub"));
        logger.info("HTTP Method    : {}", request.getMethod());
        logger.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        logger.info("IP             : {}", request.getRemoteAddr());
        try {
            logger.info("Request Args   : \n{}", JSONUtil.toJsonPrettyStr(joinPoint.getArgs()));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

    }


    /**
     * 环绕，若抛出异常则不走完around，要额外处理
     */
    @Around("aop()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = pjp.proceed();

            // 打印出参
            logger.info("Response Args  : \n{}", JSONUtil.toJsonPrettyStr(JSONUtil.parseObj(result, false)));

        } catch (AppException ex) {
            AppExceptionHandler handler = new AppExceptionHandler();
            logger.info("Response Args  : \n{}", JSONUtil.toJsonPrettyStr(handler.handleAppException(ex)));

            throw ex;
        } catch (RuntimeException ex) {
            AppExceptionHandler handler = new AppExceptionHandler();
            logger.info("Response Args  : \n{}", JSONUtil.toJsonPrettyStr(handler.handleRuntimeException(ex)));

            throw ex;
        } catch (Exception ex) {
            AppExceptionHandler handler = new AppExceptionHandler();
            logger.info("Response Args  : \n{}", JSONUtil.toJsonPrettyStr(handler.handleOtherException(ex)));

            throw ex;
        } finally {
            logger.info("Duration       : {} ms", System.currentTimeMillis() - startTime);
            logger.info("=========================================== End ===========================================" + LINE_SEPARATOR);
        }

        return result;
    }


}
