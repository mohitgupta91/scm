package com.snapdeal.scm.poller.model;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author gaurav
 *
 */
public interface PollarPropertyRepository extends MongoRepository<PollerProperty, String>{
	
	public PollerProperty findByName(String name);

}
