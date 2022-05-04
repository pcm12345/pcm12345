package com.carrot.cons.dao;

import com.carrot.common.CommonVO;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;

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
public class CompanyStaticPersonVO {

    // 남자(명)
    private int genderMan;

    // 여자(명)
    private int genderWoman;

    // 성별 기타(명)
    private int genderNone;

    // 10대(명)
    private int age10;

    // 20대(명)
    private int age20;

    // 30대(명)
    private int age30;

    // 40대(명)
    private int age40;

    // 50대(명)
    private int age50;

    // 연령대 기타(명)
    private int ageNone;

    // 신규 상담자(명)
    private int newPersonCnt;

    // 기존 상담자(명)
    private int oldPersonCnt;

    // 총 상담자(명)
    private int totalPersonCnt;

    // 남자 상담회수(회)
    private int countMan;

    // 여자 상담회수(회)
    private int countWoman;

    // 성별 미등록자 상담회수(회)
    private int countGenderNone;

    // 10대 상담회수(회)
    private int countAge10;

    // 20대 상담회수(회)
    private int countAge20;

    // 30대 상담회수(회)
    private int countAge30;

    // 40대 상담회수(회)
    private int countAge40;

    //50대 상담회수(회)
    private int countAge50;

    //연령 미등록록자 상회수(회)
    private int countAgeNone;

}
