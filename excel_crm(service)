package com.carrot.excel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import com.carrot.client.dao.ClientVO;
import com.carrot.client.dao.PersonVO;
import com.carrot.client.service.ClientService;
import com.carrot.common.CommonEnum;
import com.carrot.common.mapper.crm.CommonMapper;
import com.carrot.common.service.AbstractService;
import com.carrot.cons.dao.ReservationVO;
import com.carrot.status.dao.StatusVO;
import com.carrot.status.service.StatusService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExcelService extends AbstractService {

   @Autowired
   private ResourceLoader resourceLoader;

   @Autowired
   private AdminService adminService;

   @Autowired
   private ClientService clientService;

   @Autowired
   private StatusService statusService;

   @Autowired
   private CommonMapper commonMapper;

   public void excelDownCenterStatus(int idxCrmCenter, String startDate, String endDate, HttpServletResponse response) throws Exception {

      String templateFileName = "centerStatus_excel_template.xlsx";
      InputStream templateFile = resourceLoader.getResource("classpath:template/excel/" + templateFileName).getInputStream();

      // 엑셀템플릿파일 지정(지정안하고 빈 통합문서로도 가능)
      XSSFWorkbook xssfWorkBook = new XSSFWorkbook(templateFile);

      // 엑셀템플릿파일에 쓰여질 부분 검색
      Sheet originSheet = xssfWorkBook.getSheetAt(0);
      int rowNo = originSheet.getLastRowNum();

      // SXSSF 생성
      SXSSFWorkbook sxssfWorkBook = new SXSSFWorkbook(xssfWorkBook, 100);
      Sheet sheet = sxssfWorkBook.getSheetAt(0);

      Font headFont = sxssfWorkBook.createFont(); // 헤더용 폰트
      headFont.setFontHeightInPoints((short)11);
      headFont.setFontName("맑은고딕");

      CellStyle headStyle = sxssfWorkBook.createCellStyle(); // 헤더용 스타일
      headStyle.setAlignment(CellStyle.ALIGN_CENTER);
      headStyle.setFont(headFont);

       Font bodyFont = sxssfWorkBook.createFont();
       bodyFont.setFontHeightInPoints((short) 9);
       bodyFont.setFontName("돋움");

       // 바디 스타일 설정
       CellStyle bodyStyle = sxssfWorkBook.createCellStyle();
       bodyStyle.setFont(bodyFont);
       bodyStyle.setWrapText(true);
       bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
       bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//높이 가운데 정렬

      // 검색 목록 가져오기
      Map<Object, Object> result = new LinkedHashMap<Object, Object>();
      result = adminService.selectCenterStatus(idxCrmCenter, startDate, endDate);
      ClientVO clientCnt = (ClientVO) result.get("clientCnt");
      List<ReservationVO> reservationList = (List<ReservationVO>) result.get("reservationList");

      DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

log.info("##### lastRow : " + rowNo);
       for(int i=0;i<reservationList.size();i++) {
          Row row = sheet.createRow(++rowNo);

           // 엑셀 cell 생성 및 값 주입
           // 내방종류에 따라서 String 값 변환 시켜주고 Cell 값 넣어주는 부분도 달라짐
           Cell cell = row.createCell(0);                                      // 상담사명
           if(reservationList.get(i).getStaffName() != null) {
              cell.setCellValue(reservationList.get(i).getStaffName());
           } else {
              cell.setCellValue("");
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(1);                                   // Total
           if(reservationList.get(i).getTotal() != 0) {
              cell.setCellValue(reservationList.get(i).getTotal());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(2);                                   // 일반
           if(reservationList.get(i).getNormal() != 0) {
              cell.setCellValue(reservationList.get(i).getNormal());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(3);                                   // EAP
           if(reservationList.get(i).getEap() != 0) {
              cell.setCellValue(reservationList.get(i).getEap());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(4);                                   // 도박
           if(reservationList.get(i).getGamble() != 0) {
              cell.setCellValue(reservationList.get(i).getGamble());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(5);                                   // 범피
           if(reservationList.get(i).getCrime() != 0) {
              cell.setCellValue(reservationList.get(i).getCrime());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(6);                                   // 검사
           if(reservationList.get(i).getTest() != 0) {
              cell.setCellValue(reservationList.get(i).getTest());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(7);                                   // 소견서
           if(reservationList.get(i).getOpinion() != 0) {
              cell.setCellValue(reservationList.get(i).getOpinion());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(8);                                   // 기타
           if(reservationList.get(i).getEtc() != 0) {
              cell.setCellValue(reservationList.get(i).getEtc());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           if(i==0) {
              cell = row.createCell(9);                                   // 전체고객수
              if(clientCnt.getACnt() != 0) {
                 cell.setCellValue(clientCnt.getACnt());
              } else {
                 cell.setCellValue(0);
              }
              cell.setCellStyle(bodyStyle);

              cell = row.createCell(10);                                      // 신규
              if(clientCnt.getNCnt() != 0) {
                 cell.setCellValue(clientCnt.getNCnt());
              } else {
                 cell.setCellValue(0);
              }
              cell.setCellStyle(bodyStyle);

              cell = row.createCell(11);                                      // 진행
              if(clientCnt.getPCnt() != 0) {
                 cell.setCellValue(clientCnt.getPCnt());
              } else {
                 cell.setCellValue(0);
              }
              cell.setCellStyle(bodyStyle);

              cell = row.createCell(12);                                      // 홀딩
              if(clientCnt.getHCnt() != 0) {
                 cell.setCellValue(clientCnt.getHCnt());
              } else {
                 cell.setCellValue(0);
              }
              cell.setCellStyle(bodyStyle);

              cell = row.createCell(13);                                      // 종결
              if(clientCnt.getECnt() != 0) {
                 cell.setCellValue(clientCnt.getECnt());
              } else {
                 cell.setCellValue(0);
              }
              cell.setCellStyle(bodyStyle);
           }
       }

       // 디스크로 flush
       ((SXSSFSheet)sheet).flushRows(reservationList.size());


       // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=centerStatus_excel_template.xlsx");

        // 엑셀 출력
        sxssfWorkBook.write(response.getOutputStream());

        sxssfWorkBook.dispose();

   }

   public void excelDownStatusMainStaff(String type, String name, int idxCrmCompany, int idxCrmCenter, String clientType, String startDate, String endDate, HttpServletResponse response) throws Exception {

      String templateFileName = "status_main_template.xlsx";
      InputStream templateFile = resourceLoader.getResource("classpath:template/excel/" + templateFileName).getInputStream();

      // 엑셀템플릿파일 지정(지정안하고 빈 통합문서로도 가능)
      XSSFWorkbook xssfWorkBook = new XSSFWorkbook(templateFile);

      // 엑셀템플릿파일에 쓰여질 부분 검색
      Sheet originSheet = xssfWorkBook.getSheetAt(0);
      int rowNo = originSheet.getLastRowNum();

      // SXSSF 생성
      SXSSFWorkbook sxssfWorkBook = new SXSSFWorkbook(xssfWorkBook, 100);
      Sheet sheet = sxssfWorkBook.getSheetAt(0);

      Font headFont = sxssfWorkBook.createFont(); // 헤더용 폰트
      headFont.setFontHeightInPoints((short)11);
      headFont.setFontName("맑은고딕");

      CellStyle headStyle = sxssfWorkBook.createCellStyle(); // 헤더용 스타일
      headStyle.setAlignment(CellStyle.ALIGN_CENTER);
      headStyle.setFont(headFont);

       Font bodyFont = sxssfWorkBook.createFont();
       bodyFont.setFontHeightInPoints((short) 9);
       bodyFont.setFontName("돋움");

       // 바디 스타일 설정
       CellStyle bodyStyle = sxssfWorkBook.createCellStyle();
       bodyStyle.setFont(bodyFont);
       bodyStyle.setWrapText(true);
       bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
       bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//높이 가운데 정렬

      // 검색 목록 가져오기
      Map<Object, Object> result = new LinkedHashMap<Object, Object>();
      if("staff".equals(type)) {
         result = statusService.selectMainStatusStaff(name, idxCrmCompany, idxCrmCenter, clientType, startDate, endDate);
      } else if ("product".equals(type)) {
         result = statusService.selectMainStatusProduct(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate);
      }
      List<StatusVO> status = (List<StatusVO>) result.get("status");

      DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

log.info("##### lastRow : " + rowNo);

       if("staff".equals(type)) {
          originSheet.getRow(0).createCell(0).setCellValue("상담사명");
          originSheet.getRow(0).createCell(7).setCellValue("매출액");
       } else if ("product".equals(type)) {
          originSheet.getRow(0).createCell(0).setCellValue("상품구분");
      }

       for(int i=0;i<status.size();i++) {
          Row row = sheet.createRow(rowNo++);

           // 엑셀 cell 생성 및 값 주입
           // 내방종류에 따라서 String 값 변환 시켜주고 Cell 값 넣어주는 부분도 달라짐
           Cell cell = row.createCell(0);                                      // 상담사명
           if("staff".equals(type)) {

              if(status.get(i).getStaffName() != null) {
                 cell.setCellValue(status.get(i).getStaffName());
              } else {
                 cell.setCellValue("");
              }
           } else {
              if(status.get(i).getType() != null) {
                 cell.setCellValue(status.get(i).getType());
              } else {
                 cell.setCellValue("");
              }
           }

           cell.setCellStyle(bodyStyle);

           cell = row.createCell(1);
           if(status.get(i).getClientCnt() != 0) {
              cell.setCellValue(status.get(i).getClientCnt());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(2);
           if(status.get(i).getReservationCnt() != 0) {
              cell.setCellValue(status.get(i).getReservationCnt());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(3);
           if(status.get(i).getReportCnt() != 0) {
              cell.setCellValue(status.get(i).getReportCnt());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(4);
           if(status.get(i).getTurnoverRate() != 0) {
              cell.setCellValue(status.get(i).getTurnoverRate() + "%");
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(5);
           if(status.get(i).getAvgReportCnt() != 0) {
              cell.setCellValue(status.get(i).getAvgReportCnt());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(6);
           if(status.get(i).getCancelRate() != 0) {
              cell.setCellValue(status.get(i).getCancelRate() + "%");
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           if("staff".equals(type)) {
              cell = row.createCell(7);
              if(status.get(i).getTotPrice() != 0) {
                 cell.setCellValue(decimalFormat.format(status.get(i).getTotPrice()));
              } else {
                 cell.setCellValue(0);
              }
              cell.setCellStyle(bodyStyle);
           }
       }

       // 디스크로 flush
       ((SXSSFSheet)sheet).flushRows(status.size());


       // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=status_main_template.xlsx");

        // 엑셀 출력
        sxssfWorkBook.write(response.getOutputStream());

        sxssfWorkBook.dispose();

   }

   public void excelDownStatusMainStaff2(String type, String name, int idxCrmCompany, int idxCrmCenter, String clientType, String startDate, String endDate, HttpServletResponse response) throws Exception {

      String templateFileName = "status_main_template2.xlsx";
      InputStream templateFile = resourceLoader.getResource("classpath:template/excel/" + templateFileName).getInputStream();

      // 엑셀템플릿파일 지정(지정안하고 빈 통합문서로도 가능)
      XSSFWorkbook xssfWorkBook = new XSSFWorkbook(templateFile);

      // 엑셀템플릿파일에 쓰여질 부분 검색
      Sheet originSheet = xssfWorkBook.getSheetAt(0);
      int rowNo = originSheet.getLastRowNum();

      // SXSSF 생성
      SXSSFWorkbook sxssfWorkBook = new SXSSFWorkbook(xssfWorkBook, 100);
      Sheet sheet = sxssfWorkBook.getSheetAt(0);

      Font headFont = sxssfWorkBook.createFont(); // 헤더용 폰트
      headFont.setFontHeightInPoints((short)11);
      headFont.setFontName("맑은고딕");

      CellStyle headStyle = sxssfWorkBook.createCellStyle(); // 헤더용 스타일
      headStyle.setAlignment(CellStyle.ALIGN_CENTER);
      headStyle.setFont(headFont);

       Font bodyFont = sxssfWorkBook.createFont();
       bodyFont.setFontHeightInPoints((short) 9);
       bodyFont.setFontName("돋움");

       // 바디 스타일 설정
       CellStyle bodyStyle = sxssfWorkBook.createCellStyle();
       bodyStyle.setFont(bodyFont);
       bodyStyle.setWrapText(true);
       bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
       bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//높이 가운데 정렬

      // 검색 목록 가져오기
      Map<Object, Object> result = new LinkedHashMap<Object, Object>();
      if ("type".equals(type)) {
         result = statusService.selectMainStatusType(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate);
      } else if ("subject".equals(type)) {
         result = statusService.selectMainStatusSubject(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate);
      } else if ("gender".equals(type)) {
         result = statusService.selectMainStatusGender(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate);
      } else if ("age".equals(type)) {
         result = statusService.selectMainStatusAge(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate);
      } else if ("route".equals(type)) {
         result = statusService.selectMainStatusRoute(idxCrmCompany, idxCrmCenter, clientType, startDate, endDate);
      }
      List<StatusVO> status = (List<StatusVO>) result.get("status");

      DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

log.info("##### lastRow : " + rowNo);

       if ("type".equals(type)) {
         originSheet.getRow(0).createCell(0).setCellValue("상품유형");
      } else if ("subject".equals(type)) {
         originSheet.getRow(0).createCell(0).setCellValue("상품주제");
      } else if ("gender".equals(type)) {
         originSheet.getRow(0).createCell(0).setCellValue("성별");
      } else if ("age".equals(type)) {
         originSheet.getRow(0).createCell(0).setCellValue("연령");
      } else if ("route".equals(type)) {
         originSheet.getRow(0).createCell(0).setCellValue("유입경로");
      }

       for(int i=0;i<status.size();i++) {
          Row row = sheet.createRow(rowNo++);

           // 엑셀 cell 생성 및 값 주입
           // 내방종류에 따라서 String 값 변환 시켜주고 Cell 값 넣어주는 부분도 달라짐
           Cell cell = row.createCell(0);                                      // 상담사명
           if(status.get(i).getType() != null) {
              cell.setCellValue(status.get(i).getType());
           } else {
              cell.setCellValue("");
           }

           cell.setCellStyle(bodyStyle);

           cell = row.createCell(1);
           if(status.get(i).getClientCnt() != 0) {
              cell.setCellValue(status.get(i).getClientCnt());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(2);
           if(status.get(i).getReservationCnt() != 0) {
              cell.setCellValue(status.get(i).getReservationCnt());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(3);
           if(status.get(i).getReportCnt() != 0) {
              cell.setCellValue(status.get(i).getReportCnt());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(4);
           if(status.get(i).getInspectCnt() != 0) {
              cell.setCellValue(status.get(i).getInspectCnt());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(5);
           if(status.get(i).getTurnoverRate() != 0) {
              cell.setCellValue(status.get(i).getTurnoverRate() + "%");
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(6);
           if(status.get(i).getAvgReportCnt() != 0) {
              cell.setCellValue(status.get(i).getAvgReportCnt());
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

           cell = row.createCell(7);
           if(status.get(i).getCancelRate() != 0) {
              cell.setCellValue(status.get(i).getCancelRate() + "%");
           } else {
              cell.setCellValue(0);
           }
           cell.setCellStyle(bodyStyle);

       }

       // 디스크로 flush
       ((SXSSFSheet)sheet).flushRows(status.size());


       // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=status_main_template2.xlsx");

        // 엑셀 출력
        sxssfWorkBook.write(response.getOutputStream());

        sxssfWorkBook.dispose();

   }

   public void excelDownPersonList(String clientStatus, int idxCrmCenter, String startDate, String endDate, String value, HttpServletResponse response) throws Exception {

      String templateFileName = "centerPerson_excel_template.xlsx";
      InputStream templateFile = resourceLoader.getResource("classpath:template/excel/" + templateFileName).getInputStream();

      // 엑셀템플릿파일 지정(지정안하고 빈 통합문서로도 가능)
      XSSFWorkbook xssfWorkBook = new XSSFWorkbook(templateFile);

      // 엑셀템플릿파일에 쓰여질 부분 검색
      Sheet originSheet = xssfWorkBook.getSheetAt(0);
      int rowNo = originSheet.getLastRowNum();

      // SXSSF 생성
      SXSSFWorkbook sxssfWorkBook = new SXSSFWorkbook(xssfWorkBook, 100);
      Sheet sheet = sxssfWorkBook.getSheetAt(0);

      Font headFont = sxssfWorkBook.createFont(); // 헤더용 폰트
      headFont.setFontHeightInPoints((short)11);
      headFont.setFontName("맑은고딕");

      CellStyle headStyle = sxssfWorkBook.createCellStyle(); // 헤더용 스타일
      headStyle.setAlignment(CellStyle.ALIGN_CENTER);
      headStyle.setFont(headFont);

       Font bodyFont = sxssfWorkBook.createFont();
       bodyFont.setFontHeightInPoints((short) 9);
       bodyFont.setFontName("돋움");

       // 바디 스타일 설정
       CellStyle bodyStyle = sxssfWorkBook.createCellStyle();
       bodyStyle.setFont(bodyFont);
       bodyStyle.setWrapText(true);
       bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
       bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//높이 가운데 정렬

      // 검색 목록 가져오기
      Map<Object, Object> result = new LinkedHashMap<Object, Object>();
      result = clientService.selectPersonList(clientStatus, idxCrmCenter,-1, 0, startDate, endDate, value);
      List<PersonVO> person = (List<PersonVO>) result.get("personList");

      DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

       for(int i=0;i<person.size();i++) {
          Row row = sheet.createRow(rowNo++);

           // 엑셀 cell 생성 및 값 주입
           // 내방종류에 따라서 String 값 변환 시켜주고 Cell 값 넣어주는 부분도 달라짐
           Cell cell = row.createCell(0);                                      // 고객명
           if(person.get(i).getName() != null) {
              cell.setCellValue(person.get(i).getName());
           } else {
              cell.setCellValue("");
           }

           cell.setCellStyle(bodyStyle);

           cell = row.createCell(1);                                   // 성별
           if(person.get(i).getGender() != null) {
              cell.setCellValue(person.get(i).getGender());
           } else {
              cell.setCellValue("");
           }

           cell.setCellStyle(bodyStyle);

           cell = row.createCell(2);                                   // 생년월일
           if(person.get(i).getBirth() != null) {
              cell.setCellValue(person.get(i).getBirth());
           } else {
              cell.setCellValue("");
           }

           cell.setCellStyle(bodyStyle);

           cell = row.createCell(3);                                   // 이메일
           if(person.get(i).getEmail() != null) {
              cell.setCellValue(person.get(i).getEmail());
           } else {
              cell.setCellValue("");
           }

           cell.setCellStyle(bodyStyle);

           cell = row.createCell(4);                                   // 연락처
           if(person.get(i).getPhone() != null) {
              cell.setCellValue(person.get(i).getPhone());
           } else {
              cell.setCellValue("");
           }

           cell.setCellStyle(bodyStyle);

           cell = row.createCell(5);                                   // 담당상담사
           if(person.get(i).getStaffName() != null) {
              cell.setCellValue(person.get(i).getStaffName());
           } else {
              cell.setCellValue("");
           }

           cell.setCellStyle(bodyStyle);

           cell = row.createCell(6);                                   // 유입경로
           if(person.get(i).getRouteName() != null) {
              cell.setCellValue(person.get(i).getRouteName());
           } else {
              cell.setCellValue("");
           }

           cell.setCellStyle(bodyStyle);

           cell = row.createCell(7);                                   // 작성자
           if(person.get(i).getUpdateStaff() != null) {
              cell.setCellValue(person.get(i).getUpdateStaff());
           } else {
              cell.setCellValue("");
           }

           cell.setCellStyle(bodyStyle);

           cell = row.createCell(8);                                   // 등록일
           if(person.get(i).getCreateDate() != null) {
              cell.setCellValue(adminService.formatDateToString(person.get(i).getCreateDate(), "yyyy-MM-dd hh:mm:ss"));
           } else {
              cell.setCellValue("");
           }

           cell.setCellStyle(bodyStyle);

       }

       // 디스크로 flush
       ((SXSSFSheet)sheet).flushRows(person.size());


       // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=centerPerson_excel_template.xlsx");

        // 엑셀 출력
        sxssfWorkBook.write(response.getOutputStream());

        sxssfWorkBook.dispose();

   }

}
