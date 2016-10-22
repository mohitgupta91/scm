package com.snapdeal.scm.core.dto.impl;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * @author prateek
 */
public class CourierGroupDTO extends AbstractStandardDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 4925181635350615223L;

    @Order(order = 0)
    private Integer id;
    @Order(order = 1)
    private Integer shippingProvideSid;
    @Order(order = 2)
    private String  name;
    @Order(order = 3)
    @NotBlank
    private String  code;
    @Order(order = 4)
    private String  shippingModeCode;
    @Order(order = 5)
    private String  transitTypeCode;
    @Order(order = 6)
    @NotBlank
    private String  courierType;
    @Order(order = 7)
    @NotBlank
    private String  courierGroup;
    @Order(order = 8)
    private String  wmsRlCode;
    @Order(order = 9)
    private Boolean integrated;

    public CourierGroupDTO() {
    }

    public CourierGroupDTO(Integer id, Integer shippingProvideSid, String name, String code, String shippingModeCode, String transitTypeCode, String courierType,
            String courierGroup, String wmsRlCode, Boolean integrated) {
        this.id = id;
        this.shippingProvideSid = shippingProvideSid;
        this.name = name;
        this.code = code;
        this.shippingModeCode = shippingModeCode;
        this.transitTypeCode = transitTypeCode;
        this.courierType = courierType;
        this.courierGroup = courierGroup;
        this.wmsRlCode = wmsRlCode;
        this.integrated = integrated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShippingProvideSid() {
        return shippingProvideSid;
    }

    public void setShippingProvideSid(Integer shippingProvideSid) {
        this.shippingProvideSid = shippingProvideSid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShippingModeCode() {
        return shippingModeCode;
    }

    public void setShippingModeCode(String shippingModeCode) {
        this.shippingModeCode = shippingModeCode;
    }

    public String getTransitTypeCode() {
        return transitTypeCode;
    }

    public void setTransitTypeCode(String transitTypeCode) {
        this.transitTypeCode = transitTypeCode;
    }

    public String getCourierType() {
        return courierType;
    }

    public void setCourierType(String courierType) {
        this.courierType = courierType;
    }

    public String getCourierGroup() {
        return courierGroup;
    }

    public void setCourierGroup(String courierGroup) {
        this.courierGroup = courierGroup;
    }

    public String getWmsRlCode() {
        return wmsRlCode;
    }

    public void setWmsRlCode(String wmsRlCode) {
        this.wmsRlCode = wmsRlCode;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.COURIER_GROUP;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CourierGroupDTO [").append(id).append(", ").append(shippingProvideSid).append(", ").append(name).append(", ").append(code).append(", ").append(
                shippingModeCode).append(", ").append(transitTypeCode).append(", ").append(courierType).append(", ").append(courierGroup).append(", ").append(wmsRlCode).append(", ").append(integrated).append(
                        "]");
        return builder.toString();
    }
    
    /**
	 * @return the integrated
	 */
	public Boolean getIntegrated() {
		return integrated;
	}

	/**
	 * @param integrated the integrated to set
	 */
	public void setIntegrated(Boolean integrated) {
		this.integrated = integrated;
	}

	@Override
	public boolean validateDTO() {
		if(StringUtils.isEmpty(code) || StringUtils.isEmpty(courierGroup) || StringUtils.isEmpty(courierType))
			return false;
		return true;
	}

}
