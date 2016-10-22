package com.snapdeal.scm.mongo.mao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.mongo.doc.CourierGroup;

/**
 * 
 * @author prateek
 *
 */
public interface CourierGroupRepository extends MongoRepository<CourierGroup, String>{
	
	public CourierGroup findByCourierCode(String courierCode);
}
