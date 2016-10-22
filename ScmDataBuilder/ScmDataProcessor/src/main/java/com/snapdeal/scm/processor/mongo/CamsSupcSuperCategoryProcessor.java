package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.CamsSupcSuperCategoryDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.mongo.doc.SupcDetails;
import com.snapdeal.scm.mongo.mao.repository.SupcDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 
 * @author prateek
 *
 */
@Service("CamsSupcSuperCategoryProcessor")
public class CamsSupcSuperCategoryProcessor extends AbstractMongoProcessor<CamsSupcSuperCategoryDTO>{

	private static final String SUPC = "supc";
	private static final String SUPER_CATEGORY = "superCategory";
	@Autowired
	private SupcDetailsRepository supcDetailsRepository;

	@Override
	public MongoDataDTO processStandardDTO(CamsSupcSuperCategoryDTO camsSupcSuperCategoryDTO) {
		MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.SUPC_DETAILS);
		mongoDataDTO.addQueryKeyValue(SUPC, camsSupcSuperCategoryDTO.getSupc());
		mongoDataDTO.addInsertKeyObjectValue(SUPER_CATEGORY, camsSupcSuperCategoryDTO.getSuperCategory());
		return mongoDataDTO;
	}
	
	@Override
	public MongoDocument processRecord(CamsSupcSuperCategoryDTO camsSupcSuperCategoryDTO) {
		String supc =  camsSupcSuperCategoryDTO.getSupc();
		SupcDetails supcDetail = Optional.ofNullable(supcDetailsRepository.findBySupc(supc))
				.orElse(new SupcDetails(supc));
		supcDetail.setSuperCategory(camsSupcSuperCategoryDTO.getSuperCategory());
		return supcDetail;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.CAMS_SUPC_SUPER_CATEGORY;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MongoRepository getMongoRepository() {
		return supcDetailsRepository;
	}

}
