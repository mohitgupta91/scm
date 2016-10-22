package com.snapdeal.scm.web.services.impl;

import org.springframework.http.HttpStatus;

/**
 * DataValidationException : Data Invalid Exception
 * 
 * @author pranav
 *
 */
public class DataValidationException extends Exception{

	private static final long serialVersionUID = 1L;
	private int errorCode=HttpStatus.BAD_REQUEST.value();
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public DataValidationException(String msg){
		this.message=msg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
