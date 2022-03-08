package com.chf.lightjob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.chf.lightjob.controller.web.intercepter.WebSessionFilter;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

	@Autowired
	private WebSessionFilter webSessionFilter;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
		registry.addInterceptor(webSessionFilter).addPathPatterns("/admin/**");
	}
}