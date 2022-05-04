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

public class CompanyStaticReport2VO {
    // 위험단계 별 명수
    private int perDanger1st; //1단계 해당하는 명
    private int perDanger2nd;
    private int perDanger3rd;
    private int perDanger4th;
    private int perDangerNone;

    // 회기유형 별 명수
    private int perGenrePsy;
    private int perGenrePerson;
    private int perGenrePlay;
    private int perGenreOther;
    private int perGenreNone;

    // 상담형태 별 명수
    private int perTypeFace;
    private int perTypeVideo;
    private int perTypePhone;
    private int perTypeChat;
    private int perTypeVisit;
    private int perTypeReside;
    private int perTypeNone;


    // 상담주제 별 명수
    private int perAgenda15;
    private int perAgenda16;
    private int perAgenda17;
    private int perAgenda18;
    private int perAgenda19;
    private int perAgenda20;
    private int perAgenda21;
    private int perAgenda22;
    private int perAgenda23;
    private int perAgenda24;
    private int perAgenda25;
    private int perAgenda26;
    private int perAgenda27;
    private int perAgenda28;
    private int perAgenda29;
    private int perAgenda30;
    private int perAgenda31;
    private int perAgenda32;
    private int perAgenda33;
    private int perAgenda34;
    private int perAgenda35;
    private int perAgenda36;
    private int perAgenda37;
    private int perAgenda38;
    private int perAgenda39;
    private int perAgenda40;
    private int perAgenda41;
    private int perAgenda42;
    private int perAgenda43;
    private int perAgenda44;
    private int perAgendaNone;
}
