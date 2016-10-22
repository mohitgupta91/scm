package com.snapdeal.scm.mongo.doc;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chitransh
 */
@Document(collection = "attempt_not_del_metrics")
public class AttemptNotDelMetrics extends MongoDocument {

    private String   courierGroup;
    private String   courierType;
    private String   destinationState;
    private String   destinationCity;
    private TimeUnit timeUnit;
    private Long     limit1;
    private Long     limit2;

    public AttemptNotDelMetrics() {
    }

    public AttemptNotDelMetrics(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        fieldsMap.keySet().forEach(field -> {
            switch (field) {
                case COURIER_GROUP:
                    this.setCourierGroup(fieldsMap.get(field));
                    break;
                case COURIER_TYPE:
                    this.setCourierType(fieldsMap.get(field));
                    break;
                case DESTINATION_STATE:
                    this.setDestinationState(fieldsMap.get(field));
                    break;
                case COURIER_ORIGIN_CITY:
                    this.setDestinationCity(fieldsMap.get(field));
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

    public String getCourierType() {
        return courierType;
    }

    public void setCourierType(String courierType) {
        this.courierType = courierType;
    }

    public String getDestinationState() {
        return destinationState;
    }

    public void setDestinationState(String destinationState) {
        this.destinationState = destinationState;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
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
