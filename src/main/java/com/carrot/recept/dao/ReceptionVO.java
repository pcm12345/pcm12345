package com.carrot.recept.dao;

import com.carrot.common.CommonVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ReceptionVO extends CommonVO {
    private int rownum; //rownum
    private int idx;// idx
    @ApiModelProperty(example = "홍길동")
    private String receptName;//신청자
    @ApiModelProperty(example = "남자")
    private String receptGender;//성별
    @ApiModelProperty(example = "010-3223-8647")
    private String receptPhone;//연락처
    @ApiModelProperty(example = "carrotsds@carrotglobal.com")
    private String receptEmail;//이메일
    @ApiModelProperty(example = "전화문의 내용을 기록한다.")
    private String receptMemo;//현황
    @ApiModelProperty(example = "처리중")
    private String receptStatus;//처리상태 진행중, 처리완료

    @ApiModelProperty(hidden = true)
    private String receptCreatedate;//등록일
    @ApiModelProperty(hidden = true)
    private String receptUpdatedate;//최종수정일
    @ApiModelProperty(hidden = true)
    private int idxCrmStaffCreate;//등록자
    @ApiModelProperty(hidden = true)
    private int idxCrmStaffUpdate;//최종수정자

    @ApiModelProperty(hidden = true)
    private String createStaff;//등록자
    @ApiModelProperty(hidden = true)
    private String updateStaff;//최종수정자

    public ReceptionVO(){

        this.createStaff = "";
        this.idxCrmStaffCreate = 0;
        this.updateStaff = "";
        this.idxCrmStaffUpdate = 0;

    }
}
