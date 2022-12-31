package com.matrix.rbac;


import com.matrix.rbac.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
        // 拦截所有请求
        registration.addPathPatterns("/**");
        // 排除指定请求不拦截
        registration.excludePathPatterns("/", "/login", "/error", "/easyui/**", "/css/**");
    }
}
