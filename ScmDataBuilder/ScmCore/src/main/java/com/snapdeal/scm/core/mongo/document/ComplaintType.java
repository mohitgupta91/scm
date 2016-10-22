package com.snapdeal.scm.core.mongo.document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by harshit.nimbark on 5/30/16.
 */
@Document(collection = "complaint_type")
public class ComplaintType extends MongoDocument{
    private String code;
    private String complaintCategory;


    public ComplaintType() {
    }

    public ComplaintType(String code, String complaintCategory) {
        this.code = code;
        this.complaintCategory = complaintCategory;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComplaintCategory() {
        return complaintCategory;
    }

    public void setComplaintCategory(String complaintCategory) {
        this.complaintCategory = complaintCategory;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ComplaintType{");
        sb.append("code='").append(code).append('\'');
        sb.append(", complaintCategory='").append(complaintCategory).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
