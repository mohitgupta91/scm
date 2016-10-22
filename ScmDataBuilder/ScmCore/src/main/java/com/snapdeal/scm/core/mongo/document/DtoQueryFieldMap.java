/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.core.mongo.document;

/**
 * @version 1.0, 18-Feb-2016
 * @author ashwini
 */
public class DtoQueryFieldMap {

    private String  field;
    private String  fieldType;
    private Integer queryFieldIndex;

    public DtoQueryFieldMap() {
    }

    public DtoQueryFieldMap(String field, String fieldType, Integer queryFieldIndex) {
        super();
        this.field = field;
        this.fieldType = fieldType;
        this.queryFieldIndex = queryFieldIndex;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getQueryFieldIndex() {
        return queryFieldIndex;
    }

    public void setQueryFieldIndex(Integer queryFieldIndex) {
        this.queryFieldIndex = queryFieldIndex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DtoQueryFieldMap [field=").append(field).append(", fieldType=").append(fieldType).append(", queryFieldIndex=").append(queryFieldIndex).append("]");
        return builder.toString();
    }

}
