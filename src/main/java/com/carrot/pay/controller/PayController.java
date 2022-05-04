package com.carrot.pay.controller;

import com.carrot.pay.service.PayService;
import com.carrot.reum.ReumService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PayController {

  @Autowired
  public PayService payService;

  @Autowired
  public ReumService reumService;
  
  @Autowired
	private RedisTemplate redisTemplate;

  @GetMapping(value = "/api/v1/pay/list")
  public ResponseEntity<?>getPayList(
      @RequestParam Map<String, Object> param,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "pageSize", defaultValue = "30") int pageSize
  ){
    Map<String, Object> response = payService.getList("", page, pageSize);
    return ResponseEntity.ok(response);
  }

  @GetMapping(value = "/api/v1/pay/detailList")
  public ResponseEntity<?>getDetailList(
      @RequestParam(value = "idx") int idx
  ){
    Map<String, Object> param = new HashMap<>();
    Map<String, Object> response = reumService
        .selectAll("select * from view_salary_price", param);
    return ResponseEntity.ok(response);
  }
  
  	@GetMapping("/api/v1/pay/staff/list")
	@ApiOperation(value = "[본원] 상담사급여내역 목록", notes = "상담사 급여 목록")
	public ResponseEntity<Map<Object, Object>> selectPayStaffList(
			  @RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name="centerName", required = false, defaultValue = "") String centerName
			, @RequestParam(name="name", required = false, defaultValue = "") String name
			, @RequestParam(name="startDate", required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate", required = false, defaultValue = "") String endDate
			, @RequestParam(name="pageNum", required = false, defaultValue = "1") int pageNum
			, @RequestParam(name="pageSize", required = false, defaultValue = "50") int pageSize) throws Exception {
		log.info("#### PayController.selectPayStaffList idxCrmCenter : " + idxCrmCenter + " centerName : " + centerName + " name : " + name + " startDate : " +
			startDate + " endDate : " + endDate + " pageNum : " + pageNum + " pageSize : " + pageSize);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(payService.selectPayStaffList(idxCrmCenter, centerName, name, startDate, endDate, payService.doPaging(pageNum, pageSize), pageSize),
				HttpStatus.OK);
		return rtn;
	}
  	
  	@GetMapping("/api/v1/pay/staff/{idx}")
	@ApiOperation(value = "[본원] 상담사급여 상세보기", notes = "상담사 급역 상세보기")
	public ResponseEntity<Map<Object, Object>> selectPayStaff(@PathVariable("idx") int idx
			, @RequestParam(name="date", required = false, defaultValue = "") String date, HttpServletRequest request ) throws Exception {
		log.info("#### PayController.selectPayStaffList idx : " + idx + " date : " + date);
		
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		
		// 협약센터에서 자기네 상담사 리스트만 볼 수 있도록
		if ("STAFF".equals(map.get("authority"))) {
			idx = Integer.parseInt(map.get("idx").toString());
		}
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(payService.selectPayStaff(idx, date), HttpStatus.OK);
		return rtn;
	}
  	
  	@GetMapping("/api/v1/pay/staff/detail/{idx}")
	@ApiOperation(value = "[본원] 상담사급여 사아앙세보기", notes = "상담사 급역 사아앙세보기")
	public ResponseEntity<Map<Object, Object>> selectPayStaffDetail(@PathVariable("idx") int idx
			, @RequestParam(name="date", required = false, defaultValue = "") String date
			, @RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter, HttpServletRequest request ) throws Exception {
		log.info("#### PayController.selectPayStaffDetail idx : " + idx + " date : " + date + " idxCrmCenter : " + idxCrmCenter);
		
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		
		// 협약센터에서 자기네 상담사 리스트만 볼 수 있도록
		if ("STAFF".equals(map.get("authority"))) {
			idx = Integer.parseInt(map.get("idx").toString());
		}
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(payService.selectPayStaffDetail(idx, date, idxCrmCenter), HttpStatus.OK);
		return rtn;
	}
  	
  	@GetMapping("/api/v1/pay/center/list")
	@ApiOperation(value = "[본원] 협약센터 급여 목록(삭제예정)", notes = "협약센터 급여 목록")
	public ResponseEntity<Map<Object, Object>> selectPayCenterList(@RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter
	, @RequestParam(name="startDate", required = false, defaultValue = "") String startDate
	, @RequestParam(name="endDate", required = false, defaultValue = "") String endDate
	, @RequestParam(name="pageNum", required = false, defaultValue = "1") int pageNum
	, @RequestParam(name="pageSize", required = false, defaultValue = "50") int pageSize
	, HttpServletRequest request ) throws Exception {
		log.info("#### PayController.selectPayCenterList idxCrmCenter : " + idxCrmCenter + " startDate : " + startDate + " endDate : " + endDate
				+ " pageNum : "+ pageNum + " pageSize : " + pageSize);
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		int center = -1;

		// 협약센터에서 자기네 상담사 리스트만 볼 수 있도록
		if ("CENTERDIRECTOR".equals(map.get("authority"))) {
			center = Integer.parseInt(map.get("idxCrmCenter").toString());
		} else {
			center = idxCrmCenter;
		}

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(payService.selectPayCenterList(center, startDate, endDate, payService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v2/pay/center/list")
	@ApiOperation(value = "[본원] 협약센터 급여 목록", notes = "협약센터 급여 목록")
	public ResponseEntity<Map<Object, Object>> selectPayCenterListv2(@RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name="startDate", required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate", required = false, defaultValue = "") String endDate
			, @RequestParam(name="pageNum", required = false, defaultValue = "1") int pageNum
			, @RequestParam(name="pageSize", required = false, defaultValue = "50") int pageSize
			, HttpServletRequest request ) throws Exception {
		log.info("#### PayController.selectPayCenterList idxCrmCenter : " + idxCrmCenter + " startDate : " + startDate + " endDate : " + endDate
				+ " pageNum : "+ pageNum + " pageSize : " + pageSize);

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(payService.selectPayCenterList(idxCrmCenter, startDate, endDate, payService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
		return rtn;
	}

}
