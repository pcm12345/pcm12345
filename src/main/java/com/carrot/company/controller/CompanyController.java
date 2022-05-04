package com.carrot.company.controller;

import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrot.company.dao.CompanyVO;
import com.carrot.company.service.CompanyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(description = "기업 등록/수정/삭제" , tags="기업 관리 부분")
@Slf4j
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
    private RedisTemplate redisTemplate;
	
	/**
	 * 기업 전체 조회
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/company/all")
	@ApiOperation(value="기업 전체정보 조회", notes="기업 전체 조회하기")
	public ResponseEntity<Map<Object, Object>> selectCompanyAll() throws Exception {
		log.info("#### CompanyController.selectCompanyAll");
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(companyService.selectCompanyAll(), HttpStatus.OK);
	     return rtn;
	}
	
	/**
	 * 기업 정보 조회
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/company/{idx}")
	@ApiOperation(value="기업 정보 조회", notes="기업 정보 상세조회")
	public ResponseEntity<Map<Object, Object>> selectCompany(@PathVariable("idx") int idx) throws Exception {
		log.info("#### CompanyController.selectCompany idx : " + idx);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(companyService.selectCompany(idx), HttpStatus.OK);
	     return rtn;
	}

	/**
	 * 기업 코드 중복 조회
	 * @param company_code
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/company/company_code/{company_code}")
	@ApiOperation(value="기업 코드 중복 조회", notes="기업 코드 중복 조회")
	public ResponseEntity<Map<Object, Object>> checkCompanyCode(@PathVariable("company_code") String company_code) throws Exception {
		log.info("#### CompanyController.checkCompanyCode company_code : " + company_code);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(companyService.checkCompanyCode(company_code), HttpStatus.OK);
		return rtn;
	}
	
	/**
	 * 기업 목록 조회
	 * @param value
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/company/list")
	@ApiOperation(value="기업 목록 조회", notes="기업 목록 조회")
	public ResponseEntity<Map<Object, Object>> selectCompanyList(@RequestParam(name="value", required = false, defaultValue = "") String value,
																 @RequestParam(value="pageSize", defaultValue="50") int pageSize,
																 @RequestParam(value="pageNum", defaultValue="1") int pageNum) throws Exception {
		log.info("#### CompanyController.selectCompanyList value : " + value);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(companyService.selectCompanyList(value, companyService.doPaging(pageNum, pageSize), pageSize, HttpStatus.OK), HttpStatus.OK);
	     return rtn;
	}
	
	/**
	 * 기업정보 등록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/api/v1/company")
	@ApiOperation(value="기업 정보 등록", notes="기업 등록하기"
			+ "{\r\n" + 
			"  \"code\": \"H202010332\",\r\n" + 
			"  \"consCount\": 30,\r\n" + 
			"  \"consEndDate\": \"2020-11-02T00:29:48.745Z\",\r\n" + 
			"  \"consStartDate\": \"2020-10-21T00:29:48.745Z\",\r\n" + 
			"  \"familyYn\": \"Y\",\r\n" + 
			"  \"manager\": \"김담당\",\r\n" + 
			"  \"memo\": \"\",\r\n" + 
			"  \"name\": \"현대모비스\",\r\n" + 
			"  \"phone\": \"01012344321\",\r\n" + 
			"  \"price\": 100000,\r\n" + 
			"  \"testYn\": \"Y\",\r\n" + 
			"  \"totalPrice\": 3000000,\r\n" + 
			"  \"type\": \"전화\"\r\n" + 
			"}")
	public ResponseEntity<Map<Object, Object>> insertCompany(@RequestBody CompanyVO vo) throws Exception {
		log.info("#### CompanyController.insertCompany vo : " + vo.toString());
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(companyService.insertCompany(vo), HttpStatus.OK);
	     return rtn;
	}
	
	/**
	 * 기업 정보 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/api/v1/company/{idx}")
	@ApiOperation(value="기업 정보 수정", notes="기업 정보 수정하기")
	public ResponseEntity<Map<Object, Object>> updateCompany(@PathVariable("idx") int idx, @RequestBody CompanyVO vo) throws Exception {
		log.info("#### CompanyController.updateCompany idx : " + idx + " vo : " + vo.toString());
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(companyService.updateCompany(idx, vo), HttpStatus.OK);
	     return rtn;
	}
	
	/**
	 * 기업 정보 삭제
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/api/v1/company/{idx}")
	@ApiOperation(value="기업 정보 삭제", notes="기업 정보 삭제하기")
	public ResponseEntity<Map<Object, Object>> deleteCompany(@PathVariable("idx") int idx) throws Exception {
		log.info("#### CompanyController.deleteCompany idx : " + idx);
		 ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(companyService.deleteCompany(idx), HttpStatus.OK);
	     return rtn;
	}

}
