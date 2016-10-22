package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.snapdeal.scm.core.elastic.dto.*;
import com.snapdeal.scm.utils.DateUtils;
import com.snapdeal.scm.web.core.das.IAbstractElasticDas;
import com.snapdeal.scm.web.core.enums.FilterKey;
import com.snapdeal.scm.web.core.sro.PointSRO;
import com.snapdeal.scm.web.core.utils.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Order;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * AbstractElasticDasImpl : Must be extended by all elastic das
 *
 * @author pranav, prateek
 */
@Component
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractElasticDasImpl implements IAbstractElasticDas {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractElasticDasImpl.class);

    private static final double HOURS_IN_DAY = 24;

    @Autowired
    @Qualifier("transportClient")
    private Client client;

    @Value("${scm.elastic.index.name.type}")
    private String indexType;

    @Value("${scm.elastic.index.name}")
    private String indexName;

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public String getIndexType() {
        return indexType;
    }

    @Override
    public String getIndexName() {
        return indexName;
    }

    @Override
    public ElasticDataDTO getBySuborderId(String id) {
        GetResponse response = client.prepareGet(indexName, indexType, id).get();
        if (response.isExists()) {
            return new ElasticDataDTO(id, response.getSourceAsMap());
        }
        return null;
    }


    @Override
    public Object getFieldValueBySuborderId(String id, String fieldName) {
        GetResponse response = client.prepareGet(indexName, indexType, id).setFields(fieldName).get();
        if (response.isExists()) {
            return response.getField(fieldName).getValue();
        }
        return null;
    }

    @Override
    public SearchHits getAllHitsForQuery(String query) {
        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(indexType)
                .setSource(query.toString())
                .execute().actionGet();
        return response.getHits();
    }

    @Override
    public BoolQueryBuilder applyFilterAndDateRange(ElasticFilter elasticFilter) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (null == elasticFilter) {
            return boolQuery;
        }
        EnumMap<FilterKey, String> stringTermFilters = elasticFilter.getFilterValues();
        if (null != stringTermFilters && stringTermFilters.size() > 0) {
            for (Entry<FilterKey, String> filterColName : stringTermFilters.entrySet()) {
                String filterValue = filterColName.getValue();
                if (filterValue == null || filterValue.trim().length() == 0) {
                    continue;
                }
                String[] inFilterValue = filterValue.split(StringUtils.COMMA_STRING);
                if (inFilterValue.length == 1) {
                    boolQuery.must(QueryBuilders.termQuery(filterColName.getKey().getSubOrderDetailElasticColumn().getColumnName(), filterValue));
                } else {
                    boolQuery.must(QueryBuilders.termsQuery(filterColName.getKey().getSubOrderDetailElasticColumn().getColumnName(), inFilterValue));
                }
            }
        }
        List<ElasticDateRangeFilter> dateRangeFilters = elasticFilter.getDateRangeFilters();
        if (null != dateRangeFilters && dateRangeFilters.size() > 0) {
            for (ElasticDateRangeFilter elasticDateRangeFilter : dateRangeFilters) {
                if (null != elasticDateRangeFilter) {
                    boolQuery.must(QueryBuilders.rangeQuery(elasticDateRangeFilter.getFilterKey().getSubOrderDetailElasticColumn().getColumnName())
                                           .from(elasticDateRangeFilter.getFromDate())
                                           .to(elasticDateRangeFilter.getToDate())
                                           .includeUpper(elasticDateRangeFilter.isIncludeUpper())
                                           .includeLower(elasticDateRangeFilter.isIncludeLower()));
                }
            }
        }
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();
        if (null != durationTypeDateRange) {
            if (null != durationTypeDateRange.getColumnName()) {
                boolQuery.must(QueryBuilders.rangeQuery(durationTypeDateRange.getColumnName())
                                       .from(durationTypeDateRange.getFromDate())
                                       .to(durationTypeDateRange.getToDate())
                                       .includeUpper(durationTypeDateRange.isIncludeUpper())
                                       .includeLower(durationTypeDateRange.isIncludeLower()));
            }
        }
        return boolQuery;
    }


    @Override
    public AvgBuilder avgBuilder(String fieldKey, String field) {
        return AggregationBuilders.avg(fieldKey).field(field);
    }

    @Override
    public TermsBuilder termsBuilder(String fieldKey, String field, int count) {
        TermsBuilder termsBuilder = AggregationBuilders.terms(fieldKey).field(field).order(Order.count(false));
        if (count >= 0) {
            termsBuilder.size(count);
        }
        return termsBuilder;
    }

    @Override
    public TermsBuilder termsBuilderWithMinDocCount(String fieldKey, String field, int count, int minDocCount) {
        TermsBuilder termsBuilder = termsBuilder(fieldKey, field, count);
        if (minDocCount >= 0) {
            termsBuilder.minDocCount(minDocCount);
        }
        return termsBuilder;
    }

    @Override
    public TermsBuilder termBuilderByScript(String fieldKey, Script script, int count) {
        TermsBuilder termsBuilder = AggregationBuilders.terms(fieldKey).script(script).order(Order.count(false));
        if (count >= 0) {
            termsBuilder.size(count);
        }
        return termsBuilder;
    }

    @Override
    @Deprecated
    public TermsBuilder termBuilderByScriptWithMinDocCount(String fieldKey, Script script, int count, int minDocCount) {
        TermsBuilder termsBuilder = termBuilderByScript(fieldKey, script, count);
        if (minDocCount >= 0) {
            termsBuilder.minDocCount(minDocCount);
        }
        return termsBuilder;
    }

    @Override
    public List<Terms.Bucket> termsBuckets(SearchResponse searchResponse, String fieldkey) {
        Map<String, Aggregation> aggMap      = searchResponse.getAggregations().asMap();
        StringTerms              stringTerms = (StringTerms) aggMap.get(fieldkey);
        return stringTerms.getBuckets();
    }

    @Override
    public List<Terms.Bucket> termsBuckets(Terms.Bucket termBucket, String fieldkey) {
        Map<String, Aggregation> aggMap      = termBucket.getAggregations().asMap();
        StringTerms              stringTerms = (StringTerms) aggMap.get(fieldkey);
        return stringTerms.getBuckets();
    }

    @Override
    public List<InternalHistogram.Bucket> dateHistogramBuckets(SearchResponse searchResponse, String fieldkey) {
        Map<String, Aggregation> aggMap = searchResponse.getAggregations().asMap();
        InternalHistogram        terms  = (InternalHistogram) aggMap.get(fieldkey);
        return terms.getBuckets();
    }


    @Override
    public SearchResponse executeQuery(BoolQueryBuilder boolQuery, AbstractAggregationBuilder aggregationBuilders) {
        SearchRequestBuilder searchRequestBuilder = getClient()
                .prepareSearch(getIndexName())
                .setTypes(getIndexType())
                .setQuery(boolQuery)
                .setSize(0)
                .addAggregation(aggregationBuilders);
        LOG.info("Elastic Query : " + searchRequestBuilder);
        return searchRequestBuilder.get();
    }

    @Override
    public SearchResponse executeQuery(BoolQueryBuilder boolQuery) {
        SearchRequestBuilder searchRequestBuilder = getClient()
                .prepareSearch(getIndexName())
                .setTypes(getIndexType())
                .setQuery(boolQuery)
                .setSize(0);
        LOG.info("Elastic Query : " + searchRequestBuilder);
        return searchRequestBuilder.get();
    }

    @Override
    public DateHistogramBuilder dateHistogramBuilder(String fieldKey, ElasticDurationTypeDateRange durationTypeDateRange) {
        return AggregationBuilders.dateHistogram(fieldKey)
                .field(durationTypeDateRange.getColumnName())
                .interval(durationTypeDateRange.getDateHistogramInterval())
                .timeZone("+05:30")
                .minDocCount(0)
                .extendedBounds(durationTypeDateRange.getFromDate().getTime(),
                                durationTypeDateRange.getToDate().getTime());
    }

    @Override
    public DateHistogramBuilder dateHistogramBuilderByScript(String fieldKey, ElasticDurationTypeDateRange durationTypeDateRange, Script script) {
        return AggregationBuilders.dateHistogram(fieldKey)
                .script(script)
                .interval(durationTypeDateRange.getDateHistogramInterval())
                .timeZone("+05:30")
                .minDocCount(0)
                .extendedBounds(durationTypeDateRange.getFromDate().getTime(),
                                durationTypeDateRange.getToDate().getTime());
    }

    @Override
    public Double getAverageValue(double value) {
        return Double.isNaN(value) ? 0 : (value / HOURS_IN_DAY);
    }

    @Override
    public Double getPercentage(double totalDocCount, long docCount) {
        if (totalDocCount == 0 || docCount == 0) {
            return 0.0;
        }
        return (docCount * 100) / totalDocCount;
    }

    @Override
    public ExistsQueryBuilder existsField(SubOrderDetailElasticColumn column) {
        return QueryBuilders.existsQuery(column.getColumnName());
    }

    @Override
    public ListMultimap<String, PointSRO> executeAndParseDailySubAggregatedQuery(BoolQueryBuilder boolQuery, ElasticFilter elasticFilter, SubOrderDetailElasticColumn subAggregationColumn) throws Exception {

        ListMultimap<String, PointSRO> multimap                 = ArrayListMultimap.create();
        String                         subAggregationColumnName = subAggregationColumn.getColumnName();
        ElasticDurationTypeDateRange   durationTypeDateRange    = elasticFilter.getDurationTypeDateRange();

        DateHistogramBuilder histogramBuilder = dateHistogramBuilder("date_buckets", durationTypeDateRange)
                .interval(DateHistogramInterval.DAY)
                .subAggregation(termsBuilder(subAggregationColumnName, subAggregationColumnName, 0));

        org.elasticsearch.action.search.SearchResponse elasticResponse = executeQuery(boolQuery, histogramBuilder);
        List<InternalHistogram.Bucket>                 datedBuckets    = elasticResponse.getAggregations().<InternalHistogram>get("date_buckets").getBuckets();

        for (InternalHistogram.Bucket datedBucket : datedBuckets) {
            String             dateAsString    = DateUtils.MONTH_DAY_YEAR_FORMAT.format(DateUtils.ELASTIC_DATE_FORMAT.parse(datedBucket.getKeyAsString()));
            List<Terms.Bucket> categoryBuckets = datedBucket.getAggregations().<InternalTerms>get(subAggregationColumnName).getBuckets();
            categoryBuckets.stream()
                    .filter(categoryBucket -> categoryBucket.getDocCount() > 0)
                    .forEach(categoryBucket ->
                             {
                                 Long     docCount = categoryBucket.getDocCount();
                                 PointSRO value    = new PointSRO(dateAsString, docCount.doubleValue());
                                 multimap.put(categoryBucket.getKeyAsString(), value);
                             });
        }
        return multimap;
    }
}
