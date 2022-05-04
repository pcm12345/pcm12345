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
public class PersonVO extends CommonVO {
	
	// idx idx
    private int idx;

    // 고객명 고객명
    private String name;

    private int rownum;

    // 성별 성별
    private String gender;

    // 생년월일 생년월일
    private String birth;

    // 연령대
    private String ageRange;

    // 이메일 이메일
    private String email;

    // 유입경로 유입경로
    private int idxCrmMetaRoute;

    // 휴대폰 휴대폰
    private String phone;
    
    private int idxCrmStaffCreate;
    private int idxCrmStaffUpdate;
    
    private String routeName;
    private String createStaff;
    private String updateStaff;
    
    private String staffName;

}
