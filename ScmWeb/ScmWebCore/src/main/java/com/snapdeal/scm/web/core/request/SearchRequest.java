package com.snapdeal.scm.web.core.request;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by Harsh Gupta on 26/02/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRequest extends BaseRequest {

    private static final long serialVersionUID = -7191710901031348258L;

    private Map<String, List<String>> filters;

    private Map<String, String> options;

    private Map<String, Map<String, List<String>>> chartFilters;

    private String stage;

    public Map<String, List<String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<String>> filters) {
        this.filters = filters;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public Map<String, Map<String, List<String>>> getChartFilters() {
        return chartFilters;
    }

    public void setChartFilters(Map<String, Map<String, List<String>>> chartFilters) {
        this.chartFilters = chartFilters;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SearchRequest [filters=");
        builder.append(filters);
        builder.append(", options=");
        builder.append(options);
        builder.append(", chartFilters=");
        builder.append(chartFilters);
        builder.append(", stage=");
        builder.append(stage);
        builder.append("]");
        return builder.toString();
    }
}
