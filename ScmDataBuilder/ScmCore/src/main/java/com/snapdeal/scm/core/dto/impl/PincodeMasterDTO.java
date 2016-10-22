package com.snapdeal.scm.core.dto.impl;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * @author prateek
 */
public class PincodeMasterDTO extends AbstractStandardDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 773800927546647531L;

    @Order(order = 0)
    @NotBlank
    private String pincode;
    @Order(order = 1)
    @NotBlank
    private String city;
    @Order(order = 2)
    @NotBlank
    private String state;
    @Order(order = 3)
    private String zone;
    @Order(order = 4)
    @NotBlank
    private String bdeZone;
    @Order(order = 5)
    @NotBlank
    private String scCity;
    @Order(order = 6)
    @NotBlank
    private String scState;
    @Order(order = 7)
    @NotBlank
    private String scZone;
    @Order(order = 8)
    private String ecomCity;
    @Order(order = 9)
    private String ecomState;
    @Order(order = 10)
    private String dvlCity;
    @Order(order = 11)
    private String dvlState;
    @Order(order = 12)
    private String bdCity;
    @Order(order = 13)
    private String bdState;
    @Order(order = 14)
    private String gjCity;
    @Order(order = 15)
    private String gjState;
    @Order(order = 16)
    private String redexCity;
    @Order(order = 17)
    private String redexState;

    public PincodeMasterDTO() {
    }

    public PincodeMasterDTO(String pincode, String city, String state, String zone, String bdeZone, String scCity, String scState, String scZone, String ecomCity, String ecomState,
            String dvlCity, String dvlState, String bdCity, String bdState, String gjCity, String gjState, String redexCity, String redexState) {
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.zone = zone;
        this.bdeZone = bdeZone;
        this.scCity = scCity;
        this.scState = scState;
        this.scZone = scZone;
        this.ecomCity = ecomCity;
        this.ecomState = ecomState;
        this.dvlCity = dvlCity;
        this.dvlState = dvlState;
        this.bdCity = bdCity;
        this.bdState = bdState;
        this.gjCity = gjCity;
        this.gjState = gjState;
        this.redexCity = redexCity;
        this.redexState = redexState;
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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
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

    public String getEcomCity() {
        return ecomCity;
    }

    public void setEcomCity(String ecomCity) {
        this.ecomCity = ecomCity;
    }

    public String getEcomState() {
        return ecomState;
    }

    public void setEcomState(String ecomState) {
        this.ecomState = ecomState;
    }

    public String getDvlCity() {
        return dvlCity;
    }

    public void setDvlCity(String dvlCity) {
        this.dvlCity = dvlCity;
    }

    public String getDvlState() {
        return dvlState;
    }

    public void setDvlState(String dvlState) {
        this.dvlState = dvlState;
    }

    public String getBdCity() {
        return bdCity;
    }

    public void setBdCity(String bdCity) {
        this.bdCity = bdCity;
    }

    public String getBdState() {
        return bdState;
    }

    public void setBdState(String bdState) {
        this.bdState = bdState;
    }

    public String getGjCity() {
        return gjCity;
    }

    public void setGjCity(String gjCity) {
        this.gjCity = gjCity;
    }

    public String getGjState() {
        return gjState;
    }

    public void setGjState(String gjState) {
        this.gjState = gjState;
    }

    public String getRedexCity() {
        return redexCity;
    }

    public void setRedexCity(String redexCity) {
        this.redexCity = redexCity;
    }

    public String getRedexState() {
        return redexState;
    }

    public void setRedexState(String redexState) {
        this.redexState = redexState;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.PINCODE_MASTER;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PincodeMasterDTO [");
        builder.append(pincode).append(StringUtils.COMMA).append(city).append(StringUtils.COMMA).append(state).append(StringUtils.COMMA).append(zone).append(
                StringUtils.COMMA).append(bdeZone).append(StringUtils.COMMA).append(scCity).append(StringUtils.COMMA).append(scState).append(StringUtils.COMMA).append(
                        scZone).append(StringUtils.COMMA).append(ecomCity).append(StringUtils.COMMA).append(ecomState).append(StringUtils.COMMA).append(dvlCity).append(
                                StringUtils.COMMA).append(dvlState).append(StringUtils.COMMA).append(bdCity).append(StringUtils.COMMA).append(bdState).append(
                                        StringUtils.COMMA).append(gjCity).append(StringUtils.COMMA).append(gjState).append(StringUtils.COMMA).append(redexCity).append(
                                                StringUtils.COMMA).append(redexState);
        builder.append("]");
        return builder.toString();
    }
    
    @Override
	public boolean validateDTO() {
		if(StringUtils.isEmpty(pincode) || StringUtils.isEmpty(city) || StringUtils.isEmpty(state)|| StringUtils.isEmpty(bdeZone)
				|| StringUtils.isEmpty(scState) || StringUtils.isEmpty(scCity) || StringUtils.isEmpty(scZone))
			return false;
		return true;
	}

}
