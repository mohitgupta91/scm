/**
 * 
 */
package com.snapdeal.scm.poller.dto;

import java.util.concurrent.Future;

import com.snapdeal.scm.core.enums.QueryType;

/**
 * @author gaurav on 28-Feb-2016
 */
public class JobFuture {

    QueryType queryType;

    Future<Boolean> status;

    /**
     * @return the queryType
     */
    public QueryType getQueryType() {
        return queryType;
    }

    /**
     * @param queryType the queryType to set
     */
    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    /**
     * @return the status
     */
    public Future<Boolean> getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Future<Boolean> status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "JobFuture [queryType=" + queryType + ", status=" + status + "]";
    }

}
