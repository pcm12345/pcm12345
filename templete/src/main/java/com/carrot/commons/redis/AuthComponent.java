package com.carrot.commons.redis;

import com.carrot.commons.common.Cryptos;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthComponent {

	private final RedisTemplate redisTemplate;
	
	@Autowired
  RedisComponent redisComponent;

	public AuthComponent(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public String getSessionToken(String id, String idx) throws Exception {
		
		String idEnc = Cryptos.encryptAES256(id);
		
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		map.put("id", id);
		map.put("idx", idx);
		
		//redisService.setEx(idEnc, map.toString(), 500L); map형식 안쓸거면 이걸 써도 됨 key, String value 형태
		//redisService.get(idEnc); 가져올 때는 get으로 Hash는 entries로 가져옴
		
		redisTemplate.opsForHash().putAll(idEnc, map); // idEnc라는 key값으로 map의 내용을 담음 key, Map value 형태
		redisTemplate.expire(idEnc, 5, TimeUnit.MINUTES); // 세션만료시간 1분
		return idEnc;
		
	}

	public boolean checkAccessToken(HttpServletRequest request) throws Exception {
		//log.info(">>>> checkAccessToken adminId : " + id + " accessToken : " + accessToken);
		log.info("#### request : " + request.getHeader("accessToken"));
		boolean result = false;
		String idDec = "";
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		// accessToken을 Key로 가진 session이 있는지 확인
		if(redisComponent.exists(request.getHeader("accessToken"))) {
			idDec = Cryptos.decryptAES256(request.getHeader("accessToken"));
			map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
			if(map.get("id").equals(idDec)) {
				// 	expire 해줘서 만료시간 늘려주기
				redisTemplate.expire(request.getHeader("accessToken"),1, TimeUnit.MINUTES); // 세션만료 시간 늘려주기
				result = true;
			} else {
				log.info("로그인한 id값이 다릅니다.");
			}
		} else { 
			log.info("accessToken 없음.");
		}
		
		return result;
		
	}
    
}
