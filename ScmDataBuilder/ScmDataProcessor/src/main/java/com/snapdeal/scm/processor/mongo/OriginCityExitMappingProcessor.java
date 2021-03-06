package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.OriginCityExitMappingDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.mongo.document.OriginCityExitMapping;
import com.snapdeal.scm.core.utils.CommonUtils;
import com.snapdeal.scm.das.mongo.dao.OriginCityExitMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Created by harshit on 25/4/16.
 */
@Service("OriginCityExitMapping")
public class OriginCityExitMappingProcessor extends AbstractMongoProcessor<OriginCityExitMappingDTO> {

    @Autowired
    OriginCityExitMappingRepository originCityExitMappingRepository;

    private enum Fields {
        COURIER_GROUP("courierGroup"),
        SHIPPING_MODE("shippingMode"),
        FIRST_LOCATION_CITY("firstLocationCity"),
        FIRST_LOCATION_STATE("firstLocationState"),
        ORIGIN_CITY_EXIT("originCityExit"),
        COURIER_STATUS("courierStatus"),
        COURIER_REMARKS("courierRemarks"),
        SNAPDEAL_STATUS("snapdealStatus"),
        FIRST_LOCATION("firstLocation"),
        DESTINATION_CITY("destinationCity"),
        DESTINATION_STATE("destinationState");

        private final String value;

        private Fields(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    @Override
    public MongoDocument processRecord(OriginCityExitMappingDTO dto) {
        OriginCityExitMapping originCityExitMapping = new OriginCityExitMapping();//TODO: yet to be decided whether new or not
        originCityExitMapping.setCourierGroup(dto.getCourierGroup());
        originCityExitMapping.setShippingMode(dto.getShippingMode());
        originCityExitMapping.setFirstLocationCity(CommonUtils.getCityStateMapping(dto.getFirstLocationCity(), dto.getFirstLocationState()));
        originCityExitMapping.setFirstLocationState(CommonUtils.getFormattedValue(dto.getFirstLocationState()));
        originCityExitMapping.setCourierRemarks(dto.getCourierRemarks());
        originCityExitMapping.setCourierStatus(dto.getCourierStatus());
        originCityExitMapping.setSnapdealStatus(dto.getSnapdealStatus());
        originCityExitMapping.setDestinationCity(CommonUtils.getCityStateMapping(dto.getDestinationCity(), dto.getDestinationState()));
        originCityExitMapping.setDestinationState(CommonUtils.getFormattedValue(dto.getDestinationState()));
        originCityExitMapping.setFirstLocation(dto.getFirstLocation());
        return originCityExitMapping;
    }

	@Override
	public MongoDataDTO processStandardDTO(OriginCityExitMappingDTO dto) {
        MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.ORIGIN_CITY_EXIT);
        mongoDataDTO.addQueryKeyValue(Fields.COURIER_GROUP.getValue(), dto.getCourierGroup());
        mongoDataDTO.addQueryKeyValue(Fields.SHIPPING_MODE.getValue(), dto.getShippingMode());
        mongoDataDTO.addQueryKeyValue(Fields.FIRST_LOCATION_CITY.getValue(), CommonUtils.getCityStateMapping(dto.getFirstLocationCity(), dto.getFirstLocationState()));
        mongoDataDTO.addQueryKeyValue(Fields.FIRST_LOCATION_STATE.getValue(), CommonUtils.getFormattedValue(dto.getFirstLocationState()));
        
        mongoDataDTO.addQueryKeyValue(Fields.COURIER_STATUS.getValue(), dto.getCourierStatus());
        mongoDataDTO.addQueryKeyValue(Fields.COURIER_REMARKS.getValue(), dto.getCourierRemarks());
        mongoDataDTO.addQueryKeyValue(Fields.SNAPDEAL_STATUS.getValue(), dto.getSnapdealStatus());
        mongoDataDTO.addQueryKeyValue(Fields.FIRST_LOCATION.getValue(), dto.getFirstLocation());
        mongoDataDTO.addQueryKeyValue(Fields.DESTINATION_CITY.getValue(), dto.getDestinationCity());
        mongoDataDTO.addQueryKeyValue(Fields.DESTINATION_STATE.getValue(), dto.getDestinationState());
        
        mongoDataDTO.addInsertKeyObjectValue(Fields.ORIGIN_CITY_EXIT.getValue(), dto.getOriginCityExit());
        return mongoDataDTO;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public MongoRepository getMongoRepository() {
        return originCityExitMappingRepository;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.ORIGIN_CITY_EXIT_MAPPING;
    }
}
