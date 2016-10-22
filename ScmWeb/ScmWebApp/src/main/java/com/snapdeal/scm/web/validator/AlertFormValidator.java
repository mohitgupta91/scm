package com.snapdeal.scm.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.snapdeal.scm.web.core.dto.AlertInstanceDTO;

/**
 * AlertFormValidator : Alert Form validator
 * 
 * @author pranav
 *
 */
public class AlertFormValidator implements Validator{

    @Override
    public boolean supports(Class<?> aClass) {
        return AlertInstanceDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
    	AlertInstanceDTO dto = (AlertInstanceDTO) o;

    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailId", "No email given");
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "alertInstanceTitle", "No AlertInstanceTitle Defined");
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "alertTitle", "No AlertTitle Defined");
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "operator", "No Operator Defined");
    	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "groupLogicName", "No GroupLogicName Defined");
    	
		if (dto.getCurrentDateRangeEnd() == 0
				|| dto.getCurrentDateRangeStart() == 0
				|| dto.getHistoricalDateRangeEnd() == 0
				|| dto.getHistoricalDateRangeStart() == 0) {
			errors.reject("Incorrect Date");
    	}
    		
    }
}
