package com.carrot.user.dao;

import lombok.AllArgsConstructor;

import lombok.Setter;
import lombok.ToString;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkTimeVO {

	private String day;
	
	private String time;
}
