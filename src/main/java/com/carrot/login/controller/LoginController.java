package com.carrot.login.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.redis.RedisService;
import com.carrot.common.redis.TokenService;
import com.carrot.common.service.CryptoUtilService;
import com.carrot.login.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/login")
@Api(description = "로그인/로그아웃", tags = "로그인 / 로그아웃 처리")
@Slf4j
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private CryptoUtilService cryptoUtilService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
    private RedisTemplate redisTemplate;
	
	@Autowired
	private TokenService tokenService;
	
	@GetMapping("")
	@ApiOperation(value="사용자 로그인", notes="로그인 처리")
	public ResponseEntity<Map<Object, Object>> login(@RequestParam("id") String id, @RequestParam("password") String password, HttpServletRequest request) throws Exception {
		
		log.info("#### LoginController.login id : " + id + " password : " + password);
		
		log.info("#### request.romote : " + request.getRemoteAddr());
		String ip = "";
		String agent = "";
		
		ip = loginService.getClientIP(request);
		agent = request.getHeader("User-Agent"); // 접속자 정보
		
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		String accessToken = "";		
		
		result = loginService.checkUser(id, password);
		result.put("ip", ip);
        
		// 아이디, 비밀번호가 맞으면
		if("OK".equals(result.get("result"))) {
			
			accessToken = tokenService.getSessionToken(result);
			
			result.put("userId", id);
			result.put("accessToken", accessToken);

			log.info("#### accessToken : " + accessToken);
			loginService.insertLoginLog(id, ip, agent);
			/** 
			 * 사용자 정보 or 권한 같은 것도 나중에 추가 됨
			 * 
			 * 
			 * 
			 * 
			 */
		}
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(result, HttpStatus.OK);
	    return rtn;
	}
	
	@GetMapping("/access-check")
	@ApiOperation(value= "엑세스 토큰 체크", notes="엑세스토큰 맞는지 아닌지 체크")
	public ResponseEntity<Map<Object, Object>> checkAccessToken(@RequestParam("id") String id, @RequestParam("accessToken") String accessToken, HttpServletRequest request) throws Exception {
		
		log.info("##### LoginController.checkAccessToken");
		log.info("##### id : " + id + " accessToken : " + accessToken);
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		
		String result = "";
		String authority = "";
		
		if(cryptoUtilService.checkAccessToken(id, accessToken)) {
			// authority 값 전달 추가
			result = "OK";
			authority = map.get("authority").toString();
		} else {
			result = "NO accessToken not correct";
		}
		
		log.info("#### accessToken authority : " + authority);
		map.put("result", result);
		map.put("authority", authority);
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(map, HttpStatus.OK);
		return rtn;
	}
	
	@GetMapping("/out")
	@ApiOperation(value="로그아웃", notes="사용자 로그아웃 시키기")
	public ResponseEntity<Map<Object, Object>> logout(HttpServletRequest request) throws Exception {
		
		log.info("logout 시도");

		Map<Object, Object> map = new HashMap<Object, Object>();
		
		redisService.remove(request.getHeader("accessToken"));
		log.info(request.getHeader("accessToken") + " logout 됨");
		
		map.put("err", 0);
		map.put("result", "로그아웃 되었습니다.");
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(map, HttpStatus.OK);
		return rtn;
	}
		
	@GetMapping("/msg")
	@ApiOperation(value="로그인 필요 요구", notes="로그인이 되어 있지 않다고 알려줌")
	public ResponseEntity<Map<Object, Object>> loginMsg() throws Exception {
		
		log.info("#### login need");
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		map.put("err", 401);
		map.put("result", "로그인이 필요합니다.");
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(map, HttpStatus.OK);
		return rtn;
	}
	
	@PutMapping("/pwd-reset")
	@ApiOperation(value="비밀번호 변경", notes="내가 쓸거임")
	public ResponseEntity<Map<Object, Object>> updatePwd(@RequestParam("idx") int idx, @RequestParam("pwd") String pwd) throws Exception {
		
		log.info("#### login updatePwd");
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(loginService.updatePwd(idx, pwd), HttpStatus.OK);
		return rtn;
	}
}
