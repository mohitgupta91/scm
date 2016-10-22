package com.snapdeal.scm.core.poller.dto.impl;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.enums.QueryType;

/**
 * @author prateek
 */
public class PollerQueueDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8793233665048989225L;
	@NotBlank
	String    filePath;
	@NotNull
    QueryType queryType;

    public PollerQueueDto() {

    }

    public PollerQueueDto(String filePath, QueryType queryType) {
        this.filePath = filePath;
        this.queryType = queryType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PollerQueueDto [filePath=").append(filePath).append(", queryType=").append(queryType).append("]");
        return builder.toString();
    }

}
