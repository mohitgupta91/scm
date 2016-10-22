package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.doc.MetricsInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author chitransh
 */
public interface MetricsInfoRepository extends MongoRepository<MetricsInfo, String> {

    MetricsInfo findByMetricsId(Integer metricsId);
}
