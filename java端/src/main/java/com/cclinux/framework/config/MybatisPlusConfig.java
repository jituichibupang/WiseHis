package com.cclinux.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * 分页配置类即可，配置之后即可实现分页查询
 */
@MapperScan(basePackages = {"com.cclinux.projects.*.mapper"})
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // fix MP 分页页数
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); //如果配置多个插件,切记分页最后添加

        //interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); 如果有多数据源可以不配具体类型 否则都建议配上具体的DbType


        return interceptor;
    }
}
