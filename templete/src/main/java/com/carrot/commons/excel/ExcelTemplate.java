package com.carrot.commons.excel;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTemplate {

  private Sheet originSheet;
  private Sheet sheet;
  private XSSFWorkbook xssfWorkBook;
  private SXSSFWorkbook sxssfWorkBook;

  public static ExcelTemplate init(
  ) {
    ExcelTemplate eb = new ExcelTemplate();
    eb.xssfWorkBook = new XSSFWorkbook();
    eb.originSheet = eb.xssfWorkBook.getSheetAt(0);
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

  public void print(HttpServletResponse response, String fileName) throws IOException {
    // 컨텐츠 타입과 파일명 지정
    response.setContentType("ms-vnd/excel");
    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

    // 엑셀 출력
    sxssfWorkBook.write(response.getOutputStream());
    sxssfWorkBook.dispose();
  }

}
