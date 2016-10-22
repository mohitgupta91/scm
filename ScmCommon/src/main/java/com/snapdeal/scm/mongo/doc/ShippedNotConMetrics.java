package com.snapdeal.scm.mongo.doc;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chitransh
 */
@Document(collection = "shipped_not_connected_metrics")
public class ShippedNotConMetrics extends MongoDocument {

    private String   courierGroup;
    private String   courierType;
    private String   originState;
    private String   originCity;
    private TimeUnit timeUnit;
    private Long     limit1;
    private Long     limit2;

    public ShippedNotConMetrics() {
    }

    public ShippedNotConMetrics(String courierGroup, String courierType, String originState, String originCity, TimeUnit timeUnit, Long limit1, Long limit2) {
        this.courierGroup = courierGroup;
        this.courierType = courierType;
        this.originState = originState;
        this.originCity = originCity;
        this.timeUnit = timeUnit;
        this.limit1 = limit1;
        this.limit2 = limit2;
    }

    public ShippedNotConMetrics(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        fieldsMap.keySet().forEach(field -> {
            switch (field) {
                case COURIER_GROUP:
                    this.setCourierGroup(fieldsMap.get(field));
                    break;
                case COURIER_TYPE:
                    this.setCourierType(fieldsMap.get(field));
                    break;
                case COURIER_ORIGIN_STATE:
                    this.setOriginState(fieldsMap.get(field));
                    break;
                case COURIER_ORIGIN_CITY:
                    this.setOriginCity(fieldsMap.get(field));
                    break;
            }
        });
    }

    public String getCourierGroup() {
        return courierGroup;
    }

    public void setCourierGroup(String courierGroup) {
        this.courierGroup = courierGroup;
    }

    public String getOriginState() {
        return originState;
    }

    public void setOriginState(String originState) {
        this.originState = originState;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getCourierType() {
        return courierType;
    }

    public void setCourierType(String courierType) {
        this.courierType = courierType;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Long getLimit1() {
        return limit1;
    }

    public void setLimit1(Long limit1) {
        this.limit1 = limit1;
    }

    public Long getLimit2() {
        return limit2;
    }

    public void setLimit2(Long limit2) {
        this.limit2 = limit2;
    }
}
