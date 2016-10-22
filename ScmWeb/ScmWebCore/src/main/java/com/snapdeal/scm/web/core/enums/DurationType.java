package com.snapdeal.scm.web.core.enums;

import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;

/**
 * @author vinay, chitransh
 */
public enum DurationType {

    HOURLY      ("hourly", DateHistogramInterval.HOUR),
    DAILY       ("daily", DateHistogramInterval.DAY),
    WEEKLY      ("weekly", DateHistogramInterval.WEEK),
    MONTHLY     ("monthly", DateHistogramInterval.MONTH),
    YEARLY      ("yearly", DateHistogramInterval.YEAR);

    private String type;
    private DateHistogramInterval histogramInterval;

    DurationType(String type, DateHistogramInterval histogramInterval) {
        this.type = type;
        this.histogramInterval = histogramInterval;
    }

    public DateHistogramInterval getHistogramInterval() {
        return histogramInterval;
    }

    public static DurationType getType(String type) {
        if (type == null) {
            return null;
        }
        for (DurationType durationType : values()) {
            if (durationType.type.equalsIgnoreCase(type))
                return durationType;
        }
        return null;
    }
}
