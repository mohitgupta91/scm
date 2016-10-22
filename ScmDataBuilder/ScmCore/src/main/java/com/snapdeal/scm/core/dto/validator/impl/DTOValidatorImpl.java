package com.snapdeal.scm.core.dto.validator.impl;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.scm.core.dto.validator.IDTOValidator;

/**
 * 
 * @author prateek
 *
 */
@Service("DTOValidatorImpl")
public class DTOValidatorImpl implements IDTOValidator{
	private static final Logger errorLOG = LoggerFactory.getLogger("DATAPROCESSOR-ERROR-LOGGER");

	@Autowired
	private Validator validator;

	@Override
	public <T> boolean validate(T input) {
		Set<ConstraintViolation<T>> violations = validator.validate(input);
		violations.forEach(violation -> printErrorLog(violation));
		return violations.isEmpty();
	}

	private <T> void printErrorLog(ConstraintViolation<T> violation){
		errorLOG.error("{} : {} {}",violation.getRootBeanClass().getSimpleName(), violation.getPropertyPath(), violation.getMessage());
	}
}
