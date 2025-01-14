package com.cclinux.framework.config;

import com.cclinux.framework.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Notes: MVC配置
 * @Author: cclinux0730 (weixin)
 * @Date: 2024/3/3 11:14
 * @Ver: ccminicloud-framework 3.2.1
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 设置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自己的拦截器并设置拦截的请求路径
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")  //拦截所有请求，包括静态资源
                .excludePathPatterns("/pic/**"); //放行的请求

    }

    /**
     * 设置静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }


    /**
     * 分项目路由
     *
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

        configurer.addPathPrefix("/meethis", HandlerTypePredicate.forBasePackage("com.cclinux.projects.meethis" +
                ".controller"));
    }

}
