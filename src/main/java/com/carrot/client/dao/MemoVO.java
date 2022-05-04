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
public class MemoVO extends CommonVO {
	
    // idx 
    private int idx;

    // 내담자idx 내담자idx로 매핑시켜줘도 되나 물어보기
    private int idxCrmPerson;

    private int idxCrmCenter;

    private String centerName;

    // 특이사항 
    private String memo;

    // 작성자 
    private int idxCrmStaffCreate;
    
    private String createStaff;
    
    private String formatCreateDate;
    
    private int rownum;

}
