package com.carrot.common;



import java.sql.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonVO {

	@ApiModelProperty(hidden = true)
	Date createDate;
	@ApiModelProperty(hidden = true)
	Date updateDate;
	//String createDateString;
	int pageSize;
	int pageNum;
	
	/*
	 * public String getCreateDateString() { SimpleDateFormat format = new
	 * SimpleDateFormat("yyyy-MM-dd"); String str = format.format(createDate);
	 * return str; }
	 */
	
}
