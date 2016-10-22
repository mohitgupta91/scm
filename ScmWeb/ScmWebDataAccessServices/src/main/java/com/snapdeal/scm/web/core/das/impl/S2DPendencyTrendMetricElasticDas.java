package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.Multimap;
import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticDurationTypeDateRange;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.web.core.das.ILastMileElasticDas;
import com.snapdeal.scm.web.core.enums.OptionKey;
import com.snapdeal.scm.web.core.utils.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * S2DPendencyTrendMetricElasticDas : G6
 *
 * @author pranav, ashwini
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G6")
public class S2DPendencyTrendMetricElasticDas extends AbstractElasticDasImpl implements ILastMileElasticDas {

    private static final Logger LOG = LoggerFactory.getLogger(S2DPendencyTrendMetricElasticDas.class);

    private static final String TOTAL_SHIPPED         = "totalshipped";
    private static final String DELIVERY_STATUS       = "deliverystatus";
    private static final String DELIVERED             = "delivered";
    private static final String TOTAL_CLOSED          = "totalclosed";
    private static final String NOT_DELIVERED         = "notdelivered";
    private static final String SHIPPED_NOT_DELIVERED = "shippednotdelivered";
    private static final String PENDENCY              = "pendency";
    private static final String TP_DEL_STATUS_DATE    = SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName();
    private static final String LANE_TYPE             = SubOrderDetailElasticColumn.LANE_TYPE.getColumnName();
    private static final String COURIER_GRP           = SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName();
    private static final String DESTINATION_STATE     = SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName();
    private static final String DESTINATION_CITY      = SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName();

    @Override
    public List<AggResult> findMetricData(ElasticFilter elasticFilter) {
        LOG.debug("Received Request : " + elasticFilter);
        List<AggResult>              aggResults            = new ArrayList<>();
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();
        if (null == durationTypeDateRange || durationTypeDateRange.getFromDate() == null || durationTypeDateRange.getToDate() == null) {
            return aggResults;
        }
        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        switch (elasticFilter.getStage()) {
            case ONE:
                aggResults = firstLevelAggregation(elasticFilter);
                break;
            case TWO:
                aggResults = secondLevelAggregation(elasticFilter, elasticFilter.getOptionValues());
                break;
            case THREE:
                aggResults = thirdLevelAggregation(elasticFilter, elasticFilter.getOptionValues());
                break;
            case FOUR:
                aggResults = fourthLevelAggregation(elasticFilter, elasticFilter.getOptionValues());
                break;
            case FIVE:
                aggResults = fifthLevelAggregation(elasticFilter, elasticFilter.getOptionValues());
                break;
            default:
                break;
        }
        return aggResults;
    }


    private long executeFirstQuery(ElasticFilter elasticFilter) {
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();
        Date                         originalDate          = durationTypeDateRange.getFromDate();
        boolean                      originalIncludelower  = durationTypeDateRange.isIncludeLower();
        boolean                      originalIncludeUpper  = durationTypeDateRange.isIncludeUpper();
        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        durationTypeDateRange.setFromDate(null);
        durationTypeDateRange.setIncludeLower(false);
        durationTypeDateRange.setIncludeUpper(false);
        BoolQueryBuilder boolQuery = super.applyFilterAndDateRange(elasticFilter);
        boolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName())).mustNot(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName()));
        SearchResponse searchResponse           = super.executeQuery(boolQuery);
        long           shippedNotDeliveredCount = searchResponse.getHits().getTotalHits();
        durationTypeDateRange.setFromDate(originalDate);
        durationTypeDateRange.setIncludeLower(originalIncludelower);
        durationTypeDateRange.setIncludeUpper(originalIncludeUpper);
        return shippedNotDeliveredCount;
    }

    private SearchResponse executeFirstQueryWithAggregation(ElasticFilter elasticFilter, SubOrderDetailElasticColumn columnName) {
        TermsBuilder                 termsBuilder          = super.termsBuilderWithMinDocCount(columnName.getColumnName(), columnName.getColumnName(), 0, 0);
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();
        Date                         originalDate          = durationTypeDateRange.getFromDate();
        boolean                      originalIncludelower  = durationTypeDateRange.isIncludeLower();
        boolean                      originalIncludeUpper  = durationTypeDateRange.isIncludeUpper();
        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        durationTypeDateRange.setFromDate(null);
        durationTypeDateRange.setIncludeLower(false);
        durationTypeDateRange.setIncludeUpper(false);
        BoolQueryBuilder boolQuery = super.applyFilterAndDateRange(elasticFilter);
        boolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName())).mustNot(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName()));
        SearchResponse searchResponse = super.executeQuery(boolQuery, termsBuilder);
        durationTypeDateRange.setFromDate(originalDate);
        durationTypeDateRange.setIncludeLower(originalIncludelower);
        durationTypeDateRange.setIncludeUpper(originalIncludeUpper);
        return searchResponse;
    }

    private List<AggResult> firstLevelAggregation(ElasticFilter elasticFilter) {
        List<AggResult>              resultSet             = new ArrayList<>();
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();

        long openCount = executeFirstQuery(elasticFilter);

        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        DateHistogramBuilder shippedDateHistogramBuilder = super.dateHistogramBuilder(TOTAL_SHIPPED, durationTypeDateRange);
        BoolQueryBuilder     shippedBoolQuery            = super.applyFilterAndDateRange(elasticFilter);
        shippedBoolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName()));
        SearchResponse shippedSearchResponse = super.executeQuery(shippedBoolQuery, shippedDateHistogramBuilder);

        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName());
        DateHistogramBuilder deliveredDateHistogramBuilder = super.dateHistogramBuilder(TOTAL_CLOSED, durationTypeDateRange);
        BoolQueryBuilder     deliveredBoolQuery            = super.applyFilterAndDateRange(elasticFilter);
        shippedBoolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName()));
        SearchResponse closedSearchResponse = super.executeQuery(deliveredBoolQuery, deliveredDateHistogramBuilder);

        List<InternalHistogram.Bucket> shippedBuckets = super.dateHistogramBuckets(shippedSearchResponse, TOTAL_SHIPPED);
        List<InternalHistogram.Bucket> closedBuckets  = super.dateHistogramBuckets(closedSearchResponse, TOTAL_CLOSED);

        for (int i = 0; i < shippedBuckets.size(); i++) {
            AggResult<DateTime> result            = new AggResult(shippedBuckets.get(i).getKey());
            double              totalShippedCount = (double) shippedBuckets.get(i).getDocCount();
            double              totalclosedCount  = (double) closedBuckets.get(i).getDocCount();
            openCount += totalShippedCount - totalclosedCount;
            result.addDataValue(TOTAL_SHIPPED, totalShippedCount);
            result.addDataValue(TOTAL_CLOSED, totalclosedCount);
            result.addDataValue(PENDENCY, (double) openCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> secondLevelAggregation(ElasticFilter elasticFilter, Multimap<OptionKey, String> options) {
        List<AggResult>              resultSet             = new ArrayList<>();
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();

        String optionTypeValue = getCategoryOption(options);
        if (StringUtils.isEmpty(optionTypeValue)) {
            return resultSet;
        }
        SearchResponse      firstQueryResponse = executeFirstQueryWithAggregation(elasticFilter, SubOrderDetailElasticColumn.LANE_TYPE);
        List<Terms.Bucket>  termsBuckets       = super.termsBuckets(firstQueryResponse, LANE_TYPE);
        Map<String, Double> openCountMap       = new HashMap<>();
        for (Terms.Bucket bucket : termsBuckets) {
            openCountMap.put((String) bucket.getKey(), (double) bucket.getDocCount());
        }

        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        DateHistogramBuilder shippedDateHistogramBuilder = super.dateHistogramBuilder(TOTAL_SHIPPED, durationTypeDateRange).subAggregation(super.termsBuilderWithMinDocCount(LANE_TYPE, SubOrderDetailElasticColumn.LANE_TYPE.getColumnName(), 0, 0));
        BoolQueryBuilder     shippedBoolQuery            = super.applyFilterAndDateRange(elasticFilter);
        shippedBoolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName()));
        SearchResponse shippedSearchResponse = super.executeQuery(shippedBoolQuery, shippedDateHistogramBuilder);

        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName());
        DateHistogramBuilder closedDateHistogramBuilder = super.dateHistogramBuilder(TOTAL_CLOSED, durationTypeDateRange).subAggregation(super.termsBuilderWithMinDocCount(LANE_TYPE, SubOrderDetailElasticColumn.LANE_TYPE.getColumnName(), 0, 0));
        BoolQueryBuilder     closedBoolQuery            = super.applyFilterAndDateRange(elasticFilter);
        shippedBoolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName()));
        SearchResponse closedSearchResponse = super.executeQuery(closedBoolQuery, closedDateHistogramBuilder);

        List<InternalHistogram.Bucket> shippedBuckets = super.dateHistogramBuckets(shippedSearchResponse, TOTAL_SHIPPED);
        List<InternalHistogram.Bucket> closedBuckets  = super.dateHistogramBuckets(closedSearchResponse, TOTAL_CLOSED);
        for (int i = 0; i < shippedBuckets.size(); i++) {
            AggResult<DateTime> result              = new AggResult(shippedBuckets.get(i).getKey());
            StringTerms         shippedLaneTypeTerm = shippedBuckets.get(i).getAggregations().get(LANE_TYPE);
            StringTerms         closedLaneTypeTerm  = closedBuckets.get(i).getAggregations().get(LANE_TYPE);

            Map<String, Map<String, Double>> s2dMap = new HashMap<>();

            for (Terms.Bucket bucket : shippedLaneTypeTerm.getBuckets()) {
                Map<String, Double> innerMap = new HashMap<>();
                innerMap.put(TOTAL_SHIPPED, (double) bucket.getDocCount());
                s2dMap.put((String) bucket.getKey(), innerMap);
            }

            for (Terms.Bucket bucket : closedLaneTypeTerm.getBuckets()) {
                s2dMap.computeIfAbsent((String) bucket.getKey(), v -> new HashMap<>()).put(TOTAL_CLOSED, (double) bucket.getDocCount());
            }
            List<AggResult<String>> aggResults = new ArrayList<>();
            Set<String>             laneTypes  = new HashSet<>(); //superset of laneTypes
            laneTypes.addAll(s2dMap.keySet());
            laneTypes.addAll(openCountMap.keySet());

            for (String laneType : laneTypes) {
//                AggResult<String> aggResult = new AggResult(laneType);
                double shipped = s2dMap.get(laneType) != null ? s2dMap.get(laneType).get(TOTAL_SHIPPED) != null ? s2dMap.get(laneType).get(TOTAL_SHIPPED) : 0 : 0;
                double closed  = s2dMap.get(laneType) != null ? s2dMap.get(laneType).get(TOTAL_CLOSED) != null ? s2dMap.get(laneType).get(TOTAL_CLOSED) : 0 : 0;
                double open    = (openCountMap.get(laneType) != null ? openCountMap.get(laneType) : 0) + (shipped - closed);
                openCountMap.put(laneType, open);
                if (optionTypeValue.equals(TOTAL_SHIPPED)) {
                    result.addDataValue(laneType, shipped);
                }
                if (optionTypeValue.equals(TOTAL_CLOSED)) {
                    result.addDataValue(laneType, closed);
                }
                if (optionTypeValue.equals(PENDENCY)) {
                    result.addDataValue(laneType, open);
                }
//                aggResults.add(aggResult);
            }
//            result.setAggResults(aggResults);
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> thirdLevelAggregation(ElasticFilter elasticFilter, Multimap<OptionKey, String> options) {
        List<AggResult>              resultSet             = new ArrayList<>();
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();
        String                       optionTypeValue       = getCategoryOption(options);
        if (StringUtils.isEmpty(optionTypeValue)) {
            return resultSet;
        }
        SearchResponse      firstQueryResponse = executeFirstQueryWithAggregation(elasticFilter, SubOrderDetailElasticColumn.COURIER_GROUP);
        List<Terms.Bucket>  termsBuckets       = super.termsBuckets(firstQueryResponse, COURIER_GRP);
        Map<String, Double> openCountMap       = new HashMap<>();
        for (Terms.Bucket bucket : termsBuckets) {
            openCountMap.put((String) bucket.getKey(), (double) bucket.getDocCount());
        }

        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        DateHistogramBuilder shippedDateHistogramBuilder = super.dateHistogramBuilder(TOTAL_SHIPPED, durationTypeDateRange).subAggregation(super.termsBuilderWithMinDocCount(COURIER_GRP, SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName(), 0, 0));
        BoolQueryBuilder     shippedBoolQuery            = super.applyFilterAndDateRange(elasticFilter);
        shippedBoolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName()));
        SearchResponse shippedSearchResponse = super.executeQuery(shippedBoolQuery, shippedDateHistogramBuilder);

        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName());
        DateHistogramBuilder closedDateHistogramBuilder = super.dateHistogramBuilder(TOTAL_CLOSED, durationTypeDateRange).subAggregation(super.termsBuilderWithMinDocCount(COURIER_GRP, SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName(), 0, 0));
        BoolQueryBuilder     closedBoolQuery            = super.applyFilterAndDateRange(elasticFilter);
        shippedBoolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName()));
        SearchResponse closedSearchResponse = super.executeQuery(closedBoolQuery, closedDateHistogramBuilder);

        List<InternalHistogram.Bucket> shippedBuckets = super.dateHistogramBuckets(shippedSearchResponse, TOTAL_SHIPPED);
        List<InternalHistogram.Bucket> closedBuckets  = super.dateHistogramBuckets(closedSearchResponse, TOTAL_CLOSED);
        for (int i = 0; i < shippedBuckets.size(); i++) {
            AggResult<DateTime> result              = new AggResult(shippedBuckets.get(i).getKey());
            StringTerms         shippedLaneTypeTerm = shippedBuckets.get(i).getAggregations().get(COURIER_GRP);
            StringTerms         closedLaneTypeTerm  = closedBuckets.get(i).getAggregations().get(COURIER_GRP);

            Map<String, Map<String, Double>> s2dMap = new HashMap<>();

            for (Terms.Bucket bucket : shippedLaneTypeTerm.getBuckets()) {
                Map<String, Double> innerMap = new HashMap<>();
                innerMap.put(TOTAL_SHIPPED, (double) bucket.getDocCount());
                s2dMap.put((String) bucket.getKey(), innerMap);
            }

            for (Terms.Bucket bucket : closedLaneTypeTerm.getBuckets()) {
                s2dMap.computeIfAbsent((String) bucket.getKey(), v -> new HashMap<>()).put(TOTAL_CLOSED, (double) bucket.getDocCount());
            }
            List<AggResult<String>> aggResults = new ArrayList<>();
            Set<String>             laneTypes  = new HashSet<>(); //superset of laneTypes
            laneTypes.addAll(s2dMap.keySet());
            laneTypes.addAll(openCountMap.keySet());

            for (String laneType : laneTypes) {
//                AggResult<String> aggResult = new AggResult(laneType);
                double shipped = s2dMap.get(laneType) != null ? s2dMap.get(laneType).get(TOTAL_SHIPPED) != null ? s2dMap.get(laneType).get(TOTAL_SHIPPED) : 0 : 0;
                double closed  = s2dMap.get(laneType) != null ? s2dMap.get(laneType).get(TOTAL_CLOSED) != null ? s2dMap.get(laneType).get(TOTAL_CLOSED) : 0 : 0;
                double open    = (openCountMap.get(laneType) != null ? openCountMap.get(laneType) : 0) + (shipped - closed);
                openCountMap.put(laneType, open);
                if (optionTypeValue.equals(TOTAL_SHIPPED)) {
                    result.addDataValue(laneType, shipped);
                }
                if (optionTypeValue.equals(TOTAL_CLOSED)) {
                    result.addDataValue(laneType, closed);
                }
                if (optionTypeValue.equals(PENDENCY)) {
                    result.addDataValue(laneType, open);
                }
//                aggResults.add(aggResult);
            }
//            result.setAggResults(aggResults);
            resultSet.add(result);
        }
        return resultSet;

    }

    private List<AggResult> fourthLevelAggregation(ElasticFilter elasticFilter, Multimap<OptionKey, String> options) {
        List<AggResult>              resultSet             = new ArrayList<>();
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();
        String                       optionTypeValue       = getCategoryOption(options);
        if (StringUtils.isEmpty(optionTypeValue)) {
            return resultSet;
        }
        SearchResponse      firstQueryResponse = executeFirstQueryWithAggregation(elasticFilter, SubOrderDetailElasticColumn.DESTINATION_STATE);
        List<Terms.Bucket>  termsBuckets       = super.termsBuckets(firstQueryResponse, DESTINATION_STATE);
        Map<String, Double> openCountMap       = new HashMap<>();
        for (Terms.Bucket bucket : termsBuckets) {
            openCountMap.put((String) bucket.getKey(), (double) bucket.getDocCount());
        }

        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        DateHistogramBuilder shippedDateHistogramBuilder = super.dateHistogramBuilder(TOTAL_SHIPPED, durationTypeDateRange).subAggregation(super.termsBuilderWithMinDocCount(DESTINATION_STATE, SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName(), 0, 0));
        BoolQueryBuilder     shippedBoolQuery            = super.applyFilterAndDateRange(elasticFilter);
        shippedBoolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName()));
        SearchResponse shippedSearchResponse = super.executeQuery(shippedBoolQuery, shippedDateHistogramBuilder);

        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName());
        DateHistogramBuilder closedDateHistogramBuilder = super.dateHistogramBuilder(TOTAL_CLOSED, durationTypeDateRange).subAggregation(super.termsBuilderWithMinDocCount(DESTINATION_STATE, SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName(), 0, 0));
        BoolQueryBuilder     closedBoolQuery            = super.applyFilterAndDateRange(elasticFilter);
        shippedBoolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName()));
        SearchResponse closedSearchResponse = super.executeQuery(closedBoolQuery, closedDateHistogramBuilder);

        List<InternalHistogram.Bucket> shippedBuckets = super.dateHistogramBuckets(shippedSearchResponse, TOTAL_SHIPPED);
        List<InternalHistogram.Bucket> closedBuckets  = super.dateHistogramBuckets(closedSearchResponse, TOTAL_CLOSED);
        for (int i = 0; i < shippedBuckets.size(); i++) {
            AggResult<DateTime> result              = new AggResult(shippedBuckets.get(i).getKey());
            StringTerms         shippedLaneTypeTerm = shippedBuckets.get(i).getAggregations().get(DESTINATION_STATE);
            StringTerms         closedLaneTypeTerm  = closedBuckets.get(i).getAggregations().get(DESTINATION_STATE);

            Map<String, Map<String, Double>> s2dMap = new HashMap<>();

            for (Terms.Bucket bucket : shippedLaneTypeTerm.getBuckets()) {
                Map<String, Double> innerMap = new HashMap<>();
                innerMap.put(TOTAL_SHIPPED, (double) bucket.getDocCount());
                s2dMap.put((String) bucket.getKey(), innerMap);
            }

            for (Terms.Bucket bucket : closedLaneTypeTerm.getBuckets()) {
                s2dMap.computeIfAbsent((String) bucket.getKey(), v -> new HashMap<>()).put(TOTAL_CLOSED, (double) bucket.getDocCount());
            }
            List<AggResult<String>> aggResults = new ArrayList<>();
            Set<String>             laneTypes  = new HashSet<>(); //superset of laneTypes
            laneTypes.addAll(s2dMap.keySet());
            laneTypes.addAll(openCountMap.keySet());

            for (String laneType : laneTypes) {
//                AggResult<String> aggResult = new AggResult(laneType);
                double shipped = s2dMap.get(laneType) != null ? s2dMap.get(laneType).get(TOTAL_SHIPPED) != null ? s2dMap.get(laneType).get(TOTAL_SHIPPED) : 0 : 0;
                double closed  = s2dMap.get(laneType) != null ? s2dMap.get(laneType).get(TOTAL_CLOSED) != null ? s2dMap.get(laneType).get(TOTAL_CLOSED) : 0 : 0;
                double open    = (openCountMap.get(laneType) != null ? openCountMap.get(laneType) : 0) + (shipped - closed);
                openCountMap.put(laneType, open);
                if (optionTypeValue.equals(TOTAL_SHIPPED)) {
                    result.addDataValue(laneType, shipped);
                }
                if (optionTypeValue.equals(TOTAL_CLOSED)) {
                    result.addDataValue(laneType, closed);
                }
                if (optionTypeValue.equals(PENDENCY)) {
                    result.addDataValue(laneType, open);
                }
//                aggResults.add(aggResult);
            }
//            result.setAggResults(aggResults);
            resultSet.add(result);
        }
        return resultSet;

    }

    private List<AggResult> fifthLevelAggregation(ElasticFilter elasticFilter, Multimap<OptionKey, String> options) {
        List<AggResult>              resultSet             = new ArrayList<>();
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();
        String                       optionTypeValue       = getCategoryOption(options);
        if (StringUtils.isEmpty(optionTypeValue)) {
            return resultSet;
        }
        SearchResponse      firstQueryResponse = executeFirstQueryWithAggregation(elasticFilter, SubOrderDetailElasticColumn.DESTINATION_CITY);
        List<Terms.Bucket>  termsBuckets       = super.termsBuckets(firstQueryResponse, DESTINATION_CITY);
        Map<String, Double> openCountMap       = new HashMap<>();
        for (Terms.Bucket bucket : termsBuckets) {
            openCountMap.put((String) bucket.getKey(), (double) bucket.getDocCount());
        }

        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        DateHistogramBuilder shippedDateHistogramBuilder = super.dateHistogramBuilder(TOTAL_SHIPPED, durationTypeDateRange).subAggregation(super.termsBuilderWithMinDocCount(DESTINATION_CITY, SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName(), 0, 0));
        BoolQueryBuilder     shippedBoolQuery            = super.applyFilterAndDateRange(elasticFilter);
        shippedBoolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName()));
        SearchResponse shippedSearchResponse = super.executeQuery(shippedBoolQuery, shippedDateHistogramBuilder);

        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName());
        DateHistogramBuilder closedDateHistogramBuilder = super.dateHistogramBuilder(TOTAL_CLOSED, durationTypeDateRange).subAggregation(super.termsBuilderWithMinDocCount(DESTINATION_CITY, SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName(), 0, 0));
        BoolQueryBuilder     closedBoolQuery            = super.applyFilterAndDateRange(elasticFilter);
        shippedBoolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName()));
        SearchResponse closedSearchResponse = super.executeQuery(closedBoolQuery, closedDateHistogramBuilder);

        List<InternalHistogram.Bucket> shippedBuckets = super.dateHistogramBuckets(shippedSearchResponse, TOTAL_SHIPPED);
        List<InternalHistogram.Bucket> closedBuckets  = super.dateHistogramBuckets(closedSearchResponse, TOTAL_CLOSED);
        for (int i = 0; i < shippedBuckets.size(); i++) {
            AggResult<DateTime> result              = new AggResult(shippedBuckets.get(i).getKey());
            StringTerms         shippedLaneTypeTerm = shippedBuckets.get(i).getAggregations().get(DESTINATION_CITY);
            StringTerms         closedLaneTypeTerm  = closedBuckets.get(i).getAggregations().get(DESTINATION_CITY);

            Map<String, Map<String, Double>> s2dMap = new HashMap<>();

            for (Terms.Bucket bucket : shippedLaneTypeTerm.getBuckets()) {
                Map<String, Double> innerMap = new HashMap<>();
                innerMap.put(TOTAL_SHIPPED, (double) bucket.getDocCount());
                s2dMap.put((String) bucket.getKey(), innerMap);
            }

            for (Terms.Bucket bucket : closedLaneTypeTerm.getBuckets()) {
                s2dMap.computeIfAbsent((String) bucket.getKey(), v -> new HashMap<>()).put(TOTAL_CLOSED, (double) bucket.getDocCount());
            }
            List<AggResult<String>> aggResults = new ArrayList<>();
            Set<String>             laneTypes  = new HashSet<>(); //superset of laneTypes
            laneTypes.addAll(s2dMap.keySet());
            laneTypes.addAll(openCountMap.keySet());

            for (String laneType : laneTypes) {
//                AggResult<String> aggResult = new AggResult(laneType);
                double shipped = s2dMap.get(laneType) != null ? s2dMap.get(laneType).get(TOTAL_SHIPPED) != null ? s2dMap.get(laneType).get(TOTAL_SHIPPED) : 0 : 0;
                double closed  = s2dMap.get(laneType) != null ? s2dMap.get(laneType).get(TOTAL_CLOSED) != null ? s2dMap.get(laneType).get(TOTAL_CLOSED) : 0 : 0;
                double open    = (openCountMap.get(laneType) != null ? openCountMap.get(laneType) : 0) + (shipped - closed);
                openCountMap.put(laneType, open);
                if (optionTypeValue.equals(TOTAL_SHIPPED)) {
                    result.addDataValue(laneType, shipped);
                }
                if (optionTypeValue.equals(TOTAL_CLOSED)) {
                    result.addDataValue(laneType, closed);
                }
                if (optionTypeValue.equals(PENDENCY)) {
                    result.addDataValue(laneType, open);
                }
//                aggResults.add(aggResult);
            }
//            result.setAggResults(aggResults);
            resultSet.add(result);
        }
        return resultSet;

    }

    private static Script getDeliverystatusScript() {
        StringBuilder script = new StringBuilder();
        script.append("if(!doc['" + TP_DEL_STATUS_DATE + "'].empty){ return '" + DELIVERED + "';} else {return '" + NOT_DELIVERED + "'};");
        Script scriptObj = new Script(script.toString(), ScriptType.INLINE, null, null);
        return scriptObj;
    }

    private void addDataValue(StringTerms deliveryStatusTerms, AggResult result) {
        for (Terms.Bucket deliveryStatusBucket : deliveryStatusTerms.getBuckets()) {
            result.addDataValue(deliveryStatusBucket.getKeyAsString(), (double) deliveryStatusBucket.getDocCount());
        }
        Map<String, Double> dataValue = result.getDataValue();
        if (dataValue.get(DELIVERED) == null) {
            result.addDataValue(DELIVERED, 0.0);
        }
        if (dataValue.get(NOT_DELIVERED) == null) {
            result.addDataValue(NOT_DELIVERED, 0.0);
        }
    }

    private String getCategoryOption(Multimap<OptionKey, String> options) {
        List optionList = (List) options.get(OptionKey.CATEGORY);
        if (optionList == null || optionList.size() != 1) {
            return null;
        }
        return (String) optionList.get(0);
    }
}