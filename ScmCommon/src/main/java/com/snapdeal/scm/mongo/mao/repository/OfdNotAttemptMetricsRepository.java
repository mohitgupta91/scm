package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.OfdNotAttemptMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chitransh
 */
public interface OfdNotAttemptMetricsRepository extends MongoRepository<OfdNotAttemptMetrics, String> {
}
