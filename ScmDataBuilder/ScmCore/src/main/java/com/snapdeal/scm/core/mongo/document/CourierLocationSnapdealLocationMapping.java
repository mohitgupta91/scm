package com.snapdeal.scm.core.mongo.document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by harshit on 25/4/16.
 */
@Document(collection = "courier_location_snapdeal_location_mapping")
public class CourierLocationSnapdealLocationMapping extends MongoDocument {
    private String courierCode;
    private String locationCode;
    private String snapdealLocationCity;
    private String snapdealLocationState;
    private Boolean enabled;

    public CourierLocationSnapdealLocationMapping() {
    }

    public CourierLocationSnapdealLocationMapping(String courierCode, String locationCode, String snapdealLocationCity, String snapdealLocationState, Boolean enabled) {
        this.courierCode = courierCode;
        this.locationCode = locationCode;
        this.snapdealLocationCity = snapdealLocationCity;
        this.snapdealLocationState = snapdealLocationState;
        this.enabled = enabled;
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
}
