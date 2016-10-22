package com.snapdeal.scm.mongo.mao.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * 
 * @author pranav
 *
 */
public class SupcDetailsRepositoryImpl implements SupcDetailsRepositoryCustom{

	@Autowired
	private MongoOperations mongoOperations;

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findDistinctSuperCategory() {
		return mongoOperations.getCollection("supc_details").distinct("superCategory");
	}

}
