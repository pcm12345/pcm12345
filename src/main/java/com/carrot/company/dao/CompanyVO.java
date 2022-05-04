package com.carrot.company.dao;

import java.util.Date;

import com.carrot.common.CommonVO;

import lombok.ToString;

import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompanyVO extends CommonVO{

    // idx 
    private int idx;

    private int rownum;

    // 기업명 
    private String name;

    // 담당자
    private String companyCode;

    // 담당자 
    private String manager;

    // 연락처 
    private String phone;

    // 계약시작일 
    private Date consStartDate;

    // 계약종료일
    private Date consEndDate;

    // 계약총액 
    private int totalPrice;

    // 상담1회비용 
    private int price;

    // 지원회기 
    private int consCount;

    // 검사기능여부 가능Y 불가능N
    private String testYn;

    // 가족지원여부 가능Y 불가능N
    private String familyYn;

    // 유형 찾아가는상담/전화/화상/법률/채팅/대면상담
    private String type;

    // 기업메모 
    private String memo;
}
