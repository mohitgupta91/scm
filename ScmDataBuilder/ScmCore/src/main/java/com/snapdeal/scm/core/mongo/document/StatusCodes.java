package com.snapdeal.scm.core.mongo.document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author prateek
 */
@Document(collection = "status_codes")
public class StatusCodes extends MongoDocument {

    private String         statusCode;
    private String         description;
    private Integer        statusCodeId;
    private StatusCodeType statusCodeType;

    public StatusCodes() {

    }

    public StatusCodes(String statusCode, String description, Integer statusCodeId, StatusCodeType statusCodeType) {
        this.statusCode = statusCode;
        this.description = description;
        this.statusCodeId = statusCodeId;
        this.statusCodeType = statusCodeType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatusCodeId() {
        return statusCodeId;
    }

    public void setStatusCodeId(Integer statusCodeId) {
        this.statusCodeId = statusCodeId;
    }

    public StatusCodeType getStatusCodeType() {
        return statusCodeType;
    }

    public void setStatusCodeType(StatusCodeType statusCodeType) {
        this.statusCodeType = statusCodeType;
    }

    @Override
    public String toString() {
        return "StatusCodes [statusCode=" + statusCode + ", description=" + description + ", statusCodeId=" + statusCodeId + ", statusCodeType=" + statusCodeType + "]";
    }

    public enum StatusCodeType {
        SHIPPING_PACKAGE, SHIPPING_ORDER_ITEM, TRACKING_PACKAGE;
    }

}
