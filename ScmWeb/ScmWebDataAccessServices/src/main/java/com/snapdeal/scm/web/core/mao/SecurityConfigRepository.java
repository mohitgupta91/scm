package com.snapdeal.scm.web.core.mao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.web.core.mongo.documents.SecurityPermission;

/**
 * SecurityConfigRepository : Repository Access to Security permissions.
 * 
 * @author mohit
 */
public interface SecurityConfigRepository extends MongoRepository<SecurityPermission, String> {

}
