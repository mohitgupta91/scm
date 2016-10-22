package com.snapdeal.scm.enums;

/**
 * Created by harshit.nimbark on 5/30/16.
 */
public enum ComplaintResolution {

    RETURNED("returned"),
    REFUNDED("refunded"),
    PENDING("pending");

    private final String value;

    private ComplaintResolution(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
