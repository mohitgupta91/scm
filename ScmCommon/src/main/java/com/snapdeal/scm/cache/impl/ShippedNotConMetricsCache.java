package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.core.dto.TimeRange;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.mongo.doc.ShippedNotConMetrics;
import com.snapdeal.scm.mongo.mao.repository.ShippedNotConMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.Map;

/**
 * @author chitransh
 */
@Cache(cacheKey = CacheKey.SHIPPED_NOT_CON_METRICS, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class ShippedNotConMetricsCache extends PriorityMetricsCache<ShippedNotConMetrics> {

    @Autowired
    private ShippedNotConMetricsRepository repository;

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<ShippedNotConMetrics>[] getKeyPriorityLogic() {

        Comparator<ShippedNotConMetrics> firstPreference = (first, second) -> {
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
            return 0;
        };

        Comparator<ShippedNotConMetrics> secondPreference = (first, second) -> {
            int compareTo = nullSafeCompare(first.getOriginCity(), second.getOriginCity());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getOriginState(), second.getOriginState());
            if (compareTo != 0) {
                return compareTo;
            }
            compareTo = nullSafeCompare(first.getCourierType(), second.getCourierType());
            if (compareTo != 0) {
                return compareTo;
            }
            return 0;
        };

        Comparator<ShippedNotConMetrics> thirdPreference = (first, second) ->
                nullSafeCompare(first.getCourierType(), second.getCourierType());

        Comparator<ShippedNotConMetrics>[] keyPriorityOrder = (Comparator<ShippedNotConMetrics>[]) new Comparator[3];
        keyPriorityOrder[0] = firstPreference;
        keyPriorityOrder[1] = secondPreference;
        keyPriorityOrder[2] = thirdPreference;
        return keyPriorityOrder;
    }

    @Override
    public ShippedNotConMetrics getMongoDocument(Map<SubOrderDetailElasticColumn, String> fieldsMap) {
        return new ShippedNotConMetrics(fieldsMap);
    }

    @Override
    public TimeRange getTimeRange(ShippedNotConMetrics metrics) {
        return new TimeRange(metrics.getTimeUnit(), metrics.getLimit1(), metrics.getLimit2());
    }

    @Override
    public String getScmPropertyNameForDefaultValue() {
        return "pendency.integrated.shippedNotConnectedMetrics";
    }
}
