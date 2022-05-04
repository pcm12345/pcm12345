package com.carrot.client.dao;

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
public class PurchaseVO extends CommonVO {

    // idx 
    private Integer idx;

    private Integer rownum;

    // 고객idx 
    private int idxCrmClient;

    // 기업idx 
    private int idxCrmCompany;

    // 고객명 
    private String name;

    // 고객타입 B2C/B2B
    private String clientType;

    // 일자 
    private String purDate;

    // 수납/환불 상태 수납/환불
    private String status;

    // 금액 
    private int purPrice;
    
    private int payPrice;
    
    private int noPrice;
    
    private int price;
    
    private int refundPrice;

    // 수납방법 카드/계좌이체/미수금/상품권
    private String purPayWay;

    // 메모 
    private String memo;
    
    private String clientCode;
    
    private String companyName;
    
    private int idxCrmCenter;
    
    private int consCount;
    
    private int consCountUse;
    
    private String centerName;
    
    private int purCash;
    
    private int purCard;
    
    private int purTexBill;
    
    private int purRefund;
    
    private int purGiftcard;
    
    private int purReceivable;
    
    private int purTotal; 
    
    private String gubun;
    
    private String clientName;
    
    private String staffName;
   
    
}

