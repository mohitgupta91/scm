package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.CenterMasterDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.mongo.doc.FulfillmentProvider;
import com.snapdeal.scm.mongo.doc.FulfillmentProvider.FulfillmentType;
import com.snapdeal.scm.mongo.mao.repository.FulfillmentProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * 
 * @author prateek
 *
 */
@Service("CenterMasterProcessor")
public class CenterMasterProcessor extends AbstractMongoProcessor<CenterMasterDTO> {

	@Autowired
	private FulfillmentProviderRepository fulfillmentProviderRepository;

	private enum Fields {

		CODE("code"),
		PINCODE("pincode"),
		FULFILLMENT_TYPE("fulfillmentType");

		private final String value;

		private Fields(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	@Override
	public MongoDataDTO processStandardDTO(CenterMasterDTO centerMasterDTO) {
		MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.FULFILLMENT_PROVIDER);
		mongoDataDTO.addQueryKeyValue(Fields.CODE.getValue(), centerMasterDTO.getCode());
		mongoDataDTO.addInsertKeyObjectValue(Fields.PINCODE.getValue(), centerMasterDTO.getPincode());
		mongoDataDTO.addInsertKeyObjectValue(Fields.FULFILLMENT_TYPE.getValue(), FulfillmentType.CENTER);
		return mongoDataDTO;
	}
	
	@Override
	public MongoDocument processRecord(CenterMasterDTO centerMasterDTO) {
		String code = centerMasterDTO.getCode();
		FulfillmentProvider fulfillmentProvider = Optional.ofNullable(
				fulfillmentProviderRepository.findByCode(code))
				.orElse(new FulfillmentProvider(code, FulfillmentType.CENTER));
		fulfillmentProvider.setPincode(centerMasterDTO.getPincode());
		return fulfillmentProvider;
	}
	
	@Override
	public QueryType getQueryType() {
		return QueryType.CENTER_MASTER;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MongoRepository getMongoRepository() {
		return fulfillmentProviderRepository;
	}
}
