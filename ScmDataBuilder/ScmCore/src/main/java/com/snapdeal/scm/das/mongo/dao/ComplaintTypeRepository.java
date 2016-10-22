package com.snapdeal.scm.das.mongo.dao;

import com.snapdeal.scm.core.mongo.document.ComplaintType;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by harshit.nimbark on 5/30/16.
 */
public interface ComplaintTypeRepository extends MongoRepository<ComplaintType, String>{
}
