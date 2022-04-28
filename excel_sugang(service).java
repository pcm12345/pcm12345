package com.carrot.excel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.carrot.admin.service.AdminService;
import com.carrot.common.CommonEnum;
import com.carrot.common.mapper.sugang.SugangMapper;
import com.carrot.excel.dao.ExcelLogVO;
import com.carrot.excel.dao.ExcelVO;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Service
@Slf4j
public class ExcelService {

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private AdminService adminService;

  @Autowired
  private SugangMapper sugangMapper;

  public void excelDownSugangUserList(int pageIdx, String name, String email,
      HttpServletResponse response, String status, String payStatus, Map<Object, Object> pageMap)
      throws Exception {

    String templateFileName = "sugangUserList_excel_template.xlsx";
    //InputStream templateFile = resourceLoader.getResource("classpath:template/excel/" + templateFileName).getInputStream();

    Map pageInfo = (Map) pageMap.get("pageInfo");
    pageInfo.forEach((o, o2) -> {
      System.out.println(o.toString() + "," + o2.toString());
    });

    // 엑셀템플릿파일 지정(지정안하고 빈 통합문서로도 가능)
    XSSFWorkbook xssfWorkBook = new XSSFWorkbook();

    // 엑셀템플릿파일에 쓰여질 부분 검색
    Sheet originSheet = xssfWorkBook.createSheet();

    Row headerRow = originSheet.createRow(0);

    // 검색 목록 가져오기
    Map<Object, Object> result = new LinkedHashMap<Object, Object>();
    result = adminService.selectSugangApplyUserList(pageIdx, name, email, 1, -1, status, payStatus);
    List<Map<Object, Object>> applyList = (List<Map<Object, Object>>) result.get("applyList");

    Map<String, Object> param = new HashMap<String, Object>();

log.info("#### 데이터 존재여부 체크");
    boolean program_nameNum = applyList.stream().filter(value -> value.get("program_name") != null && !value.get("program_name").equals("")).collect(Collectors.toList()).size() > 0;
    boolean subject_nameNum = applyList.stream().filter(value -> value.get("subject_name") != null && !value.get("subject_name").equals("")).collect(Collectors.toList()).size() > 0;
    boolean class_nameNum = applyList.stream().filter(value -> value.get("class_name") != null && !value.get("class_name").equals("")).collect(Collectors.toList()).size() > 0;
    boolean languageNum = applyList.stream().filter(value -> value.get("language") != null && !value.get("language").equals("")).collect(Collectors.toList()).size() > 0;
    boolean levelNum = applyList.stream().filter(value -> value.get("level") != null && !value.get("level").equals("")).collect(Collectors.toList()).size() > 0;
    boolean dayNum = applyList.stream().filter(value -> value.get("day") != null && !value.get("day").equals("")).collect(Collectors.toList()).size() > 0;
    boolean subject_start_timeNum = applyList.stream().filter(value -> value.get("subject_start_time") != null && !value.get("subject_start_time").equals("")).collect(Collectors.toList()).size() > 0;
    boolean placeNum = applyList.stream().filter(value -> value.get("place") != null && !value.get("place").equals("")).collect(Collectors.toList()).size() > 0;
    boolean teacherNum = applyList.stream().filter(value -> value.get("teacher") != null && !value.get("teacher").equals("")).collect(Collectors.toList()).size() > 0;
    boolean edu_priceNum = applyList.stream().filter(value -> value.get("edu_price") != null && !value.get("edu_price").equals("")).collect(Collectors.toList()).size() > 0;
    boolean book_priceNum = applyList.stream().filter(value -> value.get("book_price") != null && !value.get("book_price").equals("")).collect(Collectors.toList()).size() > 0;
    boolean etcNum = applyList.stream().filter(value -> value.get("etc") != null && !value.get("etc").equals("")).collect(Collectors.toList()).size() > 0;
    boolean nameNum = applyList.stream().filter(value -> value.get("name") != null && !value.get("name").equals("")).collect(Collectors.toList()).size() > 0;
    boolean emailNum = applyList.stream().filter(value -> value.get("email") != null && !value.get("email").equals("")).collect(Collectors.toList()).size() > 0;
    boolean numberNum = applyList.stream().filter(value -> value.get("number") != null && !value.get("number").equals("")).collect(Collectors.toList()).size() > 0;
//    boolean positionNum = applyList.stream().filter(value -> value.get("position") != null && !value.get("position").equals("")).collect(Collectors.toList()).size() > 0;
    param.put("pageIdx",pageIdx);
    param.put("itemName","직급");
    boolean positionNum = sugangMapper.statusYN(param) == "Y";
    param.clear();
    boolean mobileNum = applyList.stream().filter(value -> value.get("mobile") != null && !value.get("mobile").equals("")).collect(Collectors.toList()).size() > 0;
    boolean companyNum = applyList.stream().filter(value -> value.get("company") != null && !value.get("company").equals("")).collect(Collectors.toList()).size() > 0;
//    boolean deptNum = applyList.stream().filter(value -> value.get("dept") != null && !value.get("dept").equals("")).collect(Collectors.toList()).size() > 0;
    param.put("pageIdx",pageIdx);
    param.put("itemName","부서");
    boolean deptNum = sugangMapper.statusYN(param) == "Y";
    param.clear();
    boolean pointNum = applyList.stream().filter(value -> value.get("point") != null && !value.get("point").equals("")).collect(Collectors.toList()).size() > 0;
    boolean option_name1Num = applyList.stream().filter(value -> value.get("option_name1") != null && !value.get("option_name1").equals("")).collect(Collectors.toList()).size() > 0;
    boolean option_contents1Num = option_name1Num;
    boolean option_name2Num = applyList.stream().filter(value -> value.get("option_name2") != null && !value.get("option_name2").equals("")).collect(Collectors.toList()).size() > 0;
    boolean option_contents2Num = option_name2Num;
    boolean option_name3Num = applyList.stream().filter(value -> value.get("option_name3") != null && !value.get("option_name3").equals("")).collect(Collectors.toList()).size() > 0;
    boolean option_contents3Num = option_name3Num;
    boolean option_name4Num = applyList.stream().filter(value -> value.get("option_name4") != null && !value.get("option_name4").equals("")).collect(Collectors.toList()).size() > 0;
    boolean option_contents4Num = option_name4Num;
    boolean option_name5Num = applyList.stream().filter(value -> value.get("option_name5") != null && !value.get("option_name5").equals("")).collect(Collectors.toList()).size() > 0;
    boolean option_contents5Num = option_name5Num;
    boolean statusNum = applyList.stream().filter(value -> value.get("status") != null && !value.get("status").equals("")).collect(Collectors.toList()).size() > 0;
    boolean priceNum = applyList.stream().filter(value -> value.get("price") != null && !value.get("price").equals("")).collect(Collectors.toList()).size() > 0;
    boolean pay_wayNum = applyList.stream().filter(value -> value.get("pay_way") != null && !value.get("pay_way").equals("")).collect(Collectors.toList()).size() > 0;
    boolean pay_statusNum = applyList.stream().filter(value -> value.get("pay_status") != null && !value.get("pay_status").equals("")).collect(Collectors.toList()).size() > 0;
    boolean create_dateNum = applyList.stream().filter(value -> value.get("create_date") != null && !value.get("create_date").equals("")).collect(Collectors.toList()).size() > 0;

    int lastCellNum = headerRow.getLastCellNum();
    List<String> defaultHeaders = new ArrayList<>();

log.info("#### 헤더값 넣기");
    if (program_nameNum) {
      defaultHeaders.add("프로그램명");
    }
    if (subject_nameNum) {
      defaultHeaders.add("과정명");
    }
    if (class_nameNum) {
      defaultHeaders.add("과목");
    }
    if (languageNum) {
      defaultHeaders.add("언어");
    }
    if (levelNum) {
      defaultHeaders.add("레벨");
    }
    if (dayNum) {
      defaultHeaders.add("요일");
    }
    if (subject_start_timeNum) {
      defaultHeaders.add("교육시간");
    }
    if (placeNum) {
      defaultHeaders.add("장소");
    }
    if (teacherNum) {
      defaultHeaders.add("강사");
    }
    if (edu_priceNum) {
      defaultHeaders.add("교육비");
    }
    if (book_priceNum) {
      defaultHeaders.add("교재비");
    }
    if (etcNum) {
      defaultHeaders.add("상세 및 기타");
    }
    if (nameNum) {
      defaultHeaders.add("이름");
    }
    if (emailNum) {
      defaultHeaders.add("이메일");
    }
    if (numberNum) {
      defaultHeaders.add("사번");
    }
    if (positionNum) {
      defaultHeaders.add("직급");
    }
    if (mobileNum) {
      defaultHeaders.add("핸드폰번호");
    }
    if (companyNum) {
      defaultHeaders.add("회사");
    }
    if (deptNum) {
      defaultHeaders.add("부서");
    }
    if (pointNum) {
      defaultHeaders.add("공인시험점수");
    }
    if (option_name1Num) {
      defaultHeaders.add("옵션1");
    }
    if (option_contents1Num) {
      defaultHeaders.add("내용1");
    }
    if (option_name2Num) {
      defaultHeaders.add("옵션2");
    }
    if (option_contents2Num) {
      defaultHeaders.add("내용2");
    }
    if (option_name3Num) {
      defaultHeaders.add("옵션3");
    }
    if (option_contents3Num) {
      defaultHeaders.add("내용3");
    }
    if (option_name4Num) {
      defaultHeaders.add("옵션4");
    }
    if (option_contents4Num) {
      defaultHeaders.add("내용4");
    }
    if (option_name5Num) {
      defaultHeaders.add("옵션5");
    }
    if (option_contents5Num) {
      defaultHeaders.add("내용5");
    }
    if (statusNum) {
      defaultHeaders.add("상태");
    }
    if (priceNum) {
      defaultHeaders.add("결제금액");
    }
    if (pay_wayNum) {
      defaultHeaders.add("결제방법");
    }
    if (pay_statusNum) {
      defaultHeaders.add("결제여부");
    }
    if (create_dateNum) {
      defaultHeaders.add("신청일");
    }

    for (String header : defaultHeaders) {
      Cell cell = headerRow.createCell(++lastCellNum);
      cell.setCellValue(header);
    }

    int rowNo = originSheet.getLastRowNum();

    // SXSSF 생성
log.info("#### SXSSF 생성");
    SXSSFWorkbook sxssfWorkBook = new SXSSFWorkbook(xssfWorkBook, 100);
    Sheet sheet = sxssfWorkBook.getSheetAt(0);

    Font headFont = sxssfWorkBook.createFont(); // 헤더용 폰트
    headFont.setFontHeightInPoints((short) 11);
    headFont.setFontName("맑은고딕");

    CellStyle headStyle = sxssfWorkBook.createCellStyle(); // 헤더용 스타일
    headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    headStyle.setFont(headFont);

    Font bodyFont = sxssfWorkBook.createFont();
    bodyFont.setFontHeightInPoints((short) 9);
    bodyFont.setFontName("돋움");

log.info("#### 바디 스타일 설정");
    // 바디 스타일 설정
    CellStyle bodyStyle = sxssfWorkBook.createCellStyle();
    bodyStyle.setFont(bodyFont);
    bodyStyle.setWrapText(true);
    bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
    bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//높이 가운데 정렬

    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

log.info("#### applyList : " + applyList.toString());
    // 엑셀 row 생성
log.info("엑셀 row 생성");
    for (int i = 0; i < applyList.size(); i++) {
      Row row = sheet.createRow(++rowNo);
      int cellCount = 0;

      // 엑셀 cell 생성 및 값 주입
      // 내방종류에 따라서 String 값 변환 시켜주고 Cell 값 넣어주는 부분도 달라짐
      Cell cell = row.createCell(0);

      if (program_nameNum) {
        cell = row.createCell(cellCount++);//0);                        // 프로그램명
        if(applyList.get(i).get("program_name") != null){
          cell.setCellValue(applyList.get(i).get("program_name").toString());
        }else{
          cell.setCellValue("");
        }
      }

      if (subject_nameNum) {
        cell = row.createCell(cellCount++);//0);                        // 과정명
        if(applyList.get(i).get("subject_name") != null){
          cell.setCellValue(applyList.get(i).get("subject_name").toString());
        }else{
          cell.setCellValue("");
        }
      }

      cell.setCellStyle(bodyStyle);
      if (class_nameNum) {
        cell = row.createCell(cellCount++);//1);    // 과목
        if(applyList.get(i).get("class_name") != null){
          cell.setCellValue(applyList.get(i).get("class_name").toString());
        } else {
          cell.setCellValue("");
        }
      }

      cell.setCellStyle(bodyStyle);
      if (languageNum) {
        cell = row.createCell(cellCount++);//2);    // 언어
        if(applyList.get(i).get("language") != null){
          cell.setCellValue(applyList.get(i).get("language").toString());
        } else {
          cell.setCellValue("");
        }
      }

      cell.setCellStyle(bodyStyle);
      if (levelNum) {
        cell = row.createCell(cellCount++);//3);    // 레벨
        if(applyList.get(i).get("level") != null){
          cell.setCellValue(applyList.get(i).get("level").toString());
        }else{
          cell.setCellValue("");
        }
      }

      cell.setCellStyle(bodyStyle);
      if (dayNum) {
        cell = row.createCell(cellCount++);//4);    // 요일
        if(applyList.get(i).get("day") != null){
          cell.setCellValue(applyList.get(i).get("day").toString().replace("^", ","));
        }else{
          cell.setCellValue("");
        }
      }

      cell.setCellStyle(bodyStyle);
      if (subject_start_timeNum) {
        cell = row.createCell(cellCount++);//5);    // 교육시간
        if(applyList.get(i).get("subject_start_time") != null){
          cell.setCellValue(
              applyList.get(i).get("subject_start_time").toString() + "~" + applyList.get(i).get("subject_end_time").toString());
        }else{
          cell.setCellValue("");
        }
      }

      cell.setCellStyle(bodyStyle);
      if (placeNum) {
        cell = row.createCell(cellCount++);//6);    // 장소
        if(applyList.get(i).get("place") != null) {
          cell.setCellValue(applyList.get(i).get("place").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (teacherNum) {
        cell = row.createCell(cellCount++);//7);    // 강사
        if(applyList.get(i).get("teacher") != null){
          cell.setCellValue(applyList.get(i).get("teacher").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (edu_priceNum) {
        cell = row.createCell(cellCount++);//8);    // 교육비
        if(applyList.get(i).get("edu_price") != null){
          cell.setCellValue(
              decimalFormat.format(Integer.parseInt(applyList.get(i).get("edu_price").toString())));
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (book_priceNum) {
        cell = row.createCell(cellCount++);//9);    // 교재비
        if(applyList.get(i).get("book_price") != null){
          cell.setCellValue(
              decimalFormat.format(Integer.parseInt(applyList.get(i).get("book_price").toString())));
        }else{
          cell.setCellValue("");
        }

      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (etcNum) {
        cell = row.createCell(cellCount++);//10);    // 상세 및 기타
        if(applyList.get(i).get("etc") != null){
          cell.setCellValue(applyList.get(i).get("etc").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (nameNum) {
        cell = row.createCell(cellCount++);//11);                            // 이름
        if(applyList.get(i).get("name") != null){
          cell.setCellValue(applyList.get(i).get("name").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (emailNum) {                // 이메일
        cell = row.createCell(cellCount++);//12);
        if(applyList.get(i).get("email") != null){
          cell.setCellValue(applyList.get(i).get("email").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (numberNum) {
        cell = row.createCell(cellCount++);//13);                            // 사번
        if(applyList.get(i).get("number") != null){
          cell.setCellValue(applyList.get(i).get("number").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (positionNum) {
        cell = row.createCell(cellCount++);//14);                            // 직급
        if(applyList.get(i).get("position") != null){
          cell.setCellValue(applyList.get(i).get("position").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (mobileNum) {
        cell = row.createCell(cellCount++);//15);                            // 연착처
        if(applyList.get(i).get("mobile") != null){
          cell.setCellValue(applyList.get(i).get("mobile").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (companyNum) {
        cell = row.createCell(cellCount++);//16);                            // 회사
        if(applyList.get(i).get("company") != null){
          cell.setCellValue(applyList.get(i).get("company").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (deptNum) {
        cell = row.createCell(cellCount++);//17);                            // 부서
        if(applyList.get(i).get("dept") != null){
          cell.setCellValue(applyList.get(i).get("dept").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (pointNum) {
        cell = row.createCell(cellCount++);//18);                            // 공인시험점수
        if(applyList.get(i).get("point") != null){
          cell.setCellValue(applyList.get(i).get("point").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (option_name1Num) {
        cell = row.createCell(cellCount++);//19);                            // 옵션1
        if(applyList.get(i).get("option_name1") != null){
          cell.setCellValue(applyList.get(i).get("option_name1").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
      }

      cell.setCellStyle(bodyStyle);
      if (option_contents1Num) {
        cell = row.createCell(cellCount++);//20);                            // 옵션 1 내용
        if(applyList.get(i).get("option_contents1") != null){
          cell.setCellValue(applyList.get(i).get("option_contents1").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (option_name2Num) {
        cell = row.createCell(cellCount++);//21);                            // 옵션 2
        if(applyList.get(i).get("option_name2") != null){
          cell.setCellValue(applyList.get(i).get("option_name2").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (option_contents2Num) {
        cell = row.createCell(cellCount++);//22);                            // 옵션 2 내용
        if(applyList.get(i).get("option_contents2") != null){
          cell.setCellValue(applyList.get(i).get("option_contents2").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (option_name3Num) {
        cell = row.createCell(cellCount++);//23);        // 옵션 3
        if(applyList.get(i).get("option_name3") != null){
          cell.setCellValue(applyList.get(i).get("option_name3").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (option_contents3Num) {
        cell = row.createCell(cellCount++);//24);                            // 옵션 3 내용
        if(applyList.get(i).get("option_contents3") != null){
          cell.setCellValue(applyList.get(i).get("option_contents3").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (option_name4Num) {
        cell = row.createCell(cellCount++);//25);                            // 옵션 4
        if(applyList.get(i).get("option_name4") != null){
          cell.setCellValue(applyList.get(i).get("option_name4").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (option_contents4Num) {
        cell = row.createCell(cellCount++);//26);                            // 옵션 4 내용
        if(applyList.get(i).get("option_contents4") != null){
          cell.setCellValue(applyList.get(i).get("option_contents4").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (option_name5Num) {
        cell = row.createCell(cellCount++);//27);                            // 옵션 5
        if(applyList.get(i).get("option_name5") != null){
          cell.setCellValue(applyList.get(i).get("option_name5").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (option_contents5Num) {
        cell = row.createCell(cellCount++);//28);                            // 옵션 5 내용
        if(applyList.get(i).get("option_contents5") != null){
          cell.setCellValue(applyList.get(i).get("option_contents5").toString());
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      // 상태 : 대기(Y) / 신청완료(N) / 신청취소(C)
      if (statusNum) {
        cell = row.createCell(cellCount++);//29);
        if(applyList.get(i).get("status") != null){
          if ("Y".equals(applyList.get(i).get("status"))) {              // 대기(Y)
            cell.setCellValue("대기");
          } else if ("N".equals(applyList.get(i).get("status"))) {
            cell.setCellValue("신청완료");                      // 신청완료(N)
          } else {
            cell.setCellValue("신청취소");                      // 신청취소(C)
          }
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (priceNum) {
        cell = row.createCell(cellCount++);//30);                            // 결제금액
        if(applyList.get(i).get("price") != null){
          cell.setCellValue(
              decimalFormat.format(Integer.parseInt(applyList.get(i).get("price").toString())));
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      if (pay_wayNum) {
        cell = row.createCell(cellCount++);//31);                          // 결제방법
        if(applyList.get(i).get("pay_way") != null){
          if ("C".equals(applyList.get(i).get("pay_way").toString())) {
            cell.setCellValue("카드");
          } else {
            cell.setCellValue("실시간 계좌이체");
          }
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      cell.setCellStyle(bodyStyle);
      // 결제여부 : 결제완료(Y) / 미결제(N) : 학습자단 결제하기출력, 관리자단 미결제 출력 / 결제없음(X) / 결제취소(C) / 결제취소대기(P)
      if (applyList.get(i).get("pay_status") != null && pay_statusNum) {
        cell = row.createCell(cellCount++);//32);
        if(applyList.get(i).get("pay_status") != null){
          if ("Y".equals(applyList.get(i).get("pay_status").toString())) {
            cell.setCellValue("결제완료");
          } else if ("N".equals(applyList.get(i).get("pay_status").toString())) {
            cell.setCellValue("미결제");
          } else if ("X".equals(applyList.get(i).get("pay_status").toString())) {
            cell.setCellValue("결제없음");
          } else if ("C".equals(applyList.get(i).get("pay_status").toString())) {
            cell.setCellValue("결제취소");
          } else if ("P".equals(applyList.get(i).get("pay_status").toString())) {
            cell.setCellValue("결제취소대기");
          }
        }else{
          cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }

      // 신청일
      cell.setCellStyle(bodyStyle);
      cell = row.createCell(cellCount++);//33);
      cell.setCellValue(applyList.get(i).get("create_date").toString());
      cell.setCellStyle(bodyStyle);
    }

      /*Row row = sheet.getRow(1);
       sheet.removeRow(row);
       sheet.del*/
    //sheet.shiftRows(3,3, 1);

    // 디스크로 flush
log.info("디스크로 flush");
    ((SXSSFSheet) sheet).flushRows(applyList.size());

    // 컨텐츠 타입과 파일명 지정
    response.setContentType("ms-vnd/excel");
    response.setHeader("Content-Disposition", "attachment;filename=apply_student_list_excel.xlsx");

    // 엑셀 출력
    sxssfWorkBook.write(response.getOutputStream());

    sxssfWorkBook.dispose();

  }

  public void excelDownSugangCancleUserList(int pageIdx, HttpServletResponse response,
      String payStatus) throws Exception {

    String templateFileName = "sugang_cancle_excel_template.xlsx";
    InputStream templateFile = resourceLoader
        .getResource("classpath:template/excel/" + templateFileName).getInputStream();

    // 엑셀템플릿파일 지정(지정안하고 빈 통합문서로도 가능)
    XSSFWorkbook xssfWorkBook = new XSSFWorkbook(templateFile);

    // 엑셀템플릿파일에 쓰여질 부분 검색
    Sheet originSheet = xssfWorkBook.getSheetAt(0);
    int rowNo = originSheet.getLastRowNum();

    // SXSSF 생성
    SXSSFWorkbook sxssfWorkBook = new SXSSFWorkbook(xssfWorkBook, 100);
    Sheet sheet = sxssfWorkBook.getSheetAt(0);

    Font headFont = sxssfWorkBook.createFont(); // 헤더용 폰트
    headFont.setFontHeightInPoints((short) 11);
    headFont.setFontName("맑은고딕");

    CellStyle headStyle = sxssfWorkBook.createCellStyle(); // 헤더용 스타일
    headStyle.setAlignment(CellStyle.VERTICAL_CENTER);
    headStyle.setFont(headFont);

    Font bodyFont = sxssfWorkBook.createFont();
    bodyFont.setFontHeightInPoints((short) 10);
    bodyFont.setFontName("맑은고딕");

    // 바디 스타일 설정
    CellStyle bodyStyle = sxssfWorkBook.createCellStyle();
    bodyStyle.setFont(bodyFont);
    bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
    bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//높이 가운데 정렬
    bodyStyle.setWrapText(true);
    //bodyStyle.setBorderBottom(CellStyle.BORDER_THIN);
    //bodyStyle.setBorderLeft(CellStyle.BORDER_THIN);
    //bodyStyle.setBorderRight(CellStyle.BORDER_THIN);
    //bodyStyle.setBorderTop(CellStyle.BORDER_THIN);

    // 검색 목록 가져오기
    Map<Object, Object> result = new LinkedHashMap<Object, Object>();
    result = adminService.selectCancleUserList(pageIdx, payStatus);
    List<Map<Object, Object>> cancleUserList = (List<Map<Object, Object>>) result
        .get("cancleUserList");
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    String subjectName = "";
    String subjectStartTime = "";
    String subjectEndTime = "";
log.info("#### cancleUserList : " + cancleUserList.toString());
    // 엑셀 row 생성
    for (int i = 0; i < cancleUserList.size(); i++) {
      Row row = sheet.createRow(++rowNo);

      // 엑셀 cell 생성 및 값 주입
      // 내방종류에 따라서 String 값 변환 시켜주고 Cell 값 넣어주는 부분도 달라짐
      Cell cell = row.createCell(0);                        // 이름
      if (cancleUserList.get(i).get("name") != null) {
        cell.setCellValue(cancleUserList.get(i).get("name").toString());
      } else {
        //cell.setCellValue("");
      }
      cell.setCellStyle(bodyStyle);

      cell = row.createCell(1);                            // 이메일
      if (cancleUserList.get(i).get("email") != null) {
        cell.setCellValue(cancleUserList.get(i).get("email").toString());
      } else {
        //cell.setCellValue("");
      }
      cell.setCellStyle(bodyStyle);

      cell = row.createCell(2);
      if (cancleUserList.get(i).get("program_name") != null) {
        cell.setCellValue(cancleUserList.get(i).get("program_name").toString());    // 프로그램명
      } else {

      }
      cell.setCellStyle(bodyStyle);

      cell = row.createCell(3);                            // 신청과정 명(날짜)
      if (cancleUserList.get(i).get("subject_name") != null) {
        subjectName = cancleUserList.get(i).get("subject_name").toString();
      } else {
        subjectName = "";
      }
      if (cancleUserList.get(i).get("subject_start_time") != null) {
        subjectStartTime = cancleUserList.get(i).get("subject_start_time").toString();
      } else {
        subjectStartTime = "";
      }
      if (cancleUserList.get(i).get("subject_end_time") != null) {
        subjectEndTime = cancleUserList.get(i).get("subject_end_time").toString();
      } else {
        subjectEndTime = "";
      }
      cell.setCellValue(subjectName + "\n " + subjectStartTime + " ~ " + subjectEndTime);
      cell.setCellStyle(bodyStyle);

      cell = row.createCell(4);                            // 취소신청일
      if (cancleUserList.get(i).get("update_date") != null) {
        cell.setCellValue(cancleUserList.get(i).get("update_date").toString());
      } else {
        //cell.setCellValue("");
      }
      cell.setCellStyle(bodyStyle);

      cell = row.createCell(5);                            // 취소 상태
      if (cancleUserList.get(i).get("cancle_status") != null) {
        if ("P".equals(cancleUserList.get(i).get("cancle_status").toString())) {
          cell.setCellValue("관리자삭제");
        } else {
          cell.setCellValue("신청자취소");
        }
      } else {
        //cell.setCellValue("");
      }
      cell.setCellStyle(bodyStyle);

      cell = row.createCell(6);                            // 결제방법
      if (cancleUserList.get(i).get("pay_way") != null) {
        if ("C".equals(cancleUserList.get(i).get("pay_way").toString())) {
          cell.setCellValue("카드");
          cell.setCellStyle(bodyStyle);
          cell = row.createCell(7);                            // 카드사명
          if (cancleUserList.get(i).get("cancle_card_name") != null) {
            cell.setCellValue(cancleUserList.get(i).get("cancle_card_name").toString());
          } else {
            //cell.setCellValue("");
          }
        } else {
          cell.setCellValue("실시간 계좌이체");
          cell.setCellStyle(bodyStyle);
          cell = row.createCell(7);                            // 카드사명
          //cell.setCellValue("");
        }
      } else {
        //cell.setCellValue("");
      }
      cell.setCellStyle(bodyStyle);

      // 결제여부 : 결제완료(Y) / 미결제(N) : 학습자단 결제하기출력, 관리자단 미결제 출력 / 결제없음(X) / 결제취소(C) / 결제취소대기(P)
      cell = row.createCell(8);                            // 결제여부
      if (cancleUserList.get(i).get("pay_status") != null) {
        if ("Y".equals(cancleUserList.get(i).get("pay_status").toString())) {
          cell.setCellValue("결제완료");
        } else if ("N".equals(cancleUserList.get(i).get("pay_status").toString())) {
          cell.setCellValue("미결제");
        } else if ("X".equals(cancleUserList.get(i).get("pay_status").toString())) {
          cell.setCellValue("결제없음");
        } else if ("C".equals(cancleUserList.get(i).get("pay_status").toString())) {
          cell.setCellValue("결제취소");
        } else if ("P".equals(cancleUserList.get(i).get("pay_status").toString())) {
          cell.setCellValue("결제취소대기");
        }
      } else {
        //cell.setCellValue("");
      }
      cell.setCellStyle(bodyStyle);

      cell = row.createCell(9);                            // 결제금액
      if (cancleUserList.get(i).get("price") != null) {
        cell.setCellValue(
            decimalFormat.format(Integer.parseInt(cancleUserList.get(i).get("price").toString())));
      } else {
        //cell.setCellValue("");
      }

    }

    // 디스크로 flush
    ((SXSSFSheet) sheet).flushRows(cancleUserList.size());

    // 컨텐츠 타입과 파일명 지정
    response.setContentType("ms-vnd/excel");
    response.setHeader("Content-Disposition", "attachment;filename=cancle_student_list_excel.xlsx");

    // 엑셀 출력
    sxssfWorkBook.write(response.getOutputStream());

    sxssfWorkBook.dispose();

  }

  public void excelUploadBanUser(ExcelVO vo) throws Exception {

    InetAddress ip = InetAddress.getLocalHost();
    String ipAddress = ip.getHostAddress();
log.info("#### excelUploadBanUser current ip... : " + ipAddress);

    Map<String, Object> info = new HashMap<String, Object>();
    Map<String, Object> data = new HashMap<String, Object>();
    Map<String, Object> param = new HashMap<String, Object>();
    String path = "";
    String sql = "";

    // stage이거나 prod 일때는 서버용 패스를 타도록 수정
    if (ipAddress.equals(CommonEnum.ipStage.getValue()) || ipAddress.equals(CommonEnum.ipProd.getValue()) || ipAddress.equals(CommonEnum.ipProd2.getValue())) {
      path = CommonEnum.SERVER_PATHNAME.getValue(); // 서버용
    } else {
      path = CommonEnum.LOCAL_PATHNAME.getValue(); // 로컬 테스트용
    }

    File tempFile = new File(path + vo.getFile().getOriginalFilename());

    vo.getFile().transferTo(tempFile);

    FileInputStream fis = new FileInputStream(tempFile);

    XSSFWorkbook workbook = new XSSFWorkbook(fis);

    int rowindex = 0;
    int columnindex = 0;
    //시트 수 (첫번째에만 존재하므로 0을 준다)
    //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
    XSSFSheet sheet = workbook.getSheetAt(0);

    //행의 수
    int rows = sheet.getPhysicalNumberOfRows();
log.info("#### rows : " + rows);
    for (rowindex = 1; rowindex < rows; rowindex++) {
      //행을읽는다
      XSSFRow row = sheet.getRow(rowindex);
      if (row != null) {
        //셀의 수
        int cells = row.getPhysicalNumberOfCells();
        for (columnindex = 0; columnindex <= cells; columnindex++) {
          //셀값을 읽는다
          XSSFCell cell = row.getCell(columnindex);
          String value = "";
          //셀이 빈값일경우를 위한 널체크
log.info("cell " + columnindex + " : " + cell);
          if(cell==null || "".equals(cell.toString())){
            value = "";
            continue;
          }else{
            //타입별로 내용 읽기
            switch (cell.getCellType()){
              case XSSFCell.CELL_TYPE_FORMULA:
                value=cell.getCellFormula();
                break;
              case XSSFCell.CELL_TYPE_NUMERIC:
                value=cell.getNumericCellValue()+"";
                break;
              case XSSFCell.CELL_TYPE_STRING:
                value=cell.getStringCellValue()+"";
                break;
              case XSSFCell.CELL_TYPE_BLANK:
                value=cell.getBooleanCellValue()+"";
                break;
              case XSSFCell.CELL_TYPE_ERROR:
                value=cell.getErrorCellValue()+"";
                break;
            }
          }
          //info.put("idx_itm_sugang_page_main", vo.getPageIdx());
          //info.put("idx_itm_sugang_program", vo.getProgramIdx());
          if (columnindex == 0) {
            info.put("name", value);
          } else {
            info.put("email", value);
          }
log.info("각 셀 내용 :" + value);
        }
        param.put("name", info.get("name"));
        param.put("email", info.get("email"));
        param.put("pageIdx", vo.getPageIdx());
        param.put("programIdx", vo.getProgramIdx());
        param.put("infoKey", CommonEnum.INFO_HASH_KEY.getValue());
        sql =
            "SELECT COUNT(*) FROM itm_sugang_ban WHERE CAST(AES_DECRYPT(unhex(name), #{param.infoKey}) AS CHAR) = #{param.name} AND email = #{param.email}"
                + " AND idx_itm_sugang_program = #{param.programIdx} AND idx_itm_sugang_page_main = #{param.pageIdx}";
        if (sugangMapper.selectInt(sql, param) < 1 && info.get("name") != null
            && info.get("email") != null) {
          data.clear();
          data.put("idx_itm_sugang_page_main", vo.getPageIdx());
          data.put("idx_itm_sugang_program", vo.getProgramIdx());
          data.put("email", info.get("email"));
          data.put("name",
              "HEX(AES_ENCRYPT('" + info.get("name") + "', '" + CommonEnum.INFO_HASH_KEY.getValue()
                  + "' ))");
          sugangMapper.insert("itm_sugang_ban", data);
        }
        info.clear();
        data.clear();
      }
    }
    tempFile.delete();
  }

  public void insertExcelLog(int hqIdx, String conditions, String controllerName) throws Exception {

log.info("##### ExcelService.insertExcelLog 엑셀 다운로드 로그 기록");
log.info("#### conditions : " + conditions);
    Map<Object, Object> hqInfo = new LinkedHashMap<Object, Object>();
    ExcelLogVO vo = new ExcelLogVO();

    hqInfo = adminService.selectHqInfo(hqIdx);

    vo.setHqIdx(hqIdx);
    vo.setHqName(hqInfo.get("name").toString());
    vo.setConditions(conditions);
    vo.setControllerName(controllerName);
    vo.setInfoKey(CommonEnum.INFO_HASH_KEY.getValue());
    sugangMapper.insertExcelLog(vo);
  }

  public Map<Object, Object> selectExcelLog(int idx) throws Exception {

    Map<Object, Object> result = new LinkedHashMap<Object, Object>();
    ExcelLogVO vo = new ExcelLogVO();
    vo.setIdx(idx);
    vo.setInfoKey(CommonEnum.INFO_HASH_KEY.getValue());

    vo = sugangMapper.selectExcelLog(vo);

    result.put("excelLogVO", vo);

    return result;

  }

  public static void removeRow(SXSSFSheet sheet, int rowIndex) {
    int lastRowNum = sheet.getLastRowNum();
    if (rowIndex >= 0 && rowIndex < lastRowNum) {
      sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
    }
    if (rowIndex == lastRowNum) {
      Row row = sheet.getRow(rowIndex);
      if (row != null) {
        sheet.removeRow(row);
      }
    }
  }
}
