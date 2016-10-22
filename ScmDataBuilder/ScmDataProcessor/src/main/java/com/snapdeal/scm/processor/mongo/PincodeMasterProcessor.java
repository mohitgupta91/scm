package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.PincodeMasterDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.CommonUtils;
import com.snapdeal.scm.mongo.doc.PincodeMaster;
import com.snapdeal.scm.mongo.mao.repository.PincodeMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 
 * @author prateek
 *
 */
@Service("PincodeMasterProcessor")
public class PincodeMasterProcessor extends AbstractMongoProcessor<PincodeMasterDTO>{
	
	@Autowired
	private PincodeMasterRepository pincodeMasterRepository;

	private enum Fields {
		PINCODE("pincode"),
		CITY("city"),
		STATE("state"),
		ZONE("zone"),
		SC_CITY("scCity"),
		SC_STATE("scState"),
		SC_ZONE("scZone"),
		BDE_ZONE("bdeZone");

		private final String value;

		private Fields(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}
	@Override
	public MongoDataDTO processStandardDTO(PincodeMasterDTO dto) {
		MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.PINCODE_MASTER);
		mongoDataDTO.addQueryKeyValue(Fields.PINCODE.getValue(), dto.getPincode());
		mongoDataDTO.addInsertKeyObjectValue(Fields.CITY.getValue(), dto.getCity());
		mongoDataDTO.addInsertKeyObjectValue(Fields.STATE.getValue(), dto.getState());
		mongoDataDTO.addInsertKeyObjectValue(Fields.ZONE.getValue(), dto.getZone());
		mongoDataDTO.addInsertKeyObjectValue(Fields.SC_CITY.getValue(), dto.getScCity());
		mongoDataDTO.addInsertKeyObjectValue(Fields.SC_STATE.getValue(), dto.getScState());
		mongoDataDTO.addInsertKeyObjectValue(Fields.SC_ZONE.getValue(), dto.getScZone());
		mongoDataDTO.addInsertKeyObjectValue(Fields.BDE_ZONE.getValue(), dto.getBdeZone());
		return mongoDataDTO;
	}
	
	@Override
	public MongoDocument processRecord(PincodeMasterDTO pincodeMasterDTO) {
		String pincode = pincodeMasterDTO.getPincode();
		PincodeMaster pincodeMaster = Optional.ofNullable(pincodeMasterRepository.findByPincode(pincode))
				.orElse(new PincodeMaster(pincode));
		pincodeMaster.setCity(CommonUtils.getCityStateMapping(pincodeMasterDTO.getCity(),pincodeMasterDTO.getState()));
		pincodeMaster.setState(CommonUtils.getFormattedValue(pincodeMasterDTO.getState()));
		pincodeMaster.setZone(pincodeMasterDTO.getZone());
		pincodeMaster.setBdeZone(pincodeMasterDTO.getBdeZone());
		pincodeMaster.setScCity(pincodeMasterDTO.getScCity());
		pincodeMaster.setScState(pincodeMasterDTO.getScState());
		pincodeMaster.setScZone(pincodeMasterDTO.getScZone());
		return pincodeMaster;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.PINCODE_MASTER;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MongoRepository getMongoRepository() {
		return pincodeMasterRepository;
	}
}