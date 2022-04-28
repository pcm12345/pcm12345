package com.carrot.exception;

import java.io.IOException;
import java.net.BindException;
import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	private ErrorHandleObject createErrorObject(Exception ex, HttpStatus status, WebRequest request) {
		
		ErrorHandleObject error = new ErrorHandleObject();
			error.setLocale(ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName());
			error.setSysmsg(ex.getMessage());
			error.setTimestamp(LocalDateTime.now());
			error.setStatus(status.getReasonPhrase());
			error.setCode(status.value());
			error.setUri(((ServletWebRequest)request).getRequest().getRequestURI());
			error.setHttpStatus(status);
		  
			return error;
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> globalHandleException(Exception ex, WebRequest request){
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorHandleObject error = createErrorObject(ex, status, request);
		log.error("globalHandleException >>>>> " + ex);
		error.setMessage("관리자에게 문의하세요");
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
		
	}
	
	@ExceptionHandler({RuntimeException.class})
	public ResponseEntity<Object> runtimeException(RuntimeException ex, WebRequest request){
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorHandleObject error = createErrorObject(ex, status, request);
		log.error("runtimeException >>>>> " + ex);
		error.setMessage("관리자에게 문의하세요");
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
		
	}

	
	@ExceptionHandler({NullPointerException.class, BindException.class, IOException.class})
	public ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErrorHandleObject error = createErrorObject(ex, status, request);
		log.error("handleBadRequestException >>>>> " + ex);
		error.setMessage("Parameter 값을 확인해주세요");
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
		
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		log.debug("#### GlobalExceptionHandler ####");
		log.error("#### ERROR INFO : " + ex);
		log.error("### STATUS : " + status);
		log.error("### BODY : " + body);
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	 
}
