package com.snapdeal.scm.web.services;

import java.util.List;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;

import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;

/**
 * IElasticService : Elastic service provider
 * 
 * @author pranav
 *
 */
public interface IElasticService {

	int findCountOfMaster();

	ElasticDataDTO getBySuborderId(String id);

	void printQueryResponse(String query);

	Histogram runAggregate(@SuppressWarnings("rawtypes") AggregationBuilder aggregation, String query);

	List<AggResult> findMetricData(ElasticFilter ef);
}
