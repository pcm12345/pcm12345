package com.carrot.client.dao;

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
public class ClientVO extends CommonVO {

	private String nickName;
	
    // idx 
    private int idx;

    private int rownum;

    // 고객명 
    private String name;

    // 성별 
    private String gender;

    // 생년월일 
    private String birth;

    // 연령대
    private String ageRange;

    // 이메일 
    private String email;
    
    private String phone;

    // 소속 
    private int idxCrmCompany;

    // 유입경로 
    private int idxCrmMetaRoute;

    // 상담센터idx 
    private int idxCrmCenter;

    // 상담사idx 
    private int idxCrmStaff;

    // 상담유형 
    private int idxCrmMetaType;

    // 상담구분 
    private int idxCrmMetaProduct;

    // 사용가능회기 
    private int consCount;
    private int consCountUse;
    private int consCountRest;

    // 고객코드 
    private String clientCode;

    // 고객타입 b2b 인지 b2c 인지
    private String clientType;

    // 희망지역eap 1
    private String eapHopeArea;
    
 // 희망지역eap 2
    private String eapHopeArea2;

    // 희망일정eap 
    private String eapHopeDate;
    
    // 직원과의 관계eap 
    private String eapRel;
    
    private int etcPrice;

    // 진행현황eap 의뢰/센터접수/반려/확정
    private String eapProcessStatus;
    
    private String centerName;
    
    private String staffName;

    private String typeName;
    
    private String productName;
    
    private String eapYn;
    
    private String eapType;
    
    private String clientStatus;
    
    private String companyName;
    
    private int idxCrmMetaSubject;
    
    private String subjectName;
    
    private String consDate;
    
    private String gubun;
    
    private String memo;
    
    private String routeName;
    
    private String status;
    
    private String eapStatus;
    
    private int productPrice;
    private int aCnt;
    private int pCnt;
    private int eCnt;
    private int hCnt;
    private int nCnt;
    
    private String goal;
    private String plan;
    
    private int idxCrmStaffCreate;
    private int idxCrmStaffUpdate;
    
    private int idxCrmPerson;
    
}
