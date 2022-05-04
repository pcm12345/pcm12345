package com.carrot.alt.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrot.alt.service.AltService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/alt")
@Api(description = "알림톡 발송", tags = "알림톡 발송하는 기능")
@Slf4j
public class AltController {

	@Autowired
	private AltService altService;
	
	@GetMapping("")
	@ApiOperation(value="알림톡 발송", notes = "알림톡 발송[EAP등록/EAP승인/협약센터승인]")
	public ResponseEntity<Map<Object, Object>> insertTalk(@RequestParam("idxCrmCenter") int idxCrmCenter, @RequestParam("id") int id) throws Exception {
		log.info("#### AltController.insertTalk idxCrmCenter : " + idxCrmCenter + " id : " + id);
		ResponseEntity<Map<Object, Object>> rtn = new ResponseEntity<Map<Object, Object>>(altService.insertTalk(idxCrmCenter, id), HttpStatus.OK);
		return rtn;
	}
	
}
