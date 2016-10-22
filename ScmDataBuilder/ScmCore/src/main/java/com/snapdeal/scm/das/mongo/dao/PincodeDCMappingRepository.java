package com.snapdeal.scm.das.mongo.dao;

import com.snapdeal.scm.core.mongo.document.PincodeDCMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by harshit on 22/4/16.
 */
public interface PincodeDCMappingRepository extends MongoRepository<PincodeDCMapping, String> {
    //TODO method for lookup
}
