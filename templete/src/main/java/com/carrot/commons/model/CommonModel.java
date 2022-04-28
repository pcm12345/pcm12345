package com.carrot.commons.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonModel {

	String createDate;
	String updateDate;
	int pageSize;
	int pageNum;
	
}
