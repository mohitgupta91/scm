package com.snapdeal.scm.mongo.mao;

import java.util.List;

import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;

/**
 * 
 * @author prateek
 *
 */
public interface MongoRepositoryCustom {

	public void upsert(MongoDataDTO mongoDataDTO);
	
	public void upsertAll(List<MongoDataDTO> mongoDataDTOs);
}
