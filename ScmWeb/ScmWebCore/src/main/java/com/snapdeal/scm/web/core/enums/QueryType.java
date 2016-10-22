package com.snapdeal.scm.web.core.enums;

import java.io.Serializable;

/**
 * @author prateek
 */
public enum QueryType implements Serializable {

    GENERIC_JOB_NAME,
    FIRST_PROCESSOR,
    
    SHIPPING_SOI_SD,
    SHIPPING_SOI_SOID,
    SHIPPING_SOI_SP,
    
    COURIER_GROUP,
    CENTER_MASTER,
    VENDOR_CONTACT,
    PINCODE_MASTER,
    
    SUB_ORDER_SP_STATUS_CODES,
    SUB_ORDER_TP_STATUS_CODES,
    SUB_ORDER_SOI_STATUS_CODES,
    
    SP_STATUS_CODE_DETAILS,
    TP_STATUS_CODE_DETAILS,
    SOI_STATUS_CODE_DETAILS,
    
    CAMS_SUPC_SUPER_CATEGORY,
    FILMS_SUPC_MTO;

}