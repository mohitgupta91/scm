package com.snapdeal.scm.web.core.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.snapdeal.scm.web.core.validation.SCMError;

/**
 * Created by Harsh Gupta on 24/02/16.
 */
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -7191710901031348258L;

    private int code;

    private String message;

    private List<SCMError> errors;
    
    private T data;
    
    public BaseResponse(){
    	
    }
    
    public BaseResponse(int code, String message, List<SCMError> errors, T data) {
		super();
		this.code = code;
		this.message = message;
		this.errors = errors;
		this.data = data;
	}

	public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SCMError> getErrors() {
        return errors;
    }

    public void setErrors(List<SCMError> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @SuppressWarnings("rawtypes")
	public BaseResponse addError(SCMError error) {
        if (this.errors == null) {
            this.errors = new ArrayList<SCMError>();
        }
        this.errors.add(error);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public BaseResponse addErrors(List<SCMError> errors) {
        if (errors != null) {
            if (this.errors == null) {
                this.errors = new ArrayList<SCMError>();
            }
            this.errors.addAll(errors);
        }
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseResponse{");
        sb.append("code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append(", errors=").append(errors);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
