package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.ShippedNotConMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chitransh
 */
public interface ShippedNotConMetricsRepository extends MongoRepository<ShippedNotConMetrics, String> {
}
