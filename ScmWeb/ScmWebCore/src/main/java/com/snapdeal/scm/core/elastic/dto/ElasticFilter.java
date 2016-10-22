package com.snapdeal.scm.core.elastic.dto;

import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.snapdeal.scm.web.core.enums.ChartFilterKey;
import com.snapdeal.scm.web.core.enums.FilterKey;
import com.snapdeal.scm.web.core.enums.OptionKey;
import com.snapdeal.scm.web.core.enums.Stage;
import org.apache.commons.collections.CollectionUtils;

/**
 * ElasticFilter : Elastic Filter
 * 
 * @author pranav, prateek, Ashwini
 */
public class ElasticFilter {
    private EnumMap<FilterKey, String>            filterValues     = new EnumMap<FilterKey, String>(FilterKey.class);
    private List<ElasticDateRangeFilter>          dateRangeFilters = new ArrayList<ElasticDateRangeFilter>();
    private Stage                                 stage            = Stage.ONE;
    private ElasticDurationTypeDateRange          durationTypeDateRange;
    private Multimap<OptionKey, String>           optionValues     = ArrayListMultimap.create();
    private Table<Stage, ChartFilterKey, List<FilterKey>> chartGroupValue  = HashBasedTable.create();

    public ElasticFilter() {
    }

    public ElasticFilter(EnumMap<FilterKey, String> filterValues, ElasticDateRangeFilter dateRangeFilter, Stage stage, Multimap<OptionKey, String> optionValues) {
        this.filterValues = filterValues;
        this.dateRangeFilters.add(dateRangeFilter);
        this.stage = stage;
        this.optionValues = optionValues;
    }

    public EnumMap<FilterKey, String> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(EnumMap<FilterKey, String> filterValues) {
        this.filterValues = filterValues;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Multimap<OptionKey, String> getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(Multimap<OptionKey, String> optionValues) {
        this.optionValues = optionValues;
    }

    public List<ElasticDateRangeFilter> getDateRangeFilters() {
        return dateRangeFilters;
    }

    public void setDateRangeFilters(List<ElasticDateRangeFilter> dateRangeFilters) {
        this.dateRangeFilters = dateRangeFilters;
    }

    public ElasticDurationTypeDateRange getDurationTypeDateRange() {
        return durationTypeDateRange;
    }

    public void setDurationTypeDateRange(ElasticDurationTypeDateRange durationTypeDateRange) {
        this.durationTypeDateRange = durationTypeDateRange;
    }

    public Table<Stage, ChartFilterKey, List<FilterKey>> getChartGroupValue() {
        return chartGroupValue;
    }

    public void setChartGroupValue(Table<Stage, ChartFilterKey, List<FilterKey>> chartGroupValue) {
        this.chartGroupValue = chartGroupValue;
    }

    public FilterKey getChartFilterKeyByStageAndChartFilter(Stage stage, ChartFilterKey chartFilterKey){
        if(null!=chartGroupValue) {
            List<FilterKey> filterKey = chartGroupValue.get(stage, chartFilterKey);
            if (!CollectionUtils.isEmpty(filterKey)) {
                return filterKey.get(0);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ElasticFilter [filterValues=");
        builder.append(filterValues);
        builder.append(", dateRangeFilters=");
        builder.append(dateRangeFilters);
        builder.append(", stage=");
        builder.append(stage);
        builder.append(", durationTypeDateRange=");
        builder.append(durationTypeDateRange);
        builder.append(", optionValues=");
        builder.append(optionValues);
        builder.append(", chartGroupValue=");
        builder.append(chartGroupValue);
        builder.append("]");
        return builder.toString();
    }

}
