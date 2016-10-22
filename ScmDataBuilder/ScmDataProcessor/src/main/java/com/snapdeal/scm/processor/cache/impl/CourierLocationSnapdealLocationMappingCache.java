package com.snapdeal.scm.processor.cache.impl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.core.mongo.document.CourierLocationSnapdealLocationMapping;
import com.snapdeal.scm.das.mongo.dao.CourierLocationSnapdealLocationMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by harshit on 25/4/16.
 */
@Cache(cacheKey = CacheKey.COURIER_LOCATION_SNAPDEAL_LOCATION_MAPPING, MIN_REPEAT_TIME_IN_HOUR = 24)
public class CourierLocationSnapdealLocationMappingCache implements ICache {


    @Autowired
    CourierLocationSnapdealLocationMappingRepository repository;
    Table<String, String, SnapdealLocation> courierSnapdealMapping = HashBasedTable.create();

    public SnapdealLocation getSnapdealLocation(String courierCode, String locationCode) {
        return courierSnapdealMapping.get(courierCode, locationCode);
    }


    @Override
    public void load() {
        for (CourierLocationSnapdealLocationMapping mapping : repository.findAll()) {
            courierSnapdealMapping.put(mapping.getCourierCode(), mapping.getLocationCode(), new SnapdealLocation(mapping.getSnapdealLocationCity(), mapping.getSnapdealLocationState()));
        }
    }

    public static class SnapdealLocation {
        private String snapdealLocationCity;
        private String snapdealLocationState;

        public SnapdealLocation() {
        }

        public SnapdealLocation(String snapdealLocationCity, String snapdealLocationState) {
            this.snapdealLocationCity = snapdealLocationCity;
            this.snapdealLocationState = snapdealLocationState;
        }

        public String getSnapdealLocationCity() {
            return snapdealLocationCity;
        }

        public void setSnapdealLocationCity(String snapdealLocationCity) {
            this.snapdealLocationCity = snapdealLocationCity;
        }

        public String getSnapdealLocationState() {
            return snapdealLocationState;
        }

        public void setSnapdealLocationState(String snapdealLocationState) {
            this.snapdealLocationState = snapdealLocationState;
        }

        @Override
        public String toString() {
            return "SnapdealLocation{" +
                    "snapdealLocationCity='" + snapdealLocationCity + '\'' +
                    ", snapdealLocationState='" + snapdealLocationState + '\'' +
                    '}';
        }
    }
}
