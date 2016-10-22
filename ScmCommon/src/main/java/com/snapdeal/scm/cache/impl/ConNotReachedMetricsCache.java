package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.core.dto.TimeRange;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.mongo.doc.ConNotReachedMetrics;
import com.snapdeal.scm.mongo.mao.repository.ConNotReachedMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.Map;

/**
 * @author chitransh
 */
@Cache(cacheKey = CacheKey.CON_NOT_REACHED_METRICS, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class ConNotReachedMetricsCache extends PriorityMetricsCache<ConNotReachedMetrics> {

    @Autowired
    private ConNotReachedMetricsRepository repository;

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<ConNotReachedMetrics>[] getKeyPriorityLogic() {

        Comparator<ConNotReachedMetrics> firstPreference = (first, second) -> {
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

        Comparator<ConNotReachedMetrics> secondPreference = (first, second) -> {
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

        Comparator<ConNotReachedMetrics> thirdPreference = (first, second) -> {
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

        Comparator<ConNotReachedMetrics> fourthPreference = (first, second) -> {
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

        Comparator<ConNotReachedMetrics> fifthPreference = (first, second) ->
                nullSafeCompare(first.getLaneType(), second.getLaneType());

        Comparator<ConNotReachedMetrics>[] keyPriorityOrder = (Comparator<ConNotReachedMetrics>[]) new Comparator[5];
        keyPriorityOrder[0] = firstPreference;
        keyPriorityOrder[1] = secondPreference;
        keyPriorityOrder[2] = thirdPreference;
        keyPriorityOrder[3] = fourthPreference;
        keyPriorityOrder[4] = fifthPreference;
        return keyPriorityOrder;
    }

    @Override
    public ConNotReachedMetrics getMongoDocument(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        return new ConNotReachedMetrics(fieldsMap);
    }

    @Override
    public TimeRange getTimeRange(ConNotReachedMetrics metrics) {
        return new TimeRange(metrics.getTimeUnit(), metrics.getLimit1(), metrics.getLimit2());
    }

    @Override
    public String getScmPropertyNameForDefaultValue() {
        return "pendency.integrated.connectedNotReachedMetrics";
    }
}
