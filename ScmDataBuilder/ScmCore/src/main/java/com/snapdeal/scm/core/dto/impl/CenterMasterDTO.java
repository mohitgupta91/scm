package com.snapdeal.scm.core.dto.impl;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * @author prateek
 */
public class CenterMasterDTO extends AbstractStandardDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3287332877463877409L;

	@Order(order = 0)
	@NotBlank
	private String code;
	@Order(order = 1)
	private String name;
	@Order(order = 2)
	private Boolean enabled;
	@Order(order = 3)
	private String type;
	@Order(order = 4)
	@NotBlank
	private String pincode;
	@Order(order = 5)
	private String city;
	@Order(order = 6)
	private String state;
	@Order(order = 7)
	private Date updated;
	@Order(order = 8)
	private String scoreCity;
	@Order(order = 9)
	private String scoreTier;
	@Order(order = 10)
	private String scoreState;
	@Order(order = 11)
	private String logiopsCity;
	@Order(order = 12)
	private String logiopsState;
	@Order(order = 13)
	private String logiopsZone;
	@Order(order = 14)
	private String bdeZone;
	@Order(order = 15)
	private String scCity;
	@Order(order = 16)
	private String scState;
	@Order(order = 17)
	private String scZone;
	@Order(order = 18)
	private String shipNearZone;
	@Order(order = 19)
	private String centerType;

	public CenterMasterDTO() {
	}

	public CenterMasterDTO(String code, String name, Boolean enabled, String type, String pincode, String city, String state, Date updated, String scoreCity,
			String scoreTier, String scoreState, String logiopsCity, String logiopsState, String logiopsZone,
			String bdeZone, String scCity, String scState, String scZone, String shipNearZone, String centerType) {
		this.code = code;
		this.name = name;
		this.enabled = enabled;
		this.type = type;
		this.pincode = pincode;
		this.city = city;
		this.state = state;
		this.updated = updated;
		this.scoreCity = scoreCity;
		this.scoreTier = scoreTier;
		this.scoreState = scoreState;
		this.logiopsCity = logiopsCity;
		this.logiopsState = logiopsState;
		this.logiopsZone = logiopsZone;
		this.bdeZone = bdeZone;
		this.scCity = scCity;
		this.scState = scState;
		this.scZone = scZone;
		this.shipNearZone = shipNearZone;
		this.centerType = centerType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
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

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getScoreCity() {
		return scoreCity;
	}

	public void setScoreCity(String scoreCity) {
		this.scoreCity = scoreCity;
	}

	public String getScoreTier() {
		return scoreTier;
	}

	public void setScoreTier(String scoreTier) {
		this.scoreTier = scoreTier;
	}

	public String getScoreState() {
		return scoreState;
	}

	public void setScoreState(String scoreState) {
		this.scoreState = scoreState;
	}

	public String getLogiopsCity() {
		return logiopsCity;
	}

	public void setLogiopsCity(String logiopsCity) {
		this.logiopsCity = logiopsCity;
	}

	public String getLogiopsState() {
		return logiopsState;
	}

	public void setLogiopsState(String logiopsState) {
		this.logiopsState = logiopsState;
	}

	public String getLogiopsZone() {
		return logiopsZone;
	}

	public void setLogiopsZone(String logiopsZone) {
		this.logiopsZone = logiopsZone;
	}

	public String getBdeZone() {
		return bdeZone;
	}

	public void setBdeZone(String bdeZone) {
		this.bdeZone = bdeZone;
	}

	public String getScCity() {
		return scCity;
	}

	public void setScCity(String scCity) {
		this.scCity = scCity;
	}

	public String getScState() {
		return scState;
	}

	public void setScState(String scState) {
		this.scState = scState;
	}

	public String getScZone() {
		return scZone;
	}

	public void setScZone(String scZone) {
		this.scZone = scZone;
	}

	public String getShipNearZone() {
		return shipNearZone;
	}

	public void setShipNearZone(String shipNearZone) {
		this.shipNearZone = shipNearZone;
	}

	public String getCenterType() {
		return centerType;
	}

	public void setCenterType(String centerType) {
		this.centerType = centerType;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.CENTER_MASTER;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CenterMasterDTO [").append(code).append(", ").append(name).append(", ").append(enabled)
				.append(", ").append(type).append(", ").append(pincode).append(", ").append(city).append(", ").append(state).append(", ")
				.append(updated).append(", ").append(scoreCity).append(", ").append(scoreTier).append(", ")
				.append(scoreState).append(", ").append(logiopsCity).append(", ").append(logiopsState).append(", ")
				.append(logiopsZone).append(", ").append(bdeZone).append(", ").append(scCity).append(", ")
				.append(scState).append(", ").append(scZone).append(", ").append(shipNearZone).append(", ")
				.append(centerType).append("]");
		return builder.toString();
	}
	
	@Override
	public boolean validateDTO() {
		if(StringUtils.isEmpty(code) || StringUtils.isEmpty(pincode))
			return false;
		return true;
	}

}
