package com.snapdeal.scm.web.core.das;

import com.google.common.collect.ListMultimap;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticDurationTypeDateRange;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.web.core.sro.PointSRO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgBuilder;

import java.util.List;

/**
 * IAbstractElasticDas : Must be extended by all Das Interface
 *
 * @author pranav, prateek
 */
public interface IAbstractElasticDas {

    Client getClient();

    String getIndexType();

    String getIndexName();

    ElasticDataDTO getBySuborderId(String id);

    SearchHits getAllHitsForQuery(String query);

    BoolQueryBuilder applyFilterAndDateRange(ElasticFilter elasticFilter);

    SearchResponse executeQuery(BoolQueryBuilder boolQuery, AbstractAggregationBuilder aggregationBuilders);

    TermsBuilder termsBuilder(String fieldKey, String field, int count);

    TermsBuilder termsBuilderWithMinDocCount(String fieldKey, String field, int count, int minDocCount);

    TermsBuilder termBuilderByScript(String fieldKey, Script script, int count);

    @Deprecated
    TermsBuilder termBuilderByScriptWithMinDocCount(String fieldKey, Script script, int count, int minDocCount);

    AvgBuilder avgBuilder(String fieldKey, String field);

    List<Terms.Bucket> termsBuckets(Terms.Bucket termBucket, String fieldkey);

    List<InternalHistogram.Bucket> dateHistogramBuckets(SearchResponse searchResponse, String fieldkey);

    List<Terms.Bucket> termsBuckets(SearchResponse searchResponse, String fieldkey);

    SearchResponse executeQuery(BoolQueryBuilder boolQuery);

    DateHistogramBuilder dateHistogramBuilder(String fieldKey, ElasticDurationTypeDateRange durationTypeDateRange);

    DateHistogramBuilder dateHistogramBuilderByScript(String fieldKey, ElasticDurationTypeDateRange durationTypeDateRange, Script script);

    Double getAverageValue(double value);

    Double getPercentage(double totalDocCount, long docCount);

    Object getFieldValueBySuborderId(String id, String fieldName);

    ExistsQueryBuilder existsField(SubOrderDetailElasticColumn column);

    ListMultimap<String, PointSRO> executeAndParseDailySubAggregatedQuery(BoolQueryBuilder boolQuery, ElasticFilter elasticFilter, SubOrderDetailElasticColumn subAggregationColumn) throws Exception;
}