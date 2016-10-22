package com.snapdeal.scm.das.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.core.mongo.document.StatusCodes;

/**
 * 
 * @author prateek
 *
 */
public interface StatusCodesRepository extends MongoRepository<StatusCodes, String>{

}
