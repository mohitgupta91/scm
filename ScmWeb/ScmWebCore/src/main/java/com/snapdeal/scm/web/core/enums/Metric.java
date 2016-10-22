/**
 *
 */
package com.snapdeal.scm.web.core.enums;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author gaurav, vinay, chitransh
 */
public enum Metric {

    LAST_MILE_O2D_DELIVERED             ("avgo2dperformancedelivered", "shiptodeliver", "lastmile", "G1", 1),
    LAST_MILE_O2S_SHIPPED               ("avgo2sperformanceshipped", "shiptodeliver", "lastmile", "G2", 2),
    LAST_MILE_DELIVERY_ADHERANCE        ("deliveryadherance", "shiptodeliver", "lastmile", "G3", 3),
    LAST_MILE_ATTEMPT_ADHERANCE         ("attemptadherance", "shiptodeliver", "lastmile", "G4", 4),
    LAST_MILE_DELIVERED_PERFORMANCE     ("percentagedeliveredperformance", "shiptodeliver", "lastmile", "G5", 5),
    LAST_MILE_S2D_PENDENCY_TREND        ("s2dpendencytrend", "shiptodeliver", "lastmile", "G6", 6),
    LAST_MILE_ATTEMPTED_PERFORMANCE     ("percentageattemptperformance", "shiptodeliver", "lastmile", "G7", 7),
    PENDENCY_INTEGRATED_COURIERS        ("s2dpendencyintegratedcouriers", "shiptodeliver", "lastmile", "G8", 8),
    PENDENCY_NON_INTEGRATED_COURIERS    ("s2dpendencynonintegratedcouriers", "shiptodeliver", "lastmile", "G9", 9),
    UD_MONITORING                       ("currentundeliveredpendency", "shiptodeliver", "lastmile", "G10", 10),
    LAST_MILE_RTO_INITIATED_COUNT       ("rtoinitiatedcountdaywise", "rto", "lastmile", "G38", 38),
    LAST_MILE_RTO_PERCENTAGE            ("rtopercentage", "rto", "lastmile", "G39", 39),
    LAST_MILE_RTO_PENDENCY              ("rtopendency","rto","lastmile","G40",40),
    COMPLAINTS_PRE_DELIVERY				("complaintspredelivery","complaints","lastmile","G21",21),
    COMPLAINTS_POST_DELIVERY			("complaintspostdelivery","complaints","lastmile","G22",22),
    COURIER_ALLOCATION      			("courierallocationsalience","loadallocation","lastmile","G11",11);

    private String code;
    private String submodule;
    private String module;
    private String gridCode;
    private Integer metricsId;

    private static Table<Metric, Stage, OptionKey> metricStageOptionMapping = HashBasedTable.create();
    private static Table<Metric, Stage, ChartFilterKey> metricStageChartFilterMapping = HashBasedTable.create();

    static {
        metricStageOptionMapping.put(Metric.LAST_MILE_O2D_DELIVERED, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.LAST_MILE_O2D_DELIVERED, Stage.TWO, OptionKey.LANE_TYPE);
        metricStageOptionMapping.put(Metric.LAST_MILE_O2D_DELIVERED, Stage.THREE, OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.LAST_MILE_O2D_DELIVERED, Stage.FOUR, OptionKey.DESTINATION_STATE);
        metricStageOptionMapping.put(Metric.LAST_MILE_O2D_DELIVERED, Stage.FIVE, OptionKey.DESTINATION_CITY);

        metricStageOptionMapping.put(Metric.LAST_MILE_O2S_SHIPPED, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.LAST_MILE_O2S_SHIPPED, Stage.TWO, OptionKey.FULFILLMENT_MODEL);
        metricStageOptionMapping.put(Metric.LAST_MILE_O2S_SHIPPED, Stage.THREE, OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.LAST_MILE_O2S_SHIPPED, Stage.FOUR, OptionKey.SOURCE_STATE);
        metricStageOptionMapping.put(Metric.LAST_MILE_O2S_SHIPPED, Stage.FIVE, OptionKey.SOURCE_CITY);

        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERY_ADHERANCE, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERY_ADHERANCE, Stage.TWO, OptionKey.LANE_TYPE);
        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERY_ADHERANCE, Stage.THREE, OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERY_ADHERANCE, Stage.FOUR, OptionKey.DESTINATION_STATE);
        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERY_ADHERANCE, Stage.FIVE, OptionKey.DESTINATION_CITY);

        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPT_ADHERANCE, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPT_ADHERANCE, Stage.TWO, OptionKey.LANE_TYPE);
        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPT_ADHERANCE, Stage.THREE, OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPT_ADHERANCE, Stage.FOUR, OptionKey.DESTINATION_STATE);
        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPT_ADHERANCE, Stage.FIVE, OptionKey.DESTINATION_CITY);

        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERED_PERFORMANCE, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERED_PERFORMANCE, Stage.TWO, OptionKey.LANE_TYPE);
        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERED_PERFORMANCE, Stage.THREE, OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERED_PERFORMANCE, Stage.FOUR, OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERED_PERFORMANCE, Stage.FIVE, OptionKey.DESTINATION_STATE);
        metricStageOptionMapping.put(Metric.LAST_MILE_DELIVERED_PERFORMANCE, Stage.SIX, OptionKey.DESTINATION_CITY);

        metricStageOptionMapping.put(Metric.LAST_MILE_S2D_PENDENCY_TREND, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.LAST_MILE_S2D_PENDENCY_TREND, Stage.TWO, OptionKey.LANE_TYPE);
        metricStageOptionMapping.put(Metric.LAST_MILE_S2D_PENDENCY_TREND, Stage.THREE, OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.LAST_MILE_S2D_PENDENCY_TREND, Stage.FOUR, OptionKey.DESTINATION_STATE);
        metricStageOptionMapping.put(Metric.LAST_MILE_S2D_PENDENCY_TREND, Stage.FIVE, OptionKey.DESTINATION_CITY);

        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPTED_PERFORMANCE, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPTED_PERFORMANCE, Stage.TWO, OptionKey.LANE_TYPE);
        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPTED_PERFORMANCE, Stage.THREE, OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPTED_PERFORMANCE, Stage.FOUR, OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPTED_PERFORMANCE, Stage.FIVE, OptionKey.DESTINATION_STATE);
        metricStageOptionMapping.put(Metric.LAST_MILE_ATTEMPTED_PERFORMANCE, Stage.SIX, OptionKey.DESTINATION_CITY);

        metricStageOptionMapping.put(Metric.PENDENCY_INTEGRATED_COURIERS, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.PENDENCY_INTEGRATED_COURIERS, Stage.TWO, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.PENDENCY_INTEGRATED_COURIERS, Stage.THREE, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.PENDENCY_INTEGRATED_COURIERS, Stage.FOUR, OptionKey.LANE);

        metricStageOptionMapping.put(Metric.PENDENCY_NON_INTEGRATED_COURIERS, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.PENDENCY_NON_INTEGRATED_COURIERS, Stage.TWO, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.PENDENCY_NON_INTEGRATED_COURIERS, Stage.THREE, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.PENDENCY_NON_INTEGRATED_COURIERS, Stage.FOUR, OptionKey.LANE);

        metricStageOptionMapping.put(Metric.UD_MONITORING, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.UD_MONITORING, Stage.TWO, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.UD_MONITORING, Stage.THREE, OptionKey.LANE);

        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_INITIATED_COUNT, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_INITIATED_COUNT, Stage.TWO, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_INITIATED_COUNT, Stage.THREE, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_INITIATED_COUNT, Stage.FOUR, OptionKey.DESTINATION_STATE);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_INITIATED_COUNT, Stage.FIVE, OptionKey.DESTINATION_CITY);

        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_PERCENTAGE, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_PERCENTAGE, Stage.TWO, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_PERCENTAGE, Stage.THREE, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_PERCENTAGE, Stage.FOUR, OptionKey.DESTINATION_STATE);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_PERCENTAGE, Stage.FIVE, OptionKey.DESTINATION_CITY);

        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_PENDENCY, Stage.ONE, OptionKey.CATEGORY);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_PENDENCY, Stage.TWO, OptionKey.RTO_STATES);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_PENDENCY, Stage.THREE, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_PENDENCY, Stage.FOUR, OptionKey.MULTIPLE);
        metricStageOptionMapping.put(Metric.LAST_MILE_RTO_PENDENCY, Stage.FIVE, OptionKey.LANE);
        
        metricStageOptionMapping.put(Metric.COMPLAINTS_PRE_DELIVERY, Stage.ONE, OptionKey.COMPLAINT_CATEGORY);
        metricStageOptionMapping.put(Metric.COMPLAINTS_PRE_DELIVERY, Stage.TWO, OptionKey.COMPLAINT_ORIGIN);
        metricStageOptionMapping.put(Metric.COMPLAINTS_PRE_DELIVERY, Stage.THREE,OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.COMPLAINTS_PRE_DELIVERY, Stage.FOUR,OptionKey.OVERALL);

        metricStageOptionMapping.put(Metric.COMPLAINTS_POST_DELIVERY, Stage.ONE, OptionKey.COMPLAINT_CATEGORY);
        metricStageOptionMapping.put(Metric.COMPLAINTS_POST_DELIVERY, Stage.TWO, OptionKey.COMPLAINT_ORIGIN);
        metricStageOptionMapping.put(Metric.COMPLAINTS_POST_DELIVERY, Stage.THREE,OptionKey.COURIER_GROUP);
        metricStageOptionMapping.put(Metric.COMPLAINTS_POST_DELIVERY, Stage.FOUR,OptionKey.FULFILLMENT_MODEL);

        metricStageOptionMapping.put(Metric.COURIER_ALLOCATION, Stage.ONE, OptionKey.OVERALL);
        metricStageOptionMapping.put(Metric.COURIER_ALLOCATION, Stage.TWO, OptionKey.OVERALL);
    }

    static {

        metricStageChartFilterMapping.put(Metric.PENDENCY_INTEGRATED_COURIERS, Stage.TWO, ChartFilterKey.GROUP_BY);
        metricStageChartFilterMapping.put(Metric.PENDENCY_INTEGRATED_COURIERS, Stage.THREE, ChartFilterKey.GROUP_BY);
        metricStageChartFilterMapping.put(Metric.PENDENCY_INTEGRATED_COURIERS, Stage.FOUR, ChartFilterKey.GROUP_BY);

        metricStageChartFilterMapping.put(Metric.PENDENCY_NON_INTEGRATED_COURIERS, Stage.TWO, ChartFilterKey.GROUP_BY);
        metricStageChartFilterMapping.put(Metric.PENDENCY_NON_INTEGRATED_COURIERS, Stage.THREE, ChartFilterKey.GROUP_BY);
        metricStageChartFilterMapping.put(Metric.PENDENCY_NON_INTEGRATED_COURIERS, Stage.FOUR, ChartFilterKey.GROUP_BY);

        metricStageChartFilterMapping.put(Metric.UD_MONITORING, Stage.TWO, ChartFilterKey.GROUP_BY);
        metricStageChartFilterMapping.put(Metric.UD_MONITORING, Stage.THREE, ChartFilterKey.GROUP_BY);

        metricStageChartFilterMapping.put(Metric.LAST_MILE_RTO_INITIATED_COUNT, Stage.TWO, ChartFilterKey.GROUP_BY);
        metricStageChartFilterMapping.put(Metric.LAST_MILE_RTO_INITIATED_COUNT, Stage.THREE, ChartFilterKey.GROUP_BY);

        metricStageChartFilterMapping.put(Metric.LAST_MILE_RTO_PERCENTAGE, Stage.TWO, ChartFilterKey.GROUP_BY);
        metricStageChartFilterMapping.put(Metric.LAST_MILE_RTO_PERCENTAGE, Stage.THREE, ChartFilterKey.GROUP_BY);

        metricStageChartFilterMapping.put(Metric.LAST_MILE_RTO_PENDENCY, Stage.THREE, ChartFilterKey.GROUP_BY);
        metricStageChartFilterMapping.put(Metric.LAST_MILE_RTO_PENDENCY, Stage.FOUR, ChartFilterKey.GROUP_BY);
    }

    private Metric(String code, String submodule, String module, String gridCode, Integer metricsId) {
        this.code = code;
        this.submodule = submodule;
        this.module = module;
        this.gridCode = gridCode;
        this.metricsId = metricsId;
    }

    public String getCode() {
        return code;
    }

    public String getSubmodule() {
        return submodule;
    }

    public String getModule() {
        return module;
    }

    public Integer getMetricsId() {
        return metricsId;
    }

    public String getGridCode() {
        return gridCode;
    }

    public String getURL() {
        return generateURL(module, submodule, code);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Metric{");
        sb.append("code='").append(code).append('\'');
        sb.append(", submodule='").append(submodule).append('\'');
        sb.append(", module='").append(module).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static Metric fromCode(String code) {
        for (Metric metric : values()) {
            if (metric.code.equals(code)) {
                return metric;
            }
        }
        return null;
    }

    private static String generateURL(String module, String submodule, String metricId) {
        return module + "/" + submodule + "/" + metricId;
    }

    public static Metric fromURL(String module, String submodule, String metricId) {
        String url = generateURL(module, submodule, metricId);
        for (Metric metric : values()) {
            if (metric.getURL().equalsIgnoreCase(url)) {
                return metric;
            }
        }
        return null;
    }

    public static OptionKey getOption(Metric metric, Stage stage) {
        return metricStageOptionMapping.get(metric, stage);
    }

    public static ChartFilterKey getChartFilter(Metric metric, Stage stage) {
        return metricStageChartFilterMapping.get(metric, stage);
    }
}
