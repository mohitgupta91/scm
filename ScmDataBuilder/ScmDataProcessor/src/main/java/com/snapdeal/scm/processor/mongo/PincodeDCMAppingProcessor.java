package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.PincodeDCMappingDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.mongo.document.PincodeDCMapping;
import com.snapdeal.scm.core.utils.CommonUtils;
import com.snapdeal.scm.das.mongo.dao.PincodeDCMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Created by harshit on 22/4/16.
 */
@Service("PincodeDCMAppingProcessor")
public class PincodeDCMAppingProcessor extends AbstractMongoProcessor<PincodeDCMappingDTO> {

    @Autowired
    PincodeDCMappingRepository pincodeDCMappingRepository;

    private enum Fields {
        PINCODE("pincode"),
        COURIER_GROUP("courierGroup"),
        SHIPPING_MODE_CODE("shippingModeCode"),
        DELIVERY_CENTRE("deliveryCenter"),
        DC_CITY("dcCity"),
        DC_STATE("dcState");

        private final String value;

        private Fields(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    public MongoDataDTO processStandardDTO(PincodeDCMappingDTO dto) {
        MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.PINCODE_DC_MAPPING);
        mongoDataDTO.addQueryKeyValue(Fields.PINCODE.getValue(), dto.getPincode());
        mongoDataDTO.addQueryKeyValue(Fields.SHIPPING_MODE_CODE.getValue(), dto.getShippingModeCode());
        mongoDataDTO.addQueryKeyValue(Fields.COURIER_GROUP.getValue(), dto.getCourierGroup());
        mongoDataDTO.addInsertKeyObjectValue(Fields.DELIVERY_CENTRE.getValue(), dto.getDeliveryCenter());
        mongoDataDTO.addInsertKeyObjectValue(Fields.DC_CITY.getValue(), dto.getDcCity());
        mongoDataDTO.addInsertKeyObjectValue(Fields.DC_STATE.getValue(), dto.getDcState());
        return mongoDataDTO;
    }

    @Override
    public MongoDocument processRecord(PincodeDCMappingDTO pincodeDCMappingDTO) {
        PincodeDCMapping pincodeDCMapping = new PincodeDCMapping();//TODO: yet to be decided whether new or not
        pincodeDCMapping.setCourierGroup(pincodeDCMappingDTO.getCourierGroup());
        pincodeDCMapping.setDcCity(CommonUtils.getCityStateMapping(pincodeDCMappingDTO.getDcCity(), pincodeDCMappingDTO.getDcState()));
        pincodeDCMapping.setDcState(CommonUtils.getFormattedValue(pincodeDCMappingDTO.getDcState()));
        pincodeDCMapping.setDeliveryCenter(pincodeDCMappingDTO.getDeliveryCenter());
        pincodeDCMapping.setPincode(pincodeDCMappingDTO.getPincode());
        pincodeDCMapping.setShippingModeCode(pincodeDCMappingDTO.getShippingModeCode());
        return pincodeDCMapping;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.PINCODE_DC_MAPPING;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public MongoRepository getMongoRepository() {
        return pincodeDCMappingRepository;
    }
}
