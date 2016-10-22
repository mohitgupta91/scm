package com.snapdeal.scm.mongo.mao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.mongo.doc.PincodeMaster;

/**
 * 
 * @author prateek
 *
 */
public interface PincodeMasterRepository extends MongoRepository<PincodeMaster, String>{

	public PincodeMaster findByPincode(String pincode);
}
