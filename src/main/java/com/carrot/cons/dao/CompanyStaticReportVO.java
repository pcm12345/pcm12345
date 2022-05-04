package com.carrot.cons.dao;

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
public class CompanyStaticReportVO {

    // 신규 상담자(회)
    private int newReportCnt;

    // 기존 상담자(회)
    private int oldReportCnt;

    // 총 상담자(회)
    private int totalReportCnt;

    //위험단계
    private int danger1st;
    private int danger2nd;
    private int danger3rd;
    private int danger4th;
    private int dangerNone;

    //회기유형-심리검사
    private int genrePsy;

    //회기유형-개인상담
    private int genrePerson;

    //회기유형-미술/놀이치료
    private int genrePlay;

    //회기유형-기타
    private int genreOther;

    //회기유형-else
    private int genreNone;

    //상담형태-대면
    private int typeFace;

    //상담형태-화상
    private int typeVideo;

    //상담형태-전화
    private int typePhone;

    //상담형태-채팅
    private int typeChat;

    //상담형태-방문
    private int typeVisit;

    //상담형태-상주
    private int typeReside;

    //상담형태-Else
    private int typeNone;

    //상담주제별
    private int agenda15;
    private int agenda16;
    private int agenda17;
    private int agenda18;
    private int agenda19;
    private int agenda20;
    private int agenda21;
    private int agenda22;
    private int agenda23;
    private int agenda24;
    private int agenda25;
    private int agenda26;
    private int agenda27;
    private int agenda28;
    private int agenda29;
    private int agenda30;
    private int agenda31;
    private int agenda32;
    private int agenda33;
    private int agenda34;
    private int agenda35;
    private int agenda36;
    private int agenda37;
    private int agenda38;
    private int agenda39;
    private int agenda40;
    private int agenda41;
    private int agenda42;
    private int agenda43;
    private int agenda44;
    private int agendaNone;

}
