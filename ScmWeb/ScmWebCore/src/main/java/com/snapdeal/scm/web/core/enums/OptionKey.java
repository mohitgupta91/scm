package com.snapdeal.scm.web.core.enums;

/**
 *
 * @author prateek
 *
 */
public enum OptionKey {

    MULTIPLE                    ("multiple"),
	CATEGORY                    ("category"),
	OVERALL                     ("overall"),
	LANE_TYPE                   (FilterKey.LANE_TYPE.getFilterKey()),
	COURIER_GROUP               (FilterKey.COURIER_GROUP.getFilterKey()),
	DESTINATION_STATE           (FilterKey.DESTINATION_STATE.getFilterKey()),
	DESTINATION_CITY            (FilterKey.DESTINATION_CITY.getFilterKey()),
	SOURCE_STATE                (FilterKey.SOURCE_STATE.getFilterKey()),
	SOURCE_CITY                 (FilterKey.SOURCE_CITY.getFilterKey()),
    FULFILLMENT_MODEL           (FilterKey.FULFILLMENT_MODEL.getFilterKey()),
	LANE                        (FilterKey.LANE.getFilterKey()),
    RTO_STATES                  ("rtostates"),
    COMPLAINT_CATEGORY          (FilterKey.COMPLAINT_CATEGORY.getFilterKey()),
    COMPLAINT_ORIGIN            (FilterKey.COMPLAINT_ORIGIN.getFilterKey());

    String optionName;

	OptionKey(String optionName) {
        this.optionName = optionName;
	}

	public String getOptionName() {
		return optionName;
	}

    public static OptionKey getOptionByName(String optionName){
        if (optionName == null) return null;
        for (OptionKey optionKey: values()){
            if (optionKey.optionName.equalsIgnoreCase(optionName)) return optionKey;
        }
        return null;
    }
}
