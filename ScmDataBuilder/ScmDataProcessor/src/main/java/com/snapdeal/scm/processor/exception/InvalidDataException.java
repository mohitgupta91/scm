package com.snapdeal.scm.processor.exception;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.snapdeal.scm.core.dto.IStandardDTO;

/**
 * throw {@link InvalidDataException} exception in case data(i.e. variable of {@link IStandardDTO})
 *  is null or not as expected.
 * Here data is different from mandatory data.
 * For mandatory data put {@link NotBlank}, {@link NotEmpty}, etc. annotation on
 * respective DTO {@link IStandardDTO}.
 * 
 * @author prateek
 *
 */
public class InvalidDataException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4869888861639824326L;

	public InvalidDataException() {
	}
	
	public InvalidDataException(String message){
		super(message);
	}
}
