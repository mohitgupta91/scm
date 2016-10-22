package com.snapdeal.scm.das.mongo.dao;

import com.snapdeal.scm.core.mongo.document.OriginCityExitMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by harshit on 25/4/16.
 */
public interface OriginCityExitMappingRepository extends MongoRepository<OriginCityExitMapping, String> {
    //TODO method for lookup

}
