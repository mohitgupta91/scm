package com.snapdeal.scm.mongo.doc;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author prateek
 */
@Document(collection = "fulfillment_provider")
public class FulfillmentProvider extends MongoDocument {

	private String     code;
	private String     pincode;
	private FulfillmentType fulfillmentType;

    public FulfillmentProvider() {

    }

    public FulfillmentProvider(String code, String pincode, FulfillmentType fulfillmentType) {
        this.code = code;
        this.pincode = pincode;
        this.fulfillmentType = fulfillmentType;
    }

    public FulfillmentProvider(String code, FulfillmentType fulfillmentType) {
    	this.code = code;
    	this.fulfillmentType = fulfillmentType;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public FulfillmentType getFulfillmentType() {
        return fulfillmentType;
    }

    public void setFulfillmentType(FulfillmentType fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
    }

    @Override
    public String toString() {
        return "CenterMaster [code=" + code + ", pincode=" + pincode + ", fulfillmentType=" + fulfillmentType + "]";
    }

    public enum FulfillmentType {
        CENTER,
        VENDOR
    }

}
