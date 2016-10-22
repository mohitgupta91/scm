package com.snapdeal.scm.mongo.dao;

import com.snapdeal.scm.mongo.doc.NotificationEventMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * EventMappingRepository : Mongo Repository
 * 
 * @author Ashwini
 */
public interface EventMappingRepository extends MongoRepository<NotificationEventMapping, String> {

    public NotificationEventMapping findByAlertIdAndGroupLogicName(Long alertId, String groupLogicName);
}
