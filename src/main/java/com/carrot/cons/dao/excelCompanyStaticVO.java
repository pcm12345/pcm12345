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
public class excelCompanyStaticVO extends CommonVO {
    //
    private String companyName;
    private String centerName;
    private String personName;
    private String gender;
    private String ageRange;
    private String consDate;
    private String permissionDate;
    private String danger_stepTitle;
    private String titleGenre;
    private String titleType;
    private String titleAgendaFirst;
    private String titleAgendaSecond;
}
