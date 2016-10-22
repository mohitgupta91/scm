package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.core.dto.TimeRange;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.mongo.doc.UD2NotUD3Metrics;

import java.util.Comparator;
import java.util.Map;

/**
 * @author chitransh
 */
@Cache(cacheKey = CacheKey.UD2_NOT_UD3_METRICS, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class UD2NotUD3MetricsCache extends PriorityMetricsCache<UD2NotUD3Metrics> {

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<UD2NotUD3Metrics>[] getKeyPriorityLogic() {

        Comparator<UD2NotUD3Metrics> firstPreference = (first, second) -> {
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
            compareTo = nullSafeCompare(first.getUd2Bucket(), second.getUd2Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD2NotUD3Metrics> secondPreference = (first, second) -> {
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
            compareTo = nullSafeCompare(first.getUd2Bucket(), second.getUd2Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD2NotUD3Metrics> thirdPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getCourierType(), second.getCourierType());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getUd2Bucket(), second.getUd2Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD2NotUD3Metrics>[] keyPriorityOrder = (Comparator<UD2NotUD3Metrics>[]) new Comparator[3];
        keyPriorityOrder[0] = firstPreference;
        keyPriorityOrder[1] = secondPreference;
        keyPriorityOrder[2] = thirdPreference;
        return keyPriorityOrder;
    }

    @Override
    public UD2NotUD3Metrics getMongoDocument(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        return new UD2NotUD3Metrics(fieldsMap);
    }

    @Override
    public TimeRange getTimeRange(UD2NotUD3Metrics metrics) {
        return new TimeRange(metrics.getTimeUnit(), metrics.getLimit1(), metrics.getLimit2());
    }

    @Override
    public String getScmPropertyNameForDefaultValue() {
        return "pendency.undelivered.uD2NotUD3Metrics";
    }
}
