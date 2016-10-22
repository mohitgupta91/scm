package com.snapdeal.scm.web.services.impl;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.web.core.das.ILastMileElasticDas;
import com.snapdeal.scm.web.services.IElasticService;
/**
 * ElasticServiceImpl : Elastic Serivce Impl : Currently its only used for running elastic test cases
 * 
 * @author pranav
 *
 */
@Service
public class ElasticServiceImpl implements IElasticService{

	private static final Logger LOG = LoggerFactory.getLogger(ElasticServiceImpl.class);

	@Autowired
	@Qualifier("G1")
	private ILastMileElasticDas elasticDas;

	@Override
	public int findCountOfMaster(){
		LOG.info("Count the master Request is received");
		return -1;
	}

	@Override
	public ElasticDataDTO getBySuborderId(String id){
		return elasticDas.getBySuborderId(id);
	}

	private SearchHits getAllHitsForQuery(String query){
		return elasticDas.getAllHitsForQuery(query);
	}

	@Override
	public void printQueryResponse(String query){
		SearchHits hits = getAllHitsForQuery(query);
		for (int i = 0; i < hits.totalHits(); i++) {
			SearchHit hit = hits.getAt(i);
			System.out.println(hit.sourceAsString());
			System.out.println(hit.sourceAsMap().size());
		}
	}

	// For test case only
	@Value("${scm.elastic.index.name.type}")
	private String indexType;

	@Value("${scm.elastic.index.name}")
	private String indexName;
	
	@Autowired
	private Client client;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Histogram runAggregate(AggregationBuilder aggregation, String query) {
		SearchResponse sr = client.prepareSearch(indexName)
				.setTypes(indexType)
				.setSource(query.toString())
				.addAggregation( aggregation)
				.execute().actionGet();
		Histogram agg = sr.getAggregations().get("agg");
		return agg;	
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<AggResult> findMetricData(ElasticFilter ef) {
		return elasticDas.findMetricData(ef);
	}
}
