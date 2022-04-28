package com.carrot.service;

import com.carrot.commons.common.Commons;
import com.carrot.mapper.main.CommonMapper;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService extends CarrotService {
	
	@Autowired
	private CommonMapper commonMapper;

	public Map<Object, Object> checkUser(String id, String password) {
		
		String sql ="";
		Map<String, Object> param = new HashMap<String, Object>();
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		
		sql = "select idx, id, password from user where id = #{param.id}";
		param.put("id", id);
		
		Map<Object, Object> user = commonMapper.selectOne(sql, param);
		
		if(user == null) {
			result.put("err", 104);
			result.put("result", "존재하지 않는 ID 입니다.");
		} else {
			if(Commons.bcryptCheck(password, user.get("password").toString())) {
				result.put("err", 0);
				result.put("idx", user.get("idx"));
				result.put("id", user.get("id"));
				result.put("result", "OK");
			} else {
				result.put("err", 140);
				result.put("result", "잘못된 패스워드 입니다.");
			}
		}
		
		return result;		
	}


	// 아이디, 비밀번호가 맞으면
		/*if("OK".equals(result.get("result"))) {

		accessToken = authComponent.getSessionToken(id, result.get("idx").toString());

		result.put("userId", id);
		result.put("accessToken", accessToken);

		log.info("#### accessToken : " + accessToken);

		*//**
		 * 사용자 정보 or 권한 같은 것도 나중에 추가 됨
		 *
		 *
		 *
		 *
		 *//*
	}
*/
}
