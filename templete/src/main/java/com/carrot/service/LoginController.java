package com.carrot.service;

import com.carrot.commons.common.Cryptos;
import com.carrot.commons.redis.RedisComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Slf4j
@Api("로그인처리")
public class LoginController {

	private final LoginService loginService;
	private final RedisComponent redisComponent;

	public LoginController(LoginService loginService,
			RedisComponent redisComponent) {
		this.loginService = loginService;
		this.redisComponent = redisComponent;
	}

	@GetMapping("")
	@ApiOperation(value="사용자 로그인", notes="로그인 처리")
	public ResponseEntity<?> login(@RequestParam("id") String id, @RequestParam("password") String password, HttpServletRequest request) {
		
		log.info("#### UserController.login id : " + id + " password : " + password);

		Map<Object, Object> result = new HashMap<>();
		String accessToken = "";		
		//HttpSession session = request.getSession();
		
		result = loginService.checkUser(id, password);

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(result, HttpStatus.OK);
	    return rtn;
	}
	
	@GetMapping("/access-check")
	@ApiOperation(value= "엑세스 토큰 체크", notes="엑세스토큰 맞는지 아닌지 체크")
	public ResponseEntity<?> checkAccessToken(@RequestParam("id") String id, @RequestParam("accessToken") String accessToken)
			throws Exception {
		
		log.info("##### KuderLoginController.checkAccessToken");
		log.info("##### id : " + id + " accessToken : " + accessToken);
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		String result = "";
		if(Cryptos.checkAccessToken(id, accessToken)) {
			result = "OK";
		} else {
			result = "NO accessToken not correct";
		}
		
		map.put("result", result);
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(map, HttpStatus.OK);
		return rtn;
	}
	
	@GetMapping("/out")
	@ApiOperation(value="로그아웃", notes="사용자 로그아웃 시키기")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		
		log.info("logout 시도");

		Map<Object, Object> map = new HashMap<Object, Object>();
		
		redisComponent.remove(request.getHeader("accessToken"));
		log.info(request.getHeader("accessToken") + " logout 됨");
		
		map.put("err", 0);
		map.put("result", "로그아웃 되었습니다.");
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(map, HttpStatus.OK);
		return rtn;
	}
		
	@GetMapping("/msg")
	@ApiOperation(value="로그인 필요 요구", notes="로그인이 되어 있지 않다고 알려줌")
	public ResponseEntity<?> loginMsg() {
		
		log.info("#### login need");
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		map.put("err", 401);
		map.put("result", "로그인이 필요합니다.");
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(map, HttpStatus.OK);
		return rtn;
	}
	
}
