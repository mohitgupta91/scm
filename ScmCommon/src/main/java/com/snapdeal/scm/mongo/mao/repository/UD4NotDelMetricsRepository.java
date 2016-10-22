package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.UD4NotDelMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chitransh
 */
public interface UD4NotDelMetricsRepository extends MongoRepository<UD4NotDelMetrics, String> {
}
