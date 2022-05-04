package com.carrot.reum;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/solution")
public class ReumController {

  private final ReumService reumService;

  public ReumController(ReumService reumService) {
    this.reumService = reumService;
  }

  @GetMapping(value = "/info/{tableName}")
  public ResponseEntity<?> reumInfo(
      @PathVariable(value = "tableName") String tableName,
      @RequestParam int idx
  ) {
    return ResponseEntity.ok(reumService.info(tableName, idx));
  }

  @GetMapping(value = "/list/{tableName}")
  public ResponseEntity<?> reumInfos(
      @PathVariable(value = "tableName") String tableName,
      @RequestParam Map<String, Object> params
  ) {
    int page;
    if (params.get("page") != null) {
      page = (int) params.get("page");
      params.remove("page");
    } else {
      page = 1;
    }

    int pageSize;
    if (params.get("pagesize") != null) {
      pageSize = (int) params.get("pagesize");
      params.remove("pagesize");
    } else {
      pageSize = 30;
    }



    return ResponseEntity.ok(reumService.list(tableName, params, page, pageSize));
  }

  @PostMapping(value = "/insdata/{tableName}")
  public ResponseEntity<?> reumInsert(
      @PathVariable(value = "tableName") String tableName,
      @RequestBody Map<String, Object> params
  ) {
    return ResponseEntity.ok(reumService.insData(tableName, params));
  }

  @PostMapping(value = "/updata/{tableName}")
  public ResponseEntity<?> reumUpdate(
      @PathVariable(value = "tableName") String tableName,
      @RequestBody Map<String, Object> params
  ) {
    int idx = (int) params.get("idx");
    params.remove("idx");
    return ResponseEntity.ok(reumService.upData(tableName, params, idx));
  }

  @PostMapping(value = "/deldata/{tableName}")
  public ResponseEntity<?> reumDelete(
      @PathVariable(value = "tableName") String tableName,
      @RequestBody Map<String, Object> params
  ) {
    int idx = (int) params.get("idx");
    return ResponseEntity.ok(reumService.delData(tableName, idx));
  }



}
