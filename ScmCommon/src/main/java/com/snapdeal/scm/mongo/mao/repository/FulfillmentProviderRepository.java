package com.snapdeal.scm.mongo.mao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.mongo.doc.FulfillmentProvider;

/**
 * 
 * @author prateek
 *
 */
public interface FulfillmentProviderRepository extends MongoRepository<FulfillmentProvider, String>{
	public FulfillmentProvider findByCode(String code);
	
}
