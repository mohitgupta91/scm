package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.core.dto.TimeRange;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.mongo.doc.AttemptNotDelMetrics;
import com.snapdeal.scm.mongo.mao.repository.AttemptNotDelMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.Map;

/**
 * @author chitransh
 */
@Cache(cacheKey = CacheKey.ATTEMPT_NOT_DEL_METRICS, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class AttemptNotDelMetricsCache extends PriorityMetricsCache<AttemptNotDelMetrics> {

    @Autowired
    private AttemptNotDelMetricsRepository repository;

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<AttemptNotDelMetrics>[] getKeyPriorityLogic() {

        Comparator<AttemptNotDelMetrics> firstPreference = (first, second) -> {
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
            return 0;
        };

        Comparator<AttemptNotDelMetrics> secondPreference = (first, second) -> {
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
            return 0;
        };

        Comparator<AttemptNotDelMetrics> thirdPreference = (first, second) ->
        {
            int compareTo = nullSafeCompare(first.getCourierType(), second.getCourierType());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<AttemptNotDelMetrics>[] keyPriorityOrder = (Comparator<AttemptNotDelMetrics>[]) new Comparator[3];
        keyPriorityOrder[0] = firstPreference;
        keyPriorityOrder[1] = secondPreference;
        keyPriorityOrder[2] = thirdPreference;
        return keyPriorityOrder;
    }

    @Override
    public AttemptNotDelMetrics getMongoDocument(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        return new AttemptNotDelMetrics(fieldsMap);
    }

    @Override
    public TimeRange getTimeRange(AttemptNotDelMetrics metrics) {
        return new TimeRange(metrics.getTimeUnit(), metrics.getLimit1(), metrics.getLimit2());
    }

    @Override
    public String getScmPropertyNameForDefaultValue() {
        return "pendency.integrated.attemptedNotDeliveredMetrics";
    }
}
