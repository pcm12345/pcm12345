package com.carrot.cons.dao;

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
public class ReportVO extends CommonVO {

    //rownum
    private int rownum;

    // idx
    private int idx;

    // 고객idx
    private int idxCrmClient;

    // 고객연령대
    private String ageRange;

    // 상담사idx
    private int idxCrmStaff;

    // 회기유형 idx
    private int idxGenreMeta;

    // 상담타입 idx
    private int idxTypeMeta;

    // 상담대주제 idx
    private int idxAgendaFirst;

    // 상담대주제 idx
    private int idxAgendaSecond;

    // 승인유무 Y/N
    private String permission;

    // 상담사소견
    private String consOpinion;

    // 심리검사
    private String mentalTest;

    // 이후계획
    private String nextPlan;

    // 상담내용
    private String consContents;

    // 첨부파일명
    private String attachFileName;

    // 첨부파일url
    private String attachFileUrl;

    // 진행현황 승인 -> 완료 승인전 -> 미완료
    private String processStatus;

    // 상담주제
    private int idxCrmMetaSubject;

    private int idxCrmMetaDanger;

    // 위험단계 1단계~4단계
    private String dangerStepTitle;

    // 전체상담의목표
    private String goal;

    // 상담계획
    private String plan;

    // 주호소문제
    private String mainProblem;

    private MultipartFile attachFile;

    private String clientName;

    private String staffName;

    private String productName;

    private String eapYn;

    private String eapType;

    private Date consDate;

    private String reportDate;

    private int idxCrmReservation;

    private int idxReservation;

    private int idxReport;

    private Date permissionDate;

    private String reportYn;

    private String consProcess;

    private String centerName;

    private String reservationStatus;

    private String dangerStepInfo;

    private int reservationIdxCrmStaff;

    private String consDate2;

    private Integer consCount;

    private String companyName;
    private String titleType;
    private String titleAgendaFirst;
    private String titleAgendaSecond;

    //상담사에게 수정요청(Y인경우)
    private String requestModify;
}
