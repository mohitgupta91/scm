package com.snapdeal.scm.core.dto.impl;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * @author prateek
 */
public class VendorContactDTO extends AbstractStandardDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 780952096472398196L;

	@Order(order = 0)
	@NotBlank
	private String pincode;
	@Order(order = 1)
	@NotBlank
	private String vendorCode;

	public VendorContactDTO() {
	}

	public VendorContactDTO(String pincode, String vendorCode) {
		this.pincode = pincode;
		this.vendorCode = vendorCode;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.VENDOR_CONTACT;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VendorContactDTO [").append(pincode).append(", ").append(vendorCode).append("]");
		return builder.toString();
	}

	@Override
	public boolean validateDTO() {
		if(StringUtils.isEmpty(pincode) || StringUtils.isEmpty(vendorCode))
			return false;
		return true;
	}
}
