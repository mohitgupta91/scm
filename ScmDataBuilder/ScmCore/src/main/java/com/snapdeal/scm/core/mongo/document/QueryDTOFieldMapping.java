/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.core.mongo.document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.enums.QueryType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @version 1.0, 17-Feb-2016
 * @author ashwini
 */

@Document(collection = "query_dto_field_mapping")
public class QueryDTOFieldMapping extends MongoDocument {

    private QueryType                jobName;
    private String                 jobClass;
    private List<DtoQueryFieldMap> fieldMapping;

    public QueryType getJobName() {
        return jobName;
    }

    public void setJobName(QueryType jobName) {
        this.jobName = jobName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public List<DtoQueryFieldMap> getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(List<DtoQueryFieldMap> fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DtoFieldQueryFieldMapping [jobName=").append(jobName).append(", jobClass=").append(jobClass).append(", fieldMapping=").append(fieldMapping).append("]");
        return builder.toString();
    }

}
