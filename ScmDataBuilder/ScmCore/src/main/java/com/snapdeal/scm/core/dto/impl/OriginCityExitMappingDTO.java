package com.snapdeal.scm.core.dto.impl;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by harshit on 22/4/16.
 */
public class OriginCityExitMappingDTO extends AbstractStandardDTO {

    @Order(order = 0)
    @NotBlank
    private String courierGroup;
    @Order(order = 1)
    @NotBlank
    private String shippingMode;
    @Order(order = 2)
    @NotBlank
    private String firstLocationCity;
    @Order(order = 3)
    @NotBlank
    private String firstLocationState;
    @Order(order = 4)
    private String courierStatus;
    @Order(order = 5)
    private String courierRemarks;
    @Order(order = 6)
    private String snapdealStatus;
    @Order(order = 7)
    private String firstLocation;
    @Order(order = 8)
    private String destinationCity;
    @Order(order = 9)
    private String destinationState;
    @Order(order = 10)
    @NotBlank
    private String originCityExit;

    /**
	 * @return the originCityExit
	 */
	public String getOriginCityExit() {
		return originCityExit;
	}

	/**
	 * @param originCityExit the originCityExit to set
	 */
	public void setOriginCityExit(String originCityExit) {
		this.originCityExit = originCityExit;
	}

	public OriginCityExitMappingDTO() {
    }

    public OriginCityExitMappingDTO(String courierGroup, String shippingMode, String firstLocationCity, 
    		String firstLocationState, String courierStatus, String courierRemarks, String snapdealStatus, 
    		String firstLocation, String destinationCity, String destinationState, String originCityExit) {
        this.courierGroup = courierGroup;
        this.shippingMode = shippingMode;
        this.firstLocationCity = firstLocationCity;
        this.firstLocationState = firstLocationState;
        this.courierStatus = courierStatus;
        this.courierRemarks = courierRemarks;
        this.snapdealStatus = snapdealStatus;
        this.firstLocation = firstLocation;
        this.destinationCity = destinationCity;
        this.destinationState = destinationState;
        this.originCityExit = originCityExit;
    }

    public String getCourierGroup() {
        return courierGroup;
    }

    public void setCourierGroup(String courierGroup) {
        this.courierGroup = courierGroup;
    }

    public String getShippingMode() {
        return shippingMode;
    }

    public void setShippingMode(String shippingMode) {
        this.shippingMode = shippingMode;
    }

    public String getFirstLocationCity() {
        return firstLocationCity;
    }

    public void setFirstLocationCity(String firstLocationCity) {
        this.firstLocationCity = firstLocationCity;
    }

    public String getFirstLocationState() {
        return firstLocationState;
    }

    public void setFirstLocationState(String firstLocationState) {
        this.firstLocationState = firstLocationState;
    }

    public String getCourierStatus() {
        return courierStatus;
    }

    public void setCourierStatus(String courierStatus) {
        this.courierStatus = courierStatus;
    }

    public String getCourierRemarks() {
        return courierRemarks;
    }

    public void setCourierRemarks(String courierRemarks) {
        this.courierRemarks = courierRemarks;
    }

    public String getSnapdealStatus() {
        return snapdealStatus;
    }

    public void setSnapdealStatus(String snapdealStatus) {
        this.snapdealStatus = snapdealStatus;
    }

    public String getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(String firstLocation) {
        this.firstLocation = firstLocation;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDestinationState() {
        return destinationState;
    }

    public void setDestinationState(String destinationState) {
        this.destinationState = destinationState;
    }

    
    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OriginCityExitMappingDTO [courierGroup=" + courierGroup
				+ ", shippingMode=" + shippingMode + ", firstLocationCity="
				+ firstLocationCity + ", firstLocationState="
				+ firstLocationState + ", courierStatus=" + courierStatus
				+ ", courierRemarks=" + courierRemarks + ", snapdealStatus="
				+ snapdealStatus + ", firstLocation=" + firstLocation
				+ ", destinationCity=" + destinationCity
				+ ", destinationState=" + destinationState
				+ ", originCityExit=" + originCityExit + "]";
	}

	@Override
    public QueryType getQueryType() {
        return QueryType.ORIGIN_CITY_EXIT_MAPPING;
    }

    @Override
    public boolean validateDTO() {
		// TODO Auto-generated method stub
		return false;
	}
}
