package com.snapdeal.scm.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.mongo.doc.Alert;

/**
 * AlertMongoRepository : Mongo Repository
 * 
 * @author pranav
 */
public interface AlertRepository extends MongoRepository<Alert, String> {

    public Alert findByAlertId(Long alertId);
    public Alert findByTitle(String title);
}
