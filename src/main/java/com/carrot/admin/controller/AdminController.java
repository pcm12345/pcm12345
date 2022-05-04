package com.carrot.admin.controller;

import com.carrot.reum.ReumService;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrot.admin.service.AdminService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/admin")
@Api(description = "/api/v1/admin", tags = "관리자가 왠지 해야 할것 같은 느낌이 드는 기능")
@Slf4j
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private ReumService reumService;

	@PostMapping(value = "/register")
	public ResponseEntity<Map<String,Object>> postAdmin(@RequestBody Map<String, Object> params	) throws Exception {
		params.put("authority", "ADMIN");
		params.put("permission", "Y");
		params.put("idx_crm_center", "0");
		log.info("##### AdminController.postAdmin params : " + params.toString());
		return ResponseEntity.ok(adminService.registerAdmin(params));
	}

	@PostMapping(value = "/modify")
	public ResponseEntity<Map<String,Object>> modifyAdmin( @RequestBody Map<String, Object> params ) throws Exception {
		params.put("authority", "ADMIN");
		int idx = (int) params.get("idx");
		params.remove("idx");
		return ResponseEntity.ok(adminService.modifyAdmin(idx, params));
	}

	@GetMapping(value = "/list")
	public ResponseEntity<?> reumInfos( @RequestParam Map<String, Object> params	) throws Exception {
		
		log.info("#### params : " + params.toString());
		int page;
		if (params.get("page") != null) {
			page = Integer.parseInt(params.get("page").toString());
			params.remove("page");
		} else {
			page = 1;
		}

		int pageSize;
		if (params.get("pagesize") != null) {
			pageSize = Integer.parseInt(params.get("pagesize").toString());
			params.remove("pagesize");
		} else {
			pageSize = 30;
		}

		return ResponseEntity.ok(adminService.getStaffList(page, pageSize));
	}

	// 승인
	// 상담일지 crm_report
	// 협약센터 crm_center
	// 상담사 crm_staff
	@PutMapping("/permission")
	@ApiOperation(value = "[관리자, 본원] 승인 기능", notes = "승인하기")
	public ResponseEntity<Map<Object, Object>> updatePermission(@RequestParam(name="type", required = false) String type, @RequestParam("idx") String idx) throws Exception {
		log.info("#### AdminController.updatePermission type :  " + type + "  idx : " + idx);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(adminService.updatePermission(type, idx), HttpStatus.OK);
		return rtn;
	}
	
	/*
	@PutMapping("/permission/once")
	@ApiOperation(value = "[관리자] 일괄승인기능", notes = "일괄 승인하기 1/2/3/")
	public ResponseEntity<Map<Object, Object>> updatePermissionOnce(@RequestParam("idxCrmReport") String idxCrmReport) throws Exception {
		log.info("#### AdminController.updatePermissionOnce idxCrmReport : " + idxCrmReport);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(adminService.updatePermissionOnce(idxCrmReport), HttpStatus.OK);
		return rtn;
	}
	*/
	
	@GetMapping("/center/status")
	@ApiOperation(value = "[관리자] 센터현황", notes = "센터현황 조회 하기")
	public ResponseEntity<Map<Object, Object>> selectCenterStatus(@RequestParam("idxCrmCenter") int idxCrmCenter, @RequestParam("startDate") String startDate
			, @RequestParam("endDate") String endDate) throws Exception {
		log.info("#### AdminController.selectCenterStatus idxCrmCenter : " + idxCrmCenter + " startDate : " + startDate + " endDate : " + endDate);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(adminService.selectCenterStatus(idxCrmCenter, startDate, endDate), HttpStatus.OK);
		return rtn;
	}
}
