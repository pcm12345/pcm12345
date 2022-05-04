package com.carrot.admin.service;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carrot.alt.service.AltService;
import com.carrot.client.dao.ClientVO;
import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.mapper.crm.ReumMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.common.service.CryptoUtilService;
import com.carrot.cons.dao.ReservationVO;
import com.carrot.reum.ReumService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService extends AbstractService {

  @Autowired
  private CommonMapper commonMapper;

  @Autowired
  private ReumService reumService;

  @Autowired
  private ReumMapper reumMapper;
  
  @Autowired
  private CryptoUtilService cryptoUtilService;
  
  @Autowired
  private AltService altService;

  public Map<String, Object> registerAdmin(Map<String, Object> params) throws Exception {
    Map<String, Object> response;

    /// 아이디 중복검사
    String id = params.get("id") + "";
    Map<String, Object> info = reumService.info("crm_staff", id, "id");

    boolean b = info == null;
    if (info == null) {
      /// 암호해쉬화
      String pwd =  cryptoUtilService.encryptAES256(params.get("pwd") + "");
      params.put("pwd", pwd);

      /// 관리자 데이터 추가
      reumService.insData("crm_staff", params);
      response = Map.of("err", 0);
    } else {
      response = Map.of("err", 101, "err_msg", "이미 존재하는 관리자 아이디 입니다.");
    }

    return response;
  }

  public Map<String, Object> getStaffList(int page, int pageSize) {
    /// 페이지네이션
    String limit = " limit " + (page-1) * pageSize + ", " + pageSize;
    int size = reumMapper
        .queryInt("select count(*) from crm_staff where authority = 'ADMIN'");

    String query = String.format(
        "select * from crm_staff where authority = 'ADMIN' ORDER BY create_date DESC %s", limit);
    List<Map<Object, Object>> list = reumMapper.queryAll(query);

    Map<String, Object> response = new HashMap<>();
    response.put("list", list);
    if(list.size() > 0 ) { 
    	response.put("err", 0);
    } else {
    	response.put("err", 105);
    }
    response.put("size", size);
    return response;
  }

  public Map<String, Object> modifyAdmin(int idx, Map<String, Object> data) throws Exception {
    /// 암호해쉬화
    String pwd =  cryptoUtilService.encryptAES256(data.get("pwd") + "");
    data.put("pwd", pwd);

    reumMapper.upData("crm_staff", data, "idx = " + idx);

    Map<String, Object> response = new HashMap<>();
    response.put("err", 0);
    return response;
  }
  
  public Map<Object, Object> updatePermission(String type, String idx) throws Exception {
		
	Map<Object, Object> result = new LinkedHashMap<Object, Object>();
	Map<Object, Object> report = new LinkedHashMap<Object, Object>();
	Map<Object, Object> client = new LinkedHashMap<Object, Object>();
	Map<String, Object> param = new HashMap<String, Object>();
	Map<String, Object> data = new HashMap<String, Object>();
	String sql = "";
	String table = "";
	String[] arrayIdx = idx.split("/");
	
	for(int i=0;i<arrayIdx.length;i++) {
		
		data.clear();
		param.clear();
		
		if("STAFF".equals(type)) {											// 상담사 승인
			table = "crm_staff";
			data.put("permission", "Y");
			param.put("idx", arrayIdx[i]);
			commonMapper.update(table, data, " idx = #{param.idx}", param);
			
		} else if ("CENTER".equals(type)) {								// 협약센터 승인
			table = "crm_center";
			data.put("permission", "Y");
			param.put("idx", arrayIdx[i]);
			commonMapper.update(table, data, " idx = #{param.idx}", param);
			// 알림톡 승인나고 테스트 되는지 보고 주석 풀어줌 것 HJ
			altService.insertTalk(Integer.parseInt(idx), 15539);
			
			param.clear();
			param.put("idxCrmCenter", arrayIdx[i]);
			commonMapper.update("crm_staff", data, " idx_crm_center = #{param.idxCrmCenter} AND authority = 'CENTERDIRECTOR'", param);
			
		} else if("REPORT".equals(type)){																		// 일지 승인

			Date nowDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			table = "crm_report";
			data.put("permission", "Y");
			data.put("process_status", "완료");
			data.put("permission_date", simpleDateFormat.format(nowDate));
			param.put("idx", arrayIdx[i]);
			commonMapper.update(table, data, " idx = #{param.idx}", param);
			
			sql = "SELECT * FROM crm_report WHERE idx = #{param.idx}";
			
			report = commonMapper.selectOne(sql, param);
			// 일지 승인하면 회기횟수 차감 해주기 추가
			param.clear();
			data.clear();
			param.put("idx", report.get("idx_crm_client"));
			sql = "SELECT * FROM crm_client WHERE idx = #{param.idx}";
			client = commonMapper.selectOne(sql, param);
			
			data.put("cons_count_use", Integer.parseInt(client.get("cons_count_use").toString()) + 1);
			data.put("cons_count_rest", Integer.parseInt(client.get("cons_count_rest").toString()) - 1);
			log.info("#### 총회기 : " + client.get("cons_count") + " 사용회기 : " + client.get("cons_count_use") + " 남은회기 : " + client.get("cons_count_rest"));
			// crm_client 
			commonMapper.update("crm_client", data, " idx = #{param.idx}", param);
		}
		
	}
	
	result.put("err", 0);
	result.put("result", "승인되었습니다.");
	
	return result;
  }

  public Map<Object, Object> sendSms(int idx) throws Exception {
	  
	  Map<Object, Object> result = new LinkedHashMap<Object, Object>();
	  Map<String, Object> data = new HashMap<String, Object>();
	  
	  
	  return result;
  }
  
  public Map<Object, Object> updatePermissionOnce(String idxCrmReport) throws Exception {
	  
	  Map<Object, Object> result = new LinkedHashMap<Object, Object>();
	  Map<String, Object> data = new LinkedHashMap<String, Object>();
	  Map<String, Object> param = new LinkedHashMap<String, Object>();
	  
	  String[] idxArray = idxCrmReport.split("/");
	  
	  for(int i=0;i<idxArray.length;i++) {
		  data.put("permission", "Y");
		  data.put("process_status", "완료");
		  param.put("idx", idxArray[i]);
		  
		  commonMapper.update("crm_report", data, " idx = #{param.idx}", param);
		  data.clear();
		  param.clear();
	  }
	  
	  result.put("err", 0);
	  result.put("result", "승인되었습니다.");
		
	  return result;
	  
  }
  
  public Map<Object, Object> selectCenterStatus(int idxCrmCenter, String startDate, String endDate) throws Exception {
	  
	  Map<Object, Object> result = new LinkedHashMap<Object, Object>();
	  Map<String, Object> param = new HashMap<String, Object>();
	  ClientVO clientCnt = new ClientVO();
	  List<ReservationVO> reservationList = new ArrayList<ReservationVO>();
	  
	  param.put("idxCrmCenter", idxCrmCenter);
	  param.put("startDate", startDate);
	  param.put("endDate", endDate);
	  
	  clientCnt = commonMapper.selectCenterStatusClientNum(param);
	  reservationList = commonMapper.selectCenterStatusStaffConsNum(param);
	  
	  result.put("err", 0);
	  result.put("result", "OK.");
	  result.put("clientCnt", clientCnt);
	  result.put("reservationList", reservationList);
	  
	  return result;
  }
}
