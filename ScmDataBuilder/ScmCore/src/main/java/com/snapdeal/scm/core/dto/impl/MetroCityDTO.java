package com.snapdeal.scm.core.dto.impl;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * 
 * @author prateek
 *
 */
public class MetroCityDTO extends AbstractStandardDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private String city;
	@NotBlank
	private String state;
	
	public MetroCityDTO() {
	}

	public MetroCityDTO(String city, String state) {
		this.city = city;
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.METRO_CITY;
	}
	
	@Override
	public boolean validateDTO() {
		if(StringUtils.isEmpty(city) || StringUtils.isEmpty(state))
			return false;
		return true;
	}
}