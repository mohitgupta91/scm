package com.snapdeal.scm.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.snapdeal.scm.mongo.doc.AlertGroupLogic;

/**
 * AlertMongoRepository : Mongo Repository
 * 
 * @author pranav
 */
public interface AlertGroupLogicRepository extends MongoRepository<AlertGroupLogic, String> {

    public List<AlertGroupLogic> findByAlertId(Long alertTypeId);

  /*  @Query(value = "{alertId : ?0, groupLogicName : ?1}")*/
	public AlertGroupLogic findByAlertIdAndGroupLogicName(Long alertId, String groupLogicName);
}
