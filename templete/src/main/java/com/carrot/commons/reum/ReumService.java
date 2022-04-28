package com.carrot.commons.reum;

import com.carrot.mapper.main.ReumMapper;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ReumService {

  private final ReumMapper reumMapper;

  public ReumService(ReumMapper reumMapper) {
    this.reumMapper = reumMapper;
  }


  public List viewXlsx(FileInputStream fileInputStream) {
    try {
      List outList = new ArrayList<>();
      List out = new ArrayList<>();
      Workbook workbook = new XSSFWorkbook(fileInputStream);
      return getExcelValues(outList, out, workbook);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("파일을 찾을수 없습니다.");
    } catch (IOException e) {
      throw new RuntimeException(e.getLocalizedMessage());
    }
  }

  public List viewXls(FileInputStream fileInputStream) {
    try {
      List outList = new ArrayList<>();
      List out = new ArrayList<>();
      Workbook workbook = new HSSFWorkbook(fileInputStream);
      return getExcelValues(outList, out, workbook);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("파일을 찾을수 없습니다.");
    } catch (IOException e) {
      throw new RuntimeException(e.getLocalizedMessage());
    }
  }

  private List getExcelValues(List outList, List out, Workbook workbook) {
    int columnIndex;
    int rowindex;
    Sheet sheet = workbook.getSheetAt(0);

    int rows = Math.min(sheet.getPhysicalNumberOfRows(), 1000);
    for (rowindex = 0; rowindex < rows; rowindex++) {
      Row row = sheet.getRow(rowindex);
      if (row != null) {
        int cells = row.getPhysicalNumberOfCells();
        cells = Math.min(cells, 1000);
        out = new ArrayList<>();
        for (columnIndex = 0; columnIndex <= cells; columnIndex++) {
          Cell cell = row.getCell(columnIndex);
          String value = "";
          if (cell == null) {
            continue;
          } else {
            //타입별로 내용 읽기
            switch (cell.getCellType()) {
              case HSSFCell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula();
                break;
              case HSSFCell.CELL_TYPE_NUMERIC:
                value = cell.getNumericCellValue() + "";
                break;
              case HSSFCell.CELL_TYPE_STRING:
                value = cell.getStringCellValue() + "";
                break;
              case HSSFCell.CELL_TYPE_BLANK:
                value = "";
                break;
              case HSSFCell.CELL_TYPE_ERROR:
                value = "";
                break;
            }
          }
          out.add(value);
        }
      }
      outList.add(out);
    }
    return outList;
  }


  public Map<String, Object> insData(String tableName, Map<String, Object> param) {
    int idx = reumMapper.insData(tableName, param);

    Map<String, Object> response = Map.of("idx", idx, "err", 0);
    return response;
  }

  public Map<String, Object> upData(String tableName, Map<String, Object> params, int idx) {
    reumMapper.upData(tableName, params, "idx = " + idx);
    Map<String, Object> response = new HashMap<>();
    response.put("err", 0);
    return response;
  }

  public Map<String, Object> info(String tableName, int idx) {
    String query = String
        .format("select * from %s where idx = #{param.idx} limit 1", tableName, idx + "");
    Map<String, Object> param = Map.of("idx", idx);
    Map<String, Object> info = reumMapper.selectOne(query, param);

    Map<String, Object> response = Map.of("info", info, "err", 0);
    return response;
  }

  public Map<String, Object> info(String tableName, String value, String field) {
    String query = String
        .format("select * from %s where %s = #{para+m.value}  limit 1", tableName, field);
    Map<String, Object> param = Map.of("value", value);
    return reumMapper.selectOne(query, param);
  }

  public Map<String, Object> list(String tableName, Map<String, Object> param, int page,
      int pageSize) {
    Set<String> whereKeySet = param.keySet();
    Map<String, Object> param_add = new HashMap<>();
    String val = "";
    String pkey = "";
    String where = "";
    if (whereKeySet.size() > 0) {
      ///where절
      List<String> trash = new ArrayList<>();

      int t = 0;
      where = "where 1 ";
      for (String key : whereKeySet) {
        t++;
        String substring = key.substring(key.length() - 2);

        if (substring.equals(">>")) {
          pkey = key.substring(0, key.length() - 2);
          val = param.get(key).toString();
          param_add.put(pkey + "_reum_param_" + t, val);
          trash.add(key);
          where += String.format(" and %s > #{param.%s_reum_param_%d}", pkey, pkey, t);
          continue;
        }
        if (substring.equals("<<")) {
          pkey = key.substring(0, key.length() - 2);
          val = param.get(key).toString();
          param_add.put(pkey + "_reum_param_" + t, val);
          trash.add(key);
          where += String.format(" and %s < #{param.%s_reum_param_%d}", pkey, pkey, t);
          continue;
        }

        substring = key.substring(key.length() - 1);
        if (substring.equals(">")) {
          pkey = key.substring(0, key.length() - 1);
          val = param.get(key).toString();
          param_add.put(pkey + "_reum_param_" + t, val);
          trash.add(key);
          where += String.format(" and %s >= #{param.%s_reum_param_%d}", pkey, pkey, t);
          continue;
        }
        if (substring.equals("<")) {
          pkey = key.substring(0, key.length() - 1);
          val = param.get(key).toString();
          param_add.put(pkey + "_reum_param_" + t, val);
          where += String.format(" and %s <= #{param.%s_reum_param_%d}", pkey, pkey, t);
          continue;
        }
        where += String.format(" and %s = #{param.%s}", key, param.get(key));
        param_add.put(key, param.get(key));

      }
    }

    /// 페이지네이션
    String limit = " limit " + (page - 1) * pageSize + ", " + pageSize;
    String query = String.format("select count(*) from %s %s", tableName, where);
    int size = reumMapper
        .queryInt(query, param_add);

    List<Map<Object, Object>> list = reumMapper
        .queryAll(String.format("select * from %s %s", tableName, where + limit), param_add);

    Map<String, Object> response = Map.of(
        "list", list,
        "size", size,
        "err", 0
    );
    return response;
  }

  public Map<String, Object> selectAll(String query, Map<String, Object> param) {
    List<Map<Object, Object>> list = reumMapper.selectAll(query, param);
    Map<String, Object> response = Map.of(
        "list", list,
        "err", 0
    );
    return response;
  }

  public Map<String, Object> delData(String tableName, int idx) {
    Map<String, Object> params = Map.of("idx", idx);
    reumMapper.delData(tableName, params);

    Map<String, Object> response = Map.of("err", 0);
    return response;
  }
}
