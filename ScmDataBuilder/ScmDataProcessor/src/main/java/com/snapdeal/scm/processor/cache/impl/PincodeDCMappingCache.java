package com.snapdeal.scm.processor.cache.impl;

import com.google.common.base.Objects;
import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.core.mongo.document.PincodeDCMapping;
import com.snapdeal.scm.das.mongo.dao.PincodeDCMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by harshit on 22/4/16.
 */
@Cache(cacheKey = CacheKey.PINCODE_DC_MAPPING, MIN_REPEAT_TIME_IN_HOUR = 24)
public class PincodeDCMappingCache implements ICache {

    @Autowired
    PincodeDCMappingRepository pincodeDCMappingRepository;

    //TODO: placeholder property, maybe useless maybe not.
    Map<CourierModeDetails, DCDetails> pincodeDCMap = new HashMap<>();

    public DCDetails getMappedDC(String pincode, String courierGroup, String shippingModeCode) {
        return pincodeDCMap.get(new CourierModeDetails(pincode, courierGroup, shippingModeCode));
    }

    @Override
    public void load() {
        for (PincodeDCMapping mapping : pincodeDCMappingRepository.findAll()) {
            pincodeDCMap.put(new CourierModeDetails(mapping.getPincode(), mapping.getCourierGroup(), mapping.getShippingModeCode()), new DCDetails(mapping.getDcCity(), mapping.getDcState()));
        }
    }

    public static class DCDetails {
        private String dcCity;
        private String dcState;

        public DCDetails() {
        }

        public DCDetails(String dcCity, String dcState) {
            this.dcCity = dcCity;
            this.dcState = dcState;
        }

        public String getDcCity() {
            return dcCity;
        }

        public void setDcCity(String dcCity) {
            this.dcCity = dcCity;
        }

        public String getDcState() {
            return dcState;
        }

        public void setDcState(String dcState) {
            this.dcState = dcState;
        }

        @Override
        public String toString() {
            return "DCDetails{" +
                    "dcCity='" + dcCity + '\'' +
                    ", dcState='" + dcState + '\'' +
                    '}';
        }
    }

    public static class CourierModeDetails {
        private String pincode;//
        private String courierGroup;//
        private String shippingModeCode;//

        public CourierModeDetails() {
        }

        public CourierModeDetails(String pincode, String courierGroup, String shippingModeCode) {
            this.pincode = pincode;
            this.courierGroup = courierGroup;
            this.shippingModeCode = shippingModeCode;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getCourierGroup() {
            return courierGroup;
        }

        public void setCourierGroup(String courierGroup) {
            this.courierGroup = courierGroup;
        }

        public String getShippingModeCode() {
            return shippingModeCode;
        }

        public void setShippingModeCode(String shippingModeCode) {
            this.shippingModeCode = shippingModeCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CourierModeDetails that = (CourierModeDetails) o;
            return Objects.equal(pincode, that.pincode) &&
                    Objects.equal(courierGroup, that.courierGroup) &&
                    Objects.equal(shippingModeCode, that.shippingModeCode);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(pincode, courierGroup, shippingModeCode);
        }

        @Override
        public String toString() {
            return "CourierModeDetails{" +
                    "pincode='" + pincode + '\'' +
                    ", courierGroup='" + courierGroup + '\'' +
                    ", shippingModeCode='" + shippingModeCode + '\'' +
                    '}';
        }
    }
}
