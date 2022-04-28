package com.carrot.interceptor;

import com.carrot.commons.redis.AuthComponent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
public class SessionCheckInterceptor extends HandlerInterceptorAdapter {
	
	// Controller 실행전 interceptor session체크
	// return이 false이면 controller를 호출하지 않음
	@Autowired
	private AuthComponent authComponent;
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		log.info("SessionCheckInterceptor - preHandle");
        log.info("##### prehandler request.Header : " + request.getHeader("accessToken"));
        if(authComponent.checkAccessToken(request)) {
        	return true;
        } else {
        	response.sendRedirect("/login/msg");
        	log.info("#### no session");
        	return false;
        }
        
    }
}
