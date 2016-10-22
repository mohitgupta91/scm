package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.AttemptNotDelMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chitransh
 */
public interface AttemptNotDelMetricsRepository extends MongoRepository<AttemptNotDelMetrics, String> {
}
