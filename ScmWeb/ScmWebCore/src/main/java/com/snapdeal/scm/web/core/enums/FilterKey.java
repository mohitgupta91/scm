package com.snapdeal.scm.web.core.enums;

import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;

/**
 * @author prateek
 */
public enum FilterKey {

    DURATION_TYPE                   ("durationtype", null),
    DURATION_TYPE_START_TIME        ("durationtype-startdate", null),
    DURATION_TYPE_END_TIME          ("durationtype-enddate", null),
    SHIPPING_MODE                   ("mode", SubOrderDetailElasticColumn.SHIPPING_MODE),
    MTO                             ("type", SubOrderDetailElasticColumn.MTO),
    METRO                           ("metro", SubOrderDetailElasticColumn.METRO),
    DELIVERY_TYPE                   ("paidndd", SubOrderDetailElasticColumn.DELIVERY_TYPE),
    FULFILLMENT_MODEL               ("fulfillmentmodel", SubOrderDetailElasticColumn.FULFILLMENT_MODEL),
    LANE_TYPE                       ("lanetype", SubOrderDetailElasticColumn.LANE_TYPE),
    LANE_GROUP                      ("lanegroup", null),
    LANE                            ("lane", SubOrderDetailElasticColumn.LANE),
    SOURCE_STATE                    ("originstate", SubOrderDetailElasticColumn.SOURCE_STATE),
    SOURCE_CITY                     ("origincity", SubOrderDetailElasticColumn.SOURCE_CITY),
    DESTINATION_STATE               ("destinationstate", SubOrderDetailElasticColumn.DESTINATION_STATE),
    DESTINATION_CITY                ("destinationcity", SubOrderDetailElasticColumn.DESTINATION_CITY),
    DESTINATION_REGION              ("destinationregion", SubOrderDetailElasticColumn.DESTINATION_REGION),
    DESTINATION_TIER                ("destinationtier", SubOrderDetailElasticColumn.DESTINATION_TIER),
    COURIER_GROUP                   ("couriergroup", SubOrderDetailElasticColumn.COURIER_GROUP),
    COURIER_TYPE                    ("couriertype", SubOrderDetailElasticColumn.COURIER_TYPE),
    DELIVERED_DATE                  ("delivereddate", SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE),
    SHIPPED_DATE                    ("shippeddate", SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE),
    ATTEMPTED_DATE                  ("attempteddate", SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE),
    COURIER_CODE                    ("couriercode", SubOrderDetailElasticColumn.COURIER_CODE),
    PAYMENT_MODE                    ("paymentmode", SubOrderDetailElasticColumn.PAYMENT_MODE),
    CHANNEL_WISE                    ("channelwise", SubOrderDetailElasticColumn.SOI_CHANNEL_CODE),
    RTO_REASON                      ("rtoreason", SubOrderDetailElasticColumn.RTO_REASON),
    SUPER_CATEGORY                  ("supercategory", SubOrderDetailElasticColumn.SUPER_CATEGORY),
    USER_TYPE                       ("usertype", SubOrderDetailElasticColumn.SOI_USER_TYPE),
    NDR_BUCKET                      ("ndrbucketwise", SubOrderDetailElasticColumn.UD_REASON),
    TRUE                            ("true", null),
    FALSE                           ("false", null),
    TP_CURRENT_STATUS               ("currentstatus", SubOrderDetailElasticColumn.TP_CURRENT_STATUS),
    ITEM_VALUE                      ("itemvalue", SubOrderDetailElasticColumn.ITEM_PRICE),
    FULFILLMENT_CENTER              ("fulfillmentcenter", null),
    COMPLAINT_CATEGORY              ("complaintcategory", SubOrderDetailElasticColumn.COMPLAINT_CATEGORY),
    COMPLAINT_ORIGIN                ("complaintorigin", SubOrderDetailElasticColumn.COMPLAINT_ORIGIN),
    REFUND_INITIATED                ("refundinitiated", SubOrderDetailElasticColumn.REFUND_INITITATED);

    String                      filterKey;
    SubOrderDetailElasticColumn subOrderDetailElasticColumn;

    FilterKey(String filterKey, SubOrderDetailElasticColumn subOrderDetailElasticColumn) {
        this.filterKey = filterKey;
        this.subOrderDetailElasticColumn = subOrderDetailElasticColumn;
    }

    public SubOrderDetailElasticColumn getSubOrderDetailElasticColumn() {
        return subOrderDetailElasticColumn;
    }

    public String getFilterKey() {
        return filterKey;
    }

    public static FilterKey fromElasticColumnName(SubOrderDetailElasticColumn ecn) {
        for (FilterKey filterKey : values()) {
            if (filterKey.subOrderDetailElasticColumn == ecn) {
                return filterKey;
            }
        }
        return null;
    }

    public static FilterKey fromFilterKey(String filterKey) {
        for (FilterKey filter : values()) {
            if (filter.filterKey.equals(filterKey)) {
                return filter;
            }
        }
        return null;
    }
}