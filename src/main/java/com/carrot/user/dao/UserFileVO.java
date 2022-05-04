package com.carrot.user.dao;

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
public class UserFileVO extends CommonVO {
	
	// idx 
    private int idx;

    // 상담사idx 
    private int idxCrmStaff;

    // 자격증파일명 
    private String fileName;

    // 자격증파일url 
    private String fileUrl;
    
    private String storedFileName;
    
    private MultipartFile file;
    
    private String fileType;
    
    private String type;
    
}
