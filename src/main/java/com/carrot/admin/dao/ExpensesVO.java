package com.carrot.admin.dao;

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
public class ExpensesVO extends CommonVO {

    // idx 
    private int idx;

    private int rownum;

    // 사용자 
    private String expensesName;

    // 기업명 
    private String expensesCompanyName;

    // 지출내용 
    private String expText;

    // 지출금액 
    private int expPrice;

    // 지출날짜 
    private Date expensesDate;

    // 센터idx 
    private int idxCrmCenter;

    // 센터명 
    private String centerName;

    // 메모 
    private String expensesMemo;
    
    private String expensesType;
    
}
