package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.LaneGroupDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.mongo.doc.LaneGroup;
import com.snapdeal.scm.mongo.mao.repository.LaneGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by siddhant, prateek on 2/3/16.
 */
@Service("laneGroupProcessor")
public class LaneGroupProcessor extends AbstractMongoProcessor<LaneGroupDTO>{

	@Autowired
	private LaneGroupRepository laneGroupRepository;

	private enum Fields {

		LANE_GROUP("laneGroup"),
		LANES("lanes");

		private final String value;

		private Fields(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@Override
	public MongoDataDTO processStandardDTO(LaneGroupDTO laneGroupDTO) {
		MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.LANE_GROUP);
		mongoDataDTO.addQueryKeyValue(Fields.LANE_GROUP.getValue(), laneGroupDTO.getLaneGroup());
		mongoDataDTO.addInsertKeyObjectValue(Fields.LANES.getValue(), laneGroupDTO.getLanes());
		return mongoDataDTO;
	}

	@Override
	public MongoDocument processRecord(LaneGroupDTO laneGroupDTO) {
		String laneGroup = laneGroupDTO.getLaneGroup();
		LaneGroup laneGrp = java.util.Optional.ofNullable(laneGroupRepository.findByLaneGroup(laneGroup))
				.orElse(new LaneGroup(laneGroup));
		Set<String> lanes = laneGrp.getLanes();
		lanes.addAll(laneGroupDTO.getLanes());
		return laneGrp;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.LANE_GROUP;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MongoRepository getMongoRepository() {
		return laneGroupRepository;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean mergeDataDTO(MongoDataDTO mongoDataDTO, List<MongoDataDTO> mongoDataDTOs) {
		int indexOf = mongoDataDTOs.indexOf(mongoDataDTO);
		if(indexOf >= 0) {
			MongoDataDTO previousMongoDataDTO = mongoDataDTOs.get(indexOf);
			Set<Object> previousValues = (Set<Object>)previousMongoDataDTO.getInsertKeyValues().get(Fields.LANES.getValue());
			previousValues.addAll((Set<Object>)mongoDataDTO.getInsertKeyValues().get(Fields.LANES.getValue()));
			return true;
		}
		return false;
	}
}