package com.snapdeal.scm.common.domain.mongo;

/**
 * 
 * @author prateek
 *
 */
public enum MongoCollectionName {

	SUPC_DETAILS("supc_details"),
	CONNECTED_RAD_STATUS("connected_rad_status"),
	ORIGIN_CITY_EXIT("origin_city_stating_exit_mapping"),
	PINCODE_DC_MAPPING("pincode_dc_mapping"),
	PINCODE_MASTER("pincode_master"),
	METRO_CITY("metro_city"),
	LANE_GROUP("lane_group"),
	FULFILLMENT_PROVIDER("fulfillment_provider"),
	COURIER_GROUP("courier_group"),
	COURIER_LOCATION_SNAPDEAL_LOCATION("courier_location_snapdeal_location_mapping");
	private String collectionsName;
	
	private MongoCollectionName(String collectionsName) {
		this.collectionsName = collectionsName;
	}

	public String getCollectionsName() {
		return collectionsName;
	}
}