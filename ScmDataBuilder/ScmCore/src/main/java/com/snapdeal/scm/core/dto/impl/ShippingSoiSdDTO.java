package com.snapdeal.scm.core.dto.impl;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author prateek
 */
public class ShippingSoiSdDTO extends AbstractStandardDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 8881739746600293777L;

    @Order(order = 0)
    @NotBlank
    private String subOrderCode;
    @Order(order = 1)
    @NotBlank
    private String supc;
    @Order(order = 2)
    @NotBlank
    private String customerDestinationPincode;
    @Order(order = 3)
    private String centerCode;
    @Order(order = 4)
    @NotBlank
    private String fullfilmentModel;
    @Order(order = 5)
    @NotNull
    private Date   orderCreatedDate;
    @Order(order = 6)
    private String vendorCode;
    @Order(order = 7)
    private Boolean onHold;
    @Order(order = 8)
    private Double itemPrice;
    @Order(order = 9)
    private String paymentMode;
    @Order(order = 10)
    private Date created;

    public ShippingSoiSdDTO() {
    }

    public ShippingSoiSdDTO(String subOrderCode, String supc, String customerDestinationPincode, String centerCode,
                            String fullfilmentModel, Date orderCreatedDate, String vendorCode, Boolean onHold) {
        this.subOrderCode = subOrderCode;
		this.supc = supc;
		this.customerDestinationPincode = customerDestinationPincode;
		this.centerCode = centerCode;
		this.fullfilmentModel = fullfilmentModel;
		this.orderCreatedDate = orderCreatedDate;
		this.vendorCode = vendorCode;
        this.onHold = onHold;
    }

    //TODO: remove one of the constructor
    public ShippingSoiSdDTO(String supc, String subOrderCode, String customerDestinationPincode, String centerCode, String fullfilmentModel, Date orderCreatedDate, String vendorCode, Boolean onHold, Double itemPrice, String shippingMethodCode) {
        this.supc = supc;
        this.subOrderCode = subOrderCode;
        this.customerDestinationPincode = customerDestinationPincode;
        this.centerCode = centerCode;
        this.fullfilmentModel = fullfilmentModel;
        this.orderCreatedDate = orderCreatedDate;
        this.vendorCode = vendorCode;
        this.onHold = onHold;
        this.itemPrice = itemPrice;
        this.paymentMode = shippingMethodCode;
    }

    public ShippingSoiSdDTO(String subOrderCode, String supc, String customerDestinationPincode, String centerCode, String fullfilmentModel, Date orderCreatedDate, String vendorCode, Boolean onHold, Double itemPrice, String paymentMode, Date created) {
        this.subOrderCode = subOrderCode;
        this.supc = supc;
        this.customerDestinationPincode = customerDestinationPincode;
        this.centerCode = centerCode;
        this.fullfilmentModel = fullfilmentModel;
        this.orderCreatedDate = orderCreatedDate;
        this.vendorCode = vendorCode;
        this.onHold = onHold;
        this.itemPrice = itemPrice;
        this.paymentMode = paymentMode;
        this.created = created;
    }

    public String getSubOrderCode() {
        return subOrderCode;
    }

    public void setSubOrderCode(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public String getSupc() {
        return supc;
    }

    public void setSupc(String supc) {
        this.supc = supc;
    }

    public String getCustomerDestinationPincode() {
        return customerDestinationPincode;
    }

    public void setCustomerDestinationPincode(String customerDestinationPincode) {
        this.customerDestinationPincode = customerDestinationPincode;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getFullfilmentModel() {
        return fullfilmentModel;
    }

    public void setFullfilmentModel(String fullfilmentModel) {
        this.fullfilmentModel = fullfilmentModel;
    }

    public Date getOrderCreatedDate() {
        return orderCreatedDate;
    }

    public void setOrderCreatedDate(Date orderCreatedDate) {
        this.orderCreatedDate = orderCreatedDate;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public Boolean getOnHold() {
        return onHold;
    }

    public void setOnHold(Boolean onHold) {
        this.onHold = onHold;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ShippingSoiSdDTO{");
        sb.append("subOrderCode='").append(subOrderCode).append('\'');
        sb.append(", supc='").append(supc).append('\'');
        sb.append(", customerDestinationPincode='").append(customerDestinationPincode).append('\'');
        sb.append(", centerCode='").append(centerCode).append('\'');
        sb.append(", fullfilmentModel='").append(fullfilmentModel).append('\'');
        sb.append(", orderCreatedDate=").append(orderCreatedDate);
        sb.append(", vendorCode='").append(vendorCode).append('\'');
        sb.append(", onHold=").append(onHold);
        sb.append(", itemPrice=").append(itemPrice);
        sb.append(", paymentMode='").append(paymentMode).append('\'');
        sb.append(", created=").append(created);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SHIPPING_SOI_SD;
    }
    
    @Override
	public boolean validateDTO() {
		if(StringUtils.isEmpty(subOrderCode) || StringUtils.isEmpty(supc) || StringUtils.isEmpty(customerDestinationPincode)|| StringUtils.isEmpty(fullfilmentModel)
				|| orderCreatedDate == null)
			return false;
		return true;
	}
}
