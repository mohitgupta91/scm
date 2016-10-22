package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.FilmsSupcMtoDTO;
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
@Service("FilmsSupcMtoProcessor")
public class FilmsSupcMtoProcessor extends AbstractMongoProcessor<FilmsSupcMtoDTO> {
	
	@Autowired
	private SupcDetailsRepository supcDetailsRepository;

	private enum Fields {

		SUPC("supc"),
		SUPER_CATEGORY("superCategory"),
		MTO("mto");

		private final String value;

		private Fields(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	@Override
	public MongoDataDTO processStandardDTO(FilmsSupcMtoDTO filmsSupcMtoDTO) {
		MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.SUPC_DETAILS);
		mongoDataDTO.addQueryKeyValue(Fields.SUPC.getValue(), filmsSupcMtoDTO.getSupc());
		mongoDataDTO.addInsertKeyObjectValue(Fields.MTO.getValue(), filmsSupcMtoDTO.getMto());
		return mongoDataDTO;
	}
	@Override
	public MongoDocument processRecord(FilmsSupcMtoDTO filmsSupcMtoDTO) {
		String supc =  filmsSupcMtoDTO.getSupc();
		SupcDetails supcDetail = Optional.ofNullable(supcDetailsRepository.findBySupc(supc))
				.orElse(new SupcDetails(supc));
		supcDetail.setMto(filmsSupcMtoDTO.getMto());
		return supcDetail;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.FILMS_SUPC_MTO;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MongoRepository getMongoRepository() {
		return supcDetailsRepository;
	}
}
