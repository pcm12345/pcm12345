package com.carrot.commons.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ResponseBuilder {

  ResponseData responseData;

  private ResponseBuilder() {
    responseData = new ResponseData();
    responseData.data = new HashMap<>();
    responseData.status = 0;
    responseData.message = "OK";
  }

  public static ResponseBuilder init() {
    return new ResponseBuilder();
  }

  public static ResponseBuilder init(int status) {
    ResponseBuilder responseBuilder = new ResponseBuilder();
    responseBuilder.responseData.status = status;
    return responseBuilder;
  }

  public static ResponseBuilder init(Map<String, ?> data) {
    ResponseBuilder responseBuilder = new ResponseBuilder();
    responseBuilder.responseData.data = data;
    return responseBuilder;
  }

  public static ResponseBuilder init(String message) {
    ResponseBuilder responseBuilder = new ResponseBuilder();
    responseBuilder.responseData.message = message;
    return responseBuilder;
  }

  public static ResponseBuilder init(int status, Map<String, ?> data) {
    ResponseBuilder responseBuilder = new ResponseBuilder();
    responseBuilder.responseData.status = status;
    responseBuilder.responseData.data = data;
    return responseBuilder;
  }

  public ResponseBuilder error(int status) {
    this.responseData.status = status;
    return this;
  }

  public ResponseBuilder result(Map<String, ?> data) {
    this.responseData.data = data;
    return this;
  }

  public ResponseBuilder message(String message) {
    this.responseData.message = message;
    return this;
  }

  public ResponseEntity<?> execute() {
    return ResponseEntity.ok(this.responseData);
  }

  @Getter
  class ResponseData {
    private Map<String, ?> data;
    private int status;
    private String message;
  }


}
