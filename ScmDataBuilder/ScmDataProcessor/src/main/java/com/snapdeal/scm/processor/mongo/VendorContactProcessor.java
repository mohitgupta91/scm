package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.VendorContactDTO;
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
@Service("VendorContactProcessor")
public class VendorContactProcessor extends AbstractMongoProcessor<VendorContactDTO> {
	
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
	public MongoDataDTO processStandardDTO(VendorContactDTO vendorContactDTO) {
		MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.FULFILLMENT_PROVIDER);
		mongoDataDTO.addQueryKeyValue(Fields.CODE.getValue(), vendorContactDTO.getVendorCode());
		mongoDataDTO.addInsertKeyObjectValue(Fields.PINCODE.getValue(), vendorContactDTO.getPincode());
		mongoDataDTO.addInsertKeyObjectValue(Fields.FULFILLMENT_TYPE.getValue(), FulfillmentType.VENDOR);
		return mongoDataDTO;
	}
	
	@Override
	public MongoDocument processRecord(VendorContactDTO vendorContactDTO) {
		String vendorCode = vendorContactDTO.getVendorCode();
		FulfillmentProvider fulfillmentProvider = Optional.ofNullable(
				fulfillmentProviderRepository.findByCode(vendorCode))
				.orElse(new FulfillmentProvider(vendorCode, FulfillmentType.VENDOR));
		fulfillmentProvider.setPincode(vendorContactDTO.getPincode());
		return fulfillmentProvider;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.VENDOR_CONTACT;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MongoRepository getMongoRepository() {
		return fulfillmentProviderRepository;
	}
}
