package com.carrot.cons.dao;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.carrot.common.CommonVO;

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
public class ReservationVO extends CommonVO {

    // idx 
    private int idx;

    // 고객idx 
    private int idxCrmClient;

    // 고객코드
    private String clientCode;

    // 고객명 
    private String name;
    
    private String typeName;

    // 상담날짜 
    private String consDate;

    // 상담시간 
    private String consTime;

    // 메모 
    private String memo;

    // 상담사idx 
    private int idxCrmStaff;

    private String staffName;
    
    // 센터idx 
    private int idxCrmCenter;
    
    private String centerName;
    
    private String reservationStatus;
    
    private int consNum;
    
    private int total;
    private int normal;
    private int gamble;
    private int crime;
    private int eap;
    private int test;
    private int opinion;
    private int etc;
    
    private String reportYn;
    
    private int idxCrmStaffCreate;
    private int idxCrmStaffUpdate;
    
    private String gubunName;
    private BigDecimal consCountUse;
    private String createStaff;
    private String updateStaff;
    
    private String phone;
    
    private int rownum;
    
    private String clientType;
    private int idxCrmCompany;
    private int idxCrmMetaType;
    private String clientStatus;
    
    private int realEap;
    
    private String companyName;

    //상담구분 : 일반,도박,범피,EAP,검사,소견서
    private int idxCrmMetaProduct;

    //상담형태 : 대면, 비대면(화상), 비대면(전화)
    private int idxCrmMetaContact;
    //상담형태 텍스트
    private String contactType;
    //기타권 금액
    private int etcPrice;
    //첫상담일
    private String firstDate;
}
