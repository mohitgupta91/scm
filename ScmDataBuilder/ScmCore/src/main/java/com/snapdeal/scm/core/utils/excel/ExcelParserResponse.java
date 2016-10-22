package com.snapdeal.scm.core.utils.excel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author chitransh
 */
public class ExcelParserResponse<T> implements Serializable {

    private static final long serialVersionUID = -2100252939026643322L;

    private boolean successful;
    private List<T> records;
    private String error;

    public ExcelParserResponse() {
        this.successful = false;
        this.records = new LinkedList<>();
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<T> getRecords() {
        return records;
    }

    public void addRecord(T dto) {
        records.add(dto);
    }
}
