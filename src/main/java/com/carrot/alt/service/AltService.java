package com.carrot.alt.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrot.center.dao.CenterVO;
import com.carrot.common.CommonEnum;
import com.carrot.common.mapper.alt.AltMapper;
import com.carrot.common.mapper.crm.CommonMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AltService {

	@Autowired
	private AltMapper altMapper;
	
	@Autowired
	private CommonMapper commonMapper;
	
	public Map<Object, Object> insertTalk(int idxCrmCenter, int templateId) throws Exception {
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> alt = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		CenterVO center = new CenterVO();
		String sql = "";
		
		param.put("id", templateId);
		sql = "SELECT * FROM crm_alt WHERE alt_id = #{param.id}";
		alt = commonMapper.selectOne(sql, param);
		
		param.put("idx", idxCrmCenter);
		center = commonMapper.selectCenter(param);
		
		data.put("MESSAGE_SEQNO", "TSMS_GET_SEQNO()");
		data.put("SERVICE_SEQNO", 2010051899);
		data.put("JOB_TYPE", "R00");
		data.put("REGISTER_BY", "CRM");
		data.put("SEND_MESSAGE", alt.get("alt_contents"));
		data.put("SUBJECT", alt.get("alt_subject"));
		data.put("BACKUP_MESSAGE", alt.get("alt_contents"));
		data.put("RECEIVE_MOBILE_NO", center.getPhone().replace("-", ""));
		data.put("CALLBACK_NO", CommonEnum.CELL.getValue());
		data.put("TEMPLATE_CODE", templateId);
		data.put("SEND_DATE", "DATE_FORMAT(NOW(), '%Y-%m-%d %T')");
		
		altMapper.insert("TSMS_ATALK_AGENT_MESSAGE", data);
		
		return result;
		
	}

	public Map<Object, Object> insertReserveTalk(String reserveDate,String reveiveMobileNo, int templateId) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> alt = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String sql = "";

		param.put("id", templateId);
		sql = "SELECT * FROM crm_alt WHERE alt_id = #{param.id}";
		alt = commonMapper.selectOne(sql, param);

		data.put("MESSAGE_SEQNO", "TSMS_GET_SEQNO()");
		data.put("SERVICE_SEQNO", 2010051899);
		data.put("JOB_TYPE", "R00");
		data.put("REGISTER_BY", "CRM");
		data.put("SEND_MESSAGE", alt.get("alt_contents").toString().replace("#{reserve_date}", reserveDate));
		data.put("SUBJECT", alt.get("alt_subject").toString().replace("#{reserve_date}", reserveDate));
		data.put("BACKUP_MESSAGE", alt.get("alt_contents").toString().replace("#{reserve_date}", reserveDate));
		data.put("RECEIVE_MOBILE_NO", reveiveMobileNo.replace("-", ""));
		data.put("CALLBACK_NO", CommonEnum.CELL.getValue());
		data.put("TEMPLATE_CODE", templateId);
		data.put("SEND_DATE", "DATE_FORMAT(NOW(), '%Y-%m-%d %T')");

		altMapper.insert("TSMS_ATALK_AGENT_MESSAGE", data);

		return result;

	}
}
