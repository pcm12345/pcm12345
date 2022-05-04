package com.carrot.user.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
public class UserVO extends CommonVO {
	
    // idx 
    private int idx;

    private int rownum;

    // 아이디 
    private String id;

    // 패스워드 
    private String pwd;

    // 성명 
    private String name;

    // 성별 여자/남자
    private String gender;

    // 휴대폰 
    private String phone;

    // 이메일 
    private String email;

    // 생년월일 
    private String birth;

    // 최종학력 중학교, 고등학교, 대학졸업(2,3년), 대학졸업(4년), 석사졸업, 박사졸업
    private String education;

    // 경력 
    private String career;

    // 한줄소개 
    private String introduce;

    // 사진파일명 
    private String photoName;

    // 사진파일url 
    private String photoUrl;

    // 센터idx 
    private int idxCrmCenter;

    // 센터명
    private String centerName;
    
    // 권한 마스터관리자(삭제 권한 및 상담일지 열람)/관리자/본원상담사/협약상담사/센터장
    private String authority;

    // 메모 관리자등록 메모
    private String memo;

    // 학교/전공 
    private String educationInfo;

    // 재직현황 재직/휴직/퇴직
    private String duty;

    // 전문분야 아동/청소년/성인/부부/커플/가족
    private int idxCrmMetaPro;
    
    // 회기당급여 
    private int pay;

    // 근무시간
    private List<WorkTimeVO> workTimeList;
    
    // 승인유무 Y/N
    private String permission;
    
    private MultipartFile profilePhoto;
    
    private String proName;
    
    private int idxCrmMetaType;
    
}
