package com.carrot.client.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrot.client.dao.ClientVO;
import com.carrot.client.dao.MemoVO;
import com.carrot.client.dao.PersonVO;
import com.carrot.client.dao.PurchaseVO;
import com.carrot.client.service.ClientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(description = "/api/v1/client" , tags="내담자 및 고객차트 & 수납 관리 부분")
@Slf4j
public class ClientController {

	@Autowired
    private RedisTemplate redisTemplate;

	@Autowired
	private ClientService clientService;

	/**
	 * 내담자 특이사항 등록
	 * @param vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/api/v1/client/memo")
	@ApiOperation(value="내담자 특이사항 등록", notes="내담자 특이사항 등록하기")
	public ResponseEntity<Map<Object, Object>> insertMemo(@RequestBody MemoVO vo, HttpServletRequest request) throws Exception {
		log.info("#### ClientController.insertMemo vo : " + vo.toString());
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		vo.setIdxCrmStaffCreate(Integer.parseInt(map.get("idx").toString()));

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.insertMemo(vo), HttpStatus.OK);
	    return rtn;
	}

	/**
	 * 특이사항 목록
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/memo/list")
	@ApiOperation(value="내담자 특이사항 목록", notes="내담자 특이사항 목록")
	public ResponseEntity<Map<Object, Object>> selectMemoList(@RequestParam("idxCrmPerson") int idxCrmPerson
		, @RequestParam(value = "idxCrmStaff", defaultValue = "-1") int idxCrmStaff
		) throws Exception {
		log.info("#### ClientController.selectMemoList");
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectMemoList(idxCrmPerson, idxCrmStaff), HttpStatus.OK);
	    return rtn;
	}

	/**
	 * 특이사항 삭제
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/api/v1/client/memo/{idx}")
	@ApiOperation(value="내담자 특이사항 삭제", notes="내담자 특이사항 삭제하기")
	public ResponseEntity<Map<Object, Object>> deleteMemo(@PathVariable("idx") int idx) throws Exception {
		log.info("#### ClientController.deleteMemo idx : " + idx);

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.deleteMemo(idx), HttpStatus.OK);
	    return rtn;
	}

	@GetMapping("/api/v1/client/person/dup_phone")
	@ApiOperation(value="phone 중복체크", notes = "phone 중복체크")
	public ResponseEntity<Map<Object, Object>> duplicatPerson(@RequestParam("phone") String phone) throws Exception {
		log.info("#### ClientController.duplicatPerson phone : " + phone);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.duplicatPerson(phone),
				HttpStatus.OK);
		return rtn;
	}

	/**
	 * 내담자 정보 등록
	 * @param vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/api/v1/client/person")
	@ApiOperation(value="내담자 정보 등록", notes="내담자 등록하기")
	public ResponseEntity<Map<Object, Object>> insertPerson(@RequestBody PersonVO vo, HttpServletRequest request) throws Exception {
		log.info("#### ClientController.insertPerson vo : " + vo.toString());
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		vo.setIdxCrmStaffCreate(Integer.parseInt(map.get("idx").toString()));

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.insertPerson(vo), HttpStatus.OK);
	    return rtn;
	}

	/**
	 * 내담자 정보 수정
	 * @param vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/api/v1/client/person/{idx}")
	@ApiOperation(value="내담자 정보 수정", notes="내담자 수정하기")
	public ResponseEntity<Map<Object, Object>> updatePerson(@PathVariable("idx") int idx, @RequestBody PersonVO vo, HttpServletRequest request) throws Exception {
		log.info("#### ClientController.updatePerson idx : " + idx + " vo : " + vo.toString());
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		vo.setIdxCrmStaffUpdate(Integer.parseInt(map.get("idx").toString()));

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.updatePerson(idx, vo), HttpStatus.OK);
	    return rtn;
	}

	/**
	 * 내담자 정보 조회
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/person/{idx}")
	@ApiOperation(value="내담자 정보 조회", notes="내담자 조회하기")
	public ResponseEntity<Map<Object, Object>> selectPerson(@PathVariable("idx") int idx, HttpServletRequest request ) throws Exception {
		log.info("#### ClientController.selectPerson idx : " + idx);
		int idxCrmStaff = -1;
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		if(request.getHeader("accessToken") != null){
			map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
			if("STAFF".equals(map.get("authority").toString())) {
				idxCrmStaff = Integer.parseInt(map.get("idx").toString());
			}
		}

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectPerson(idx, idxCrmStaff), HttpStatus.OK);
	    return rtn;
	}

	/**
	 * 내담자 목록 조회
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/person/list")
	@ApiOperation(value="내담자 목록 조회", notes="내담자 목록 조회하기")
	public ResponseEntity<Map<Object, Object>> selectPersonList (
			@RequestParam(value="clientStatus", defaultValue="") String clientStatus
			, @RequestParam(value="idxCrmCenter", defaultValue="-1") int idxCrmCenter
			, @RequestParam(value="pageSize", defaultValue="50") int pageSize
			, @RequestParam(value="pageNum", defaultValue="-1") int pageNum
			, @RequestParam(value="startDate", defaultValue="") String startDate
			, @RequestParam(value="endDate", defaultValue="") String endDate
			, @RequestParam(value="value", defaultValue="") String value
	) throws Exception {
		log.info("#### ClientController.selectPersonList pageSize : " + pageSize + " pageNum : " + pageNum + " startDate : " + startDate + " endDate : " + endDate);
		int startPage = 0;
		if(pageNum == -1) {
			startPage = -1;
		} else {
			startPage = clientService.doPaging(pageNum, pageSize);
		}
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectPersonList(clientStatus, idxCrmCenter, startPage, pageSize, startDate, endDate, value), HttpStatus.OK);
	     return rtn;
	}


	/**
	 * 고객 정보 등록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/api/v1/client")
	@ApiOperation(value="고객 차트 정보 등록", notes="고객 차트 등록하기"
//			+ "{\r\n" +
//			"  \"belong\": \"현대모비스\",\r\n" +
//			"  \"birth\": \"2002-10-21\",\r\n" +
//			"  \"clientCode\": \"123456\",\r\n" +
//			"  \"clientType\": \"유형\",\r\n" +
//			"  \"consCount\": 10,\r\n" +
//			"  \"consProcess\": \"\",\r\n" +
//			"  \"dangerStepTitle\": \"\",\r\n" +
//			"  \"eapAddress\": \"\",\r\n" +
//			"  \"eapCompany\": \"\",\r\n" +
//			"  \"eapDetailAddress\": \"\",\r\n" +
//			"  \"eapHope\": \"\",\r\n" +
//			"  \"eapPost\": \"\",\r\n" +
//			"  \"eapProcessStatus\": \"\",\r\n" +
//			"  \"eapRel\": \"\",\r\n" +
//			"  \"email\": \"hong@email.com\",\r\n" +
//			"  \"gender\": \"남\",\r\n" +
//			"  \"goal\": \"\",\r\n" +
//			"  \"idxCrmCenter\": -1,\r\n" +
//			"  \"idxCrmMetaProduct\": 1,\r\n" +
//			"  \"idxCrmMetaSubject\": 27,\r\n" +
//			"  \"idxCrmMetaType\": 0,\r\n" +
//			"  \"idxCrmStaff\": 1,\r\n" +
//			"  \"mainProblem\": \"\",\r\n" +
//			"  \"name\": \"홍길동\",\r\n" +
//			"  \"plan\": \"\",\r\n" +
//			"  \"price\": 0,\r\n" +
//			"  \"route\": \"\"\r\n" +
//			"}"
		)
	public ResponseEntity<Map<Object, Object>> insertClient(@RequestBody ClientVO vo, HttpServletRequest request) throws Exception {
		log.info("#### ClientController.insertClient vo : " + vo.toString());
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		vo.setIdxCrmStaffCreate(Integer.parseInt(map.get("idx").toString()));

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.insertClient(vo), HttpStatus.OK);
	    return rtn;
	}

	/**
	 * 고객 차트 정보 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/api/v1/client/{idx}")
	@ApiOperation(value="고객 차트 정보 수정", notes="고객 차트 수정하기"
			+ "{\r\n" +
			"  \"belong\": \"눈누난나\",\r\n" +
			"  \"birth\": \"2005-05-05\",\r\n" +
			"  \"clientCode\": \"123457\",\r\n" +
			"  \"consCount\": 5,\r\n" +
			"  \"consProcess\": \"상담과정\",\r\n" +
			"  \"email\": \"hongdu@email.com\",\r\n" +
			"  \"gender\": \"남\",\r\n" +
			"  \"goal\": \"목표는 언제나 위를 향해\",\r\n" +
			"  \"mainProblem\": \"string\",\r\n" +
			"  \"name\": \"홍두깨\",\r\n" +
			"\"eap_rel\": \"\"\r\n" +
			"}")
	public ResponseEntity<Map<Object, Object>> updateClient(@PathVariable("idx") int idx, @RequestBody ClientVO vo, HttpServletRequest request) throws Exception {
		log.info("#### ClientController.updateClient idx : " + idx + " vo : " + vo.toString());

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		vo.setIdxCrmStaffUpdate(Integer.parseInt(map.get("idx").toString()));

		if(vo.getClientCode() == null || "".equals(vo.getClientCode())){
			vo.setClientCode( Integer.toString(idx + 1000) );
		}

		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.updateClient(idx, vo), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 그냥 따로 만듬 고객차트에서 상담차트 목록
	 * @param idxCrmPerson
	 * @param reportYn
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/chart/list")
	@ApiOperation(value="고객 차트에서 상담차트 목록", notes="고객 차트에서 상담차트 목록 조회하기")
	public ResponseEntity<Map<Object, Object>> selectChartList(
			@RequestParam("idxCrmPerson") int idxCrmPerson
			, @RequestParam(value = "idxCrmStaff", defaultValue = "-1") int idxCrmStaff
		) throws Exception {
		log.info("#### ClientController.selectChartList idxCrmPerson : " + idxCrmPerson + " idxCrmStaff : " + idxCrmStaff);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectChartList(idxCrmPerson, idxCrmStaff), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 고객차트에서 B2C 수납 정보 목록
	 * @param idx
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/chart/pur/b2c/{idx}")
	@ApiOperation(value="고객차트에서 B2C 수납 정보 목록", notes="고객차트에서 B2C 수납 정보 목록")
	public ResponseEntity<Map<Object, Object>> selectChartPurchaseB2C(@PathVariable("idx") int idx
			, @RequestParam(value="pageSize", defaultValue="5") int pageSize, @RequestParam(value="pageNum", defaultValue="1") int pageNum) throws Exception {
		log.info("#### ClientController.selectChartPurchaseB2C idx : " + idx + " pageSize : " + pageSize + " pageNum : " + pageNum);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectChartPurchaseB2C(idx, clientService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 고객 차트 정보 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/{idx}")
	@ApiOperation(value="고객 차트 정보 조회", notes="고객 차트 조회하기")
	public ResponseEntity<Map<Object, Object>> selectClient(@PathVariable("idx") int idx, @RequestParam(value="reportYn", required = false, defaultValue = "") String reportYn) throws Exception {
		log.info("#### ClientController.selectClient idx : " + idx + " reportYn : " + reportYn);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectClient(idx, reportYn), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 고객 차트 목록 조회
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/list")
	@ApiOperation(value="고객 차트 목록 조회", notes="고객 차트 목록 조회하기")
	public ResponseEntity<Map<Object, Object>> selectClientList(
			@RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name="value", required = false) String value
			, @RequestParam(value="pageSize", defaultValue="50") int pageSize
			, @RequestParam(value="pageNum", defaultValue="1") int pageNum
			, @RequestParam(name="clientStatus", required = false) String clientStatus
			, @RequestParam(value="idxCrmStaff", defaultValue="-1") int idxCrmStaff
			, @RequestParam(value="consStartDate", required = false) String consStartDate
			, @RequestParam(value="consEndDate", required = false) String consEndDate) throws Exception {
		log.info("#### ClientController.selectClientList idxCrmCenter : " + idxCrmCenter + " clientStatus : " + clientStatus + " value : " + value + " consStartDate : " + consStartDate + " consEndDate : " + consEndDate);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectClientList(idxCrmCenter, clientStatus, value, idxCrmStaff, consStartDate, consEndDate, clientService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 상담사 고객 차트 목록 조회
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/counselor/list")
	@ApiOperation(value="상담사 고객 차트 목록 조회", notes="상담사 고객 차트 목록 조회하기")
	public ResponseEntity<Map<Object, Object>> selectCounselorClientList(
			@RequestParam(name="idxCenter", required = false, defaultValue = "-1") int idxCenter
			, @RequestParam(name="value", required = false) String value
			, @RequestParam(value="pageSize", defaultValue="50") int pageSize
			, @RequestParam(value="pageNum", defaultValue="1") int pageNum
			, @RequestParam(name="clientStatus", required = false) String clientStatus
			, @RequestParam(value="reserveStatus", required = false) String reserveStatus
			, @RequestParam(value="consStartDate", required = false) String consStartDate
			, @RequestParam(value="consEndDate", required = false) String consEndDate
			, @RequestParam(value="idxCrmStaff", required = false, defaultValue = "0") int idxCrmStaff
			, HttpServletRequest request
		) throws Exception {
		log.info("#### ClientController.selectCounselorClientList idxCenter : " + idxCenter + " clientStatus : " + clientStatus + " value : " + value + " consStartDate : " + consStartDate + " consEndDate : " + consEndDate);
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		if(idxCrmStaff == 0 ){
			//세션을 통해서 가져온 상담사 정보를 통해 조회한다.
			//음수 값이면 상담사 세션과 관계없이 전체 정보를 조회한다.
			//양수 값이면 상담사 세션과 관계없이 전달받은 idx에 해당하는 상담사의 고객 목록을 조회한다.
			idxCrmStaff = Integer.parseInt(map.get("idx").toString());
		}
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectCounselorClientList(idxCenter, clientStatus, reserveStatus, value, consStartDate, consEndDate, clientService.doPaging(pageNum, pageSize), pageSize, idxCrmStaff), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/client/list/gm")
	@ApiOperation(value="고객 차트 목록 조회(고문님 버전)", notes="고객 차트 목록 조회하기")
	  public ResponseEntity<?>getClientList(
	      @RequestParam Map<String, Object> param,
	      @RequestParam(value = "page", defaultValue = "1") int page,
	      @RequestParam(value = "pageSize", defaultValue = "30") int pageSize
	  ){
		log.info("#####HJLOG gm getClientList : " + param.toString());
	    Map<String, Object> response = clientService.getList(param, page, pageSize);
	    return ResponseEntity.ok(response);
	  }

	/**
	 * 고객 차트정보 삭제
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/api/v1/client/{idx}")
	@ApiOperation(value="고객 차트 정보 삭제", notes="고객 차트 삭제하기")
	public ResponseEntity<Map<Object, Object>> deleteClient(@PathVariable("idx") int idx) throws Exception {
		log.info("#### ClientController.deleteClient idx : " + idx);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.deleteClient(idx), HttpStatus.OK);
	     return rtn;
	}

	@GetMapping("/api/v1/client/all")
	@ApiOperation(value="고객 차트 전체 조회", notes="고객 차트 전체 조회하기")
	public ResponseEntity<Map<Object, Object>> selectClientAll(@RequestParam(value="type", required = false, defaultValue = "") String type
			, @RequestParam(value="idxCrmStaff", required = false, defaultValue = "-1") int idxCrmStaff) throws Exception {
		log.info("#### ClientController.selectClientAll type : " + type + " idxCrmStaff : " + idxCrmStaff);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectClientAll(type, idxCrmStaff), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 수납정보 등록하기
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/api/v1/client/pur")
	@ApiOperation(value="수납 정보 등록", notes="수납정보 등록하기"
			+ "{\r\n" +
			"  \r\n" +
			"  \"idxCrmClient\": 1,\r\n" +
			"  \"idxCrmCompany\": 1,\r\n" +
			"  \"memo\": \"수납을 하셨군요??\",\r\n" +
			"  \"name\": \"월요일\",\r\n" +
			"  \"purDate\": \"2020-11-01\",\r\n" +
			"  \"purPayWay\": \"현금\",\r\n" +
			"  \"purPrice\": 5000,\r\n" +
			"  \"status\": \"미수금\"\r\n" +
			"}")
	public ResponseEntity<Map<Object, Object>> insertPurchase(@RequestBody PurchaseVO vo) throws Exception {
		log.info("#### ClientController.insertPurchase vo : " + vo);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.insertPurchase(vo), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 수납정보 조회
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/pur/{idx}")
	@ApiOperation(value="수납 정보 조회", notes="수납정보 조회")
	public ResponseEntity<Map<Object, Object>> selectPurchase(@PathVariable("idx") int idx) throws Exception {
		log.info("#### ClientController.selectPurchase idx : " + idx);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectPurchase(idx), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 수납 정보 조회
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/pur/b2c/{idx}")
	@ApiOperation(value="B2C수납 정보 조회", notes="B2C수납정보 조회")
	public ResponseEntity<Map<Object, Object>> selectPurchaseB2C(@PathVariable("idx") int idx
			, @RequestParam(value="pageSize", defaultValue="50") int pageSize, @RequestParam(value="pageNum", defaultValue="1") int pageNum) throws Exception {
		log.info("#### ClientController.selectPurchaseB2C idx : " + idx + " pageSize : " + pageSize + " pageNum : " + pageNum);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectPurchaseB2C(idx, clientService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 수납 정보 조회
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/pur/b2b/{idx}")
	@ApiOperation(value="B2B수납 정보 조회", notes="B2B수납정보 조회")
	public ResponseEntity<Map<Object, Object>> selectPurchaseB2B(@PathVariable("idx") int idx
			, @RequestParam(value="pageSize", defaultValue="50") int pageSize, @RequestParam(value="pageNum", defaultValue="1") int pageNum
			, @RequestParam(value="startDate", defaultValue="") String startDate, @RequestParam(value="endDate", defaultValue="") String endDate ) throws Exception {
		log.info("#### ClientController.selectPurchaseB2B idx : " + idx + " pageSize : " + pageSize + " pageNum : " + pageNum + " startDate : " + startDate + " endDate : " + endDate);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectPurchaseB2B(idx, startDate, endDate, clientService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 수납 정보 수정
	 * @param idx
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/api/v1/client/pur/{idx}")
	@ApiOperation(value="수납 정보 수정", notes="수납정보 수정하기"
			+ "{\r\n" +
			"  \r\n" +
			"  \"idxCrmClient\": 1,\r\n" +
			"  \"idxCrmCompany\": 1,\r\n" +
			"  \"memo\": \"수납을 하셨군요??\",\r\n" +
			"  \"name\": \"월요일\",\r\n" +
			"  \"purDate\": \"2020-11-01\",\r\n" +
			"  \"purPayWay\": \"현금\",\r\n" +
			"  \"purPrice\": 50000,\r\n" +
			"  \"status\": \"수납\"\r\n" +
			"}")
	public ResponseEntity<Map<Object, Object>> updatePurchase(@PathVariable("idx") int idx, @RequestBody PurchaseVO vo) throws Exception {
		log.info("#### ClientController.updatePurchase idx : " + idx + " vo : " + vo);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.updatePurchase(idx, vo), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * B2C수납 목록 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/pur/list")
	@ApiOperation(value="수납 목록 조회", notes="수납 목록조회하기")
	public ResponseEntity<Map<Object, Object>> selectPurchaseList(@RequestParam(name="value", required = false) String value, @RequestParam(name="type", required = false, defaultValue = "B2C") String type,
			@RequestParam(value="pageSize", defaultValue="50") int pageSize, @RequestParam(value="pageNum", defaultValue="1") int pageNum) throws Exception {
		log.info("#### ClientController.selectPurchaseList  type : " + type + " value : " + value + " pageSize : " + pageSize + " pageNum : " + pageNum);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectPurchaseList(type, value, clientService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * B2B목록에서 눌렀을 때 사용자 목록 나오는 API 추가 필요
	 * @param idxCrmCenter
	 * @param idxCrmCompany
	 * @param value
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/pur/user/list")
	@ApiOperation(value="기업수납 목록 조회에서 고객 차트 조회", notes="B2B목록에서 눌렀을 때 사용자 목록 나오는 API")
	public ResponseEntity<Map<Object, Object>> selectPurchaseB2BUserList(@RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name="idxCrmCompany", required = false, defaultValue = "-1") int idxCrmCompany, @RequestParam(name="value", required = false) String value
			, @RequestParam(value="pageSize", defaultValue="50") int pageSize, @RequestParam(value="pageNum", defaultValue="1") int pageNum) throws Exception {
		log.info("#### ClientController.selectPurchaseB2BUserList  idxCrmCenter : " + idxCrmCenter + " idxCrmCompany : " + idxCrmCompany + " value : " + value + " pageSize : " + pageSize + " pageNum : " + pageNum);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectPurchaseB2BUserList(idxCrmCompany, idxCrmCenter, value, clientService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 수납정보 삭제
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/api/v1/client/pur/{idx}")
	@ApiOperation(value="수납 정보 삭제", notes="수납정보 삭제하기")
	public ResponseEntity<Map<Object, Object>> deletePurchase(@PathVariable("idx") int idx) throws Exception {
		log.info("#### ClientController.deletePurchase idx : " + idx);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.deletePurchase(idx), HttpStatus.OK);
	     return rtn;
	}

	@GetMapping("/api/v1/client/gubun")
	@ApiOperation(value="상담구분 조회", notes="상담구분에 따른 상품명 가져오기")
	public ResponseEntity<Map<Object, Object>> selectGubun(@RequestParam(name="value", required = false, defaultValue = "") String value) throws Exception {
		log.info("#### ClientController.selectGubun value : " + value);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectGubun(value), HttpStatus.OK);
	     return rtn;
	}

	// 메타데이터 불러오기
	@GetMapping("/api/v1/client/meta")
	@ApiOperation(value="메타데이터 불러오기", notes="고객 차트 등록/수정 화면 때 상담구분/유형/주제 데이터")
	public ResponseEntity<Map<Object, Object>> selectMetaAll() throws Exception {
		log.info("#### ClientController.selectMetaAll");
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectMetaAll(), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * EAP 배정현황 목록
	 * @param status
	 * @param value
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/eap/list")
	@ApiOperation(value="[본원] EAP 배정현황 목록", notes="EAP 배정현황 목록 조회")
	public ResponseEntity<Map<Object, Object>> selectEapList(@RequestParam(name="status", required = false, defaultValue = "") String status
			, @RequestParam(name="value", required = false, defaultValue = "") String value , @RequestParam(name="pageNum", required = false, defaultValue = "-1") int pageNum
			, @RequestParam(name="pageSize", required = false, defaultValue = "-1") int pageSize) throws Exception {
		log.info("#### ClientController.selectEapList status : " + status + " value : " + value + " pageNum : " + pageNum + " pageSize : " + pageSize);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectEapList(status, value, clientService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
	     return rtn;
	}

	@PutMapping("/api/v1/client/eap/permission")
	@ApiOperation(value="[본원] EAP 배정현황 목록 승인", notes="EAP 배정현황 목록에서 승인하기")
	public ResponseEntity<Map<Object, Object>> applyEap(@RequestParam(name="idxCrmClient", required = false, defaultValue = "-1") int idxCrmClient
			, @RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter ) throws Exception {
		log.info("#### ClientController.applyEap idxCrmClient : " + idxCrmClient + " idxCrmCenter : " + idxCrmCenter);
		// crm_client 테이블 center idx 들어감
		// crm_eap 테이블 status들 수정
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.applyEap(idxCrmClient, idxCrmCenter), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 본원 매출내역
	 * @param type
	 * @param clientName
	 * @param staffName
	 * @param purPayWay
	 * @param gubun
	 * @param typeName
	 * @param idxCrmCenter
	 * @param pageSize
	 * @param pageNum
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/client/history/pur")
	@ApiOperation(value="[본원] 매출내역", notes="B2B/B2C 매출내역")
	public ResponseEntity<Map<Object, Object>> selectPurchaseHistory(
			@RequestParam(name="type", required = true, defaultValue = "B2C") String type
			, @RequestParam(name="clientName", required = true, defaultValue = "") String clientName
			, @RequestParam(name="staffName", required = true, defaultValue = "") String staffName
			, @RequestParam(name="purPayWay", required = true, defaultValue = "") String purPayWay
			, @RequestParam(name="gubun", required = true, defaultValue = "") String gubun
			, @RequestParam(name="typeName", required = true, defaultValue = "") String typeName
			, @RequestParam(value="idxCrmCenter", defaultValue="-1") int idxCrmCenter
			, @RequestParam(value="idxCrmCompany", defaultValue="-1") int idxCrmCompany
			, @RequestParam(value="pageSize", defaultValue="50") int pageSize
			, @RequestParam(value="pageNum", defaultValue="1") int pageNum
			, @RequestParam(value="startDate", required = false) String startDate
			, @RequestParam(value="endDate", required = false) String endDate ) throws Exception {
		log.info("#### ClientController.selectPurchaseHistory type: " + type + " clientName : " + clientName + " staffName : " + staffName + " purPayWay : " + purPayWay
				+ " gubun : " + gubun + " typeName : " + typeName + " idxCrmCenter : " + idxCrmCenter + " idxCrmCompany : " + idxCrmCompany + " pageSize : " + pageSize + " pageNum : " + pageNum
				 + " startDate : " + startDate + " endDtae : " + endDate);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(clientService.selectPurchaseHistory(type, clientName, staffName, purPayWay,
				 gubun, typeName, idxCrmCenter, idxCrmCompany, clientService.doPaging(pageNum, pageSize), pageSize, startDate, endDate), HttpStatus.OK);
		 return rtn;
	}

	@GetMapping("/api/v1/client/testtest")
	public void testtest() throws Exception {

		String ddate = clientService.formatDateToString(new Date(), "yyMMdd");

		String a = "";
		int b = 0;
		b = Integer.parseInt(ddate+"101");
		a = Integer.parseInt(ddate+"101") + 5 + "";

		log.info("#### client Code : " + b);
		log.info("#### client Code : " + a);
	}

}
