package com.snapdeal.scm.cache;

/**
 * @author chitransh
 */
public enum CacheKey {

    JOBS                                            ("Jobs", CacheGroup.DATA_POLLER),
    LANE_GROUP                                      ("LaneGroup", CacheGroup.WEB_ALL),
    POLLER                                          ("Poller", CacheGroup.DATA_POLLER),
    SCM_PROPERTY                                    ("ScmProperty", CacheGroup.DATA_ALL, CacheGroup.WEB_ALL),
    SCM_WEB_PROPERTY                                ("ScmWebProperty", CacheGroup.WEB_ALL),
    ROLE_PERMISSIONS                                ("RolePermissions", CacheGroup.WEB_ALL),
    STATUS_CODES                                    ("StatusCodes", CacheGroup.DATA_PROCESSOR),
    PINCODE_DC_MAPPING                              ("PincodeDCMapping", CacheGroup.DATA_PROCESSOR),
    FULFILLMENT_PROVIDER                            ("FulfillmentProvider", CacheGroup.DATA_PROCESSOR, CacheGroup.WEB_ALL),
    DTO_QUERY_FIELD_MAP                             ("DtoQueryFieldMap", CacheGroup.DATA_FILE_HANDLER),
    ORIGIN_CITY_EXIT_MAPPING                        ("OriginCityExitMapping", CacheGroup.DATA_PROCESSOR),
    METRO_CITY                                      ("MetroCity", CacheGroup.WEB_ALL, CacheGroup.DATA_PROCESSOR),
    COURIER_GROUP                                   ("CourierGroup", CacheGroup.WEB_ALL, CacheGroup.DATA_PROCESSOR),
    SUPC_CACHE										("supc", CacheGroup.DATA_PROCESSOR),
    PINCODE_MASTER                                  ("PincodeMaster", CacheGroup.WEB_ALL, CacheGroup.DATA_PROCESSOR),
    COURIER_LOCATION_SNAPDEAL_LOCATION_MAPPING      ("CourierLocationSnapdealLocationMapping", CacheGroup.DATA_PROCESSOR),
    SHIPPED_NOT_CON_METRICS                         ("ShippedNotConMetricsCache", CacheGroup.WEB_ALL),
    ATTEMPT_NOT_DEL_METRICS                         ("AttemptNotDelMetricsCache", CacheGroup.WEB_ALL),
    OFD_NOT_ATTEMPT_METRICS                         ("OfdNotAttemptMetricsCache", CacheGroup.WEB_ALL),
    CON_NOT_REACHED_METRICS                         ("ConNotReachedMetricsCache", CacheGroup.WEB_ALL),
    REACHED_NOT_OFD_METRICS                         ("ReachedNotOFDMetricsCache", CacheGroup.WEB_ALL),
    ATTEMPT_NOT_DEL_NON_INTEGRATED_METRICS          ("AttemptNotDelNonIntgrMetricsCache", CacheGroup.WEB_ALL),
    SHIP_NOT_ATTEMPT_NON_INTEGRATED_METRICS         ("ShipNotAttemptNonIntgrMetricsCache", CacheGroup.WEB_ALL),
    UD1_NOT_UD2_METRICS                             ("UD1NotUD2MetricsCache", CacheGroup.WEB_ALL),
    UD2_NOT_UD3_METRICS                             ("UD2NotUD3MetricsCache", CacheGroup.WEB_ALL),
    UD3_NOT_UD4_METRICS                             ("UD3NotUD4MetricsCache", CacheGroup.WEB_ALL),
    UD4_NOT_DEL_METRICS                             ("UD4NotDelMetricsCache", CacheGroup.WEB_ALL),
    SUPER_CATEGORY                                  ("SuperCategoryCache", CacheGroup.WEB_ALL),
    COMPLAINT_TYPE                                  ("ComplaintType", CacheGroup.DATA_PROCESSOR);

    String cacheName;
    CacheGroup[] cacheGroups;

    CacheKey(String cacheName, CacheGroup... cacheGroups) {
        this.cacheName = cacheName;
        this.cacheGroups = cacheGroups;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public CacheGroup[] getCacheGroups() {
        return cacheGroups;
    }

    public void setCacheGroups(CacheGroup[] cacheGroups) {
        this.cacheGroups = cacheGroups;
    }

    public boolean hasCacheGroup(CacheGroup cacheGroup) {
        for (CacheGroup group : this.cacheGroups) {
            if (group.equals(cacheGroup)) {
                return true;
            }
        }
        return false;
    }
}
