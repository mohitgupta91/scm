package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.Table;
import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticDurationTypeDateRange;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.web.core.das.ILastMileElasticDas;
import com.snapdeal.scm.web.core.enums.ChartFilterKey;
import com.snapdeal.scm.web.core.enums.FilterKey;
import com.snapdeal.scm.web.core.enums.Stage;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RTOCountMetricElasticDas : G38
 *
 * @author Ashwini
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G38")
public class RTOCountMetricElasticDas extends AbstractElasticDasImpl implements ILastMileElasticDas {

    private static final Logger LOG = LoggerFactory.getLogger(RTOCountMetricElasticDas.class);

    private static final String RTO_COUNT         = "rtocount";
    private static final String DESTINATION_STATE = SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName();
    private static final String DESTINATION_CITY  = SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName();

    @Override
    public List<AggResult> findMetricData(ElasticFilter elasticFilter) {
        LOG.info("Elastic Search Query Filter : " + elasticFilter);
        List<AggResult>              aggResults            = new ArrayList<>();
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();
        if (null == durationTypeDateRange || durationTypeDateRange.getFromDate() == null || durationTypeDateRange.getToDate() == null) {
            return aggResults;
        }
        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.RTO_DATE.getColumnName());
        BoolQueryBuilder boolQuery = super.applyFilterAndDateRange(elasticFilter);

        Table<Stage, ChartFilterKey, List<FilterKey>> chartFilter = elasticFilter.getChartGroupValue();

        switch (elasticFilter.getStage()) {
            case ONE:
                aggResults = firstLevelAggregation(boolQuery, super.dateHistogramBuilder(RTO_COUNT, durationTypeDateRange));
                break;
            case TWO:
                aggResults = secondLevelAggregation(boolQuery, super.dateHistogramBuilder(RTO_COUNT, durationTypeDateRange), chartFilter.row(Stage.TWO));
                break;
            case THREE:
                aggResults = thirdLevelAggregation(boolQuery, super.dateHistogramBuilder(RTO_COUNT, durationTypeDateRange), chartFilter.row(Stage.THREE));
                break;
            case FOUR:
                aggResults = fourthLevelAggregation(boolQuery);
                break;
            case FIVE:
                aggResults = fifthLevelAggregation(boolQuery);
                break;
            default:
                break;
        }
        LOG.info("Elastic Search Query Response : " + aggResults);
        return aggResults;
    }

    private List<AggResult> firstLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder) {
        SearchResponse  searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (InternalHistogram.Bucket rtoCountBucket : super.dateHistogramBuckets(searchResponse, RTO_COUNT)) {
            AggResult<DateTime> result        = new AggResult(rtoCountBucket.getKey());
            double              totalDocCount = (double) rtoCountBucket.getDocCount();
            result.addDataValue(RTO_COUNT, totalDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> secondLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder, Map<ChartFilterKey, List<FilterKey>> groupBy) {
        String filterKey = groupBy.get(ChartFilterKey.GROUP_BY).get(0).getSubOrderDetailElasticColumn().getColumnName();

        dateHistogramBuilder.subAggregation(super.termsBuilderWithMinDocCount(filterKey, filterKey, 0, 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (InternalHistogram.Bucket rtoCountBucket : super.dateHistogramBuckets(searchResponse, RTO_COUNT)) {
            AggResult<DateTime>     result        = new AggResult(rtoCountBucket.getKey());
            StringTerms             filterKeyTerm = rtoCountBucket.getAggregations().get(filterKey);
            List<AggResult<String>> aggResults    = new ArrayList<AggResult<String>>();
            result.setAggResults(aggResults);
            for (Bucket filterKeyBucket : filterKeyTerm.getBuckets()) {
                double            totalDocCount = (double) filterKeyBucket.getDocCount();
                AggResult<String> aggResult     = new AggResult("");
                aggResult.addDataValue(filterKeyBucket.getKeyAsString(), totalDocCount);
                aggResults.add(aggResult);
            }
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> thirdLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder, Map<ChartFilterKey, List<FilterKey>> groupBy) {
        String filterKey = groupBy.get(ChartFilterKey.GROUP_BY).get(0).getSubOrderDetailElasticColumn().getColumnName();

        dateHistogramBuilder.subAggregation(super.termsBuilderWithMinDocCount(filterKey, filterKey, 0, 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, RTO_COUNT)) {
            AggResult<DateTime>     result        = new AggResult(deliveredCountBucket.getKey());
            StringTerms             filterKeyTerm = deliveredCountBucket.getAggregations().get(filterKey);
            List<AggResult<String>> aggResults    = new ArrayList<AggResult<String>>();
            result.setAggResults(aggResults);
            for (Bucket filterKeyBucket : filterKeyTerm.getBuckets()) {
                double            totalCourierGroupDocCount = (double) filterKeyBucket.getDocCount();
                AggResult<String> aggResult                 = new AggResult("");
                aggResult.addDataValue(filterKeyBucket.getKeyAsString(), totalCourierGroupDocCount);
                aggResults.add(aggResult);
            }
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> fourthLevelAggregation(BoolQueryBuilder boolQuery) {
        TermsBuilder    termsBuilder   = super.termsBuilder(DESTINATION_STATE, SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName(), 0);
        SearchResponse  searchResponse = super.executeQuery(boolQuery, termsBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (Bucket destinationStateBucket : super.termsBuckets(searchResponse, DESTINATION_STATE)) {
            AggResult<DateTime> result                        = new AggResult(destinationStateBucket.getKey());
            double              totalDestinationStateDocCount = (double) destinationStateBucket.getDocCount();
            result.addDataValue(RTO_COUNT, totalDestinationStateDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> fifthLevelAggregation(BoolQueryBuilder boolQuery) {
        TermsBuilder    termsBuilder   = super.termsBuilder(DESTINATION_CITY, SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName(), 0);
        SearchResponse  searchResponse = super.executeQuery(boolQuery, termsBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (Bucket destinationCityBucket : super.termsBuckets(searchResponse, DESTINATION_CITY)) {
            AggResult<DateTime> result                       = new AggResult(destinationCityBucket.getKey());
            double              totalDestinationCityDocCount = (double) destinationCityBucket.getDocCount();
            result.addDataValue(RTO_COUNT, totalDestinationCityDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }
}