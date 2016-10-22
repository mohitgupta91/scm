package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.ConNotReachedMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chitransh
 */
public interface ConNotReachedMetricsRepository extends MongoRepository<ConNotReachedMetrics, String> {
}
