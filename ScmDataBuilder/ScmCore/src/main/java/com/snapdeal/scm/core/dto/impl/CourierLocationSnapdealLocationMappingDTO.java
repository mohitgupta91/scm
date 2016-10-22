package com.snapdeal.scm.core.dto.impl;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by harshit on 25/4/16.
 */
public class CourierLocationSnapdealLocationMappingDTO extends AbstractStandardDTO {

    @Order(order = 0)
    private Integer id;
    @Order(order = 1)
    @NotBlank
    private String courierCode;
    @Order(order = 2)
    @NotBlank
    private String locationCode;
    @Order(order = 3)
    @NotBlank
    private String snapdealLocationCity;
    @Order(order = 4)
    @NotBlank
    private String snapdealLocationState;
    @Order(order = 5)
    private Boolean enabled;

    public CourierLocationSnapdealLocationMappingDTO() {
    }

    public CourierLocationSnapdealLocationMappingDTO(Integer id, String courierCode, String locationCode, String snapdealLocationCity, String snapdealLocationState, Boolean enabled) {
        this.id = id;
        this.courierCode = courierCode;
        this.locationCode = locationCode;
        this.snapdealLocationCity = snapdealLocationCity;
        this.snapdealLocationState = snapdealLocationState;
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourierCode() {
        return courierCode;
    }

    public void setCourierCode(String courierCode) {
        this.courierCode = courierCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getSnapdealLocationCity() {
        return snapdealLocationCity;
    }

    public void setSnapdealLocationCity(String snapdealLocationCity) {
        this.snapdealLocationCity = snapdealLocationCity;
    }

    public String getSnapdealLocationState() {
        return snapdealLocationState;
    }

    public void setSnapdealLocationState(String snapdealLocationState) {
        this.snapdealLocationState = snapdealLocationState;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.COURIER_LOCATION_SNAPDEAL_LOCATION_MAPPING;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CourierLocationSnapdealLocationMappingDTO{");
        sb.append("id=").append(id);
        sb.append(", courierCode='").append(courierCode).append('\'');
        sb.append(", locationCode='").append(locationCode).append('\'');
        sb.append(", snapdealLocationCity='").append(snapdealLocationCity).append('\'');
        sb.append(", snapdealLocationState='").append(snapdealLocationState).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean validateDTO() {
		// TODO Auto-generated method stub
		return false;
	}
}
