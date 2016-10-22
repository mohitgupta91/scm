package com.snapdeal.scm.alerts;

/**
 * Created by ashwini.kumar on 10/05/16.
 */
public enum GroupLogic {
    OVERALL("overall"), LANE_TYPE("lanetype"), COURIER_LANE("courierlane"), COURIER("courier");
    String name;

    GroupLogic(String name) {
        this.name = name;
    }

    public static GroupLogic fromGroupLogic(String logic) {
        for (GroupLogic groupLogic : values()) {
            if (groupLogic.name.equals(logic)) {
                return groupLogic;
            }
        }
        return null;
    }
}
