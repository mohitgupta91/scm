package com.snapdeal.scm.web.core.sro;

import java.util.Map;

/**
 * @author chitransh
 */
public class ChartSRO<K> {

    private String title;
    private Map<K, Long> chartData;

    public ChartSRO() {
    }

    public ChartSRO(String title) {
        this.title = title;
    }

    public ChartSRO(String title, Map<K, Long> chartData) {
        this.title = title;
        this.chartData = chartData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<K, Long> getChartData() {
        return chartData;
    }

    public void setChartData(Map<K, Long> chartData) {
        this.chartData = chartData;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChartSRO{");
        sb.append("title='").append(title).append('\'');
        sb.append(", chartData=").append(chartData);
        sb.append('}');
        return sb.toString();
    }
}
