package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.ShipNotAttemptNonIntgrMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chitransh
 */
public interface ShipNotAttemptNonIntgrMetricsRepository extends MongoRepository<ShipNotAttemptNonIntgrMetrics, String> {
}
