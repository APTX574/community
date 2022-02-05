package com.community.config;

import com.community.controller.interceptor.LoginRequiredInterceptor;
import com.community.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author aptx
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 检查登录验证
     */
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    /**
     * 检查需要登录的请求的状态
     * 使用注解开发
     */
    @Autowired
    private LoginRequiredInterceptor loginRequired;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor).
                excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
        registry.addInterceptor(loginRequired).
                excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

    }


}
