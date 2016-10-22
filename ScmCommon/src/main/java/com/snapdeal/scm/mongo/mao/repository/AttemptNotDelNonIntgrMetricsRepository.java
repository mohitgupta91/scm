package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.AttemptNotDelNonIntgrMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chitransh
 */
public interface AttemptNotDelNonIntgrMetricsRepository extends MongoRepository<AttemptNotDelNonIntgrMetrics, String> {
}
