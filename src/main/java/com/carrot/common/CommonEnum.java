package com.carrot.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonEnum {
	
	NAVER_OBJECT("https://kr.object.ncloudstorage.com/"),
	INFO_HASH_KEY("carrot"),
	MEMBER("ROLE_MEMBER"),
	ADMIN("ROLE_ADMIN"),
	CELL("025169060"),
	ROUTEID("carrotglobal01"),
	ipStage("10.41.152.143"),
	publicIpStage("49.50.165.245:8102"),
	ipProd("10.41.178.119"),
	publicIpProd("118.67.129.196:8102");
	
	private String value;
	
}
