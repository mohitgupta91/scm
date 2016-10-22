package com.snapdeal.scm.core.dto.impl;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by harshit on 22/4/16.
 */
public class PincodeDCMappingDTO extends AbstractStandardDTO {

    @Order(order = 0)
    @NotBlank
    private String pincode;
    @Order(order = 1)
    @NotBlank
    private String courierGroup;
    @Order(order = 2)
    @NotBlank
    private String shippingModeCode;
    @Order(order = 3)
    private String deliveryCenter;
    @Order(order = 4)
    private String dcCity;
    @Order(order = 5)
    private String dcState;


    public PincodeDCMappingDTO() {
    }

    public PincodeDCMappingDTO(String pincode, String courierGroup, String shippingModeCode, String deliveryCenter, String dcCity, String dcState) {
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

    @Override
    public QueryType getQueryType() {
        return QueryType.PINCODE_DC_MAPPING;
    }

    @Override
    public String toString() {
        return "PincodeDCMappingDTO{" +
                "pincode='" + pincode + '\'' +
                ", courierGroup='" + courierGroup + '\'' +
                ", shippingModeCode='" + shippingModeCode + '\'' +
                ", deliveryCenter='" + deliveryCenter + '\'' +
                ", dcCity='" + dcCity + '\'' +
                ", dcState='" + dcState + '\'' +
                '}';
    }

	@Override
	public boolean validateDTO() {
		// TODO Auto-generated method stub
		return false;
	}
}
