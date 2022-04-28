package com.carrot.commons.reum;

import com.carrot.mapper.main.ReumMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/api/v1/solution")
public class ReumController {

  private final boolean isLive = false;
  private final ReumService reumService;
  private final ReumMapper reumMapper;

  public ReumController(ReumService reumService, ReumMapper reumMapper) {
    this.reumService = reumService;
    this.reumMapper = reumMapper;
  }

  @GetMapping(value = "/schemaList/{dbName}")
  public ResponseEntity databaseView(
      @PathVariable String dbName
  ) {
    Map<String, Object> result = new HashMap<>();
    if (!isLive) {
      Map<String, Object> param = new HashMap<>();
      param.put("dbName", dbName);

      List<Map<Object, Object>> tables = reumMapper.selectAll(
          "select TABLE_NAME, TABLE_COMMENT from information_schema.TABLES where TABLE_SCHEMA = #{param.dbName}",
          param);

      Map<String, Object> rtn = new HashMap<>();

      tables.forEach(object -> {
        Map map = object;
        String tableName = (String) map.get("TABLE_NAME");
        String tableComment = (String) map.get("TABLE_COMMENT");
        rtn.put(tableName, tableComment);
      });

      result.put("schema", rtn);
    }
    result.put("err", 0);
    return ResponseEntity.ok(result);
  }

  @GetMapping(value = "/schema/{tableName}")
  public ResponseEntity schemaView(
      @PathVariable String tableName
  ) {
    Map<String, Object> result = new HashMap<>();

    if (!isLive) {
      List columns = reumMapper
          .selectAll(String.format("SELECT "
              + "COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT \n"
              + "from information_schema.COLUMNS \n"
              + "where TABLE_NAME ='%s'", tableName));

      Map<String, Object> rtn = new HashMap<>();

      columns.forEach(object -> {
        Map map = (Map) object;
        String columnName = (String) map.get("COLUMN_NAME");
        String columnComment = (String) map.get("COLUMN_COMMENT");
        String dataType = (String) map.get("DATA_TYPE");
        rtn.put(columnName, dataType + ":" + columnComment);
      });

      result.put("param", rtn);
    }


    result.put("err", 0);
    return ResponseEntity.ok(result);
  }

  @GetMapping(value = "/schemajava/{dbName}")
  public ResponseEntity<?> schemajava(
      @PathVariable String dbName
  ) {
    Map<String, Object> result = new HashMap<>();
    Map<String, Object> rtn = new HashMap<>();

    if (!isLive) {
      List<?> tables = reumMapper
          .selectAll(String.format("select "
              + "TABLE_NAME, TABLE_COMMENT \n"
              + "from information_schema.TABLES \n"
              + "where TABLE_SCHEMA ='%s'", dbName));

      tables.forEach(object -> {
        Map map = (Map) object;
        String tableName = (String) map.get("TABLE_NAME");
        String query = String.format("select "
            + "COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT \n"
            + "from information_schema.COLUMNS \n"
            + "where TABLE_NAME ='%s' and TABLE_SCHEMA = '%s'", tableName, dbName);
        List columns = reumMapper.selectAll(query);
        String className = tableName.substring(0);

        String.format("public class %sVO {", tableName.substring(0));
        String.format("}", tableName);
      });

      result.put("param", rtn);
    }
    result.put("err", 0);
    return ResponseEntity.ok(result);
  }

  @PostMapping(value = "/excel")
  public ResponseEntity excelViewer(
      HttpServletRequest request
      , @RequestParam(value = "file") MultipartFile file
  ) {
    Map<String, Object> result = new HashMap<>();
    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
    Iterator<String> fileNames = mRequest.getFileNames();
    List excel;

    if (fileNames.hasNext()) {
      MultipartFile excelFile = mRequest.getFile("file");
      try {
        boolean isXlsx = file.getOriginalFilename().contains(".xlsx");
        if (isXlsx) {
          excel = reumService.viewXlsx((FileInputStream) excelFile.getInputStream());
        } else {
          excel = reumService.viewXls((FileInputStream) excelFile.getInputStream());
        }
        result.put("excel", excel);
      } catch (IOException e) {
        e.printStackTrace();
      }
      result.put("err", 0);
    } else {
      result.put("err", 105);
    }

    return ResponseEntity.ok(result);
  }

  @GetMapping(value = "/info/{tableName}")
  public ResponseEntity<?> reumInfo(
      @PathVariable(value = "tableName") String tableName
      , @RequestParam int idx
  ) {
    return ResponseEntity.ok(reumService.info(tableName, idx));
  }

  @GetMapping(value = "/list/{tableName}")
  public ResponseEntity<?> reumInfos(
      @PathVariable(value = "tableName") String tableName
      , @RequestParam Map<String, Object> params
  ) {
    int page;
    if (params.get("page") != null) {
      page = Integer.parseInt(params.get("page").toString());
      params.remove("page");
    } else {
      page = 1;
    }

    int pageSize;
    if (params.get("pagesize") != null) {
      try {
        pageSize = Integer.valueOf(params.get("pagesize").toString());
      } catch (Exception e) {
        pageSize = 30;
      }
      params.remove("pagesize");
    } else {
      pageSize = 30;
    }

    return ResponseEntity.ok(reumService.list(tableName, params, page, pageSize));
  }

  @PostMapping(value = "/insdata/{tableName}")
  public ResponseEntity<?> reumInsert(
      @PathVariable(value = "tableName") String tableName
      , @RequestBody Map<String, Object> params
  ) {
    return ResponseEntity.ok(reumService.insData(tableName, params));
  }

  @PostMapping(value = "/updata/{tableName}")
  public ResponseEntity<?> reumUpdate(
      @PathVariable(value = "tableName") String tableName
      , @RequestBody Map<String, Object> params
  ) {
    int idx = (int) params.get("idx");
    params.remove("idx");
    return ResponseEntity.ok(reumService.upData(tableName, params, idx));
  }

  @PostMapping(value = "/deldata/{tableName}")
  public ResponseEntity<?> reumDelete(
      @PathVariable(value = "tableName") String tableName
      , @RequestBody Map<String, Object> params
  ) {
    int idx = (int) params.get("idx");
    return ResponseEntity.ok(reumService.delData(tableName, idx));
  }
}
