package com.snapdeal.scm.cache.impl;

import com.google.common.collect.Lists;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.core.dto.TimeRange;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.enums.PendencyMetricsStatus;
import com.snapdeal.scm.mongo.doc.ScmProperty;
import com.snapdeal.scm.mongo.mao.repository.ScmPropertyRepository;
import com.snapdeal.scm.utils.PriorityKeyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.snapdeal.scm.enums.PendencyMetricsStatus.*;

/**
 * @author chitransh
 */
public abstract class PriorityMetricsCache<Document> implements ICache {

    private Long                                interval;
    private Long                                minCutoff;
    private Long                                maxCutoff;
    private PriorityKeyMap<Document, TimeRange> priorityKeyMap;

    @Autowired
    private MongoRepository<Document, String> repository;

    @Autowired
    private ScmPropertyRepository scmPropertyRepository;

    private static final Logger LOG = LoggerFactory.getLogger(PriorityMetricsCache.class);

    @SuppressWarnings("unchecked")
    public abstract Comparator<Document>[] getKeyPriorityLogic();

    public abstract Document getMongoDocument(Map<SubOrderDetailElasticColumn, String> fieldsMap);

    public abstract TimeRange getTimeRange(Document document);

    public abstract String getScmPropertyNameForDefaultValue();

    public final PendencyMetricsStatus getMetricsStatus(Long hoursElapsed, Map<SubOrderDetailElasticColumn, String> fieldsMap) {

        TimeRange timeMetrics = this.priorityKeyMap.get(getMongoDocument(fieldsMap));
        if (timeMetrics != null) {
            TimeUnit timeUnit       = timeMetrics.getTimeUnit();
            Long     endTimeHours   = timeUnit.toHours(timeMetrics.getEndTime());
            Long     startTimeHours = timeUnit.toHours(timeMetrics.getStartTime());
            return hoursElapsed > endTimeHours ? CRITICAL : hoursElapsed < startTimeHours ? GOOD : NORMAL;
        }

        if (this.minCutoff != null && this.maxCutoff != null) {
            LOG.info("Using default values for finding the pendency status");
            return hoursElapsed > this.maxCutoff ? CRITICAL : hoursElapsed < this.minCutoff ? GOOD : NORMAL;
        }

        LOG.info("Not even able to find default values for calculating the pendency status for metrics: {}", this.getClass().getName());
        return null;
    }

    @Override
    public final void load() {
        this.priorityKeyMap = new PriorityKeyMap<>(getKeyPriorityLogic());
        Set<Long> times = new HashSet<>();
        repository.findAll()
                .forEach(metric -> {
                    TimeRange timeRange = getTimeRange(metric);
                    priorityKeyMap.put(metric, timeRange);
                    times.add(timeRange.getStartTime());
                    times.add(timeRange.getEndTime());
                });
        this.interval = gcd(times);
        String scmPropertyName = getScmPropertyNameForDefaultValue();
        this.minCutoff = getDefaultPropertyValue(scmPropertyName + ".minCutOff");
        this.maxCutoff = getDefaultPropertyValue(scmPropertyName + ".maxCutOff");
    }

    private Long getDefaultPropertyValue(String propertyName) {

        ScmProperty property = scmPropertyRepository.findByName(propertyName);
        if (property != null) {
            String stringValue = property.getStringValue();
            if (stringValue != null && !stringValue.isEmpty()) {
                try {
                    return Long.parseLong(stringValue);
                } catch (NumberFormatException e) {
                    LOG.info("WARNING: Unable to parse the value of property {} to long", property);
                }
            }
            LOG.info("WARNING: No default value set for property: {}", property);
        }
        return null;
    }

    private static long gcd(long a, long b) {

        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        if (a == 1 || b == 1) {
            return 1;
        }
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static long gcd(Set<Long> numbers) {
        long result = 1;
        if (!numbers.isEmpty()) {
            List<Long> list = Lists.newArrayList(numbers);
            result = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                Long number = list.get(i);
                if (number != null) {
                    result = gcd(result, number);
                }
            }
        }
        return result;
    }

    protected final int nullSafeCompare(String a, String b) {

        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        return a.compareTo(b);
    }

    public Long getInterval() {
        return this.interval;
    }
}
