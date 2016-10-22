package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.core.dto.TimeRange;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.mongo.doc.UD1NotUD2Metrics;

import java.util.Comparator;
import java.util.Map;

/**
 * @author chitransh
 */
@Cache(cacheKey = CacheKey.UD1_NOT_UD2_METRICS, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class UD1NotUD2MetricsCache extends PriorityMetricsCache<UD1NotUD2Metrics> {

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<UD1NotUD2Metrics>[] getKeyPriorityLogic() {

        Comparator<UD1NotUD2Metrics> firstPreference = (first, second) -> {
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
            compareTo = nullSafeCompare(first.getUd1Bucket(), second.getUd1Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD1NotUD2Metrics> secondPreference = (first, second) -> {
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
            compareTo = nullSafeCompare(first.getUd1Bucket(), second.getUd1Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD1NotUD2Metrics> thirdPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getCourierType(), second.getCourierType());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getUd1Bucket(), second.getUd1Bucket());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<UD1NotUD2Metrics>[] keyPriorityOrder = (Comparator<UD1NotUD2Metrics>[]) new Comparator[3];
        keyPriorityOrder[0] = firstPreference;
        keyPriorityOrder[1] = secondPreference;
        keyPriorityOrder[2] = thirdPreference;
        return keyPriorityOrder;
    }

    @Override
    public UD1NotUD2Metrics getMongoDocument(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        return new UD1NotUD2Metrics(fieldsMap);
    }

    @Override
    public TimeRange getTimeRange(UD1NotUD2Metrics metrics) {
        return new TimeRange(metrics.getTimeUnit(), metrics.getLimit1(), metrics.getLimit2());
    }

    @Override
    public String getScmPropertyNameForDefaultValue() {
        return "pendency.undelivered.uD1NotUD2Metrics";
    }
}
