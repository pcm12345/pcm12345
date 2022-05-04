package com.carrot.excel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.carrot.center.dao.CenterVO;
import com.carrot.center.service.CenterService;
import com.carrot.company.dao.CompanyVO;
import com.carrot.company.service.CompanyService;
import com.carrot.cons.dao.ReportVO;
import com.carrot.cons.dao.ReservationVO;
import com.carrot.cons.service.ConsService;
import com.carrot.pay.dao.PayVO;
import com.carrot.pay.service.PayService;
import com.carrot.recept.dao.ReceptionVO;
import com.carrot.recept.service.ReceptionService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrot.excel.service.ExcelService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Api(description = "엑셀다운로드, 업로드 기능" , tags="엑셀다운로드/업로드")
@Slf4j
public class ExcelController {

	@Autowired
	private ExcelService excelService;

	@Autowired
    private RedisTemplate redisTemplate;

	@Autowired
	private ReceptionService receptionService;

	@Autowired
	private ConsService consService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CenterService centerService;

	@Autowired
	private PayService payService;




	@GetMapping("/api/v1/excel/center-status-list")
	@ApiOperation(value="엑셀 다운로드 기능[완]", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownCenterStatus(@RequestParam("idxCrmCenter") int idxCrmCenter, @RequestParam(value="startDate", required = false) String startDate, @RequestParam(name="endDate", required = false) String endDate
			, HttpServletResponse response, HttpServletRequest request ) throws Exception {
		log.info("##### 엑셀다운로드");
		
		log.info("#### ExcelController.excelDownSugangUserList idxCrmCenter : " + idxCrmCenter + " startDate : " + startDate + " endDate : " + endDate);
		
		excelService.excelDownCenterStatus(idxCrmCenter, startDate, endDate, response);

	}
	
	@GetMapping("/api/v1/excel/status-main")
	@ApiOperation(value="엑셀 다운로드 기능[완]", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownStatusMainStaff(@RequestParam(value = "type", required = false, defaultValue = "") String type
			, @RequestParam(value = "name", required = false, defaultValue = "") String name
			, @RequestParam(name = "idxCrmCompany" , required = false, defaultValue = "-1") int idxCrmCompany
			, @RequestParam(name="idxCrmCenter" , required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name="clientType" , required = false, defaultValue = "-1") String clientType
			, @RequestParam(name="startDate" , required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate" , required = false, defaultValue = "") String endDate
			, HttpServletResponse response, HttpServletRequest request ) throws Exception {
		log.info("##### 엑셀다운로드");
		
		log.info("#### ExcelController.excelDownStatusMainStaff type : " + type + " name : " + name + " idxCrmCompany : " + idxCrmCompany + " idxCrmCenter : " + idxCrmCenter + " startDate : " + clientType + "clientType" + startDate + " endDate : " + endDate);
		
		excelService.excelDownStatusMainStaff(type, name, idxCrmCompany, idxCrmCenter, clientType, startDate, endDate, response);

	}
	
	@GetMapping("/api/v1/excel/status-main2")
	@ApiOperation(value="엑셀 다운로드 기능[완]", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownStatusMainStaff2(@RequestParam(value = "type", required = false, defaultValue = "") String type
			, @RequestParam(value = "name", required = false, defaultValue = "") String name
			, @RequestParam(name = "idxCrmCompany" , required = false, defaultValue = "-1") int idxCrmCompany
			, @RequestParam(name="idxCrmCenter" , required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name = "clientType" , required = false, defaultValue = "-1") String clientType
			, @RequestParam(name="startDate" , required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate" , required = false, defaultValue = "") String endDate
			, HttpServletResponse response, HttpServletRequest request ) throws Exception {
		log.info("##### 엑셀다운로드");
		
		log.info("#### ExcelController.excelDownStatusMainStaff2 type : " + type + " name : " + name + " idxCrmCompany : " + idxCrmCompany + " idxCrmCenter : " + idxCrmCenter + "clientType" + clientType + " startDate : " + startDate + " endDate : " + endDate);
		
		excelService.excelDownStatusMainStaff2(type, name, idxCrmCompany, idxCrmCenter, clientType, startDate, endDate, response);

	}
	
	@GetMapping("/api/v1/excel/person-list")
	@ApiOperation(value="엑셀 다운로드 기능[완]", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownPersonList(
			@RequestParam(name="clientStatus", defaultValue="", required = false) String clientStatus,
			@RequestParam(name="idxCrmCenter", defaultValue="-1", required = false) int idxCrmCenter,
			@RequestParam(value="startDate", defaultValue="", required = false) String startDate,
			@RequestParam(name="endDate", defaultValue="", required = false) String endDate,
			@RequestParam(name="value", defaultValue="", required = false) String value,
			HttpServletResponse response, HttpServletRequest request ) throws Exception {
		log.info("##### 엑셀다운로드");
		
		log.info("#### ExcelController.excelDownPersonList startDate : " + startDate + " endDate : " + endDate);
		
		excelService.excelDownPersonList(clientStatus, idxCrmCenter, startDate, endDate, value, response);

	}

	@GetMapping("/api/v1/excel/reception-list")
	@ApiOperation(value="전화접수 목록 다운로드", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownReceptionList(
			@RequestParam(name = "receptGender", required = false, defaultValue = "") String receptGender,
			@RequestParam(name = "receptStatus", required = false, defaultValue = "") String receptStatus,
			@RequestParam(name = "searchText", required = false, defaultValue = "") String searchText,
			@RequestParam(name = "receptStartDate", defaultValue = "") String receptStartDate,
			@RequestParam(name = "receptEndDate", defaultValue = "") String receptEndDate,
			HttpServletResponse response, HttpServletRequest request ) throws Exception {

		log.info("#### ExcelController.excelDownExceptionList : " );

		List<ReceptionVO> testList = receptionService.selectReceptionListAll(receptGender, receptStatus, searchText, receptStartDate, receptEndDate);

		//Vo List 를 Map List 로 변환한다.
		List<Map<String, String>> excuteList = new ArrayList<Map<String, String>>();
		for(ReceptionVO value : testList) {
			Map<String, String> convertMap = BeanUtils.describe(value);
			excuteList.add(convertMap);
		}

		List<Map<String, String>> heading = new ArrayList<Map<String, String>>();
		heading = setHeading(heading, "rownum", "번호");
		heading = setHeading(heading, "receptName", "신청자이름");
		heading = setHeading(heading, "receptGender", "신청자성별");
		heading = setHeading(heading, "receptPhone", "신청자전화번호");
		heading = setHeading(heading, "receptEmail", "신청자이메일");
		heading = setHeading(heading, "receptStatus", "처리상태");
		heading = setHeading(heading, "createStaff", "작성자");
		heading = setHeading(heading, "updateStaff", "수정자");

		com.carrot.commons.excel.ExcelTemplate.init().body(heading, excuteList).print(response, "reception-list.xlsx");
	}

	@GetMapping("/api/v1/excel/reservation-list")
	@ApiOperation(value="상담예약 관리 다운로드", notes = "검색된 예약목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownReservationList(
			@RequestParam(name = "idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
			@RequestParam(name = "idxCrmMetaPro", required = false, defaultValue = "-1") int idxCrmMetaPro,
			@RequestParam(name = "value", required = false) String value,
			@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "consStartDate", defaultValue = "") String consStartDate,
			@RequestParam(name = "consEndDate", defaultValue = "") String consEndDate,
			@RequestParam(name = "orderType", required = false, defaultValue = "idx") String orderType,
			@RequestParam(value = "pageSize", defaultValue = "50") int pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			HttpServletResponse response, HttpServletRequest request ) throws Exception{

		log.info("#### ExcelController.excelDownReservationList : ");

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		int center = -1;
		String sql = "";
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));


		log.info("#### map : " + map.toString());
		if(map.get("authority").toString().indexOf("STAFF") > -1) {
			idxCrmStaff = Integer.parseInt(map.get("idx").toString());
		}

		//전체를 다운받을 수 있도록 한다.
		Map<Object, Object> resultMap = consService.selectReservationList(idxCrmStaff, idxCrmCenter, idxCrmMetaPro, value, type, consStartDate, consEndDate, orderType, -1, pageSize);
		List<ReservationVO> testList = (List<ReservationVO>)resultMap.get("resList");

		List<Map<String, String>> excuteList = new ArrayList<Map<String, String>>();
		for(ReservationVO listValue : testList) {
			Map<String, String> convertMap = BeanUtils.describe(listValue);
			excuteList.add(convertMap);
		}

		List<Map<String, String>> heading = new ArrayList<Map<String, String>>();
		heading = setHeading(heading, "rownum", "번호");
		heading = setHeading(heading, "centerName", "상담센터");
		heading = setHeading(heading, "staffName", "상담사");
		heading = setHeading(heading, "name", "고객");
		heading = setHeading(heading, "phone", "연락처");
		heading = setHeading(heading, "gubunName","상품구분");
		heading = setHeading(heading, "consCountUse", "누적시간");
		heading = setHeading(heading, "typeName", "상품유형");
		heading = setHeading(heading, "reservationStatus", "예약현황");
		heading = setHeading(heading, "contactType", "상담형태");
		heading = setHeading(heading, "consDate", "상담날짜");
		heading = setHeading(heading, "createStaff", "등록자");
		heading = setHeading(heading, "createDate", "등록일");

		com.carrot.commons.excel.ExcelTemplate.init().body(heading, excuteList).print(response, "reservation-list.xlsx");
	}

	@GetMapping("/api/v1/excel/report-list")
	@ApiOperation(value="상담일지 목록 다운로드", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownReportList(
			@RequestParam(name = "idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
			@RequestParam(name = "idxCrmClient", required = false, defaultValue = "-1") int idxCrmClient,
			@RequestParam(name = "value", required = false, defaultValue = "") String value,
			@RequestParam(name = "permission", required = false, defaultValue = "") String permission,
			@RequestParam(name = "reportYn", required = false, defaultValue = "") String reportYn,
			@RequestParam(name = "idxCrmMetaProduct", required = false, defaultValue = "-1") int idxCrmMetaProduct,
			@RequestParam(name = "startDate", defaultValue = "") String startDate,
			@RequestParam(name = "endDate", defaultValue = "") String endDate,
			HttpServletResponse response, HttpServletRequest request ) throws Exception {

		log.info("#### ExcelController.excelDownReportList : ");

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		int center = -1;
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		log.info("#### map : " + map.toString());
		if("STAFF".equals(map.get("authority"))) {
			idxCrmStaff = Integer.parseInt(map.get("idx").toString());
		}
		center = idxCrmCenter;

		List<ReportVO> testList = consService.selectReportListAll(idxCrmStaff, center, idxCrmClient, value, permission, reportYn, idxCrmMetaProduct, startDate, endDate);

		//Vo List 를 Map List 로 변환한다.
		List<Map<String, String>> excuteList = new ArrayList<Map<String, String>>();
		for(ReportVO listValue : testList) {
			Map<String, String> convertMap = BeanUtils.describe(listValue);
			excuteList.add(convertMap);
		}

		List<Map<String, String>> heading = new ArrayList<Map<String, String>>();
		heading = setHeading(heading, "rownum", "번호");
		heading = setHeading(heading, "consDate2", "일지등록날짜");
		heading = setHeading(heading, "centerName", "상담센터");
		heading = setHeading(heading, "staffName", "상담사");
		heading = setHeading(heading, "clientName", "고객");
		heading = setHeading(heading, "companyName", "유형/기업명");
		heading = setHeading(heading, "dangerStepTitle", "위험단계");
		heading = setHeading(heading, "titleType", "상담구분");
		heading = setHeading(heading, "titleAgendaFirst", "상담대주제");
		heading = setHeading(heading, "titleAgendaSecond", "상담소주제");
		heading = setHeading(heading, "reservationStatus", "상담현황");

		com.carrot.commons.excel.ExcelTemplate.init().body(heading, excuteList).print(response, "report-list.xlsx");
	}

	@GetMapping("/api/v1/excel/salary-list")
	@ApiOperation(value="상담사 급여내역 목록 다운로드", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownSalaryList(
			@RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name="centerName", required = false, defaultValue = "") String centerName
			, @RequestParam(name="name", required = false, defaultValue = "") String name
			, @RequestParam(name="startDate", required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate", required = false, defaultValue = "") String endDate
			, @RequestParam(name="pageNum", required = false, defaultValue = "1") int pageNum
			, @RequestParam(name="pageSize", required = false, defaultValue = "50") int pageSize
			, HttpServletResponse response, HttpServletRequest request ) throws Exception {

		log.info("#### ExcelController.excelDownSalaryList : ");

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		int center = -1;
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		Map<Object, Object> resultMap = payService.selectPayStaffList(idxCrmCenter, centerName, name, startDate, endDate, payService.doPaging(pageNum, pageSize), pageSize);
		List<PayVO> testList = (List<PayVO>)resultMap.get("payList");


		//Vo List 를 Map List 로 변환한다.
		List<Map<String, String>> excuteList = new ArrayList<Map<String, String>>();
		for(PayVO listValue : testList) {
			Map<String, String> convertMap = BeanUtils.describe(listValue);

			convertMap.put("price",String.format("%,d",listValue.getPrice()+listValue.getEtcPrice()));
			convertMap.put("staffPay",String.format("%,d",listValue.getStaffPay()));
			convertMap.put("incomeTax",String.format("%,d",listValue.getIncomeTax()));
			convertMap.put("localIncomeTax",String.format("%,d",listValue.getLocalIncomeTax()));
			convertMap.put("realStaffPay",String.format("%,d",listValue.getRealStaffPay()));

			excuteList.add(convertMap);
		}

		List<Map<String, String>> heading = new ArrayList<Map<String, String>>();

		heading = setHeading(heading, "centerName", "상담센터");
		heading = setHeading(heading, "staffName", "상담사명");
		heading = setHeading(heading, "reportCnt", "회기");
		heading = setHeading(heading, "price", "매출액\n(미수금포함)");
		heading = setHeading(heading, "staffPay", "총 급여");
		heading = setHeading(heading, "incomeTax", "소득세");
		heading = setHeading(heading, "localIncomeTax", "지방소득세");
		heading = setHeading(heading, "realStaffPay", "실지급액");


		com.carrot.commons.excel.ExcelTemplate.init().body(heading, excuteList).print(response, "pay-list.xlsx");
	}

	@GetMapping("/api/v1/excel/company-list")
	@ApiOperation(value="기업목록 다운로드", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownCompanyList(
			@RequestParam(name="value", required = false, defaultValue = "") String value,
			@RequestParam(value="pageSize", defaultValue="50") int pageSize,
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
		log.info("#### ExcelController.excelDownCompanyList : ");

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		int center = -1;
		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));
		Map<Object, Object> resultMap = companyService.selectCompanyList(value, companyService.doPaging(pageNum, pageSize),pageSize, OK);
		List<CompanyVO> testList = (List<CompanyVO>)resultMap.get("companyList");

		//Vo List 를 Map List 로 변환한다.
		List<Map<String, String>> excuteList = new ArrayList<Map<String, String>>();

		for(CompanyVO listValue : testList) {
			Map<String, String> convertMap = BeanUtils.describe(listValue);
			convertMap.put("totalDate",String.format("%tF",listValue.getConsStartDate()) + " ~ "
					+ String.format("%tF",listValue.getConsEndDate()));
			convertMap.put("totalPrice",String.format("%,d",listValue.getTotalPrice()));
			excuteList.add(convertMap);
		}
		List<Map<String, String>> heading = new ArrayList<Map<String, String>>();

		heading = setHeading(heading, "name", "기업명");
		heading = setHeading(heading, "manager", "담당자");
		heading = setHeading(heading, "phone", "연락처");
		heading = setHeading(heading, "totalDate", "계약일");
		heading = setHeading(heading, "totalPrice", "계약총액");
		heading = setHeading(heading, "consCount", "지원회기");
		heading = setHeading(heading, "type", "유형");

		com.carrot.commons.excel.ExcelTemplate.init().body(heading, excuteList).print(response, "company-list.xlsx");
	}

	@GetMapping("/api/v1/excel/centerManagement-list")
	@ApiOperation(value="센터관리 목록 엑셀 다운로드", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownCenterList(@RequestParam(name="name", required = false, defaultValue = "") String name,
											  @RequestParam(name="permission", required = false, defaultValue = "") String permission,
											  @RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter,
											  @RequestParam(value="startDate", defaultValue="") String startDate,
											  @RequestParam(value="endDate", defaultValue="") String endDate,
											  @RequestParam(value="pageSize", defaultValue="50") int pageSize,
											  @RequestParam(value="pageNum", defaultValue="1") int pageNum,
											  HttpServletResponse response, HttpServletRequest request) throws Exception {

		log.info("#### ExcelController.excelDownCenterList : ");

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		int center = -1;

		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		Map<Object, Object> resultMap = centerService.selectCenterList(name, permission, startDate, endDate, centerService.doPaging(pageNum, pageSize), pageSize);
		List<CenterVO> testList = (List<CenterVO>)resultMap.get("centerList");

		//Vo List 를 Map List 로 변환한다.
		List<Map<String, String>> excuteList = new ArrayList<Map<String, String>>();

		for(CenterVO listValue : testList) {
			Map<String, String> convertMap = BeanUtils.describe(listValue);

			convertMap.put("id",listValue.getId());
			convertMap.put("permission", listValue.getPermission().replace("Y","승인").replace("N","미승인"));

			excuteList.add(convertMap);
		}

		List<Map<String, String>> heading = new ArrayList<Map<String, String>>();

//		heading = setHeading(heading, "rownum", "번호");
		heading = setHeading(heading, "name", "센터명");
		heading = setHeading(heading, "id", "협약센터ID");
		heading = setHeading(heading, "address", "주소");
		heading = setHeading(heading, "mainNumber", "대표번호");
		heading = setHeading(heading, "phone", "휴대폰번호");
		heading = setHeading(heading, "consService", "상담서비스");
		heading = setHeading(heading, "permission", "승인유무");



		com.carrot.commons.excel.ExcelTemplate.init().body(heading, excuteList).print(response, "centerManagement-list.xlsx");
	}
	@GetMapping("/api/v1/excel/payCenterManagement-list")
	@ApiOperation(value="협약센터 급여내역 엑셀 다운로드", notes="검색된 목록 리스트를 엑셀로 다운로드 한다.")
	public void excelDownPayCenterList(@RequestParam(name="idxCrmCenter", required = false, defaultValue = "-1") int idxCrmCenter
			, @RequestParam(name="startDate", required = false, defaultValue = "") String startDate
			, @RequestParam(name="endDate", required = false, defaultValue = "") String endDate
			, @RequestParam(name="pageNum", required = false, defaultValue = "1") int pageNum
			, @RequestParam(name="pageSize", required = false, defaultValue = "50") int pageSize
			, HttpServletResponse response, HttpServletRequest request ) throws Exception {

		log.info("#### ExcelController.excelDownPayList : ");

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		int idxCrmStaff = -1;
		int center = -1;

		map = redisTemplate.opsForHash().entries(request.getHeader("accessToken"));

		Map<Object, Object> resultMap = payService.selectPayCenterList(center, startDate, endDate, payService.doPaging(pageNum, pageSize), pageSize);
		List<PayVO> testList = (List<PayVO>)resultMap.get("payList");

		//Vo List 를 Map List 로 변환한다.
		List<Map<String, String>> excuteList = new ArrayList<Map<String, String>>();

		for(PayVO listValue : testList) {
			Map<String, String> convertMap = BeanUtils.describe(listValue);

			convertMap.put("consPrice",String.format("%,d",listValue.getConsPrice()));
			convertMap.put("sumConsPrice",String.format("%,d",listValue.getSumConsPrice()));
			convertMap.put("realSumConsPrice",String.format("%,d",listValue.getRealSumConsPrice()));

			excuteList.add(convertMap);
		}

		List<Map<String, String>> heading = new ArrayList<Map<String, String>>();

//		heading = setHeading(heading, "rownum", "번호");
		heading = setHeading(heading, "consDate", "상담일자");
		heading = setHeading(heading, "staffName", "상담사명");
		heading = setHeading(heading, "clientName", "고객명");
		heading = setHeading(heading, "companyName", "기업명");
		heading = setHeading(heading, "consPrice", "상담료");
		heading = setHeading(heading, "priceType", "정산유형");
		heading = setHeading(heading, "sumConsPrice", "총계");
		heading = setHeading(heading, "realSumConsPrice", "실입금액");

		com.carrot.commons.excel.ExcelTemplate.init().body(heading, excuteList).print(response, "payCenterManagement-list.xlsx");
	}

	private List<Map<String, String>> setHeading(List<Map<String, String>> headingList, String key, String val){
		Map<String, String> col = new HashMap<>();
		col.put(key, val);
		headingList.add(col);
		return headingList;
	}
	
}
