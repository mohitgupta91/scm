package com.snapdeal.scm.core.dto.validator;

/**
 * @author prateek
 *
 */
public interface IDTOValidator {

	<T> boolean validate(T input);
	
}