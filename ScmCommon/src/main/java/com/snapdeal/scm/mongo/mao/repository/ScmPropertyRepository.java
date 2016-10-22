package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.ScmProperty;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 
 * @author prateek
 *
 */
public interface ScmPropertyRepository extends MongoRepository<ScmProperty, String>{

    ScmProperty findByName(String name);
}
