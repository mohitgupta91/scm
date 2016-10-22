package com.snapdeal.scm.web.core.dto;

import com.snapdeal.scm.web.core.enums.DurationType;
import org.joda.time.DateTime;


/**
 * Created by vinay on 13/3/16.
 */
public class DateRangeDTO {
    DateTime start;
    DateTime end;
    DurationType durationType;

    public DateRangeDTO() {
    }

    public DateRangeDTO(DateTime start, DateTime end, DurationType durationType) {
        this.start = start;
        this.end = end;
        this.durationType = durationType;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    public DurationType getDurationType() {
        return durationType;
    }

    public void setDurationType(DurationType durationType) {
        this.durationType = durationType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DateRangeDTO{");
        sb.append("start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", durationType=").append(durationType);
        sb.append('}');
        return sb.toString();
    }
}
