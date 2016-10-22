package com.snapdeal.scm.processor.exception;

/**
 * throw {@link InsufficientDataException} in case data is not found in database or cache
 * 
 * @author prateek
 *
 */
public class InsufficientDataException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2213166899961360893L;

	public InsufficientDataException(){
	}

	public InsufficientDataException(String message){
		super(message);
	}

}
