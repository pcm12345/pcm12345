package com.carrot.status.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.status.dao.StatusVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatusService extends AbstractService {
	
	@Autowired
	private CommonMapper commonMapper;

	public Map<Object, Object> selectMainStatusStaff(String name, int idxCrmCompany, int idxCrmCenter, String clientType, String startDate, String endDate) throws Exception {
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		
		List<StatusVO> statusList = new ArrayList<StatusVO>();
		String sql = "";
		
		param.put("name", name);
		param.put("idxCrmCompany", idxCrmCompany);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("clientType", clientType);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		statusList = commonMapper.selectMainStatusStaff(param);
		
		// 내담자 수 따로 count
		/*
		for(int i=0;i<statusList.size();i++) {
			param.clear();
			param.put("idxCrmStaff", statusList.get(i).getIdx());
			param.put("idxCrmCompany", idxCrmCompany);
			param.put("idxCrmCenter", idxCrmCenter);
			param.put("type", "staff");
			statusList.get(i).setClientCnt(commonMapper.selectClientCnt(param));
			param.put("clientCnt", statusList.get(i).getClientCnt());
			param.put("reportCnt", statusList.get(i).getReportCnt());
			statusList.get(i).setTurnoverRate(commonMapper.selectStatusRate(param));
		}
		*/
		if(statusList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "데이터가 없습니다.");
		}
		result.put("status", statusList);
		return result;
		
	}

	public Map<Object, Object> selectMainStatusProduct(int idxCrmCompany, int idxCrmCenter, String clientType, String startDate, String endDate) throws Exception {
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		
		List<StatusVO> statusList = new ArrayList<StatusVO>();
		String sql = "";
		
		param.put("idxCrmCompany", idxCrmCompany);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("clientType", clientType);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		statusList = commonMapper.selectMainStatusProduct(param);
		
		// 내담자 수 따로 count
		/*for(int i=0;i<statusList.size();i++) {
			param.clear();
			param.put("gubun", statusList.get(i).getType());
			param.put("idxCrmCompany", idxCrmCompany);
			param.put("idxCrmCenter", idxCrmCenter);
			param.put("type", "product");
			statusList.get(i).setClientCnt(commonMapper.selectClientCnt(param));
			param.put("clientCnt", statusList.get(i).getClientCnt());
			param.put("reportCnt", statusList.get(i).getReportCnt());
			statusList.get(i).setTurnoverRate(commonMapper.selectStatusRate(param));
		}*/
		
		if(statusList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "데이터가 없습니다.");
		}
		result.put("status", statusList);
		return result;
		
	}
	
	public Map<Object, Object> selectMainStatusType(int idxCrmCompany, int idxCrmCenter, String clientType, String startDate, String endDate) throws Exception {
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		
		List<StatusVO> statusList = new ArrayList<StatusVO>();
		
		param.put("idxCrmCompany", idxCrmCompany);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("clientType", clientType);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		statusList = commonMapper.selectMainStatusType(param);
		
		// 내담자 수 따로 count
		/*for(int i=0;i<statusList.size();i++) {
			param.clear();
			param.put("gubun", statusList.get(i).getType());
			param.put("idxCrmCompany", idxCrmCompany);
			param.put("idxCrmCenter", idxCrmCenter);
			param.put("type", "type");
			statusList.get(i).setClientCnt(commonMapper.selectClientCnt(param));
			param.put("clientCnt", statusList.get(i).getClientCnt());
			param.put("reportCnt", statusList.get(i).getReportCnt());
			statusList.get(i).setTurnoverRate(commonMapper.selectStatusRate(param));
		}*/
				
		if(statusList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "데이터가 없습니다.");
		}
		result.put("status", statusList);
		return result;
		
	}
	
	public Map<Object, Object> selectMainStatusSubject(int idxCrmCompany, int idxCrmCenter, String clientType, String startDate, String endDate) throws Exception {
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		
		List<StatusVO> statusList = new ArrayList<StatusVO>();
		
		param.put("idxCrmCompany", idxCrmCompany);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("clientType", clientType);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		statusList = commonMapper.selectMainStatusSubject(param);
		
		// 내담자 수 따로 count
		/*for(int i=0;i<statusList.size();i++) {
			param.clear();
			param.put("gubun", statusList.get(i).getType());
			param.put("idxCrmCompany", idxCrmCompany);
			param.put("idxCrmCenter", idxCrmCenter);
			param.put("type", "subject");
			statusList.get(i).setClientCnt(commonMapper.selectClientCnt(param));
			param.put("clientCnt", statusList.get(i).getClientCnt());
			param.put("reportCnt", statusList.get(i).getReportCnt());
			statusList.get(i).setTurnoverRate(commonMapper.selectStatusRate(param));
		}*/
		
		if(statusList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "데이터가 없습니다.");
		}
		result.put("status", statusList);
		return result;
		
	}
	
	public Map<Object, Object> selectMainStatusGender(int idxCrmCompany, int idxCrmCenter, String clientType, String startDate, String endDate) throws Exception {
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		
		List<StatusVO> statusList = new ArrayList<StatusVO>();
		
		param.put("idxCrmCompany", idxCrmCompany);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("clientType", clientType);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		statusList = commonMapper.selectMainStatusGender(param);
		
		// 내담자 수 따로 count
		/*for(int i=0;i<statusList.size();i++) {
			param.clear();
			param.put("gubun", statusList.get(i).getType());
			param.put("idxCrmCompany", idxCrmCompany);
			param.put("idxCrmCenter", idxCrmCenter);
			param.put("type", "gender");
			statusList.get(i).setClientCnt(commonMapper.selectClientCnt(param));
			param.put("clientCnt", statusList.get(i).getClientCnt());
			param.put("reportCnt", statusList.get(i).getReportCnt());
			statusList.get(i).setTurnoverRate(commonMapper.selectStatusRate(param));
		}*/
		
		if(statusList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "데이터가 없습니다.");
		}
		result.put("status", statusList);
		return result;
		
	}
	
	public Map<Object, Object> selectMainStatusAge(int idxCrmCompany, int idxCrmCenter, String clientType, String startDate, String endDate) throws Exception {
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		
		List<StatusVO> statusList = new ArrayList<StatusVO>();
		
		param.put("idxCrmCompany", idxCrmCompany);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("clientType", clientType);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		statusList = commonMapper.selectMainStatusAge(param);
		
		// 내담자 수 따로 count
		/*for(int i=0;i<statusList.size();i++) {
			param.clear();
			param.put("gubun", statusList.get(i).getIdx());
			param.put("idxCrmCompany", idxCrmCompany);
			param.put("idxCrmCenter", idxCrmCenter);
			statusList.get(i).setClientCnt(commonMapper.selectClientAgeCnt(param));
			param.put("clientCnt", statusList.get(i).getClientCnt());
			param.put("reportCnt", statusList.get(i).getReportCnt());
			statusList.get(i).setTurnoverRate(commonMapper.selectStatusRate(param));
		}*/
		
		if(statusList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "데이터가 없습니다.");
		}
		result.put("status", statusList);
		return result;
		
	}
	
	public Map<Object, Object> selectMainStatusRoute(int idxCrmCompany, int idxCrmCenter, String clientType, String startDate, String endDate) throws Exception {
		
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		
		List<StatusVO> statusList = new ArrayList<StatusVO>();
		
		param.put("idxCrmCompany", idxCrmCompany);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("clientType", clientType);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		statusList = commonMapper.selectMainStatusRoute(param);
		
		// 내담자 수 따로 count
		/*for(int i=0;i<statusList.size();i++) {
			param.clear();
			param.put("gubun", statusList.get(i).getType());
			param.put("idxCrmCompany", idxCrmCompany);
			param.put("idxCrmCenter", idxCrmCenter);
			param.put("type", "route");
			statusList.get(i).setClientCnt(commonMapper.selectClientCnt(param));
			param.put("clientCnt", statusList.get(i).getClientCnt());
			param.put("reportCnt", statusList.get(i).getReportCnt());
			statusList.get(i).setTurnoverRate(commonMapper.selectStatusRate(param));
		}*/
		
		if(statusList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "데이터가 없습니다.");
		}
		result.put("status", statusList);
		return result;
		
	}
}
