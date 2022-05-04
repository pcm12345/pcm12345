package com.carrot.common.service;

import com.carrot.cons.dao.excelCompanyStaticVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class excelService {

    public void excelDownList(List<String> headerList, List<excelCompanyStaticVO> bodyList, String outputFileName,
                              HttpServletResponse response)
            throws Exception {
        //String templateFileName = "sugangUserList_excel_template.xlsx";

        // 엑셀템플릿파일 지정(지정안하고 빈 통합문서로도 가능)
        XSSFWorkbook xssfWorkBook = new XSSFWorkbook();

        // 엑셀템플릿파일에 쓰여질 부분 검색
        Sheet originSheet = xssfWorkBook.createSheet();

        Row headerRow = originSheet.createRow(0);
        int lastCellNum = headerRow.getLastCellNum();

        for (String header : headerList) {
            Cell cell = headerRow.createCell(++lastCellNum);
            cell.setCellValue(header);
        }

        int rowNo = originSheet.getLastRowNum();

        // SXSSF 생성
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

        // 바디 스타일 설정
        CellStyle bodyStyle = sxssfWorkBook.createCellStyle();
        bodyStyle.setFont(bodyFont);
        bodyStyle.setWrapText(true);
        bodyStyle.setAlignment(CellStyle.ALIGN_CENTER);
        bodyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//높이 가운데 정렬

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");


        for (int i = 0; i < bodyList.size(); i++) {
            Row row = sheet.createRow(++rowNo);
            int cellCount = 0;

            // 엑셀 cell 생성 및 값 주입
            // 내방종류에 따라서 String 값 변환 시켜주고 Cell 값 넣어주는 부분도 달라짐

            Object obj = bodyList.get(i);
            // 반복문을 이용하여 해당 클래스에 정의된 필드를 가져옵니다.
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj); // 필드에 해당하는 값을 가져옵니다.
                System.out.println("field : "+field.getName()+" | value : " + value);
                System.out.println(obj.toString());
                Cell cell = row.createCell(cellCount++);

                if (value != null) {
                    //data.put(fields[j].getName(), fields[i].get(VO2));
                    cell.setCellValue(value.toString());
                } else {
                    cell.setCellValue("");
                }

            }
        }

        // 디스크로 flush
        ((SXSSFSheet) sheet).flushRows(bodyList.size());

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename="+outputFileName);

        // 엑셀 출력
        sxssfWorkBook.write(response.getOutputStream());

        sxssfWorkBook.dispose();
    }
}
