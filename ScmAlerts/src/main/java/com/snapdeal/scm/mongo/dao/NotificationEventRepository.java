package com.snapdeal.scm.mongo.dao;

import com.snapdeal.scm.mongo.doc.NotificationEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * NotificationEventRepository : Mongo Repository
 *
 * @author Ashwini
 */
public interface NotificationEventRepository extends MongoRepository<NotificationEvent, String> {

    public NotificationEvent findByEventKey(Long alertId);
}
