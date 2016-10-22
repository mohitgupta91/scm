package com.snapdeal.scm.core.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * @author chitransh
 */
public class ServiceResponse<T> {

    private boolean successful;
    private List<String> warnings;
    private List<String> errors;
    private T data;

    public ServiceResponse() {
        this.successful = false;
        this.warnings = new LinkedList<>();
        this.errors = new LinkedList<>();
    }

    public ServiceResponse(boolean successful, List<String> warnings, List<String> errors) {
        this.successful = successful;
        this.warnings = warnings;
        this.errors = errors;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

}
