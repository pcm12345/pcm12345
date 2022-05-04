package com.carrot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.carrot.interceptor.SessionCheckInterceptor;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
	
	@Bean
	public SessionCheckInterceptor sessionCheckInterceptor() {
		return new SessionCheckInterceptor();
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionCheckInterceptor())
            .excludePathPatterns("/api/v1/login*")
			.excludePathPatterns("/api/v1/center*")
            .addPathPatterns("/api/v1/user/expenses/*")
            .excludePathPatterns("/api/v1/user/*")
            .addPathPatterns("/api/v1/user/list")
            .addPathPatterns("/api/v1/cons/*")
            .addPathPatterns("/api/v1/admin/*")
            .addPathPatterns("/api/v1/client/*")
            .addPathPatterns("/api/v1/pay/*")
            .addPathPatterns("/api/v1/login/out")
            .addPathPatterns("/api/v1/login/access-check")
            .excludePathPatterns("/api/v1/client/meta");
    }
	
}