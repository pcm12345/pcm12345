package com.carrot.cons.service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.carrot.cons.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.carrot.client.dao.ClientVO;
import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.storage.service.ObjectStorageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConsService extends AbstractService {

	@Autowired
	private CommonMapper commonMapper;

	@Autowired
	private ObjectStorageService objectStorageService;

	public Map<Object, Object> insertReservation(ReservationVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String sql = "";
		int reservationCheck= 0;

		// 예약 있는지 체크
		param.put("idxCrmStaff", vo.getIdxCrmStaff());
		param.put("consDate", vo.getConsDate());
		param.put("consTime",vo.getConsTime().split("/"));
		reservationCheck = commonMapper.reservationDupCheck(param);
		param.clear();
		if (reservationCheck < 1) {
			commonMapper.insertReservation(vo);

			data.put("idx_crm_staff", vo.getIdxCrmStaff());
			param.put("idx", vo.getIdxCrmClient());
			commonMapper.update("crm_client", data, " idx = #{param.idx}", param);

			// 기존 상담예약 값에 따라 회기를 자동으로 추가/차감처리한다.
			//NS로 입력하는 경우
			String curStatus = vo.getReservationStatus();
			if ("NS".equals(curStatus)) {
				//NS로 입력하는 경우 차감한다.
				commonMapper.updateConsCountUseMinus(vo.getIdxCrmClient());
			}

			result.put("err", 0);
			result.put("result", "상담예약이 등록되었습니다.");
		}else{
			result.put("err", 105 );
			result.put("result", "이용이 불가능한 시간입니다.");
		}
		return result;
	}

	public Map<Object, Object> selectReservation(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		ReservationVO resVO = new ReservationVO();
		ClientVO clientVO = new ClientVO();
		param.put("idx", idx);
		resVO = commonMapper.selectReservation(param);

		param.clear();
		param.put("idx", resVO.getIdxCrmClient());
		clientVO = commonMapper.selectClient(param);

		// 수납정보 필요

		result.put("err", 0);
		result.put("result", "OK");
		result.put("resVO", resVO);
		result.put("clientVO", clientVO);

		return result;
	}

	public Map<Object, Object> updateReservation(int idx, ReservationVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();

		// 기존 상담예약 값에 따라 회기를 자동으로 추가/차감처리한다.
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idx", idx);
		ReservationVO rv = commonMapper.selectReservation(param);
		String prevStatus = rv.getReservationStatus();
		String postStatus = vo.getReservationStatus();
		Integer idxCrmClient = rv.getIdxCrmClient();

		if(!"NS".equals(prevStatus) && "NS".equals(postStatus)){
			//NS가 아닌 상태에서 NS로 변경하는 경우 차감한다.
			commonMapper.updateConsCountUseMinus(idxCrmClient);
		}else if("NS".equals(prevStatus) && !"NS".equals(postStatus)) {
			//NS에서 NS가 아닌 상태로 변경하는 경우 추가한다.
			commonMapper.updateConsCountUsePlus(idxCrmClient);
		}

		vo.setIdx(idx);
		commonMapper.updateReservation(vo);

		result.put("err", 0);
		result.put("result", "상담예약이 수정되었습니다.");
		result.put("resVO", vo);

		return result;
	}

	public Map<Object, Object> updateReservationStatus(int idx, String status, int idxCrmStaffUpdate) {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("idx", idx);
		data.put("reservation_status", status);
		data.put("idx_crm_staff_update", idxCrmStaffUpdate);

		// 기존 상담예약 값에 따라 회기를 자동으로 추가/차감처리한다.
		Map<String, Object> rParam = new HashMap<String, Object>();
		rParam.put("idx", idx);
		ReservationVO rv = commonMapper.selectReservation(rParam);
		String prevStatus = rv.getReservationStatus();
		String postStatus = status;
		Integer idxCrmClient = rv.getIdxCrmClient();

		if(!"NS".equals(prevStatus) && "NS".equals(postStatus)){
			//NS가 아닌 상태에서 NS로 변경하는 경우 차감한다.
			commonMapper.updateConsCountUseMinus(idxCrmClient);
		}else if("NS".equals(prevStatus) && !"NS".equals(postStatus)) {
			//NS에서 NS가 아닌 상태로 변경하는 경우 추가한다.
			commonMapper.updateConsCountUsePlus(idxCrmClient);
		}

		commonMapper.update("crm_reservation", data, " idx = #{param.idx}", param);

		result.put("err", 0);
		result.put("result", "OK");

		return result;

	}

	public Map<Object, Object> selectReservationList(int idxCrmStaff, int idxCrmCenter, int idxCrmMetaPro, String value, String type, String consStartDate, String consEndDate, String orderType, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<ReservationVO> resList = new ArrayList<ReservationVO>();
		List<ReservationVO> firstList = new ArrayList<ReservationVO>();
		List<Map<Object, Object>> staffList = new ArrayList<Map<Object, Object>>();
		String sql= "";
		String metaProSql = "";
		if(idxCrmMetaPro != 56) { // 전문분야가 쿠더가 아닐 때
			metaProSql = "and idx_crm_meta_pro != 56";
		} else {
			metaProSql = "and idx_crm_meta_pro = 56";
		}

		param.put("idxCrmCenter", idxCrmCenter);
		param.put("consStartDate", consStartDate);
		sql = "SELECT \r\n" +
				"a.*, work.`day` , GROUP_CONCAT(work.time) as work_time FROM (\r\n" +
				"SELECT\r\n" +
				"	*\r\n" +
				"FROM\r\n" +
				"	crm_staff staff\r\n" +
				"WHERE\r\n" +
				"	staff.idx_crm_center = #{param.idxCrmCenter}\r\n" +
				"	and staff.permission = 'Y'\r\n" +
				metaProSql +
				"	and staff.authority = 'STAFF'\r\n" +
				"	and staff.duty = '재직'\r\n" +
				"	) a \r\n" +
				"left join crm_staff_worktime work on\r\n" +
				"	work.idx_crm_staff = a.idx\r\n" +
				"where \r\n" +
				"	work.`day` = ( SELECT CASE DAYOFWEEK(#{param.consStartDate})\r\n" +
				"\r\n" +
				"WHEN '1' THEN '일'\r\n" +
				"WHEN '2' THEN '월'\r\n" +
				"WHEN '3' THEN '화'\r\n" +
				"WHEN '4' THEN '수'\r\n" +
				"WHEN '5' THEN '목'\r\n" +
				"WHEN '6' THEN '금'\r\n" +
				"WHEN '7' THEN '토' END ) "
				+ "GROUP BY a.idx, work.`day` ";
		staffList = commonMapper.selectAll(sql, param);
		result.put("staffList", staffList);

		param.put("idxCrmStaff", idxCrmStaff);
		param.put("value", value);
		param.put("type", type);
		param.put("consEndDate", consEndDate);
		param.put("orderType", orderType);
        param.put("startPage", startPage);
        param.put("pageSize", pageSize);
		param.put("idxCrmCenter", idxCrmCenter);
		resList = commonMapper.selectReservationList(param);

		//if(startPage == 0) {
			result.put("resListCount", commonMapper.selectReservationListCount(param));
		//}


		if(resList.size()>0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "검색 결과가 없습니다.");
		}

		result.put("resList", resList);

		return result;
	}

	public Map<Object, Object> selectChartReservationList(int idxCrmPerson, int startPage, int pageSize, int idxCrmStaff) throws Exception {
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<ReservationVO> resList = new ArrayList<ReservationVO>();

		param.put("idxCrmPerson", idxCrmPerson);
		param.put("startPage", startPage);
		param.put("pageSize", pageSize);
		param.put("idxCrmStaff", idxCrmStaff);

		resList = commonMapper.selectChartReservationList(param);
		result.put("resList", resList);

		//if(startPage == 0) {
			result.put("resListCount", commonMapper.selectChartReservationListCount(param));
		//}

		if(resList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "결과가 없습니다.");
		}

		return result;
	}

	public Map<Object, Object> deleteReservation(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();

		param.put("idx", idx);
		// 기존 상담예약 값에 따라 회기를 자동으로 추가/차감처리한다.
		//NS 상태인 경우
		ReservationVO vo = commonMapper.selectReservation(param);
		String curStatus = vo.getReservationStatus();

		if("NS".equals(curStatus)){
			//NS로 입력된 예약을 삭제하는 경우 추가한다.
			commonMapper.updateConsCountUsePlus(vo.getIdxCrmClient());
		}

		commonMapper.delete("crm_reservation", param);

		result.put("err", 0);
		result.put("result", "상담예약이 삭제 되었습니다");

		return result;
	}

	// 상담일지 등록
	public Map<Object, Object> insertReport(ReportVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> attachFile = new LinkedHashMap<Object, Object>();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		String url = "";
		Date nowDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		data.put("idx_crm_client", vo.getIdxCrmClient());
		data.put("idx_crm_staff", vo.getIdxCrmStaff());
		data.put("idx_crm_reservation", vo.getIdxCrmReservation());
		data.put("permission", "Y");
		data.put("permission_date", simpleDateFormat.format(nowDate));
		data.put("cons_opinion", vo.getConsOpinion());
		data.put("mental_test", vo.getMentalTest());
		data.put("next_plan", vo.getNextPlan());
		data.put("cons_contents", vo.getConsContents());
		data.put("cons_process", vo.getConsProcess());

		data.put("idx_agenda_first", vo.getIdxAgendaFirst());
		data.put("idx_agenda_second", vo.getIdxAgendaSecond());
		data.put("idx_genre_meta", vo.getIdxGenreMeta());
		data.put("idx_type_meta ", vo.getIdxTypeMeta ());

		if(!vo.getAttachFile().isEmpty()) {
			attachFile = checkFile(vo.getAttachFile(), "C");
			url = objectStorageService.uploadObject(vo.getAttachFile(), attachFile.get("storedFileName").toString(), "crm", "cons");
			data.put("attach_file_name", attachFile.get("originalFileName"));
			data.put("attach_file_url", url);
		}

		data.put("process_status", "완료");
		data.put("idx_crm_meta_danger", vo.getIdxCrmMetaDanger());
		data.put("danger_step_title", vo.getDangerStepTitle());

		data.put("main_problem", vo.getMainProblem());

		data.put("cons_count", vo.getConsCount()); // 상담회기 추가 20210316

		commonMapper.insert("crm_report", data);

		// crm_reservation report_yn update
		data.clear();
		data.put("report_yn", "Y");
		param.put("idx", vo.getIdxCrmReservation());
		commonMapper.update("crm_reservation", data, " idx = #{param.idx}", param);

		// 전체 상담의 목표 / 상담계획 사용자 테이블로 옮김
		data.clear();
		data.put("goal", vo.getGoal());
		data.put("plan", vo.getPlan());
		param.clear();
		param.put("idx", vo.getIdxCrmClient());
		commonMapper.update("crm_client", data, " idx = #{param.idx}", param);

		//사용자 테이블의 연령대 정보 수정
		param.clear();
		param.put("idx", vo.getIdxCrmClient());
		ClientVO clientVO = commonMapper.selectClient(param);
		param.clear();
		param.put("idx", vo.getIdxCrmClient());
		// ageRange 값은 crm_person 에 업데이트.
		data.clear();
		data.put("age_range", vo.getAgeRange());
		param.clear();
		param.put("idx", clientVO.getIdxCrmPerson());
		commonMapper.update("crm_person", data, " idx = #{param.idx}", param);

		result.put("err", 0);
		result.put("result", "등록 되었습니다");

		return result;
	}

	public Map<Object, Object> selectReport(int idx) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();

		ReportVO reportVO = new ReportVO();
		ReservationVO reservationVO = new ReservationVO();
		ClientVO clientVO = new ClientVO();

		param.put("idx", idx);
		reservationVO = commonMapper.selectReservation(param);
		reportVO = commonMapper.selectReport(param);
		param.clear();
		param.put("idx", reportVO.getIdxCrmClient());
		clientVO = commonMapper.selectClient(param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("reportVO", reportVO);
		result.put("reservationVO", reservationVO);
		result.put("clientVO", clientVO);

		return result;
	}

	// 상담일지 수정
	public Map<Object, Object> updateReport(int idx, ReportVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> attachFile = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		String url = "";

		param.put("idx", idx);

		data.put("idx_crm_client", vo.getIdxCrmClient());
		data.put("idx_crm_staff", vo.getIdxCrmStaff());
		data.put("permission", vo.getPermission());
		data.put("cons_opinion", vo.getConsOpinion());
		data.put("mental_test", vo.getMentalTest());
		data.put("next_plan", vo.getNextPlan());
		data.put("cons_contents", vo.getConsContents());
		data.put("cons_process", vo.getConsProcess());

		data.put("idx_agenda_first", vo.getIdxAgendaFirst());
		data.put("idx_agenda_second", vo.getIdxAgendaSecond());
		data.put("idx_genre_meta", vo.getIdxGenreMeta());
		data.put("idx_type_meta ", vo.getIdxTypeMeta ());

		if(!vo.getAttachFile().isEmpty()) {
			attachFile = checkFile(vo.getAttachFile(), "C");
			url = objectStorageService.uploadObject(vo.getAttachFile(), attachFile.get("storedFileName").toString(), "crm", "cons");
			data.put("attach_file_name", attachFile.get("originalFileName"));
			data.put("attach_file_url", url);
		} else {
			data.put("attach_file_name", vo.getAttachFileName());
			data.put("attach_file_url", vo.getAttachFileUrl());
		}

		data.put("process_status", vo.getProcessStatus());
		data.put("idx_crm_meta_danger", vo.getIdxCrmMetaDanger());
		data.put("danger_step_title", vo.getDangerStepTitle());
		data.put("main_problem", vo.getMainProblem());

		commonMapper.update("crm_report", data, " idx = #{param.idx}", param);

		// 전체 상담의 목표 / 상담계획 사용자 테이블로 옮김
		data.clear();
		data.put("goal", vo.getGoal());
		data.put("plan", vo.getPlan());
		param.clear();
		param.put("idx", vo.getIdxCrmClient());
		commonMapper.update("crm_client", data, " idx = #{param.idx}", param);

		//사용자 테이블의 연령대 정보 수정
		param.clear();
		param.put("idx", vo.getIdxCrmClient());
		ClientVO clientVO = commonMapper.selectClient(param);
		param.clear();
		param.put("idx", vo.getIdxCrmClient());

		// ageRange 값은 crm_person 에 업데이트.
		data.clear();
		data.put("age_range", vo.getAgeRange());
		param.clear();
		param.put("idx", clientVO.getIdxCrmPerson());
		commonMapper.update("crm_person", data, " idx = #{param.idx}", param);

		result.put("err", 0);
		result.put("result", "수정 되었습니다.");

		return result;
	}

	// 상담일지 수정
	public Map<Object, Object> updateReportRequestModify(int idx, String requestModify) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<Object, Object> attachFile = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		String url = "";

		param.put("idx", idx);

		log.info("requestModify : " +requestModify);
		if(!"Y".equals(requestModify) ) requestModify = "N";
		log.info("requestModify : " +requestModify);
		data.put("request_modify", requestModify);

		commonMapper.update("crm_report", data, " idx = #{param.idx}", param);

		result.put("err", 0);
		result.put("result", "수정 되었습니다.");

		return result;
	}

	public Map<Object, Object> selectReportList(int idxCrmCompany, int idxCrmStaff, int idxCrmCenter, int idxCrmClient, int idxCrmMetaDanger, String value, String permission, String reportYn, int idxCrmMetaProduct, String startDate, String endDate, int startPage, int pageSize) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<ReportVO> reportList = new ArrayList<ReportVO>();
		
		param.put("idxCrmCompany", idxCrmCompany);
		param.put("idxCrmStaff", idxCrmStaff);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("idxCrmClient", idxCrmClient);
		param.put("idxCrmMetaDanger", idxCrmMetaDanger);
		param.put("idxCrmMetaProduct", idxCrmMetaProduct);
		param.put("value", value);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("startPage", startPage);
		param.put("pageSize", pageSize);
		if("Y".equals(permission)) {
			permission = "승인";
		} else if("N".equals(permission)) {
			permission = "미승인";
		}
		param.put("permission", permission);

		if("Y".equals(reportYn)) {
			reportYn = "등록";
		} else if("N".equals(reportYn)) {
			reportYn = "미등록";
		}
		param.put("reportYn", reportYn);

		log.info("##### reportList : "+ reportList);

		reportList = commonMapper.selectReportList(param);

		for(int i=0;i<reportList.size();i++) {

			reportList.get(i).setConsDate2(formatDateToString(reportList.get(i).getConsDate(), "yyyy-MM-dd"));
		}
		//if(startPage == 0) {
			result.put("reportListCount", commonMapper.selectReportListCount(param));
		//}

		if(reportList.size() > 0) {
			result.put("err", 0);
			result.put("result", "OK");
		} else {
			result.put("err", 105);
			result.put("result", "검색 결과가 없습니다.");
		}

		result.put("reportList", reportList);
		return result;
	}

	public List<ReportVO> selectReportListAll(int idxCrmStaff, int idxCrmCenter, int idxCrmClient, String value, String permission, String reportYn, int idxCrmMetaProduct, String startDate, String endDate) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<ReportVO> reportList = new ArrayList<ReportVO>();

		param.put("idxCrmStaff", idxCrmStaff);
		param.put("idxCrmCenter", idxCrmCenter);
		param.put("idxCrmClient", idxCrmClient);
		param.put("idxCrmMetaProduct", idxCrmMetaProduct);
		param.put("value", value);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("startPage", -1);
		if("Y".equals(permission)) {
			permission = "승인";
		} else if("N".equals(permission)) {
			permission = "미승인";
		}
		param.put("permission", permission);

		if("Y".equals(reportYn)) {
			reportYn = "등록";
		} else if("N".equals(reportYn)) {
			reportYn = "미등록";
		}
		param.put("reportYn", reportYn);

		log.info("##### reportList : "+ reportList);

		reportList = commonMapper.selectReportList(param);

		for(int i=0;i<reportList.size();i++) {

			reportList.get(i).setConsDate2(formatDateToString(reportList.get(i).getConsDate(), "yyyy-MM-dd"));
		}

		return reportList;
	}



	public Map<Object, Object> updateCalReservation(int idx, ReservationVO vo) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("idx_crm_staff", vo.getIdxCrmStaff());
		data.put("idx_crm_meta_type", vo.getIdxCrmMetaType());
		data.put("idx_crm_staff_update", vo.getIdxCrmStaffUpdate());
		data.put("client_status", vo.getClientStatus());
		data.put("idx_crm_meta_product", vo.getIdxCrmMetaProduct());
		data.put("etc_price", vo.getEtcPrice());
		param.put("idx", vo.getIdxCrmClient());

		commonMapper.update("crm_client", data, " idx = #{param.idx}", param);

		data.clear();
		param.clear();

		data.put("cons_date", vo.getConsDate());
		data.put("cons_time", vo.getConsTime());
		data.put("idx_crm_staff", vo.getIdxCrmStaff());
		data.put("reservation_status", vo.getReservationStatus());
		data.put("idx_crm_staff_update", vo.getIdxCrmStaffUpdate());
		data.put("idx_crm_meta_contact", vo.getIdxCrmMetaContact());
		data.put("memo", vo.getMemo());
		param.put("idx", idx);

		commonMapper.update("crm_reservation", data, "idx = #{param.idx}", param);

		result.put("err", 0);
		result.put("result", "OK");

		return result;
	}

	public Map<Object, Object> selectReportMeta(int cons_genre_type, int idx_crm_meta_report){
		//cons_genre_type : 0 - 회기유형, 1 - 상담형태, 2 - 상담주제
		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<Object, Object>> genreList = new ArrayList<>();

		param.put("cons_genre_type", cons_genre_type);
		param.put("idx_crm_meta_report", idx_crm_meta_report);

		String sql = "SELECT idx, cons_title FROM crm_meta_report WHERE cons_genre_type = #{param.cons_genre_type} and idx_crm_meta_report = #{param.idx_crm_meta_report} ";
		genreList = commonMapper.selectAll(sql, param);

		result.put("err", 0);
		result.put("result", "OK");
		result.put("data", genreList);
		return result;
	}

	public Map<Object, Object> selectCompanyStaticPerson(int idx_crm_company, int idx_crm_center, String startDate, String endDate) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> status = new ArrayList<>();
		Map<String, Object> param = new HashMap<String, Object>();
		CompanyStaticPersonVO companyStaticPersonVO = new CompanyStaticPersonVO();
		CompanyStaticReportVO companyStaticReportVO = new CompanyStaticReportVO();
		CompanyStaticReport2VO companyStaticReport2VO = new CompanyStaticReport2VO();

		String sql = ""; // 종결인지 아닌지 체크해주는 부분(상담사가 로그인 했을 때)

		param.put("idx_crm_company", idx_crm_company);
		param.put("idx_crm_center", idx_crm_center);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		companyStaticPersonVO = commonMapper.selectCompanyStaticPerson(param);
		companyStaticReportVO = commonMapper.selectCompanyStaticReport(param);
		companyStaticReport2VO = commonMapper.selectCompanyStaticReport2(param); //companyStaticReportVO에 대한 인원 수 관련 mapper

		log.info("companyStaticReport2VO");
		log.info(String.valueOf(companyStaticReport2VO));

		result.put("err", 0);
		result.put("result",  "OK");
		result.put("data", mergeVO(companyStaticPersonVO, companyStaticReportVO, companyStaticReport2VO));
		// result.put("data_per", companyStaticReportVO2);

		return result;
	}

	public Map<Object, Object> mergeVO(Object VO1, Object VO2, Object VO3){
		Map<Object, Object> data = new LinkedHashMap<Object, Object>();
		Field[] fields = null;
		try {
			fields = VO1.getClass().getDeclaredFields();
			for(int i=0; i <fields.length; i++){
				fields[i].setAccessible(true);
				data.put(fields[i].getName(), fields[i].get(VO1));
			}

			fields = VO2.getClass().getDeclaredFields();
			for(int i=0; i <fields.length; i++){
				fields[i].setAccessible(true);
				data.put(fields[i].getName(), fields[i].get(VO2));
			}

			fields = VO3.getClass().getDeclaredFields();
			for(int i=0; i <fields.length; i++){
				fields[i].setAccessible(true);
				data.put(fields[i].getName(), fields[i].get(VO3));
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			return data;
		}
	}

//	@Override
//	public Map<Object, Object> mergeVO(Object VO1, Object VO2, Object VO3){
//		Map<Object, Object> data = new LinkedHashMap<Object, Object>();
//		Field[] fields = null;
//		try {
//			fields = VO1.getClass().getDeclaredFields();
//			for(int i=0; i <fields.length; i++){
//				fields[i].setAccessible(true);
//				data.put(fields[i].getName(), fields[i].get(VO1));
//			}
//
//			fields = VO2.getClass().getDeclaredFields();
//			for(int i=0; i <fields.length; i++){
//				fields[i].setAccessible(true);
//				data.put(fields[i].getName(), fields[i].get(VO2));
//			}
//
//			fields = VO3.getClass().getDeclaredFields();
//			for(int i=0; i <fields.length; i++){
//				fields[i].setAccessible(true);
//				data.put(fields[i].getName(), fields[i].get(VO3));
//			}
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} finally {
//			return data;
//		}
//	}

	public Map<Object, Object> selectCompanyStaticReport(int idx_crm_company, String startDate, String endDate) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		List<Map<Object, Object>> status = new ArrayList<>();
		Map<String, Object> param = new HashMap<String, Object>();
		CompanyStaticPersonVO companyStaticPersonVO = new CompanyStaticPersonVO();
		CompanyStaticReportVO companyStaticReportVO = new CompanyStaticReportVO();

		String sql = ""; // 종결인지 아닌지 체크해주는 부분(상담사가 로그인 했을 때)

		param.put("idx_crm_company", idx_crm_company);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		companyStaticPersonVO = commonMapper.selectCompanyStaticPerson(param);
		companyStaticReportVO = commonMapper.selectCompanyStaticReport(param);
		result.put("err", 0);
		result.put("result",  "OK");
		result.put("data",  companyStaticPersonVO);
		result.put("data",  companyStaticReportVO);

		return result;
	}

	public List<excelCompanyStaticVO> selectExcelCompanyStatic(int idx_crm_company, int idx_crm_center, String startDate, String endDate) throws Exception {

		Map<Object, Object> result = new LinkedHashMap<Object, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		List<excelCompanyStaticVO> resList = new ArrayList<>();
		List<excelCompanyStaticVO> excelCompanyStaticVO = null;

		param.put("idx_crm_company", idx_crm_company);
		param.put("idx_crm_center", idx_crm_center);
		param.put("startDate", startDate);
		param.put("endDate", endDate);

		resList = commonMapper.selectExcelCompanyStatic(param);

		return resList;
	}

}
