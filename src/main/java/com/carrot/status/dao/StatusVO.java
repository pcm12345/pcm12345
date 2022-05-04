package com.carrot.status.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StatusVO {
	
	private int idx;
	private int clientCnt;
	private int reservationCnt;
	private int reportCnt;
	private double cancelRate;
	private double avgReportCnt;
	private double turnoverRate;
	private int consultCnt;
	private int totPrice;
	private String staffName;
	private String type;
	private int inspectCnt;
	
}
