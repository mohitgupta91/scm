package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.ReachedNotOFDMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chitransh
 */
public interface ReachedNotOFDMetricsRepository extends MongoRepository<ReachedNotOFDMetrics, String> {
}
