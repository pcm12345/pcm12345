package com.carrot.center.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.carrot.center.dao.CenterVO;
import com.carrot.center.service.CenterService;
import com.sun.net.httpserver.HttpServer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(description = "센터정보 등록/수정/삭제" , tags="센터 관리 부분")
@Slf4j
public class CenterController {

	@Autowired
	private CenterService centerService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	/**
	 * 센터 정보 등록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/api/v1/center")
	@ApiOperation(value="센터정보 등록", notes="센터 정보 등록하기")
	public ResponseEntity<Map<Object, Object>> insertCenter(@ModelAttribute CenterVO vo ) throws Exception {
		
		log.info("###### CenterController.insertCenter vo : " + vo.toString());
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(centerService.insertCenter(vo), HttpStatus.OK);
	     return rtn;
	}
	
	/**
	 * 센터 전체 목록
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/center/all")
	@ApiOperation(value="센터정보 전체 목록", notes="센터전체 목록 보기")
	public ResponseEntity<Map<Object, Object>> selectCenterAll() throws Exception {
		
		log.info("###### CenterController.selectCenterAll");
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(centerService.selectCenterAll(), HttpStatus.OK);
	     return rtn;
	}
	
	/**
	 * 센터 정보 조회
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/center/{idx}")
	@ApiOperation(value="센터정보 조회", notes="센터 정보 조회")
	public ResponseEntity<Map<Object, Object>> selectCenter(@PathVariable("idx") int idx) throws Exception {
		
		log.info("###### CenterController.selectCenter");
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(centerService.selectCenter(idx), HttpStatus.OK);
	     return rtn;
	}
	
	/**
	 * 센터 정보 수정
	 * @param idx
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/api/v1/center/{idx}")
	@ApiOperation(value="센터정보 수정", notes="센터 정보 수정하기")
	public ResponseEntity<Map<Object, Object>> updateCenter(@PathVariable("idx") int idx, @ModelAttribute CenterVO vo ) throws Exception {
		
		log.info("###### CenterController.updateCenter idx : " + idx + " vo : " + vo);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(centerService.updateCenter(idx, vo), HttpStatus.OK);
	     return rtn;
	}
	
	/**
	 * 센터 검색 목록 조회
	 * @param name
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/center/list")
	@ApiOperation(value="센터검색 목록", notes="센터 검색 목록 보기")
	public ResponseEntity<Map<Object, Object>> selectCenterList(@RequestParam(name="name", required = false, defaultValue = "") String name, @RequestParam(name="permission", required = false, defaultValue = "") String permission,
			@RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter, @RequestParam(value="startDate", defaultValue="") String startDate, 
			@RequestParam(value="endDate", defaultValue="") String endDate, @RequestParam(value="pageSize", defaultValue="50") int pageSize,
			@RequestParam(value="pageNum", defaultValue="1") int pageNum) throws Exception {
		log.info("###### CenterController.insertCenter name : " + name + " permission : " + permission + " idxCrmCenter : " + idxCrmCenter + " startDate : "+ startDate + " endDate : " + endDate + " pageNum : " + pageNum + " pageSize : " + pageSize);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(centerService.selectCenterList(name, permission, startDate, endDate, centerService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
	     return rtn;
	}
	
	/**
	 * 센터 정보 삭제
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/api/v1/center/contract/{idx}")
	@ApiOperation(value="센터정보 삭제", notes="센터 정보 삭제하기(계약종료)")
	public ResponseEntity<Map<Object, Object>> deleteCenter(@PathVariable("idx") int idx) throws Exception {
		log.info("###### CenterController.deleteCenter idx : " + idx);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(centerService.deleteCenter(idx), HttpStatus.OK);
	     return rtn;
	}
		
	// 센터에서 EAP배정 접수(신청) 했을 때 API
	// crm_client에 eap_process_status(센터접수)랑 idx_crm_center 수정 됨
	
	// 센터 이름 중복체크
	@GetMapping("/api/v1/center/dup")
	@ApiOperation(value="센터 이름 중복체크", notes="센터 이름 중복체크")
	public ResponseEntity<Map<Object, Object>> duplicateCenter(@RequestParam("name") String name) throws Exception {
		log.info("###### CenterController.duplicateCenter name : " + name);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(centerService.duplicateCenter(name), HttpStatus.OK);
	     return rtn;
	}
	
	@GetMapping("/api/v1/center/staff/{idx}")
	@ApiOperation(value="센터 상담사 목록", notes="센터 소속 상담사 목록 가져오기")
	public ResponseEntity<Map<Object, Object>> selectCenterStaffList(@PathVariable("idx") int idx) throws Exception {
		log.info("###### CenterController.selectCenterStaffList idx : " + idx);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(centerService.selectCenterStaffList(idx), HttpStatus.OK);
	     return rtn;
	}
	
	@GetMapping("/api/v1/center/eap/list")
	@ApiOperation(value="[협약센터용] 센터 별 신규 상담 목록", notes="협약센터에서 신규상담 목록 조회(사용안함)")
	public ResponseEntity<Map<Object, Object>> selectCenterEapList(@RequestParam(value="pageSize", defaultValue="50") int pageSize
			, @RequestParam(value="pageNum", defaultValue="1") int pageNum, HttpServletRequest request) throws Exception {
		
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		
		log.info("###### CenterController.selectCenterEapList idxCrmCenter : " + map.get("idxCrmCenter") + " pageSize : " + pageSize + " pageNum : " + pageNum);
		
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(centerService.selectCenterEapList(Integer.parseInt(map.get("idxCrmCenter").toString()), centerService.doPaging(pageNum, pageSize), pageSize ), HttpStatus.OK);
	     return rtn;
	}
	
	// 상담진행신청 눌렀을 때
	@PutMapping("/api/v1/center/eap/apply")
	@ApiOperation(value="[협약센터용] 신규신청목록 에서 상담진행신청 눌렀을 때", notes="신규 상담 신청목록 에서 상담진행신청 눌렀을 때(사용안함)")
	public ResponseEntity<Map<Object, Object>> insertCrmEap(@RequestParam("idxCrmClient") int idxCrmClient, HttpServletRequest request) throws Exception {
		
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		
		log.info("###### CenterController.insertCrmEap idxCrmCenter : " + map.get("idxCrmCenter"));
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(centerService.insertCrmEap(idxCrmClient, Integer.parseInt(map.get("idxCrmCenter").toString())), HttpStatus.OK);
	     return rtn;
	}
}
