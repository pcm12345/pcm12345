package com.carrot.commons.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
@Slf4j
public class ExcelTemplate {

    private Sheet originSheet;
    private Sheet sheet;
    private XSSFWorkbook xssfWorkBook;
    private SXSSFWorkbook sxssfWorkBook;

    public static ExcelTemplate init(
    ) {
        ExcelTemplate eb = new ExcelTemplate();
        eb.xssfWorkBook = new XSSFWorkbook();
        eb.originSheet = eb.xssfWorkBook.createSheet("Sheet 1");
        eb.sxssfWorkBook = new SXSSFWorkbook(eb.xssfWorkBook, 100);

        Font headFont = eb.sxssfWorkBook.createFont(); // 헤더용 폰트
        headFont.setFontHeightInPoints((short) 11);
        headFont.setFontName("맑은고딕");

        CellStyle headStyle = eb.sxssfWorkBook.createCellStyle(); // 헤더용 스타일
        headStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headStyle.setFont(headFont);

        Font bodyFont = eb.sxssfWorkBook.createFont();
        bodyFont.setFontHeightInPoints((short) 9);
        bodyFont.setFontName("돋움");

        // 바디 스타일 설정
        CellStyle bodyStyle = eb.sxssfWorkBook.createCellStyle();
        bodyStyle.setFont(bodyFont);
        bodyStyle.setWrapText(true);
        bodyStyle.setBorderBottom(CellStyle.BORDER_THIN);
        bodyStyle.setBorderLeft(CellStyle.BORDER_THIN);
        bodyStyle.setBorderRight(CellStyle.BORDER_THIN);
        bodyStyle.setBorderTop(CellStyle.BORDER_THIN);

        eb.originSheet = eb.xssfWorkBook.getSheetAt(0);
        eb.sheet = eb.sxssfWorkBook.getSheetAt(0);
        return eb;
    }

    public ExcelTemplate body(String[][] values) throws IOException {
        int rowNo = originSheet.getLastRowNum();
        log.info("body.rowNo : "+rowNo);
        for(String[] value : values) {
            Row row = sheet.createRow(++rowNo);
            int cellCount = value.length;
            for (String val : value) {
                Cell cell = row.createCell(++cellCount);
                cell.setCellValue(val);
            }
        }
        ((SXSSFSheet) sheet).flushRows(values.length);
        return this;
    }

    public ExcelTemplate body(List<Map<String, String>> headList, List<Map<String, String>> values) throws IOException {
        int rowNo = originSheet.getLastRowNum();

        //heading 에서 키와 엑셀에서의 헤딩 값을 가져와서 입력한다.
        Row headRow = sheet.createRow(rowNo++);
        int cellCount = 0;
        for(Map<String, String> heading : headList) {
            Iterator<String> keys = heading.keySet().iterator();
            if(keys.hasNext()) {
                String key = keys.next();
                Cell cell = headRow.createCell(cellCount++);
                cell.setCellValue(heading.get(key));
            }
        }

        //각 리스트별로 헤딩을 루프 돌면서 키값에 해당하는 값을 각 셀에 입력해준다.
        for(Map<String, String> value : values) {
            Row row = sheet.createRow(rowNo++);
            cellCount = 0;
            for(Map<String, String> heading : headList) {
                Iterator<String> keys = heading.keySet().iterator();
                if(keys.hasNext()) {
                    String key = keys.next();
                    Cell cell = row.createCell(cellCount++);
                    cell.setCellValue(value.get(key));
                }
            }
        }
        ((SXSSFSheet) sheet).flushRows(values.size());
        return this;
    }

    public void print(HttpServletResponse response, String fileName) throws IOException {
        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        // 엑셀 출력
        sxssfWorkBook.write(response.getOutputStream());
        sxssfWorkBook.dispose();
    }

}
