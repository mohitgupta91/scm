package com.snapdeal.scm.web.core.mao;

import com.snapdeal.scm.web.core.mongo.documents.MetricDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Created by heer on 25/2/16.
 */
@Service
public interface MetricDetailsRepository extends MongoRepository<MetricDetails, String> {
    public MetricDetails findByMetricID(String metricID);
}
