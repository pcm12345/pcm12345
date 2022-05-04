package com.carrot.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

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
public class ErrorHandleObject {
	
	private String locale;						// 에러난 함수 위치
	
	private String message;					// 임의 에러 메시지
	
	private String sysmsg;				// 시스템 에러 메시지
	
	private LocalDateTime timestamp;		// 에러 발생 시간
	
	private String status;						// 에러 상태
	
	private String uri;							// 호출 rui
	
	private int code;							// 에러 코드
	
	private HttpStatus httpStatus;
	

}
