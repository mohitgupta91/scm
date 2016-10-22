package com.snapdeal.scm.web.core.sro;

import java.util.List;
import java.util.Map;

/**
 * @author vinay
 */
public class PointDataSetSRO implements IDataSetSRO {

    String title;
    Map<String, List<PointSRO>> series;

    public PointDataSetSRO() {
    }

    public PointDataSetSRO(String title, Map<String, List<PointSRO>> series) {
        this.title = title;
        this.series = series;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, List<PointSRO>> getSeries() {
        return series;
    }

    public void setSeries(Map<String, List<PointSRO>> series) {
        this.series = series;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PointDataSetSRO{");
        sb.append("title='").append(title).append('\'');
        sb.append(", series=").append(series);
        sb.append('}');
        return sb.toString();
    }
}
