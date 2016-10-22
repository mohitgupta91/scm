package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.core.dto.TimeRange;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.mongo.doc.UD3NotUD4Metrics;

import java.util.Comparator;
import java.util.Map;

/**
 * @author chitransh
 */
@Cache(cacheKey = CacheKey.UD3_NOT_UD4_METRICS, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class UD3NotUD4MetricsCache extends PriorityMetricsCache<UD3NotUD4Metrics> {

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<UD3NotUD4Metrics>[] getKeyPriorityLogic() {

        Comparator<UD3NotUD4Metrics> firstPreference = (first, second) -> {
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
            compareTo = nullSafeCompare(first.getUd3Bucket(), second.getUd3Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD3NotUD4Metrics> secondPreference = (first, second) -> {
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
            compareTo = nullSafeCompare(first.getUd3Bucket(), second.getUd3Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD3NotUD4Metrics> thirdPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getCourierType(), second.getCourierType());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getUd3Bucket(), second.getUd3Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD3NotUD4Metrics>[] keyPriorityOrder = (Comparator<UD3NotUD4Metrics>[]) new Comparator[3];
        keyPriorityOrder[0] = firstPreference;
        keyPriorityOrder[1] = secondPreference;
        keyPriorityOrder[2] = thirdPreference;
        return keyPriorityOrder;
    }

    @Override
    public UD3NotUD4Metrics getMongoDocument(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        return new UD3NotUD4Metrics(fieldsMap);
    }

    @Override
    public TimeRange getTimeRange(UD3NotUD4Metrics metrics) {
        return new TimeRange(metrics.getTimeUnit(), metrics.getLimit1(), metrics.getLimit2());
    }

    @Override
    public String getScmPropertyNameForDefaultValue() {
        return "pendency.undelivered.uD3NotUD4Metrics";
    }
}
