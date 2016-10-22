package com.snapdeal.scm.core.dto.impl;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * @author prateek
 */
public class ShippingSoiSoidDTO extends AbstractStandardDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 7214933656400796323L;

    @Order(order = 0)
    @NotBlank
    private String subOrderCode;
    @Order(order = 1)
    private String attributeName;
    @Order(order = 2)
    @NotBlank
    private String attributeValue;

    public ShippingSoiSoidDTO() {

    }

    public ShippingSoiSoidDTO(String subOrderCode, String attributeName, String attributeValue) {
        this.subOrderCode = subOrderCode;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public String getSubOrderCode() {
        return subOrderCode;
    }

    public void setSubOrderCode(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ShippingSoiSoidDTO [").append(subOrderCode).append(", ").append(attributeName).append(", ").append(attributeValue).append("]");
        return builder.toString();
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SHIPPING_SOI_SOID;
    }

    @Override
    public boolean validateDTO() {
		if(StringUtils.isEmpty(subOrderCode) || StringUtils.isEmpty(attributeName) || StringUtils.isEmpty(attributeValue))
			return false;
		return true;
	}
}
