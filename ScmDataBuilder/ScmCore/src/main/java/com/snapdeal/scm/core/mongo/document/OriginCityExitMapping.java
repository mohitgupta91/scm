package com.snapdeal.scm.core.mongo.document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by harshit on 22/4/16.
 */
@Document(collection = "origin_city_exit_mapping")
public class OriginCityExitMapping extends MongoDocument {
    private String courierGroup;
    private String shippingMode;
    private String firstLocationCity;
    private String firstLocationState;
    private String courierStatus;
    private String courierRemarks;
    private String snapdealStatus;
    private String firstLocation;
    private String destinationCity;
    private String destinationState;
    private String originCityExit;

    public OriginCityExitMapping() {
    }


    public OriginCityExitMapping(String courierGroup, String shippingMode, String firstLocationCity, String firstLocationState, String courierStatus, String courierRemarks, String snapdealStatus, String firstLocation, String destinationCity, String destinationState, String originCityExit) {
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

    public String getOriginCityExit() {
        return originCityExit;
    }

    public void setOriginCityExit(String originCityExit) {
        this.originCityExit = originCityExit;
    }
}
