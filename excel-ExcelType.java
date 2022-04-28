package com.carrot.commons.excel;

import lombok.Getter;

@Getter
public enum ExcelType {

  APPLY_LIST("apply_list.xlsx","신청자 리스트");

  ExcelType(String fileName, String subjectName) {
    this.fileName = fileName;
    this.subjectName = subjectName;
  }

  String fileName, subjectName;
}
