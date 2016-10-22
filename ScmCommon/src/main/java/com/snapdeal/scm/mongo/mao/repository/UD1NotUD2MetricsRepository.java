package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.UD1NotUD2Metrics;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chitransh
 */
public interface UD1NotUD2MetricsRepository extends MongoRepository<UD1NotUD2Metrics, String> {
}
