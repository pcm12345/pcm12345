package com.carrot.status.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrot.status.service.StatusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(description = "통계 관련 API", tags = "[본원/협약센터] 통계관련 부분")
@Slf4j
public class StatusController {
	
	@Autowired
	private StatusService statusService; 
	
	/**
	 * 본원 상담사별 통계 페이지
	 * @param name
	 * @param idxCrmCompany
	 * @param idxCrmCenter
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/status/main/staff")
	@ApiOperation(value="[본원] 상담사별 통계 페이지", notes = "본원 상담사별 통계 페이지")
	public ResponseEntity<Map<Object, Object>> selectMainStatusStaff(
			  @RequestParam(value = "name", required = false, defaultValue = "") String name
			, @RequestParam(name = "idxCrmCompany" , required = false, defaultValue = "-1") int idxCrmCompany
			, @RequestParam(name="idxCrmCenter" , required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name = "clientType" , required = false, defaultValue = "-1") String clientType
			, @RequestParam(name="startDate" , required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate" , required = false, defaultValue = "") String endDate) throws Exception {
		log.info("#### StatusController.selectMainStatusStaff name : " + name + " idxCrmCompany : " + idxCrmCompany + " idxCrmCenter : " + idxCrmCenter + "clientType" + clientType + " startDate : " + startDate + " endDate : " + endDate);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(statusService.selectMainStatusStaff(name, idxCrmCompany, idxCrmCenter, clientType, startDate, endDate), HttpStatus.OK);
		return rtn;
	}
	
	/**
	 * 본원 구분(상품)별 통계 페이지
	 * @param idxCrmCompany
	 * @param idxCrmCenter
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/status/main/product")
	@ApiOperation(value="[본원] 상품(구분)별통계 페이지", notes = "본원 구분별통계 페이지")
	public ResponseEntity<Map<Object, Object>> selectMainStatusProduct(
			  @RequestParam(name = "idxCrmCompany" , required = false, defaultValue = "-1") int idxCrmCompany
			, @RequestParam(name="idxCrmCenter" , required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name = "clientType" , required = false, defaultValue = "-1") String clientType
			, @RequestParam(name="startDate" , required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate" , required = false, defaultValue = "") String endDate) throws Exception {
		log.info("#### StatusController.selectMainStatusProduct idxCrmCompany : " + idxCrmCompany + " idxCrmCenter : " + idxCrmCenter + "clientType" + clientType + " startDate : " + startDate + " endDate : " + endDate);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(statusService.selectMainStatusProduct(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate), HttpStatus.OK);
		return rtn;
	}
	
	/**
	 * 본원 유형별 통계 페이지
	 * @param idxCrmCompany
	 * @param idxCrmCenter
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/status/main/type")
	@ApiOperation(value="[본원] 유형별통계 페이지", notes = "본원 유형별통계 페이지")
	public ResponseEntity<Map<Object, Object>> selectMainStatusType(
			  @RequestParam(name = "idxCrmCompany" , required = false, defaultValue = "-1") int idxCrmCompany
			, @RequestParam(name="idxCrmCenter" , required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name = "clientType" , required = false, defaultValue = "-1") String clientType
			, @RequestParam(name="startDate" , required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate" , required = false, defaultValue = "") String endDate) throws Exception {
		log.info("#### StatusController.selectMainStatusType idxCrmCompany : " + idxCrmCompany + " idxCrmCenter : " + idxCrmCenter + "clientType" + clientType + " startDate : " + startDate + " endDate : " + endDate);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(statusService.selectMainStatusType(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate), HttpStatus.OK);
		return rtn;
	}
	
	/**
	 * 본원 주제별 통계 페이지
	 * @param idxCrmCompany
	 * @param idxCrmCenter
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/status/main/subject")
	@ApiOperation(value="[본원] 주제별통계 페이지", notes = "본원 주제별통계 페이지")
	public ResponseEntity<Map<Object, Object>> selectMainStatusSubject(
			  @RequestParam(name = "idxCrmCompany" , required = false, defaultValue = "-1") int idxCrmCompany
			, @RequestParam(name="idxCrmCenter" , required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name = "clientType" , required = false, defaultValue = "-1") String clientType
			, @RequestParam(name="startDate" , required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate" , required = false, defaultValue = "") String endDate) throws Exception {
		log.info("#### StatusController.selectMainStatusSubject idxCrmCompany : " + idxCrmCompany + " idxCrmCenter : " + idxCrmCenter + "clientType" + clientType + " startDate : " + startDate + " endDate : " + endDate);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(statusService.selectMainStatusSubject(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate), HttpStatus.OK);
		return rtn;
	}
	
	/**
	 * 남녀 성비 별 통계 페이지
	 * @param idxCrmCompany
	 * @param idxCrmCenter
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/status/main/gender")
	@ApiOperation(value="[본원] 성비별통계 페이지", notes = "본원 성비별통계 페이지")
	public ResponseEntity<Map<Object, Object>> selectMainStatusGender(
			  @RequestParam(name = "idxCrmCompany" , required = false, defaultValue = "-1") int idxCrmCompany
			, @RequestParam(name="idxCrmCenter" , required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name = "clientType" , required = false, defaultValue = "-1") String clientType
			, @RequestParam(name="startDate" , required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate" , required = false, defaultValue = "") String endDate) throws Exception {
		log.info("#### StatusController.selectMainStatusSubject idxCrmCompany : " + idxCrmCompany + " idxCrmCenter : " + idxCrmCenter + "clientType" + clientType + " startDate : " + startDate + " endDate : " + endDate);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(statusService.selectMainStatusGender(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate), HttpStatus.OK);
		return rtn;
	}
	
	/**
	 * 본원 연령별통계 페이지
	 * @param idxCrmCompany
	 * @param idxCrmCenter
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/status/main/age")
	@ApiOperation(value="[본원] 연령별통계 페이지", notes = "본원 연령별통계 페이지")
	public ResponseEntity<Map<Object, Object>> selectMainStatusAge(
			  @RequestParam(name = "idxCrmCompany" , required = false, defaultValue = "-1") int idxCrmCompany
			, @RequestParam(name="idxCrmCenter" , required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name = "clientType" , required = false, defaultValue = "-1") String clientType
			, @RequestParam(name="startDate" , required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate" , required = false, defaultValue = "") String endDate) throws Exception {
		log.info("#### StatusController.selectMainStatusSubject idxCrmCompany : " + idxCrmCompany + " idxCrmCenter : " + idxCrmCenter + "clientType" + clientType + " startDate : " + startDate + " endDate : " + endDate);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(statusService.selectMainStatusAge(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate), HttpStatus.OK);
		return rtn;
	}
	
	/**
	 * 본원 유입경로별 통계 페이지
	 * @param idxCrmCompany
	 * @param idxCrmCenter
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/status/main/route")
	@ApiOperation(value="[본원] 유입경로별통계 페이지", notes = "본원 유입경로별통계 페이지")
	public ResponseEntity<Map<Object, Object>> selectMainStatusRoute(
			  @RequestParam(name = "idxCrmCompany" , required = false, defaultValue = "-1") int idxCrmCompany
			, @RequestParam(name="idxCrmCenter" , required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name = "clientType" , required = false, defaultValue = "-1") String clientType
			, @RequestParam(name="startDate" , required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate" , required = false, defaultValue = "") String endDate) throws Exception {
		log.info("#### StatusController.selectMainStatusSubject idxCrmCompany : " + idxCrmCompany + " idxCrmCenter : " + idxCrmCenter + "clientType" + clientType + " startDate : " + startDate + " endDate : " + endDate);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(statusService.selectMainStatusRoute(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate), HttpStatus.OK);
		return rtn;
	}
	
}
