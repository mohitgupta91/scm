package com.snapdeal.scm.core.dto.impl;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * @author prateek
 */
public class ShippingSoiSpDTO extends AbstractStandardDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -6956756331324732659L;

    @Order(order = 0)
    @NotBlank
    private String subOrderCode;
    @Order(order = 1)
    private String courierCode;
    @Order(order = 2)
    private Date   expectedDeliveryDate;
    @Order(order = 3)
    private Date   expectedDeliveryDateRangeStart;
    public ShippingSoiSpDTO() {

    }

    public ShippingSoiSpDTO(String subOrderCode, String courierCode, Date expectedDeliveryDate, Date expectedDeliveryDateRangeStart) {
		this.subOrderCode = subOrderCode;
		this.courierCode = courierCode;
		this.expectedDeliveryDate = expectedDeliveryDate;
		this.expectedDeliveryDateRangeStart = expectedDeliveryDateRangeStart;
	}

	public String getSubOrderCode() {
		return subOrderCode;
	}

	public void setSubOrderCode(String subOrderCode) {
		this.subOrderCode = subOrderCode;
	}

	public String getCourierCode() {
		return courierCode;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}

	public Date getExpectedDeliveryDate() {
		return expectedDeliveryDate;
	}

	public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	public Date getExpectedDeliveryDateRangeStart() {
		return expectedDeliveryDateRangeStart;
	}

	public void setExpectedDeliveryDateRangeStart(
			Date expectedDeliveryDateRangeStart) {
		this.expectedDeliveryDateRangeStart = expectedDeliveryDateRangeStart;
	}

	@Override
    public QueryType getQueryType() {
        return QueryType.SHIPPING_SOI_SP;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ShippingSoiSpDTO [").append(subOrderCode).append(", ").append(courierCode).append(", ").append(expectedDeliveryDate).append(", ")
        .append(expectedDeliveryDateRangeStart).append("]");
        return builder.toString();
    }
    
    @Override
    public boolean validateDTO() {
		if(StringUtils.isEmpty(subOrderCode) || (StringUtils.isEmpty(courierCode) && 
				(expectedDeliveryDate == null || expectedDeliveryDateRangeStart == null)))
			return false;
		return true;
	}
}