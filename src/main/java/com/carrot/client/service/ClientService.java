package com.carrot.client.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;

import com.carrot.alt.service.AltService;
import com.carrot.client.dao.ClientVO;
import com.carrot.client.dao.MemoVO;
import com.carrot.client.dao.PersonVO;
import com.carrot.client.dao.PurchaseVO;
import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.cons.dao.ReportVO;
import com.carrot.reum.ReumService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClientService extends AbstractService {

	@Autowired
	private CommonMapper commonMapper;

	@Autowired
	private ReumService reumService;

	@Autowired
	private AltService altService;

	public Map<Object, Object> insertMemo(MemoVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String table = "crm_memo";

		data.put("idx_crm_person", vo.getIdxCrmPerson());
		data.put("memo", vo.getMemo());
		data.put("idx_crm_staff_create", vo.getIdxCrmStaffCreate());

		commonMapper.insert(table, data);

		result.put("err", 0);
		result.put("result", "OK");

		return result;
	}

	public Map<Object, Object> selectMemoList(int idxCrmPerson, int idxCrmStaff) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<MemoVO> memoList = new ArrayList<MemoVO>();

		param.put("idxCrmPerson", idxCrmPerson);
		param.put("idxCrmStaff", idxCrmStaff);
		memoList = commonMapper.selectMemoList(param);

		if(memoList.size() > 0) {
			for(int i=0;i<memoList.size();i++) {
				memoList.get(i).setFormatCreateDate(formatDateToString(memoList.get(i).getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
			}
		}

		result.put("err", 0);
		result.put("result", "OK");
		result.put("memoList", memoList);

		return result;
	}

	public Map<Object, Object> deleteMemo(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();

		param.put("idx", idx);

		commonMapper.delete("crm_memo", param);

		result.put("err", 0);
		result.put("result", "?????????????????????.");

		return result;
	}
	public Map<Object, Object> duplicatPerson(String phone) throws Exception {

		log.info("#### duplicatPerson phone : " + phone);
		String sql ="";
		Map<String, Object> param = new HashMap<String, Object>();
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();

		sql = "SELECT count(*) FROM crm_person WHERE phone = #{param.phone}";
		param.put("phone", phone);

		int count = commonMapper.selectInt(sql, param);

		if(count < 1) {
			result.put("err", 0);
			result.put("result", "????????? ??? ?????? ?????????????????????.");
		} else {
			result.put("err", 100);
			result.put("result", "?????? ???????????? ???????????? ?????????");
		}

		return result;
	}

	public Map<Object, Object> insertPerson(PersonVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();

		commonMapper.insertPerson(vo);

		result.put("err", 0);
		result.put("result",  "OK");
		result.put("idxCrmPerson", vo.getIdx());

		return result;
	}

	public Map<Object, Object> updatePerson(int idx, PersonVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";
		List<Map<Object, Object>> clientList = new ArrayList<Map<Object,Object>>();

		vo.setIdx(idx);
		commonMapper.updatePerson(vo);

		data.put("name", vo.getName());
		data.put("gender", vo.getGender());
		data.put("birth", vo.getBirth());
		data.put("email", vo.getEmail());
		data.put("idx_crm_meta_route", vo.getIdxCrmMetaRoute());
		data.put("phone", vo.getPhone());

		param.put("idxCrmPerson", idx);

		commonMapper.update("crm_client", data, " idx_crm_person = #{param.idxCrmPerson}", param);

		sql = "SELECT idx FROM crm_client WHERE idx_crm_person = #{param.idxCrmPerson}";

		clientList = commonMapper.selectAll(sql, param);

		if(clientList.size() > 0) {
			data.clear();
			data.put("name", vo.getName());

			for(int i=0;i<clientList.size();i++) {
				param.clear();
				param.put("idxCrmClient", clientList.get(i).get("idx"));
				commonMapper.update("crm_reservation", data, " idx_crm_client = #{param.idxCrmClient}", param);
			}
		}

		result.put("err", 0);
		result.put("result",  "OK");

		return result;
	}

	public Map<Object, Object> selectPerson(int idx, int idxCrmStaff) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> status = new ArrayList<>();
		Map<String, Object> param = new HashMap<String, Object>();
		PersonVO personVO = new PersonVO();
		String sql = ""; // ???????????? ????????? ??????????????? ??????(???????????? ????????? ?????? ???)

		param.put("idx", idx);
		param.put("idxCrmStaff", idxCrmStaff);

		personVO = commonMapper.selectPerson(param);

		result.put("err", 0);
		result.put("result",  "OK");
		result.put("personVO",  personVO);

		param.put("idxCrmStaff", idxCrmStaff);
		sql = "SELECT client_status FROM crm_client WHERE client_status is not null AND idx_crm_person = #{param.idx} AND idx_crm_staff = #{param.idxCrmStaff}";
		status = commonMapper.selectAll(sql, param);
		if(status.size() > 0) {
			for(int i=0;i<status.size();i++) {
				if("??????".equals(status.get(i).get("client_status").toString())) {
					result.put("status", status.get(i).get("client_status"));
					result.put("idxCrmStaff", idxCrmStaff);
				} else {
					result.remove("idxCrmStaff");
				}
			}
		}
		return result;
	}

	public Map<Object, Object> selectPersonList(String clientStatus, int idxCrmCenter, int startPage, int pageSize, String startDate, String endDate, String value) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<PersonVO> personList = new ArrayList<PersonVO>();
//idxCrmCenter
		param.put("startPage", startPage);
        param.put("pageSize", pageSize);
		param.put("clientStatus", clientStatus);
		param.put("idxCrmCenter", idxCrmCenter);
        param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("value", value); //??????, ?????????, ????????? ??????

		personList = commonMapper.selectPersonList(param);

		result.put("personListCount", commonMapper.selectPersonListCount(param));

		if(personList.size() > 0) {
			result.put("err", 0);
			result.put("result",  "OK");
		} else {
			result.put("err", 105);
			result.put("result",  "?????? ????????? ????????????.");
		}

		result.put("personList", personList);

		return result;
	}

	public Map<Object, Object> insertClient(ClientVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> centerList = new ArrayList<Map<Object,Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String sql = "";
		int count = 0;
		//if(vo.getIdxCrmCompany() == null || "".equals(vo.getIdxCrmCompany()))
		if(vo.getIdxCrmCompany() != 0 ) { //??????????????????0??? ????????? ???????????????
			vo.setClientType("B2B");
		} else {
			vo.setClientType("B2C");
		}
		if(!"Y".equals(vo.getEapYn())) {
			vo.setBirth(formatDateToString(formatStringToDate(vo.getBirth(), "yyyy-MM-dd"), "yyyy-MM-dd"));
		}

		

		//??????????????? ?????? ????????? client_code ?????? ????????????????????? ??????.
		//client_code ?????? idx+1000 ??????.
		log.info("#### client Code : " + vo.getClientCode());
		commonMapper.insertClient(vo);

		vo.setClientCode(Integer.toString(vo.getIdx()+1000));
		commonMapper.updateClient(vo);

		result.put("idxCrmClient", vo.getIdx());

		// ???????????? ??? ?????????????????? ????????? ???????????? ??????
		// ????????? ???????????? ?????? ?????? ?????? ??? ??? HJ
		if("Y".equals(vo.getEapYn())) {
			param.clear();
			param.put("area", vo.getEapHopeArea());
			param.put("area2", vo.getEapHopeArea2());
//			sql = "SELECT * FROM crm_center cc WHERE permission = 'Y' AND address REGEXP (#{param.area} | #{param.area2})";
			sql = "SELECT * FROM crm_center cc WHERE permission = 'Y' AND address REGEXP (CONCAT(#{param.area}, '+')) AND address REGEXP(CONCAT(#{param.area2}, '+')) ";
			centerList = commonMapper.selectAll(sql, param);
			log.info("#### centerList : " + centerList.toString());

			for(int i=0;i<centerList.size();i++) {
				altService.insertTalk(Integer.parseInt(centerList.get(i).get("idx").toString()), 15537);
			}
		}

		result.put("err", 0);
		result.put("result", "?????? ???????????????");

		return result;
	}

	public Map<Object, Object> selectChartList(int idxCrmPerson, int idxCrmStaff) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<ClientVO> chartList = new ArrayList<ClientVO>();

		param.put("idxCrmPerson", idxCrmPerson);
		param.put("idxCrmStaff", idxCrmStaff);
		chartList = commonMapper.selectChartList(param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("chartList", chartList);

		return result;

	}

	public Map<Object, Object> selectClient(int idx, String reportYn) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		ClientVO clientVO = new ClientVO();
		List<ReportVO> reportList = new ArrayList<ReportVO>();

		param.put("idx", idx);

		// ????????????
		clientVO = commonMapper.selectClient(param);

		// ??????????????????
		param.put("startPage", -1);
		param.put("idxCrmClient", idx);
		if("Y".equals(reportYn)) {
			reportYn = "??????";
		} else if("N".equals(reportYn)) {
			reportYn = "?????????";
		}
		param.put("reportYn", reportYn);
		reportList = commonMapper.selectReportList(param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("clientVO", clientVO);
		result.put("reportList", reportList);

		return result;
	}

	public Map<Object, Object> updateClient(int idx, ClientVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> client = new LinkedHashMap<Object, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		PersonVO personVO = new PersonVO();
		List<Map<Object, Object>> clientList = new ArrayList<Map<Object,Object>>();
		String sql = "";
		int reportCnt = 0; // ?????? ??????????????? ?????? ????????? ?????? ?????? ?????? ???

		sql = "SELECT cons_count_use FROM crm_client WHERE idx = #{param.idx}";
		param.put("idx", idx);

		client = commonMapper.selectOne(sql, param);
		/* ????????? ????????? ????????? ????????? ?????? ??? ????????? ????????? ?????? ????????? 2020.12.30
		if(Integer.parseInt(client.get("cons_count_use").toString()) > 0) {

			result.put("err", 105);
			result.put("result", "?????? ????????? ?????? ??? ??????????????? ????????? ??? ????????????.");

		} else {
		*/

			vo.setIdx(idx);

			//if(vo.getIdxCrmCompany() == null || "".equals(vo.getIdxCrmCompany()))
			 if(vo.getIdxCrmCompany() != 0){
				vo.setClientType("B2B");
			} else {
				vo.setClientType("B2C");
			}

			if(!"Y".equals(vo.getEapYn())) {
				vo.setBirth(formatDateToString(formatStringToDate(vo.getBirth(), "yyyy-MM-dd"), "yyyy-MM-dd"));
			}


			sql ="select count(*) from crm_report where idx_crm_client = #{param.idx} and permission = 'Y'";
			reportCnt = commonMapper.selectInt(sql, param);
			vo.setConsCountUse(reportCnt);
			vo.setConsCountRest(vo.getConsCount() - reportCnt);
			commonMapper.updateClient(vo);

			// crm_person ?????? ??????
			param.clear();
			data.put("name", vo.getName());
			data.put("gender", vo.getGender());
			data.put("birth", vo.getBirth());
			data.put("email", vo.getEmail());
			data.put("idx_crm_meta_route", vo.getIdxCrmMetaRoute());
			data.put("phone", vo.getPhone());

			param.put("idxCrmPerson", vo.getIdxCrmPerson());

			commonMapper.update("crm_person", data, " idx = #{param.idxCrmPerson}", param);
			commonMapper.update("crm_client", data, " idx_crm_person = #{param.idxCrmPerson}", param);

			sql = "SELECT idx FROM crm_client WHERE idx_crm_person = #{param.idxCrmPerson}";

			clientList = commonMapper.selectAll(sql, param);

			if(clientList.size() > 0) {
				data.clear();
				data.put("name", vo.getName());

				for(int i=0;i<clientList.size();i++) {
					param.clear();
					param.put("idxCrmClient", clientList.get(i).get("idx"));
					commonMapper.update("crm_reservation", data, " idx_crm_client = #{param.idxCrmClient}", param);
				}
			}

			result.put("err", 0);
			result.put("result", "?????? ????????? ?????????????????????.");
			result.put("client", vo);

		/* } */


		return result;
	}

	public Map<Object, Object> selectClientList(int idxCrmCenter, String clientStatus, String value, int idxCrmStaff, String consStartDate, String consEndDate, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<ClientVO> clientList = new ArrayList<ClientVO>();
		Map<String, Object> param = new HashMap<String, Object>();
		int clientListCount = 0;

        //param.put("idxCrmCenter", idxCenter);

		param.put("clientStatus", clientStatus);
        param.put("value", value);
        param.put("consStartDate", consStartDate);
        param.put("consEndDate", consEndDate);
        param.put("startPage", startPage);
        param.put("pageSize", pageSize);
		param.put("idxCrmStaff", idxCrmStaff);
		param.put("idxCrmCenter", idxCrmCenter);

		clientList = commonMapper.selectClientList(param);

		log.info("#### clientList : " + clientList.toString());

		if(clientList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "?????? ????????? ????????????.");
		}

		//if(startPage == 0) {
			clientListCount = commonMapper.selectClientListCount(param);
			result.put("clientListCount", clientListCount);
		//}
		result.put("clientList", clientList);


		return result;
	}

	public Map<Object, Object> selectCounselorClientList(int idxCenter, String clientStatus, String reserveStatus, String value, String consStartDate, String consEndDate, int startPage, int pageSize, int idxCrmStaff) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<ClientVO> clientList = new ArrayList<ClientVO>();
		Map<String, Object> param = new HashMap<String, Object>();
		int clientListCount = 0;

		//param.put("idxCrmCenter", idxCenter);
		//reserveStatus
		param.put("clientStatus", clientStatus);
		param.put("value", value);
		param.put("consStartDate", consStartDate);
		param.put("consEndDate", consEndDate);
		param.put("startPage", -1);
		param.put("pageSize", pageSize);
		param.put("idxCrmStaff", idxCrmStaff);
		param.put("idxCenter", idxCenter);
		param.put("reserveStatus", reserveStatus);

		log.info(param.toString());
		clientListCount = commonMapper.selectCounselorClientList(param).size();
		result.put("clientListCount", clientListCount);

		param.remove("startPage");
		param.put("startPage", startPage);

		clientList = commonMapper.selectCounselorClientList(param);

		log.info("#### clientList : " + clientList.toString());

		if(clientList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "?????? ????????? ????????????.");
		}

		result.put("clientList", clientList);


		return result;
	}

	public Map<Object, Object> deleteClient(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		ClientVO client = new ClientVO();
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("idx", idx);
		client = commonMapper.selectClient(param);

		commonMapper.delete("crm_client", param);
		result.put("err", 0);
		result.put("result", client.getName() + " ???????????? ?????? ???????????????.");

		return result;
	}

	public Map<Object, Object> selectClientAll(String type, int idxCrmStaff) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<ClientVO> client = new ArrayList<ClientVO>();
		List<Map<Object, Object>> personList = new ArrayList<Map<Object, Object>>();
		String sql = "";

		sql = "SELECT * FROM crm_person";
		personList = commonMapper.selectAll(sql);

		param.put("type", type);
		param.put("idxCrmStaff", idxCrmStaff);
		client = commonMapper.selectClientAll(param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("client", client);
		result.put("personList", personList);

		return result;
	}

	// ?????? ??????
	public Map<Object, Object> insertPurchase(PurchaseVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();

		if(vo.getIdxCrmCompany() == -1) {
			vo.setClientType("B2C");
		} else {
			vo.setClientType("B2B");
		}

		commonMapper.insertPurchase(vo);

		result.put("err", 0);
		result.put("result", "??????????????? ?????????????????????.");

		return result;

	}

	public Map<Object, Object> selectPurchase(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> purchase = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";

		param.put("idx", idx);
		sql = "SELECT * FROM crm_purchase WHERE idx = #{param.idx}";

		purchase = commonMapper.selectOne(sql, param);

		result.put("err", 0);
		result.put("purchase", purchase);

		return result;

	}

	// B2C?????? ??????
	public Map<Object, Object> selectPurchaseB2C(int idx, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		PurchaseVO purchaseSum = new PurchaseVO();
		List<PurchaseVO> purchase = new ArrayList<PurchaseVO>();
		//purchaseSum = commonMapper.selectPurchaseDetail(param);
		param.put("idxCrmClient", idx);
		param.put("status", "??????");
		purchaseSum.setPayPrice(commonMapper.selectSumPriceClient(param));
		param.put("idxCrmClient", idx);
		param.put("status", "?????????");
		purchaseSum.setNoPrice(commonMapper.selectSumPriceClient(param));
		param.put("idxCrmClient", idx);
		param.put("status", "??????");
		purchaseSum.setRefundPrice(commonMapper.selectSumPriceClient(param));

		param.put("startPage", startPage);
		param.put("pageSize", pageSize);
		purchase = commonMapper.selectPurchaseB2C(param);

		//if(startPage == 0) {
			result.put("purchaseSize", commonMapper.selectPurchaseB2CCount(param));
		//}

		result.put("err", 0);
		result.put("result", "OK");
		result.put("purchase", purchase);
		result.put("purchaseSum", purchaseSum);

		return result;

	}

	// ????????? ?????? B2C?????? ??????
	public Map<Object, Object> selectChartPurchaseB2C(int idxCrmPerson, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		PurchaseVO purchaseSum = new PurchaseVO();
		List<PurchaseVO> purchase = new ArrayList<PurchaseVO>();

		param.put("idxCrmPerson", idxCrmPerson);
		param.put("startPage", startPage);
		param.put("pageSize", pageSize);
		purchase = commonMapper.selectChartPurchaseB2C(param);

		param.remove("startPage");
			param.put("startPage", -1);
			result.put("purchaseSize", commonMapper.selectChartPurchaseB2C(param).size());

		result.put("purchase", purchase);

		if(purchase.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "????????? ?????? ????????????.");
		}

		return result;

	}

	// B2B?????? ??????
	public Map<Object, Object> selectPurchaseB2B(int idx, String startDate, String endDate, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		PurchaseVO purchaseSum = new PurchaseVO();
		List<PurchaseVO> purchase = new ArrayList<PurchaseVO>();
		//purchaseSum = commonMapper.selectPurchaseDetail(param);
		param.put("idxCrmCompany", idx);
		param.put("status", "??????");
		purchaseSum.setPayPrice(commonMapper.selectSumPriceCompany(param));
		param.put("idxCrmCompany", idx);
		param.put("status", "?????????");
		purchaseSum.setNoPrice(commonMapper.selectSumPriceCompany(param));
		param.put("idxCrmCompany", idx);
		param.put("status", "??????");
		purchaseSum.setRefundPrice(commonMapper.selectSumPriceCompany(param));

		param.put("startPage", startPage);
		param.put("pageSize", pageSize);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		purchase = commonMapper.selectPurchaseB2B(param);

		//if(startPage == 0) {
			result.put("purchaseSize", commonMapper.selectPurchaseB2BCount(param));
		//}

		if(purchase.size() > 0 ) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "??????????????? ????????????.");
		}

		result.put("purchase", purchase);
		result.put("purchaseSum", purchaseSum);

		return result;

	}

	// ?????? ??????
	public Map<Object, Object> updatePurchase(int idx, PurchaseVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();

		vo.setIdx(idx);
		commonMapper.updatePurchase(vo);

		result.put("err", 0);
		result.put("result", "??????????????? ?????????????????????.");

		return result;

	}

	// ?????? ????????????
	public Map<Object, Object> selectPurchaseList(String type, String value, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<PurchaseVO> purList = new ArrayList<PurchaseVO>();
		int price = 0;
        param.put("value", value);
        param.put("startPage", startPage);
        param.put("pageSize", pageSize);

        if("B2C".equals(type)) {
        	purList = commonMapper.selectPurchaseB2CList(param);

        	//if(startPage == 0) {
            	result.put("purListCount", commonMapper.selectPurchaseB2CListCount(param));
            //}
        } else {
        	purList = commonMapper.selectPurchaseB2BList(param);
        	//if(startPage == 0) {
            	result.put("purListCount", commonMapper.selectPurchaseB2BListCount(param));
            //}
        }

        if(purList.size() > 0) {
        	result.put("err", 0);
    		result.put("result", "OK");
    		param.clear();
    		if("B2C".equals(type)) {
	    		for(int i=0;i<purList.size();i++) {
	    			param.clear();
	    			param.put("idxCrmClient", purList.get(i).getIdxCrmClient());
	    			param.put("status", "??????");
	    			purList.get(i).setPayPrice(commonMapper.selectSumPriceClient(param));
	    			param.clear();
	    			param.put("idxCrmClient", purList.get(i).getIdxCrmClient());
	    			param.put("status", "??????");
	    			purList.get(i).setRefundPrice(commonMapper.selectSumPriceClient(param));
	    			param.clear();
	    			param.put("idxCrmClient", purList.get(i).getIdxCrmClient());
	    			param.put("status", "?????????");
	    			purList.get(i).setNoPrice(commonMapper.selectSumPriceClient(param));
	    		}
    		} else  {

	    		for(int i=0;i<purList.size();i++) {
	    			param.clear();
	    			param.put("idxCrmCompany", purList.get(i).getIdxCrmCompany());
	    			param.put("status", "??????");
	    			purList.get(i).setPayPrice(commonMapper.selectSumPriceCompany(param));
	    			param.clear();
	    			param.put("idxCrmCompany", purList.get(i).getIdxCrmCompany());
	    			param.put("status", "??????");
	    			purList.get(i).setRefundPrice(commonMapper.selectSumPriceCompany(param));
	    			param.clear();
	    			param.put("idxCrmCompany", purList.get(i).getIdxCrmCompany());
	    			param.put("status", "?????????");
	    			purList.get(i).setNoPrice(commonMapper.selectSumPriceCompany(param));
	    		}
    		}


        }  else {
        	result.put("err", 105);
    		result.put("result", "?????? ????????? ????????????.");
        }

        result.put("purList", purList);

		return result;

	}

	// ?????? ??????
	public Map<Object, Object> deletePurchase(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("idx", idx);
		commonMapper.delete("crm_purchase", param);

		result.put("err", 0);
		result.put("result", "??????????????? ?????????????????????.");
		return result;

	}

	public Map<Object, Object> selectPurchaseB2BUserList(int idxCrmCompany, int idxCrmCenter, String value, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<PurchaseVO> purList = new ArrayList<PurchaseVO>();

		param.put("idxCrmCompany", idxCrmCompany);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("value", value);
		param.put("startPage", startPage);
		param.put("pageSize", pageSize);

		purList = commonMapper.selectPurchaseB2BUserList(param);

		if(purList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "?????? ????????? ????????????.");
		}

		//if(startPage == 0) {
			result.put("purListSize", commonMapper.selectPurchaseB2BUserListCount(param));
		//}

		result.put("purList", purList);

		return result;
	}
	public Map<Object, Object> selectMetaAll() throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> gubunList = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> typeList = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> subjectList = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> routeList = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> dangerList = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> proList = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> contactList = new ArrayList<Map<Object,Object>>();
		String sql = "";
		sql = "SELECT * FROM crm_meta WHERE category = '????????????'";
		gubunList = commonMapper.selectAll(sql);
		sql = "SELECT * FROM crm_meta WHERE category = '????????????'";
		typeList = commonMapper.selectAll(sql);
		sql = "SELECT * FROM crm_meta WHERE category = '????????????'";
		subjectList = commonMapper.selectAll(sql);
		sql = "SELECT * FROM crm_meta WHERE category = '????????????'";
		routeList = commonMapper.selectAll(sql);
		sql = "SELECT * FROM crm_meta WHERE category = '????????????'";
		dangerList = commonMapper.selectAll(sql);
		sql = "SELECT * FROM crm_meta WHERE category = '????????????'";
		proList = commonMapper.selectAll(sql);
		sql = "SELECT * FROM crm_meta WHERE category = '????????????'";
		contactList = commonMapper.selectAll(sql);

		result.put("err", 0);
		result.put("gubunList", gubunList);
		result.put("typeList", typeList);
		result.put("subjectList", subjectList);
		result.put("routeList", routeList);
		result.put("dangerList", dangerList);
		result.put("proList", proList);
		result.put("contactList", contactList);

		return result;
	}

	public Map<Object, Object> selectGubun(String value) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> gubunList = new ArrayList<Map<Object,Object>>();
		List<Map<Object, Object>> productList = new ArrayList<Map<Object,Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		String sql = "";

		sql = "SELECT DISTINCT(type) FROM crm_meta WHERE category = '????????????'";
		gubunList = commonMapper.selectAll(sql);

		if("".equals(value)) {
			sql = "SELECT idx, product FROM crm_meta WHERE category = '????????????'";
		} else {
			param.put("value", value);
			sql = "SELECT idx, product, price FROM crm_meta WHERE category = '????????????' AND type = #{param.value}";
		}

		productList = commonMapper.selectAll(sql, param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("gubunList", gubunList);
		result.put("productList", productList);
		return result;
	}

	// ???????????????
	public Map<String, Object> getList(Map<String, Object> param, int page, int pageSize) {
	    Map<String, Object> list = reumService.list("view_client_list", param, page, pageSize);

	    Map<String, Object> response = new HashMap<>();
	    response.put("list", list);
	    response.put("err", 0);
	    return response;
	  }

	public Map<Object, Object> selectEapList(String status, String value, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<ClientVO> eapList = new ArrayList<ClientVO>();

		param.put("status", status);
		param.put("value", value);
		param.put("startPage", startPage);
		param.put("pageSize", pageSize);
		eapList = commonMapper.selectEapList(param);

		/*
		 * for(int i=0;i<eapList.size();i++) {
		 * eapList.get(i).setEapHopeDate(formatStringToDate(formatDateToString(eapList.
		 * get(i).getEapHopeDate(), "yyyy-MM-dd"), "yyyy-MM-dd")); }
		 */

		if(eapList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "?????? ????????? ????????????.");
		}

		//if(startPage == 0) {
			result.put("eapListSize", commonMapper.selectEapListCount(param));
		//}
		result.put("eapList", eapList);

		return result;
	}

	public Map<Object, Object> applyEap(int idxCrmClient, int idxCrmCenter) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> eapList = new ArrayList<Map<Object,Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String sql = "";

		// crm_client ????????? center idx ?????????
		// crm_eap ????????? status??? ??????
		data.put("idx_crm_center", idxCrmCenter);
		param.put("idxCrmClient", idxCrmClient);

		commonMapper.update("crm_client", data, " idx = #{param.idxCrmClient}", param);

		sql = "SELECT * FROM crm_eap WHERE idx_crm_client = #{param.idxCrmClient}";

		eapList = commonMapper.selectAll(sql, param);

		for(int i=0;i<eapList.size();i++) {
			param.clear();
			data.clear();
			if(Integer.parseInt(eapList.get(i).get("idx_crm_center").toString()) == idxCrmCenter) {
				data.put("status", "??????");
				// ????????? ????????? ??? ?????? HJ
				altService.insertTalk(idxCrmCenter, 15538);
			} else {
				data.put("status", "??????");
			}
			param.put("idx", eapList.get(i).get("idx"));
			commonMapper.update("crm_eap", data, " idx = #{param.idx}", param);
		}

		result.put("err", 0);
		result.put("result", "OK");

		return result;
	}

	public Map<Object, Object> selectPurchaseHistory(String type, String clientName, String staffName, String purPayWay,
			String gubun, String typeName, int idxCrmCenter, int idxCrmCompany, int startPage, int pageSize, String startDate, String endDate) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		PurchaseVO purchase = new PurchaseVO();
		List<PurchaseVO> purList = new ArrayList<PurchaseVO>();

		param.put("clientName", clientName);
		param.put("staffName", staffName);
		param.put("purPayWay", purPayWay);
		param.put("gubun", gubun);
		param.put("typeName", typeName);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("idxCrmCompany", idxCrmCompany);
		param.put("startPage", startPage);
		param.put("pageSize", pageSize);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		if("B2C".equals(type)) {
			purchase = commonMapper.selectPurchaseHistoryB2CStatus(param);
			purList = commonMapper.selectPurchaseHistoryB2CList(param);

			result.put("purchase", purchase);
			result.put("purList", purList);

			//if(startPage == 0) {
				result.put("purListSize", commonMapper.selectPurchaseHistoryB2CListCount(param));
			//}

		} else {
			purchase = commonMapper.selectPurchaseHistoryB2BStatus(param);
			purList = commonMapper.selectPurchaseHistoryB2BList(param);

			result.put("purchase", purchase);
			result.put("purList", purList);

			//if(startPage == 0) {
				result.put("purListSize", commonMapper.selectPurchaseHistoryB2BListCount(param));
			//}
		}

		if(purList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "?????? ???????????? ????????????.");
		}

		return result;
	}
}
