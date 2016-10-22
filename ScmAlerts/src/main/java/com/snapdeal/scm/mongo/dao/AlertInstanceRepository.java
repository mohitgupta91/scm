package com.snapdeal.scm.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.snapdeal.scm.mongo.doc.AlertInstance;

/**
 * AlertRepository : Mongo Repository
 * 
 * @author pranav, Ashwini
 */
public interface AlertInstanceRepository extends MongoRepository<AlertInstance, String> {

	public AlertInstance findByAlertInstanceId(Long alertInstanceId);

	public AlertInstance findByAlertId(Long alertId);

	@Query(value = "{ruleTime : ?0}")
	public List<AlertInstance> findByTimeBetween(int timeRangeStart);

	public List<AlertInstance> findByAlertTitle(String title);

	public List<AlertInstance> findByCreatedBy(String createdBy);
}
