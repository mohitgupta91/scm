package com.snapdeal.scm.core.elastic.dto;

public enum SubOrderDetailElasticColumn {


	// Key
	SUB_ORDER_CODE                                  ("sub_order_code"),

	// Processor ShippingSoiSd
	MTO                                             ("mto"),
	LANE                                            ("lane"),
	METRO                                           ("metro"),
	ON_HOLD                                         ("on_hold"),
	LANE_TYPE                                       ("lane_type"),
	ITEM_PRICE                                      ("item_price"),
	CENTER_CODE										("center_code"),
	PAYMENT_MODE                                    ("payment_mode"),
	SUPER_CATEGORY                                  ("super_category"),
	FULFILLMENT_MODEL                               ("fulfillment_model"),
	ORDER_CREATED_DATE                              ("order_created_date"),

	SOURCE_ZONE                                     ("source_zone"),
	SOURCE_CITY                                     ("source_city"),
	SOURCE_NODE                                     ("source_node"),
	SOURCE_TIER                                     ("source_tier"),
	SOURCE_STATE                                    ("source_state"),
	SOURCE_REGION                                   ("source_region"),

	DESTINATION_CITY                                ("destination_city"),
	DESTINATION_ZONE                                ("destination_zone"),
	DESTINATION_NODE                                ("destination_node"),
	DESTINATION_TIER                                ("destination_tier"),
	DESTINATION_STATE                               ("destination_state"),
	DESTINATION_REGION                              ("destination_region"),

	// Processor ShippingSoiSoid
	SHIPPING_MODE                                   ("shipping_mode"),
	DELIVERY_TYPE                                   ("delivery_type"),

	// Processor ShippingSoiSp
	COURIER_TYPE                                    ("courier_type"),
	COURIER_GROUP                                   ("courier_group"),
	COURIER_CODE                                    ("courier_code"),
	EXPECTED_DELIVERY_DATE                          ("expected_delivery_date"),
	EXPECTED_DELIVERY_DATE_RANGE_START              ("expected_delivery_date_range_start"),

    // Processor SubOrderSoiStatusCodes
    SOI_SDH_STATUS_DATE                             ("soi_sdh_status_date"),
    SOI_PDI_STATUS_DATE                             ("soi_pdi_status_date"),
    SOI_PDC_STATUS_DATE                             ("soi_pdc_status_date"),
    SOI_PLI_STATUS_DATE                             ("soi_pli_status_date"),
    SOI_PLC_STATUS_DATE                             ("soi_plc_status_date"),
    SOI_CHANNEL_CODE                                ("soi_channel_code"),//not used yet
    SOI_USER_TYPE                                   ("soi_user_type"),//not used yet

	// Processor SubOrderSpStatusCodes
	SP_COURIER_READY_FOR_PICKUP_STATUS_DATE         ("sp_courier_ready_for_pickup_status_date"), // 50 - manifested
	SP_AWB_UPLOADED_STATUS_DATE                     ("sp_awb_uploaded_status_date"), // 52 - shipped
	SP_COURIER_RETURNED_STATUS_DATE                 ("sp_courier_returned_status_date"), // 80
	SP_RETURN_TRACKING_COMPLETE_STATUS_DATE         ("sp_return_tracking_complete_status_date"), // 81
	SP_RTO_UNDELIVERED_STATUS_DATE                  ("sp_rto_undelivered_status_date"), //  96
	SP_VENDOR_RETURN_CONFIRMED_STATUS_DATE			("sp_vendor_return_confirmed_status_date"), // 90
	SP_PACKAGE_LOSS_INITIATED_STATUS_DATE           ("sp_package_loss_initiated_status_date"), // 160
	SP_PACKAGE_LOSS_CONFIRMED_STATUS_DATE           ("sp_package_loss_confirmed_status_date"), // 164

	// Processor SubOrderTpStatusCodes
	TP_OFD_STATUS_DATE                              ("tp_ofd_status_date"),
	TP_FIRST_UDL_STATUS_DATE						("tp_udl_status_date_1"),
	TP_RTI_STATUS_DATE                              ("tp_rti_status_date"),
	TP_DEL_STATUS_DATE                              ("tp_del_status_date"),
	TP_SECOND_UDL_STATUS_DATE						("tp_udl_status_date_2"),
	TP_THIRD_UDL_STATUS_DATE						("tp_udl_status_date_3"),
	TP_FOURTH_UDL_STATUS_DATE						("tp_udl_status_date_4"),
	TP_FIRST_UDL_REASON                             ("tp_first_udl_reason"),
	TP_SECOND_UDL_REASON                            ("tp_second_udl_reason"),
	TP_THIRD_UDL_REASON                             ("tp_third_udl_reason"),
	TP_FOURTH_UDL_REASON                            ("tp_fourth_udl_reason"),
	TP_CONNECTED_STATUS_DATE                        ("tp_connected_status_date"),
	UD_REASON                                       ("ud_reason"),
	TP_RAD_STATUS_DATE                              ("tp_rad_status_date"),
	RTO_REASON										("rto_reason"),
	TP_CURRENT_STATUS                               ("tp_current_status"),
	TP_CURRENT_STATUS_DATE                          ("tp_current_status_date"),
	TP_COURIER_ORIGIN_CITY                          ("tp_courier_origin_city"),
	TP_COURIER_ORIGIN_STATE                         ("tp_courier_origin_state"),
	TP_COURIER_DESTINATION_CITY                     ("tp_courier_destination_city"),
	TP_COURIER_DESTINATION_STATE                    ("tp_courier_destination_state"),
	COURIER_ORIGIN_CITY                             ("courier_origin_city"),
	COURIER_ORIGIN_STATE                            ("courier_origin_state"),
	COURIER_DESTINATION_CITY                        ("courier_destination_city"),
	COURIER_DESTINATION_STATE                       ("courier_destination_state"),

	// Compute using elastic script
	ORDER_TO_SHIPPED                                ("order_to_shipped"),
	SHIPPED_TO_DELIVERED                            ("shipped_to_delivered"),
	ORDER_TO_DELIVERED                              ("order_to_delivered"),
	ATTEMPTED_STATUS_DATE                           ("attempted_status_date"),
    CLOSED_STATUS_DATE                              ("closed_status_date"),
    RTO_DATE                                        ("rto_date"),
	CLOSED_DATE                                     ("closed_date"),
	LOST_AND_DAMAGED_DATE							("lost_and_damaged_date"),

	// Creates updated for elastic search
	CREATED_ON                                      ("created_on"),
	UPDATED_ON                                      ("updated_on"),

    // Complaints
    COMPLAINT_CATEGORY					            ("complaint_category"),
    COMPLAINT_ORIGIN					            ("complaint_origin"),
    REFUND_INITITATED								("refund_initiated"),
	COMPLAINT_RESOLUTION							("complaint_resolution"),
	CC_CREATED_DATE									("cc_created_date"),
	CC_CLOSED_DATE									("cc_closed_date"),

    // Load Allocation
    ALLOCATION_DATE                                 ("allocation_date");

    private String columnName;

    SubOrderDetailElasticColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getName() {
        return columnName;
    }

    @Override
    public String toString() {
        return columnName;
    }

    public static SubOrderDetailElasticColumn fromCode(String code) {
        for (SubOrderDetailElasticColumn subOrderDetailElasticColumn : values()) {
            if (subOrderDetailElasticColumn.columnName.equals(code)) {
                return subOrderDetailElasticColumn;
            }
        }
        return null;
    }


}
