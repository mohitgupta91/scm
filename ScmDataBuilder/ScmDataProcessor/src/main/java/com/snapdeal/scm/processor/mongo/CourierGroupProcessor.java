package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.CourierGroupDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.mongo.doc.CourierGroup;
import com.snapdeal.scm.mongo.mao.repository.CourierGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 
 * @author prateek
 *
 */
@Service("CourierGroupProcessor")
public class CourierGroupProcessor extends AbstractMongoProcessor<CourierGroupDTO>{
	
	@Autowired
	private CourierGroupRepository courierGroupRepository;

	private enum Fields {

		COURIER_CODE("courierCode"),
		COURIER_TYPE("courierType"),
		COURIER_GROUP("courierGroup"),
		INTEGRATED("integrated");
		private final String value;

		private Fields(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@Override
	public MongoDataDTO processStandardDTO(CourierGroupDTO courierDto) {
		MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.COURIER_GROUP);
		mongoDataDTO.addQueryKeyValue(Fields.COURIER_CODE.getValue(), courierDto.getCode());
		mongoDataDTO.addInsertKeyObjectValue(Fields.COURIER_TYPE.getValue(), courierDto.getCourierType());
		mongoDataDTO.addInsertKeyObjectValue(Fields.COURIER_GROUP.getValue(), courierDto.getCourierGroup());
		mongoDataDTO.addInsertKeyObjectValue(Fields.INTEGRATED.getValue(), courierDto.getIntegrated());
		return mongoDataDTO;
	}
	
	@Override
	public MongoDocument processRecord(CourierGroupDTO courierDto) {
		String code = courierDto.getCode();
		// check to update without get
		CourierGroup courierGroup = Optional.ofNullable(courierGroupRepository.findByCourierCode(code))
				.orElse(new CourierGroup(code));
		courierGroup.setCourierGroup(courierDto.getCourierGroup());
		courierGroup.setCourierType(courierDto.getCourierType());
		courierGroup.setIntegrated(courierDto.getIntegrated());
		return courierGroup;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.COURIER_GROUP;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MongoRepository getMongoRepository() {
		return courierGroupRepository;
	}
}
