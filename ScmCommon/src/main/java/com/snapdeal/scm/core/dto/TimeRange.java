package com.snapdeal.scm.core.dto;

import java.util.concurrent.TimeUnit;

/**
 * @author chitransh
 */
public class TimeRange {
    private TimeUnit timeUnit;
    private Long startTime;
    private Long endTime;

    public TimeRange() {
    }

    public TimeRange(TimeUnit timeUnit, Long startTime, Long endTime) {
        this.timeUnit = timeUnit;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }
}