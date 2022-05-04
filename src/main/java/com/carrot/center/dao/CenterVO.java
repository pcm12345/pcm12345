package com.carrot.center.dao;

import java.util.Date;

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
public class CenterVO extends CommonVO {

    // idx 
    private int idx;

    private int rownum;

    // 센터명 
    private String name;

    // 우편번호 
    private String post;

    // 기본주소 
    private String address;

    // 상세주소 
    private String detailAddress;

    // 대표번호 
    private String mainNumber;

    // 센터장 
    private String centerCap;

    // 휴대폰번호 
    private String phone;

    // 상담실 
    private int consRoom;

    // 놀이치료실 
    private int playRoom;

    // 정산유형 소득공제상담비용의3.3%공제후 입금 (D)/ 과세 세금계산서(T) / 면세 일반계산서(F)
    private String purType;

    // 상담료 
    private int consPrice;

    // 상담서비스 성인/아동/청소년/가족/부부/기타
    private String consService;

    // 센터사진파일명1 
    private String centerPhotoName1;

    // 센터사진파일url 
    private String centerPhotoUrl1;

    // 센터사진파일명2 
    private String centerPhotoName2;

    // 센터사진파일url2 
    private String centerPhotoUrl2;

    // 센터사진파일명3 
    private String centerPhotoName3;

    // 센터사진파일url3 
    private String centerPhotoUrl3;

    // 사업자등록증파일명 
    private String centerRegName;

    // 사업자등록증파일url 
    private String centerRegUrl;

    // 통장사본파일명 
    private String centerBankName;

    // 통장사본파일url 
    private String centerBankUrl;

    // 홈페이지 
    private String homepage;

    // 승인현황 Y/N
    private String permission;

    // 계약종료 여부 
    private String contractEndYn;
    
    private MultipartFile centerFile1;
    private MultipartFile centerFile2;
    private MultipartFile centerFile3;
    private MultipartFile regFile;
    private MultipartFile bankFile;
    
    private String id;
    private String pwd;
    
}
