package com.snapdeal.scm.core.mongo.document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by harshit on 22/4/16.
 */
@Document(collection = "pincode_dc_mapping")
public class PincodeDCMapping extends MongoDocument {
    private String pincode;
    private String courierGroup;
    private String shippingModeCode;
    private String deliveryCenter;
    private String dcCity;
    private String dcState;

    public PincodeDCMapping() {
    }

    public PincodeDCMapping(String pincode, String courierGroup, String shippingModeCode, String deliveryCenter, String dcCity, String dcState) {
        this.pincode = pincode;
        this.courierGroup = courierGroup;
        this.shippingModeCode = shippingModeCode;
        this.deliveryCenter = deliveryCenter;
        this.dcCity = dcCity;
        this.dcState = dcState;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCourierGroup() {
        return courierGroup;
    }

    public void setCourierGroup(String courierGroup) {
        this.courierGroup = courierGroup;
    }

    public String getShippingModeCode() {
        return shippingModeCode;
    }

    public void setShippingModeCode(String shippingModeCode) {
        this.shippingModeCode = shippingModeCode;
    }

    public String getDeliveryCenter() {
        return deliveryCenter;
    }

    public void setDeliveryCenter(String deliveryCenter) {
        this.deliveryCenter = deliveryCenter;
    }

    public String getDcCity() {
        return dcCity;
    }

    public void setDcCity(String dcCity) {
        this.dcCity = dcCity;
    }

    public String getDcState() {
        return dcState;
    }

    public void setDcState(String dcState) {
        this.dcState = dcState;
    }
}
