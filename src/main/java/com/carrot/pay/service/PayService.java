package com.carrot.pay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.mapper.crm.ReumMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.pay.dao.PayVO;
import com.carrot.reum.ReumService;

@Service
@Slf4j
public class PayService extends AbstractService {

  @Autowired
  ReumMapper reumMapper;

  @Autowired
  ReumService reumService;

	@Autowired
	private CommonMapper commonMapper;


  public Map<String, Object> getList(String name, int page, int pageSize) {
    Map<String, Object> param = Map.of("counselor", name);
    Map<String, Object> list = reumService
        .list("view_salary_price", param, page, pageSize);

    Map<String, Object> response = new HashMap<>();
    response.put("list", list);
    response.put("err", 0);
    return response;
  }

  public Map<Object, Object> selectPayStaffList(int idxCrmCenter,String centerName, String name, String startDate, String endDate, int startPage, int pageSize) throws Exception {

	  Map<Object, Object> result = new LinkedHashMap<Object, Object>();
	  Map<String, Object> param = new HashMap<String, Object>();
	  List<PayVO> payList = new ArrayList<PayVO>();

	  param.put("idxCrmCenter", idxCrmCenter);
	  param.put("centerName", centerName);
	  param.put("name", name);
	  param.put("startDate", startDate);
	  param.put("endDate", endDate);
	  param.put("startPage", startPage);
	  param.put("pageSize", pageSize);

	  payList = commonMapper.selectPayStaffList(param);

	  result.put("payList", payList);

	  //if(startPage == 0) {
	  result.put("payListSize", commonMapper.selectPayStaffListCount(param));
	  //}

	  if(payList.size() > 0) {
		  result.put("err", 0);
		  result.put("result", "OK");
	  } else {
		  result.put("err", 105);
		  result.put("result", "검색 결과가 없습니다.");
	  }

	  return result;
  }

  public Map<Object, Object> selectPayStaff(int idx, String date) throws Exception {

	  Map<Object, Object> result = new LinkedHashMap<Object, Object>();
	  Map<String, Object> param = new HashMap<String, Object>();
	  List<PayVO> payList = new ArrayList<PayVO>();
	  PayVO payTotal = new PayVO();
	  String sql = "";

	  param.put("idx", idx);
	  param.put("date", date);

	  payList = commonMapper.selectPayStaff(param);
	  payTotal = commonMapper.selectPayStaffTotal(param);
	  result.put("payList", payList);
	  result.put("payTotal", payTotal);

	  result.put("staff", commonMapper.selectUser(param));

	  if(payList.size() > 0) {
		  result.put("err", 0);
		  result.put("result", "OK");
	  } else {
		  result.put("err", 105);
		  result.put("result", "검색 결과가 없습니다.");
	  }

	  return result;
  }

public Map<Object, Object> selectPayCenterList(int idxCrmCenter, String startDate, String endDate, int startPage, int pageSize) throws Exception {

	  Map<Object, Object> result = new LinkedHashMap<Object, Object>();
	  Map<String, Object> param = new HashMap<String, Object>();
	  List<PayVO> payList = new ArrayList<PayVO>();

	  param.put("idxCrmCenter", idxCrmCenter);
	  param.put("startDate", startDate);
	  param.put("endDate", endDate);
	  param.put("startPage", startPage);
	  param.put("pageSize", pageSize);

	  payList = commonMapper.selectPayCenterList(param);

	  result.put("payList", payList);

	  //if(startPage == 0) {
		  result.put("payListSize", commonMapper.selectPayCenterListCount(param));
	  //}

	  if(payList.size() > 0) {
		  result.put("err", 0);
		  result.put("result", "OK");
	  } else {
		  result.put("err", 105);
		  result.put("result", "검색 결과가 없습니다.");
	  }

	  return result;
  }

	public Map<Object, Object> selectPayStaffDetail(int idx, String date, int idxCrmCenter) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<PayVO> payList = new ArrayList<PayVO>();

		param.put("idxCrmCenter", idxCrmCenter);
		param.put("idxCrmStaff", idx);
		param.put("date", date);
		payList = commonMapper.selectPayStaffDetail(param);

		if(payList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "일지내역이 없습니다.");
		}

		result.put("payList", payList);

		return result;
	}



}
