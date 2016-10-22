package com.snapdeal.scm.web.core.sro;

import java.util.List;
import java.util.Map;

/**
 * @author chitransh
 */
public class ChartDataSetSRO implements IDataSetSRO {

    private List<String> columns;
    private Map<String, List<ChartSRO>> series;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public Map<String, List<ChartSRO>> getSeries() {
        return series;
    }

    public void setSeries(Map<String, List<ChartSRO>> series) {
        this.series = series;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChartDataSetSRO{");
        sb.append("columns=").append(columns);
        sb.append(", series=").append(series);
        sb.append('}');
        return sb.toString();
    }
}
