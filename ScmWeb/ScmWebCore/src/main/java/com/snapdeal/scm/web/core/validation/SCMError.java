package com.snapdeal.scm.web.core.validation;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Harsh Gupta on 24/02/16.
 */
public class SCMError implements Serializable {

    /**
     * 
     */
    private static final long   serialVersionUID = 3592593224648142491L;
    private int                 code;
    private String              fieldName;
    private String              description;
    private String              message;
    private Map<String, Object> errorParams;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getErrorParams() {
        return errorParams;
    }

    public void setErrorParams(Map<String, Object> errorParams) {
        this.errorParams = errorParams;
    }

    @Override
    public String toString() {
        return "SCMError{" + "code=" + code + ", fieldName='" + fieldName + '\'' + ", description='" + description + '\'' + ", message='" + message + '\'' + ", errorParams="
                + errorParams + '}';
    }
}
