package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.core.dto.TimeRange;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.mongo.doc.ShipNotAttemptNonIntgrMetrics;

import java.util.Comparator;
import java.util.Map;

/**
 * @author chitransh
 */
@Cache(cacheKey = CacheKey.SHIP_NOT_ATTEMPT_NON_INTEGRATED_METRICS, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class ShipNotAttemptNonIntgrMetricsCache extends PriorityMetricsCache<ShipNotAttemptNonIntgrMetrics> {

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<ShipNotAttemptNonIntgrMetrics>[] getKeyPriorityLogic() {

        Comparator<ShipNotAttemptNonIntgrMetrics> firstPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getOriginCity(), second.getOriginCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getOriginState(), second.getOriginState());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getCourierGroup(), second.getCourierGroup());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getCourierType(), second.getCourierType());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getDestinationCity(), second.getDestinationCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getDestinationState(), second.getDestinationState());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<ShipNotAttemptNonIntgrMetrics> secondPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getOriginCity(), second.getOriginCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getOriginState(), second.getOriginState());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getDestinationCity(), second.getDestinationCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getDestinationState(), second.getDestinationState());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<ShipNotAttemptNonIntgrMetrics> thirdPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getOriginCity(), second.getOriginCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getOriginState(), second.getOriginState());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getDestinationState(), second.getDestinationState());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getLaneType(), second.getLaneType());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<ShipNotAttemptNonIntgrMetrics> fourthPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getOriginCity(), second.getOriginCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getOriginState(), second.getOriginState());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getDestinationState(), second.getDestinationState());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<ShipNotAttemptNonIntgrMetrics> fifthPreference = (first, second) ->
                nullSafeCompare(first.getLaneType(), second.getLaneType());

        Comparator<ShipNotAttemptNonIntgrMetrics>[] keyPriorityOrder = (Comparator<ShipNotAttemptNonIntgrMetrics>[]) new Comparator[5];
        keyPriorityOrder[0] = firstPreference;
        keyPriorityOrder[1] = secondPreference;
        keyPriorityOrder[2] = thirdPreference;
        keyPriorityOrder[3] = fourthPreference;
        keyPriorityOrder[4] = fifthPreference;
        return keyPriorityOrder;
    }

    @Override
    public ShipNotAttemptNonIntgrMetrics getMongoDocument(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        return new ShipNotAttemptNonIntgrMetrics(fieldsMap);
    }

    @Override
    public TimeRange getTimeRange(ShipNotAttemptNonIntgrMetrics metrics) {
        return new TimeRange(metrics.getTimeUnit(), metrics.getLimit1(), metrics.getLimit2());
    }

    @Override
    public String getScmPropertyNameForDefaultValue() {
        return "pendency.nonIntegrated.shippedNotAttemptedMetrics";
    }
}
