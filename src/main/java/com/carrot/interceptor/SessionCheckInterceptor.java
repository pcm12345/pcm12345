package com.carrot.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.carrot.common.redis.TokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionCheckInterceptor extends HandlerInterceptorAdapter {
	
	// Controller 실행전 interceptor session체크
	// return이 false이면 controller를 호출하지 않음
	@Autowired
	private TokenService tokenService;
	
	@Value("${item.page}")
	private String page;
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		log.info("SessionCheckInterceptor - preHandle");
        log.info("##### prehandler request.Header : " + request.getHeader("accessToken"));
        if(tokenService.checkAccessToken(request)) {
        	return true;
        } else {
        	response.sendRedirect(page + "/login/msg");
        	log.info("#### no session");
        	return false;
        }
        
    }
}