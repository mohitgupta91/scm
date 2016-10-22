package com.snapdeal.scm.web.validator;

import com.snapdeal.scm.web.core.request.SearchRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by vinay on 10/3/16.
 */
public class SearchRequestValidator implements Validator{

    @Override
    public boolean supports(Class<?> aClass) {
        return SearchRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        //ValidationUtils.rejectIfEmpty(errors, "filters", "filters.empty");
        SearchRequest searchRequest = (SearchRequest) o;
        //Perform validations on searchRequest
    }
}
