package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.core.dto.TimeRange;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.mongo.doc.ReachedNotOFDMetrics;
import com.snapdeal.scm.mongo.mao.repository.ReachedNotOFDMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.Map;

/**
 * @author chitransh
 */
@Cache(cacheKey = CacheKey.REACHED_NOT_OFD_METRICS, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class ReachedNotOFDMetricsCache extends PriorityMetricsCache<ReachedNotOFDMetrics> {

    @Autowired
    private ReachedNotOFDMetricsRepository repository;

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<ReachedNotOFDMetrics>[] getKeyPriorityLogic() {

        Comparator<ReachedNotOFDMetrics> firstPreference = (first, second) -> {
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

        Comparator<ReachedNotOFDMetrics> secondPreference = (first, second) -> {
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

        Comparator<ReachedNotOFDMetrics> thirdPreference = (first, second) ->
                nullSafeCompare(first.getCourierType(), second.getCourierType());

        Comparator<ReachedNotOFDMetrics>[] keyPriorityOrder = (Comparator<ReachedNotOFDMetrics>[]) new Comparator[3];
        keyPriorityOrder[0] = firstPreference;
        keyPriorityOrder[1] = secondPreference;
        keyPriorityOrder[2] = thirdPreference;
        return keyPriorityOrder;
    }

    @Override
    public ReachedNotOFDMetrics getMongoDocument(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        return new ReachedNotOFDMetrics(fieldsMap);
    }

    @Override
    public TimeRange getTimeRange(ReachedNotOFDMetrics metrics) {
        return new TimeRange(metrics.getTimeUnit(), metrics.getLimit1(), metrics.getLimit2());
    }

    @Override
    public String getScmPropertyNameForDefaultValue() {
        return "pendency.integrated.reachedNotOutForDeliveryMetrics";
    }
}
