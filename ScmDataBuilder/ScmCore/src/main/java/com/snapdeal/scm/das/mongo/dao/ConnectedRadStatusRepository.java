package com.snapdeal.scm.das.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.core.mongo.document.ConnectedRadStatus;

/**
 * 
 * @author prateek
 *
 */
public interface ConnectedRadStatusRepository extends MongoRepository<ConnectedRadStatus, String>, ConnectedRadStatusRepositoryCustom{

	public ConnectedRadStatus findBySubOrderCode(String subOrderCode);

}
