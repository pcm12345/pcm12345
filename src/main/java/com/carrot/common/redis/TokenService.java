package com.carrot.common.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.carrot.common.service.CryptoUtilService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenService {

	@Autowired
    private RedisTemplate redisTemplate;

	@Autowired
	RedisService redisService;

	/**
	 * accessToken 암호화
	 * @param id
	 * @param
	 * @return
	 * @throws Exception
	 */
	public String getSessionToken(Map<Object, Object> result) throws Exception {

		String idEnc = CryptoUtilService.encryptAES256(result.get("id").toString());

		HashMap<Object, Object> map = new HashMap<Object, Object>();
		map.put("id", result.get("id"));
		map.put("idx", result.get("idx"));
		map.put("idxCrmCenter", result.get("idxCrmCenter"));
		map.put("authority", result.get("authority"));

		// idxCrmStaff idxCrmCenter 등의 정보도 넣어 줘야함 CRM

		//redisService.setEx(idEnc, map.toString(), 500L); map형식 안쓸거면 이걸 써도 됨 key, String value 형태
		// redisService.get(idEnc); 가져올 때는 get으로 Hash는 entries로 가져옴

		redisTemplate.opsForHash().putAll(idEnc, map); // idEnc라는 key값으로 map의 내용을 담음 key, Map value 형태
		redisTemplate.expire(idEnc, 720, TimeUnit.MINUTES); // 세션만료시간 12시간 으로 변경 60*12
		return idEnc;

	}

	/**
	 * accessToken 복호화
	 * @param id
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public boolean checkAccessToken(HttpServletRequest request) throws Exception {
		//log.info(">>>> checkAccessToken adminId : " + id + " accessToken : " + accessToken);
		log.info("#### request : " + request.getHeader("accessToken"));
		boolean result = false;
		String idDec = "";

		Map<Object, Object> map = new HashMap<Object, Object>();

		// accessToken을 Key로 가진 session이 있는지 확인
		if(request.getHeader("accessToken") != null && redisService.exists(request.getHeader("accessToken"))) {
			idDec = CryptoUtilService.decryptAES256(request.getHeader("accessToken"));
			map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
			if(map.get("id").equals(idDec)) {
				// 	expire 해줘서 만료시간 늘려주기
				redisTemplate.expire(request.getHeader("accessToken"), 720, TimeUnit.MINUTES); // 세션만료 시간 늘려주기
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
