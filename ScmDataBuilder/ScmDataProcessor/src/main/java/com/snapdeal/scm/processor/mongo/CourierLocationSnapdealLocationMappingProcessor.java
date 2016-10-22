package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.CourierLocationSnapdealLocationMappingDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.mongo.document.CourierLocationSnapdealLocationMapping;
import com.snapdeal.scm.core.utils.CommonUtils;
import com.snapdeal.scm.das.mongo.dao.CourierLocationSnapdealLocationMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Created by harshit on 25/4/16.
 */
@Service("CourierLocationSnapdealLocationMapping")
public class CourierLocationSnapdealLocationMappingProcessor extends AbstractMongoProcessor<CourierLocationSnapdealLocationMappingDTO> {

    @Autowired
    CourierLocationSnapdealLocationMappingRepository courierLocationSnapdealLocationMappingRepository;

    private enum Fields {
        COURIER_CODE("courierCode"),
        LOCATION_CODE("locationCode"),
        SNAPDEAL_LOCATION_CITY("snapdealLocationCity"),
        SNAPDEAL_LOCATION_STATE("snapdealLocationState"),
        ENABLED("enabled");

        private final String value;

        private Fields(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    @Override
    public MongoDataDTO processStandardDTO(CourierLocationSnapdealLocationMappingDTO dto) {
        MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.COURIER_LOCATION_SNAPDEAL_LOCATION);
        mongoDataDTO.addQueryKeyValue(Fields.COURIER_CODE.getValue(), dto.getCourierCode());
        mongoDataDTO.addQueryKeyValue(Fields.LOCATION_CODE.getValue(), dto.getLocationCode());
        mongoDataDTO.addInsertKeyObjectValue(Fields.SNAPDEAL_LOCATION_CITY.getValue(), CommonUtils.getCityStateMapping(dto.getSnapdealLocationCity(), dto.getSnapdealLocationState()));
        mongoDataDTO.addInsertKeyObjectValue(Fields.SNAPDEAL_LOCATION_STATE.getValue(), CommonUtils.getFormattedValue(dto.getSnapdealLocationState()));
        return mongoDataDTO;
    }

    @Override
    public MongoDocument processRecord(CourierLocationSnapdealLocationMappingDTO dto) {
        CourierLocationSnapdealLocationMapping mapping = new CourierLocationSnapdealLocationMapping();
        mapping.setCourierCode(dto.getCourierCode());
        mapping.setEnabled(dto.getEnabled());
        mapping.setLocationCode(dto.getLocationCode());
        mapping.setSnapdealLocationCity(CommonUtils.getCityStateMapping(dto.getSnapdealLocationCity(), dto.getSnapdealLocationState()));
        mapping.setSnapdealLocationState(CommonUtils.getFormattedValue(dto.getSnapdealLocationState()));
        return mapping;
    }

	@SuppressWarnings("rawtypes")
    @Override
    public MongoRepository getMongoRepository() {
        return courierLocationSnapdealLocationMappingRepository;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.COURIER_LOCATION_SNAPDEAL_LOCATION_MAPPING;
    }
}
