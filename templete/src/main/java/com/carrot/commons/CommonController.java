package com.carrot.commons;

import com.carrot.commons.common.Commons;
import com.carrot.commons.excel.ExcelTemplate;
import com.carrot.commons.model.ResponseBuilder;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("commons")
public class CommonController {

  @PostMapping(value = "/excel")
  ResponseEntity<?> getExcel(
  ) throws IOException {

    String[] a = (String[]) List.of("1", "2", "3").toArray();
    String[] b = (String[]) List.of("4", "5", "6").toArray();
    String[] c = (String[]) List.of("7", "8", "9").toArray();

    String[][] array = new String[3][];
    array[0] = a;
    array[1] = b;
    array[2] = c;

    ExcelTemplate.init().body(array).print(Commons.recallResponse(), "123123.xlsx");
    return ResponseBuilder.init().execute();
  }
}
