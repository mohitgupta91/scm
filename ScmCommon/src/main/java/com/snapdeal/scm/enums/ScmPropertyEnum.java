package com.snapdeal.scm.enums;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

/**
 * This class contains some of the common scm properties that are present in scm_property, with their default values.
 * In case, the table entry is not present, the default values of these properties as specified below will be returned;
 * otherwise the value of these properties in the mongo document scm_property is returned.
 *
 * @author chitransh
 */
public enum ScmPropertyEnum {

    PENDENCY_INTEGRATED_SHIPPED_NOT_CONNECTED_MIN       ("pendency.integrated.shippedNotConnectedMetrics.minCutOff", "24"),
    PENDENCY_INTEGRATED_SHIPPED_NOT_CONNECTED_MAX       ("pendency.integrated.shippedNotConnectedMetrics.maxCutOff", "48"),
    PENDENCY_INTEGRATED_CONNECTED_NOT_REACHED_MIN       ("pendency.integrated.connectedNotReachedMetrics.minCutOff", "24"),
    PENDENCY_INTEGRATED_CONNECTED_NOT_REACHED_MAX       ("pendency.integrated.connectedNotReachedMetrics.maxCutOff", "48"),
    PENDENCY_INTEGRATED_REACHED_NOT_OFD_MIN             ("pendency.integrated.reachedNotOutForDeliveryMetrics.minCutOff", "24"),
    PENDENCY_INTEGRATED_REACHED_NOT_OFD_MAX             ("pendency.integrated.reachedNotOutForDeliveryMetrics.maxCutOff", "48"),
    PENDENCY_INTEGRATED_OFD_NOT_ATTEMPTED_MIN           ("pendency.integrated.outForDeliveryNotAttemptedMetrics.minCutOff", "24"),
    PENDENCY_INTEGRATED_OFD_NOT_ATTEMPTED_MAX           ("pendency.integrated.outForDeliveryNotAttemptedMetrics.maxCutOff", "48"),
    PENDENCY_INTEGRATED_ATTEMPTED_NOT_DELIVERED_MIN     ("pendency.integrated.attemptedNotDeliveredMetrics.minCutOff", "24"),
    PENDENCY_INTEGRATED_ATTEMPTED_NOT_DELIVERED_MAX     ("pendency.integrated.attemptedNotDeliveredMetrics.maxCutOff", "48"),
    PENDENCY_NON_INTEGRATED_SHIPPED_NOT_ATTEMPTED_MIN   ("pendency.nonIntegrated.shippedNotAttemptedMetrics.minCutOff", "24"),
    PENDENCY_NON_INTEGRATED_SHIPPED_NOT_ATTEMPTED_MAX   ("pendency.nonIntegrated.shippedNotAttemptedMetrics.maxCutOff", "48"),
    PENDENCY_NON_INTEGRATED_ATTEMPTED_NOT_DELIVERED_MIN ("pendency.nonIntegrated.attemptedNotDeliveredMetrics.minCutOff", "24"),
    PENDENCY_NON_INTEGRATED_ATTEMPTED_NOT_DELIVERED_MAX ("pendency.nonIntegrated.attemptedNotDeliveredMetrics.maxCutOff", "48"),
    PENDENCY_UNDELIVERED_UD1_NOT_UD2_MIN                ("pendency.undelivered.uD1NotUD2Metrics.minCutOff", "24"),
    PENDENCY_UNDELIVERED_UD1_NOT_UD2_MAX                ("pendency.undelivered.uD1NotUD2Metrics.maxCutOff", "48"),
    PENDENCY_UNDELIVERED_UD2_NOT_UD3_MIN                ("pendency.undelivered.uD2NotUD3Metrics.minCutOff", "24"),
    PENDENCY_UNDELIVERED_UD2_NOT_UD3_MAX                ("pendency.undelivered.uD2NotUD3Metrics.maxCutOff", "48"),
    PENDENCY_UNDELIVERED_UD3_NOT_UD4_MIN                ("pendency.undelivered.uD3NotUD4Metrics.minCutOff", "24"),
    PENDENCY_UNDELIVERED_UD3_NOT_UD4_MAX                ("pendency.undelivered.uD2NotUD3Metrics.maxCutOff", "48"),
    PENDENCY_UNDELIVERED_UD4_NOT_DEL_MIN                ("pendency.undelivered.uD4NotDelMetrics.minCutOff", "24"),
    PENDENCY_UNDELIVERED_UD4_NOT_DEL_MAX                ("pendency.undelivered.uD4NotDelMetrics.maxCutOff", "48"),
    PRE_COMPLAINT_AGEING_BUCKETS                        ("preDeliveryComplaintsAgeingBuckets", ImmutableList.<String>builder()
                                                                        .add("0").add("7").add("10").add("15").build());

    private String              name;
    private String              stringValue;
    private List<String>        listValue;
    private Map<String, String> mapValue;

    ScmPropertyEnum(String name, String stringValue, List<String> listValue, Map<String, String> mapValue) {
        this.name = name;
        this.stringValue = stringValue;
        this.listValue = listValue;
        this.mapValue = mapValue;
    }

    ScmPropertyEnum(String name, String stringValue) {
        this(name, stringValue, null, null);
    }

    ScmPropertyEnum(String name, List<String> listValue) {
        this(name, null, listValue, null);
    }

    ScmPropertyEnum(String name, Map<String, String> mapValue) {
        this(name, null, null, mapValue);
    }

    public String getName() {
        return name;
    }

    public String getStringValue() {
        return stringValue;
    }

    public List<String> getListValue() {
        return listValue;
    }

    public Map<String, String> getMapValue() {
        return mapValue;
    }
}
