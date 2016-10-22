package com.snapdeal.scm.web.core.enums;

/**
 * Created by ashwini.kumar on 11/05/16.
 */
public enum ChartFilterKey {

    GROUP_BY        ("groupby"),
    CUMMULATIVE     ("cummulative");

    ChartFilterKey(String chartFilterName) {
        this.chartFilterName = chartFilterName;
    }

    String chartFilterName;

    public String getChartFilterName() {
        return chartFilterName;
    }

    public void setChartFilterName(String chartFilterName) {
        this.chartFilterName = chartFilterName;
    }

    public static ChartFilterKey getChartFilterByName(String chartFilterName){
        if (chartFilterName == null) return null;
        for (ChartFilterKey chartFilterKey: values()){
            if (chartFilterKey.chartFilterName.equalsIgnoreCase(chartFilterName))
                return chartFilterKey;
        }
        return null;
    }
}
