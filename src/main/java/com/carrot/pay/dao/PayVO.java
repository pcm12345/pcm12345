package com.carrot.pay.dao;

import java.sql.Date;

import lombok.AllArgsConstructor;

import lombok.Setter;
import lombok.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PayVO {
	
	private int idx;
	
	private String staffName;

	private int reportCnt;
	
	private int price;
	
	private int staffPay;
	
	private int incomeTax;
	
	private int localIncomeTax;
	
	private int realStaffPay;
	
	private String permissionMonth;
	
	private int sumConsPrice;
	
	private int consPrice;
	
	private String priceType;
	
	private int realSumConsPrice;
	
	private String centerName;

	private int idxCrmCenter;

	private int etcPrice;
	
	private Date permissionDate;
	private Date consDate;
	private String clientName;
	private String gubun;
	private String productName;
	private String companyName;
	
}
