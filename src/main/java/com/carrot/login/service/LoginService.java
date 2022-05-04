package com.carrot.login.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrot.common.CommonEnum;
import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.common.service.CryptoUtilService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService extends AbstractService {
	
	@Autowired
	private CommonMapper commonMapper;
	
	@Autowired
	private CryptoUtilService cryptoUtilService;

	public Map<Object, Object> checkUser(String id, String password) throws Exception {
		
		String sql ="";
		Map<String, Object> param = new HashMap<String, Object>();
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		
		sql = "SELECT idx"
				+ ", id"
				+ ", name"
				+ ", pwd, idx_crm_center, authority FROM crm_staff WHERE id = #{param.id} AND permission = 'Y'";
		param.put("id", id);
		
		Map<Object, Object> user = commonMapper.selectOne(sql, param);
		
		if(user == null) {
			result.put("err", 104);
			result.put("result", "존재하지 않는 ID 이거나 승인받지 않은 ID입니다.");
		} else {
			if(password.equals(cryptoUtilService.decryptAES256(user.get("pwd").toString()))) {				
				result.put("err", 0);
				result.put("idx", user.get("idx"));
				result.put("id", user.get("id"));
				result.put("idxCrmCenter", user.get("idx_crm_center"));
				result.put("authority", user.get("authority"));
				result.put("result", "OK");
			} else {
				result.put("err", 140);
				result.put("result", "잘못된 패스워드 입니다.");
			}
		}
		
		return result;		
	}
	
	public Map<Object, Object> updatePwd(int idx, String pwd) throws Exception {
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("idx", idx);
		data.put("pwd", cryptoUtilService.encryptAES256(pwd));
		
		commonMapper.update("crm_staff", data, " idx = #{param.idx}", param);
		result.put("err", 0);
		result.put("result", "패스워드 변경함");

		return result;	
	}
	
	public void insertLoginLog(String id, String ip, String agent) throws Exception {
		
		log.info("#### 로그인 로그 등록");
		log.info("#### id : " + id + " ip : " + ip + " agent : " + agent);
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("id", id);
		data.put("ip", ip);
		data.put("agent", agent);
		
		commonMapper.insert("crm_login_log", data);
		
	}
	
}
