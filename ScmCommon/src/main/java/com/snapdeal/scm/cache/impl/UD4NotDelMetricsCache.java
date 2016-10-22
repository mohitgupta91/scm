package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.core.dto.TimeRange;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.mongo.doc.UD4NotDelMetrics;

import java.util.Comparator;
import java.util.Map;

/**
 * @author chitransh
 */
@Cache(cacheKey = CacheKey.UD4_NOT_DEL_METRICS, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class UD4NotDelMetricsCache extends PriorityMetricsCache<UD4NotDelMetrics> {

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<UD4NotDelMetrics>[] getKeyPriorityLogic() {

        Comparator<UD4NotDelMetrics> firstPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getDestinationCity(), second.getDestinationCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getDestinationState(), second.getDestinationState());
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
            compareTo = nullSafeCompare(first.getUd4Bucket(), second.getUd4Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD4NotDelMetrics> secondPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getDestinationCity(), second.getDestinationCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getDestinationState(), second.getDestinationState());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getCourierType(), second.getCourierType());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getUd4Bucket(), second.getUd4Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD4NotDelMetrics> thirdPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getCourierType(), second.getCourierType());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getUd4Bucket(), second.getUd4Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD4NotDelMetrics>[] keyPriorityOrder = (Comparator<UD4NotDelMetrics>[]) new Comparator[3];
        keyPriorityOrder[0] = firstPreference;
        keyPriorityOrder[1] = secondPreference;
        keyPriorityOrder[2] = thirdPreference;
        return keyPriorityOrder;
    }

    @Override
    public UD4NotDelMetrics getMongoDocument(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        return new UD4NotDelMetrics(fieldsMap);
    }

    @Override
    public TimeRange getTimeRange(UD4NotDelMetrics metrics) {
        return new TimeRange(metrics.getTimeUnit(), metrics.getLimit1(), metrics.getLimit2());
    }

    @Override
    public String getScmPropertyNameForDefaultValue() {
        return "pendency.undelivered.uD4NotDelMetrics";
    }
}
