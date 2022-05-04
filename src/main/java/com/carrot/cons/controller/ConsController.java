package com.carrot.cons.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carrot.common.service.excelService;
import com.carrot.cons.dao.excelCompanyStaticVO;
import org.apache.commons.beanutils.BeanUtils;
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

import com.carrot.cons.dao.ReportVO;
import com.carrot.cons.dao.ReservationVO;
import com.carrot.cons.service.ConsService;
import com.carrot.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(description = "/api/v1/cons", tags = "상담(예약/일지) 관리 부분")
@Slf4j
public class ConsController {

	@Autowired
	private ConsService consService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private UserService userService;

	@PostMapping("/api/v1/cons")
	@ApiOperation(value="상담예약 등록 본원이면 idx_crm_center = 0", notes = "상담예약 등록하기"
			+ "{\r\n" +
			"  \"centerName\": \"강남센터\",\r\n" +
			"  \"consDate\": \"2020-11-05T02:50:22.797Z\",\r\n" +
			"  \"consTime\": \"10:00/10:30/11:00\",\r\n" +
			"  \"idxCrmCenter\": 1,\r\n" +
			"  \"idxCrmClient\": 1,\r\n" +
			"  \"idxCrmStaff\": 1,\r\n" +
			"  \"memo\": \"월요일을 싫어합니다.\",\r\n" +
			"  \"name\": \"월요일\",\r\n" +
			"  \"reservationStatus\": \"예약\",\r\n" +
			"  \"staffName\": \"테스터\"\r\n" +
			"}")
	public ResponseEntity<Map<Object, Object>> insertReservation(@RequestBody ReservationVO vo, HttpServletRequest request) throws Exception {
		log.info("#### ConsController.insertReservation vo : " + vo.toString());
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		String sql = "";
		int idxCrmStaff = -1;

		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		vo.setIdxCrmStaffCreate(Integer.parseInt(map.get("idx").toString()));



		//예약정보에 "상담구분" 과 "상담형태" 를 추가
		//상담구분 : 일반,도박,범피,EAP,검사,소견서 - crm_client.idx_crm_meta_product > crm_reservation.idx_crm_meta_product
		//상담형태 : 대면, 비대면(화상), 비대면(전화), 검사 - 신규추가 crm_reservation.idx_crm_meta_contact
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.insertReservation(vo), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/cons/{idx}")
	@ApiOperation(value="상담예약 조회", notes = "상담예약 조회하기")
	public ResponseEntity<Map<Object, Object>> selectReservation(@PathVariable("idx") int idx) throws Exception {
		log.info("#### ConsController.selectReservation idx : " + idx);

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.selectReservation(idx), HttpStatus.OK);
		return rtn;
	}

	@PutMapping("/api/v1/cons/{idx}")
	@ApiOperation(value="상담예약 수정", notes = "상담예약 수정하기"
			+ "{\r\n" +
			"  \"consDate\": \"2020-10-28\",\r\n" +
			"  \"consTime\": \"10:30/11:00/11:30/13:00/13:30\",\r\n" +
			"  \"idxCrmCenter\": 1,\r\n" +
			"  \"idxCrmClient\": 2,\r\n" +
			"  \"idxCrmStaff\": 1,\r\n" +
			"  \"memo\": \"달려라 하니\",\r\n" +
			"  \"name\": \"홍두깨\"\r\n" +
			"}")
	public ResponseEntity<Map<Object, Object>> updateReservation(@PathVariable("idx") int idx, @RequestBody ReservationVO vo, HttpServletRequest request) throws Exception {
		log.info("#### ConsController.updateReservation idx : " + idx + " vo : " + vo.toString());

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		vo.setIdxCrmStaffUpdate(Integer.parseInt(map.get("idx").toString()));
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.updateReservation(idx, vo), HttpStatus.OK);
		return rtn;
	}

	/**
	 * 상담상태 입실로 바꿔주기
	 * @param idx
	 * @param status
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/api/v1/cons/status/{idx}")
	@ApiOperation(value="상담예약 상태 입실로 수정", notes = "상담예약 상태 입실로 수정하기")
	public ResponseEntity<Map<Object, Object>> updateReservationStatus(@PathVariable("idx") int idx, @RequestParam(name = "status", required = true, defaultValue = "") String status, HttpServletRequest request) throws Exception {
		log.info("#### ConsController.updateReservationStatus idx : " + idx + " status : " + status);

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.updateReservationStatus(idx, status, Integer.parseInt(map.get("idx").toString())), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/cons/list")
	@ApiOperation(value="상담예약 검색 목록", notes = "상담예약 검색목록 조회")
	public ResponseEntity<Map<Object, Object>> selectReservationList (
			@RequestParam(name = "idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
			@RequestParam(name = "idxCrmMetaPro", required = false, defaultValue = "-1") int idxCrmMetaPro,
			@RequestParam(name = "value", required = false) String value,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "consStartDate", defaultValue = "") String consStartDate,
			@RequestParam(name = "consEndDate", defaultValue = "") String consEndDate,
			@RequestParam(name = "orderType", required = false, defaultValue = "idx") String orderType,
			@RequestParam(value = "pageSize", defaultValue = "50") int pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			HttpServletRequest request ) throws Exception {
		log.info("#### ConsController.selectReservationList type : " + type + " idxCrmCenter : " + idxCrmCenter + " idxCrmMetaPro : " + idxCrmMetaPro + " value : " + value + " consStartDate : " + consStartDate
				+ " consEndDate : " + consEndDate + " pageSize : " + pageSize + " + pageNum : " + pageNum);
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		String sql = "";
		int idxCrmStaff = -1;

		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
//		map = redisTemplate.opsForHash().entries("4ouwNCn2xIQ4Kkar3GUeAYfK4/nmzDlQLzjZvyBwVcn7Ep9GTyM+siFTyPFzfJC5ZxsPVg==");
		// 만약에 상담사가 상담예약현황을 볼 때
		if(map.get("authority").toString().indexOf("STAFF") > -1) {
			idxCrmStaff = Integer.parseInt(map.get("idx").toString());
		}

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.selectReservationList(idxCrmStaff, idxCrmCenter, idxCrmMetaPro, value, type, consStartDate, consEndDate, orderType, consService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
		return rtn;
	}

	/**
	 * 상담차트(고객차트)에서 상담예약현황 목록
	 * @param idxCrmPerson
	 * @param pageSize
	 * @param pageNum
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/cons/chart/list")
	@ApiOperation(value="상담차트에서 상담예약 목록", notes = "상담차트에서 상담예약 목록 조회")
	public ResponseEntity<Map<Object, Object>> selectChartReservationList (
			@RequestParam(name = "idxCrmPerson", required = false, defaultValue = "-1") int idxCrmPerson,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			HttpServletRequest request,
			@RequestParam(value = "idxCrmStaff", defaultValue = "-1") int idxCrmStaff ) throws Exception {
		log.info("#### ConsController.selectChartReservationList idxCrmPerson : " + idxCrmPerson + " pageSize : " + pageSize + " + pageNum : " + pageNum + " idxCrmStaff : " + idxCrmStaff);
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.selectChartReservationList(idxCrmPerson, consService.doPaging(pageNum, pageSize), pageSize, idxCrmStaff), HttpStatus.OK);
		return rtn;
	}

	/**
	 * 상담예약 삭제
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/api/v1/cons/{idx}")
	@ApiOperation(value="상담예약 삭제", notes = "상담예약 삭제하기")
	public ResponseEntity<Map<Object, Object>> deleteReservation(@PathVariable("idx") int idx) throws Exception {
		log.info("#### ConsController.deleteReservation idx : " + idx);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.deleteReservation(idx), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/cons/report/genre_meta")
	@ApiOperation(value="회기유형 목록 조회", notes = "회기유형 목록 조회하기")
	public ResponseEntity<Map<Object, Object>> selectGenreMeta() throws Exception {
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.selectReportMeta(0, 0), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/cons/report/type_meta")
	@ApiOperation(value="상담형태 목록 조회", notes = "상담형태 목록 조회하기")
	public ResponseEntity<Map<Object, Object>> selectTypeMeta() throws Exception {
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.selectReportMeta(1, 0), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/cons/report/agenda_meta")
	@ApiOperation(value="상담대주제 목록 조회", notes = "상담대주제 목록 조회하기")
	public ResponseEntity<Map<Object, Object>> selectAgendaMeta() throws Exception {
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.selectReportMeta(2, 0), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/cons/report/agenda_meta/{idx}")
	@ApiOperation(value="상담소주제 목록 조회", notes = "상담소주제 목록 조회하기")
	public ResponseEntity<Map<Object, Object>> selectAgendaMeta(@PathVariable("idx") int idx) throws Exception {
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.selectReportMeta(2, idx), HttpStatus.OK);
		return rtn;
	}

	/**
	 * 상담일지 등록
	 * @param vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/api/v1/cons/report")
	@ApiOperation(value="상담일지 등록", notes = "상담일지 등록하기")
	public ResponseEntity<Map<Object, Object>> insertReport(@ModelAttribute ReportVO vo, HttpServletRequest request) throws Exception {
		log.info("#### ConsController.insertReport vo : " + vo.toString());
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		idxCrmStaff = Integer.parseInt(map.get("idx").toString());

		vo.setIdxCrmStaff(idxCrmStaff);
		// 로그인한 상담사의 idx값 입력
		vo.setIdxCrmStaff(idxCrmStaff);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.insertReport(vo), HttpStatus.OK);
		return rtn;
	}

	/**
	 * 상담일지 수정
	 * @param idx
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/api/v1/cons/report/{idx}")
	@ApiOperation(value="상담일지 수정", notes = "상담일지 수정하기")
	public ResponseEntity<Map<Object, Object>> updateReport(@PathVariable("idx") int idx, @ModelAttribute ReportVO vo, HttpServletRequest request) throws Exception {
		log.info("#### ConsController.updateReport idx : " + idx + " vo : " + vo.toString());
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		idxCrmStaff = Integer.parseInt(map.get("idx").toString());

		// 로그인한 상담사의 idx값 입력
		vo.setIdxCrmStaff(idxCrmStaff);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.updateReport(idx, vo), HttpStatus.OK);
		return rtn;
	}

	@PutMapping("/api/v1/cons/report-request-modi/{idx}/{requestModify}")
	@ApiOperation(value="상담일지 수정요청", notes = "상담사에게 상담일지 수정요청하기")
	public ResponseEntity<Map<Object, Object>> updateReportRequestModify(
			@PathVariable("idx") int idx,
			@PathVariable("requestModify") String requestModify,
			HttpServletRequest request) throws Exception {
		log.info("#### ConsController.updateReport idx : " + idx + " | requestModify : " + requestModify);
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		idxCrmStaff = Integer.parseInt(map.get("idx").toString());

		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.updateReportRequestModify(idx, requestModify), HttpStatus.OK);
		return rtn;
	}

	/**
	 * 상담일지 조회
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/api/v1/cons/report/{idx}")
	@ApiOperation(value="상담일지 조회", notes = "상담일지 조회하기")
	public ResponseEntity<Map<Object, Object>> selectReport(@PathVariable("idx") int idx) throws Exception {
		log.info("#### ConsController.selectReport idx : " + idx);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.selectReport(idx), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/cons/report/list")
	@ApiOperation(value="상담일지 목록", notes = "상담일지 목록 조회하기")
	public ResponseEntity<Map<Object, Object>> selectReportList(
			@RequestParam(name = "idxCrmCompany", required = false, defaultValue =  "0") int idxCrmCompany,
			@RequestParam(name = "idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
			@RequestParam(name = "idxCrmClient", required = false, defaultValue = "-1") int idxCrmClient,
			@RequestParam(name = "idxCrmMetaDanger", required = false, defaultValue = "-1") int idxCrmMetaDanger,
			@RequestParam(name = "value", required = false, defaultValue = "") String value,
			@RequestParam(name = "permission", required = false, defaultValue = "") String permission,
			@RequestParam(name = "reportYn", required = false, defaultValue = "") String reportYn,
			@RequestParam(name = "idxCrmMetaProduct", required = false, defaultValue = "-1") int idxCrmMetaProduct,
			@RequestParam(name = "startDate", defaultValue = "") String startDate,
			@RequestParam(name = "endDate", defaultValue = "") String endDate,
			@RequestParam(value = "pageSize", defaultValue = "50") int pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			HttpServletRequest request) throws Exception {

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		log.info("#### map : " + map.toString());
		if("STAFF".equals(map.get("authority"))) {
			idxCrmStaff = Integer.parseInt(map.get("idx").toString());
		}

		log.info("#### ConsController.selectReportList idxCrmCenter : " + idxCrmCenter + " idxCrmMetaDanger " + idxCrmMetaDanger + " value : " + value + " reportYn : " + reportYn + " startDate : " + startDate + " endDate : " + endDate + " pageSize : " + pageSize + " pageNum : " + pageNum + " permission : " + permission);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.selectReportList(idxCrmCompany, idxCrmStaff, idxCrmCenter, idxCrmClient, idxCrmMetaDanger, value, permission, reportYn, idxCrmMetaProduct, startDate, endDate, consService.doPaging(pageNum, pageSize), pageSize), HttpStatus.OK);
		return rtn;
	}

	@PutMapping("/api/v1/cons/cal/{idx}")
	@ApiOperation(value="상담일정에서 상담예약 건 수정", notes = "상담일정에서 상담예약 건 수정하기")
	public ResponseEntity<Map<Object, Object>> updateCalReservation(@PathVariable("idx") int idx, @RequestBody ReservationVO vo, HttpServletRequest request) throws Exception {
		log.info("#### ConsController.updateCalReservation idx : " + idx + " vo : " + vo.toString());
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		idxCrmStaff = Integer.parseInt(map.get("idx").toString());

		vo.setIdxCrmStaffUpdate(idxCrmStaff);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.updateCalReservation(idx, vo), HttpStatus.OK);
		return rtn;
	}

	@GetMapping("/api/v1/cons/report/company_static")
	@ApiOperation(value="회사별 통계 조회", notes = "회사별 통계 조회하기")
	public ResponseEntity<Map<Object, Object>> selectGenreMeta(
			@RequestParam(name = "idxCrmCompany", required = false, defaultValue = "-1") int idxCrmCompany,
			@RequestParam(name = "idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
			@RequestParam(name = "startDate", defaultValue = "") String startDate,
			@RequestParam(name = "endDate", defaultValue = "") String endDate
	) throws Exception {
		if("".equals(startDate) || "".equals(endDate)){
			Date nowDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
			startDate = simpleDateFormat.format(nowDate);
			endDate = simpleDateFormat.format(nowDate);
		}
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(consService.selectCompanyStaticPerson(idxCrmCompany, idxCrmCenter, startDate, endDate), HttpStatus.OK);
		return rtn;
	}
	@GetMapping("/api/v1/cons/report/company_static_excel")
	@ApiOperation(value="엑셀 다운로드 기능", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownCompanyStaticList(@RequestParam(name = "idxCrmCompany", required = false, defaultValue = "-1") int idxCrmCompany,
										   @RequestParam(name = "idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
										   @RequestParam(name = "startDate", defaultValue = "") String startDate,
										   @RequestParam(name = "endDate", defaultValue = "") String endDate,
										   HttpServletResponse response) throws Exception {

		log.info("#### ExcelDown");
		if("".equals(startDate) || "".equals(endDate)){
			Date nowDate = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		startDate = simpleDateFormat.format(nowDate);
			endDate = simpleDateFormat.format(nowDate);
		}

		List<excelCompanyStaticVO> testList = consService.selectExcelCompanyStatic(idxCrmCompany, idxCrmCenter, startDate, endDate);


		List<Map<String, String>> excuteList = new ArrayList<>();

		for(excelCompanyStaticVO listValue : testList){
			Map<String, String> convertMap = BeanUtils.describe(listValue);

			excuteList.add(convertMap);
		}

		List<Map<String, String>> heading = new ArrayList<>();
		heading = setHeading(heading, "companyName", "회사 명" );
		heading = setHeading(heading, "centerName", "센터 명" );
		heading = setHeading(heading, "personName", "고객 명" );
		heading = setHeading(heading, "gender", "성별" );
		heading = setHeading(heading, "ageRange", "연령대" );
		heading = setHeading(heading, "consDate", "예약일" );
		heading = setHeading(heading, "permissionDate", "상담일지등록일" );
		heading = setHeading(heading, "danger_stepTitle", "위험단계" );
		heading = setHeading(heading, "titleGenre", "상담형태" );
		heading = setHeading(heading, "titleType", "회기 유형" );
		heading = setHeading(heading, "titleAgendaFirst", "상담주제 (대분류)" );
		heading = setHeading(heading, "titleAgendaSecond", "상담주제 (소분류)" );

		com.carrot.commons.excel.ExcelTemplate.init().body(heading, excuteList).print(response, "companyStatic.xlsx");
	}

	private List<Map<String, String>> setHeading(List<Map<String, String>> headingList, String key, String val) {
		Map<String, String> col = new HashMap<>();
		col.put(key, val);
		headingList.add(col);
		return headingList;
	}

	
//		log.info("####bodyList" + bodyList);
////		bodyList = consService.selectExcelCompanyStatic(idxCrmCompany, startDate, endDate);
////		String conditions = "";
////		Map<Object, Object> data = new HashMap<Object, Object>();
////		data.put("pageIdx", pageIdx);
////		data.put("name", name);
////		data.put("email", email);
////		data.put("status", status);
////		data.put("payStatus", payStatus);
////		conditions = objectMapper.writeValueAsString(data);
////		Map<Object, Object> pageMap  = sugangService.selectPage(pageIdx);

}

