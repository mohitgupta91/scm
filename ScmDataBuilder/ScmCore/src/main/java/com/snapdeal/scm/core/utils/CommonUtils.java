package com.snapdeal.scm.core.utils;

import com.google.common.base.Joiner;
import com.snapdeal.scm.core.configuration.ConfigUtils;
import com.snapdeal.scm.core.configuration.Property;

/**
 * Created by siddhant on 3/3/16.
 */
public class CommonUtils {

	public static String getCityStateMapping (String city, String state) {
		if(city == null || state == null) {
			return null;
		}
		String cityValue = getFormattedValue(city);
		String stateValue = getFormattedValue(state);
		String stateCode = ConfigUtils.getMapValue(Property.STATE_CODE, stateValue);
		return Joiner.on(StringUtils.DASH).join(cityValue, stateCode);
	}

	public static String getFormattedValue(String value){
		String[] splitCity = value.split(StringUtils.SPACE);
		StringBuilder finalValue = new StringBuilder();
		for (int i = 0; i< splitCity.length ; i++) {
			finalValue.append(splitCity[i].trim().toLowerCase());
		}
		return finalValue.toString();
	}

	public static String generateLane (String sourceCity, String sourceState, String destinationCity , String destinationState ) {
		String sourceCityStateMapping = getCityStateMapping(sourceCity,sourceState);
		String destinationCityStateMapping = getCityStateMapping(destinationCity,destinationState);
		return generateLane(sourceCityStateMapping, destinationCityStateMapping);
	}

	public static String generateLane (String sourceCityState, String destinationCityState) {
		if(sourceCityState != null && destinationCityState != null) {
			return Joiner.on(StringUtils.DOUBLE_COLON).join(sourceCityState,destinationCityState);
		}
		return null;
	}
}