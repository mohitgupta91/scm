package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.MetroCityDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.CommonUtils;
import com.snapdeal.scm.mongo.doc.MetroCity;
import com.snapdeal.scm.mongo.mao.repository.MetroCitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * 
 * @author prateek
 *
 */
@Service("MetroCitiesProcessor")
public class MetroCitiesProcessor extends AbstractMongoProcessor<MetroCityDTO>{

	@Autowired
	private MetroCitiesRepository metroCitiesRepository;

	private enum Fields {

		CITY("city"),
		TYPE("type");

		private final String value;

		private Fields(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	@Override
	public QueryType getQueryType() {
		return QueryType.METRO_CITY;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MongoRepository getMongoRepository() {
		return metroCitiesRepository;
	}

	@Override
	public MongoDataDTO processStandardDTO(MetroCityDTO metroCityDTO) {
		MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.METRO_CITY);
		mongoDataDTO.addQueryKeyValue(Fields.CITY.getValue(), CommonUtils.getCityStateMapping(metroCityDTO.getCity(), metroCityDTO.getState()));
		mongoDataDTO.addInsertKeyObjectValue(Fields.TYPE.getValue(), MetroCity.Type.METRO);
		return mongoDataDTO;
	}
	
	@Override
	public MongoDocument processRecord(MetroCityDTO metroCityDTO) {
        String state = metroCityDTO.getState();
        String city = metroCityDTO.getCity();
        String cityState = CommonUtils.getCityStateMapping(city, state);
        MetroCity metroCity = metroCitiesRepository.findByCity(cityState);
        if(metroCity != null)
        	return null;
        metroCity = new MetroCity();
        metroCity.setCity(cityState);
        metroCity.setType(MetroCity.Type.METRO);
        return metroCity;
	}
}