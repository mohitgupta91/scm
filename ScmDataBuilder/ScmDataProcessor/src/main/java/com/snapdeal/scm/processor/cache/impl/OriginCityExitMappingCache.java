package com.snapdeal.scm.processor.cache.impl;

import com.google.common.base.Objects;
import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.core.mongo.document.OriginCityExitMapping;
import com.snapdeal.scm.das.mongo.dao.OriginCityExitMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by harshit on 25/4/16.
 */
@Cache(cacheKey = CacheKey.ORIGIN_CITY_EXIT_MAPPING, MIN_REPEAT_TIME_IN_HOUR = 24)
public class OriginCityExitMappingCache implements ICache {

    @Autowired
    OriginCityExitMappingRepository repository;

    Map<CourierModeLocationDetail, List<OriginCityExitMapping>> courierModeLocationDetailListMap = new HashMap<>();

   /* public StatusDetails getStatusDetails(String courierGroup, String shippingMode, String firstLocationCity, String courierStatus, String courierRemarks, String snapdealStatus) {
        List<OriginCityExitMapping> mappingList = courierModeLocationDetailListMap.get(new CourierModeLocationDetail(courierGroup, shippingMode, firstLocationCity));
        if (mappingList != null) {
            for (OriginCityExitMapping mapping : mappingList) {
                String firstLocation = mapping.getFirstLocation();
                String destinationCity = mapping.getDestinationCity();
                String destinationState = mapping.getDestinationState();
                if (courierStatus.equals(mapping.getCourierStatus()) && courierRemarks.equals(mapping.getCourierRemarks()) && snapdealStatus.equals(mapping.getSnapdealStatus())) {
                    if (firstLocation != null && destinationCity != null && destinationState != null) {
                        return new StatusDetails(mapping.getCourierStatus(), mapping.getCourierRemarks(), mapping.getSnapdealStatus());
                    } else if (firstLocation != null && StringUtils.isEmpty(destinationCity) && destinationState != null) {
                        return new StatusDetails(mapping.getCourierStatus(), mapping.getCourierRemarks(), mapping.getSnapdealStatus());

                    } else if (StringUtils.isEmpty(firstLocation) && destinationCity != null && StringUtils.isEmpty(destinationState)) {
                        return new StatusDetails(mapping.getCourierStatus(), mapping.getCourierRemarks(), mapping.getSnapdealStatus());

                    } else if (StringUtils.isEmpty(firstLocation) && StringUtils.isEmpty(destinationCity) && destinationState != null) {
                        return new StatusDetails(mapping.getCourierStatus(), mapping.getCourierRemarks(), mapping.getSnapdealStatus());

                    } else if (firstLocation != null && StringUtils.isEmpty(destinationCity) && StringUtils.isEmpty(destinationState)) {
                        return new StatusDetails(mapping.getCourierStatus(), mapping.getCourierRemarks(), mapping.getSnapdealStatus());

                    } else if (StringUtils.isEmpty(firstLocation) && StringUtils.isEmpty(destinationCity) && StringUtils.isEmpty(destinationState)) {
                        return new StatusDetails(mapping.getCourierStatus(), mapping.getCourierRemarks(), mapping.getSnapdealStatus());
                    }
                }
            }
        }
        return null;
    }*/

    public OriginCityExitMapping getStatusDetails(String courierGroup, String shippingMode, String courierOriginCity,
                                          String firstLocationHub, String courierDestinationCity, String courierDestinationState) {
        List<OriginCityExitMapping> mappingList = courierModeLocationDetailListMap.get(new CourierModeLocationDetail(courierGroup, shippingMode, courierOriginCity));
        if (mappingList != null) {
            for (OriginCityExitMapping mapping : mappingList) {
                String firstLocation = mapping.getFirstLocation();
                String destinationCity = mapping.getDestinationCity();
                String destinationState = mapping.getDestinationState();
                if (firstLocation.equals(firstLocationHub) && destinationCity.equals(courierDestinationCity) && destinationState.equals(courierDestinationState)) {
                    return mapping;
                } else if (firstLocation.equals(firstLocationHub) && !destinationCity.equals(courierDestinationCity) && destinationState.equals(courierDestinationState)) {
                    return mapping;
                } else if (!firstLocation.equals(firstLocationHub) && destinationCity.equals(courierDestinationCity) && !destinationState.equals(courierDestinationState)) {
                	return mapping;
                } else if (!firstLocation.equals(firstLocationHub) && !destinationCity.equals(courierDestinationCity) && destinationState.equals(courierDestinationState)) {
                	return mapping;
                } else if (firstLocation.equals(firstLocationHub) && !destinationCity.equals(courierDestinationCity) && !destinationState.equals(courierDestinationState)) {
                	return mapping;
                } else if (!firstLocation.equals(firstLocationHub) && !destinationCity.equals(courierDestinationCity) && !destinationState.equals(courierDestinationState)) {
                	return mapping;
                }
            }
        }
        return null;
    }

    @Override
    public void load() {
        for (OriginCityExitMapping mapping : repository.findAll()) {
            courierModeLocationDetailListMap.computeIfAbsent(new CourierModeLocationDetail(mapping.getCourierGroup(), mapping.getShippingMode(), mapping.getFirstLocationCity()), v -> new ArrayList<>()).add(mapping);
        }
    }

    public static class StatusDetails {
        private String courierStatus;
        private String courierRemarks;
        private String snapdealStatus;

        public StatusDetails() {
        }

        public StatusDetails(String courierStatus, String courierRemarks, String snapdealStatus) {
            this.courierStatus = courierStatus;
            this.courierRemarks = courierRemarks;
            this.snapdealStatus = snapdealStatus;
        }

        public String getCourierStatus() {
            return courierStatus;
        }

        public void setCourierStatus(String courierStatus) {
            this.courierStatus = courierStatus;
        }

        public String getCourierRemarks() {
            return courierRemarks;
        }

        public void setCourierRemarks(String courierRemarks) {
            this.courierRemarks = courierRemarks;
        }

        public String getSnapdealStatus() {
            return snapdealStatus;
        }

        public void setSnapdealStatus(String snapdealStatus) {
            this.snapdealStatus = snapdealStatus;
        }

        @Override
        public String toString() {
            return "StatusDetails{" +
                    "courierStatus='" + courierStatus + '\'' +
                    ", courierRemarks='" + courierRemarks + '\'' +
                    ", snapdealStatus='" + snapdealStatus + '\'' +
                    '}';
        }
    }

    public static class CourierModeLocationDetail {
        private String courierGroup;
        private String shippingMode;
        private String firstLocationCity;

        public CourierModeLocationDetail() {
        }

        public CourierModeLocationDetail(String courierGroup, String shippingMode, String firstLocationCity) {
            this.courierGroup = courierGroup;
            this.shippingMode = shippingMode;
            this.firstLocationCity = firstLocationCity;
        }

        public String getCourierGroup() {
            return courierGroup;
        }

        public void setCourierGroup(String courierGroup) {
            this.courierGroup = courierGroup;
        }

        public String getShippingMode() {
            return shippingMode;
        }

        public void setShippingMode(String shippingMode) {
            this.shippingMode = shippingMode;
        }

        public String getFirstLocationCity() {
            return firstLocationCity;
        }

        public void setFirstLocationCity(String firstLocationCity) {
            this.firstLocationCity = firstLocationCity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CourierModeLocationDetail that = (CourierModeLocationDetail) o;
            return Objects.equal(courierGroup, that.courierGroup) &&
                    Objects.equal(shippingMode, that.shippingMode) &&
                    Objects.equal(firstLocationCity, that.firstLocationCity);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(courierGroup, shippingMode, firstLocationCity);
        }

        @Override
        public String toString() {
            return "CourierModeLocationDetail{" +
                    "courierGroup='" + courierGroup + '\'' +
                    ", shippingMode='" + shippingMode + '\'' +
                    ", firstLocationCity='" + firstLocationCity + '\'' +
                    '}';
        }

    }
}
