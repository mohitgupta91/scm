package com.snapdeal.scm.web.core.das.impl;

import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticDurationTypeDateRange;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.web.core.das.ILastMileElasticDas;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * DeliveredEDDRMetricElasticDas : G3
 *
 * @author prateek
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G3")
public class DeliveredEDDRMetricElasticDas extends AbstractElasticDasImpl implements ILastMileElasticDas {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveredEDDRMetricElasticDas.class);

    public static final String DELIVERED_COUNT   = "deliveredcount";
    public static final String EDDR              = "eddr";
    public static final String BEFORE_EDDR       = "percentage_delivered_before_eddr";
    public static final String WITHIN_EDDR       = "percentage_delivered_within_eddr";
    public static final String AFTER_EDDR        = "percentage_delivered_after_eddr";
    public static final String LANE_TYPE         = SubOrderDetailElasticColumn.LANE_TYPE.getColumnName();
    public static final String DESTINATION_STATE = SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName();
    public static final String DESTINATION_CITY  = SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName();
    public static final String COURIER_GRP       = SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName();

    @Override
    public List<AggResult> findMetricData(ElasticFilter elasticFilter) {
        LOG.debug("Elastic Search Query Filter : " + elasticFilter);
        List<AggResult>              aggResults            = new ArrayList<>();
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();
        if (null == durationTypeDateRange
            || durationTypeDateRange.getFromDate() == null
            || durationTypeDateRange.getToDate() == null) {
            return aggResults;
        }
        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName());
        BoolQueryBuilder boolQuery = super.applyFilterAndDateRange(elasticFilter);
        switch (elasticFilter.getStage()) {
            case ONE:
                aggResults = firstLevelAggregation(boolQuery, super.dateHistogramBuilder(DELIVERED_COUNT, durationTypeDateRange));
                break;
            case TWO:
                aggResults = secondLevelAggregation(boolQuery, super.dateHistogramBuilder(DELIVERED_COUNT, durationTypeDateRange));
                break;
            case THREE:
                aggResults = thirdLevelAggregation(boolQuery, super.dateHistogramBuilder(DELIVERED_COUNT, durationTypeDateRange));
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
        LOG.debug("Elastic Search Query Response : " + aggResults);
        return aggResults;
    }

    private List<AggResult> firstLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder) {
        dateHistogramBuilder.subAggregation(super.termBuilderByScript(EDDR, getEDDRPercentageScript(), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, DELIVERED_COUNT)) {
            AggResult<DateTime> result        = new AggResult(deliveredCountBucket.getKey());
            double              totalDocCount = (double) deliveredCountBucket.getDocCount();
            result.addDataValue(DELIVERED_COUNT, totalDocCount);
            StringTerms eddrStringTerms = deliveredCountBucket.getAggregations().get(EDDR);
            addEddrDataValue(eddrStringTerms, result, totalDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> secondLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder) {
        dateHistogramBuilder.subAggregation(super.termsBuilderWithMinDocCount(LANE_TYPE, SubOrderDetailElasticColumn.LANE_TYPE.getColumnName(), 0, 0)
                                                    .subAggregation(super.termBuilderByScript(EDDR, getEDDRPercentageScript(), 0)));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, DELIVERED_COUNT)) {
            AggResult<DateTime>     result       = new AggResult(deliveredCountBucket.getKey());
            StringTerms             laneTypeTerm = deliveredCountBucket.getAggregations().get(LANE_TYPE);
            List<AggResult<String>> aggResults   = new ArrayList<AggResult<String>>();
            result.setAggResults(aggResults);
            for (Terms.Bucket laneTypeBucket : laneTypeTerm.getBuckets()) {
                double            totalLaneTypeDocCount = (double) laneTypeBucket.getDocCount();
                AggResult<String> aggResult             = new AggResult<String>(laneTypeBucket.getKeyAsString());
                aggResult.addDataValue(DELIVERED_COUNT, totalLaneTypeDocCount);
                StringTerms eddrStringTerms = laneTypeBucket.getAggregations().get(EDDR);
                addEddrDataValue(eddrStringTerms, aggResult, totalLaneTypeDocCount);
                aggResults.add(aggResult);
            }
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> thirdLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder) {
        dateHistogramBuilder.subAggregation(super.termsBuilderWithMinDocCount(COURIER_GRP, SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName(), 0, 0)
                                                    .subAggregation(super.termBuilderByScript(EDDR, getEDDRPercentageScript(), 0)));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, DELIVERED_COUNT)) {
            AggResult<DateTime>     result           = new AggResult(deliveredCountBucket.getKey());
            StringTerms             courierGroupTerm = deliveredCountBucket.getAggregations().get(COURIER_GRP);
            List<AggResult<String>> aggResults       = new ArrayList<AggResult<String>>();
            result.setAggResults(aggResults);
            for (Terms.Bucket courierGroupBucket : courierGroupTerm.getBuckets()) {
                double            totalCourierGroupDocCount = (double) courierGroupBucket.getDocCount();
                AggResult<String> aggResult                 = new AggResult<String>(courierGroupBucket.getKeyAsString());
                aggResult.addDataValue(DELIVERED_COUNT, totalCourierGroupDocCount);
                StringTerms eddrStringTerms = courierGroupBucket.getAggregations().get(EDDR);
                addEddrDataValue(eddrStringTerms, aggResult, totalCourierGroupDocCount);
                aggResults.add(aggResult);
            }
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> fourthLevelAggregation(BoolQueryBuilder boolQuery) {
        TermsBuilder termsBuilder = super.termsBuilder(DESTINATION_STATE, SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName(), 0)
                .subAggregation(super.termBuilderByScript(EDDR, getEDDRPercentageScript(), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, termsBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (Terms.Bucket destinationStateBucket : super.termsBuckets(searchResponse, DESTINATION_STATE)) {
            AggResult<String> result                        = new AggResult<String>(destinationStateBucket.getKeyAsString());
            double            totalDestinationStateDocCount = (double) destinationStateBucket.getDocCount();
            result.addDataValue(DELIVERED_COUNT, totalDestinationStateDocCount);
            StringTerms eddrStringTerms = destinationStateBucket.getAggregations().get(EDDR);
            addEddrDataValue(eddrStringTerms, result, totalDestinationStateDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> fifthLevelAggregation(BoolQueryBuilder boolQuery) {
        TermsBuilder termsBuilder = super.termsBuilder(DESTINATION_CITY, SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName(), 0)
                .subAggregation(super.termBuilderByScript(EDDR, getEDDRPercentageScript(), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, termsBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (Terms.Bucket destinationCityBucket : super.termsBuckets(searchResponse, DESTINATION_CITY)) {
            AggResult<String> result                       = new AggResult<String>(destinationCityBucket.getKeyAsString());
            double            totalDestinationCityDocCount = (double) destinationCityBucket.getDocCount();
            result.addDataValue(DELIVERED_COUNT, totalDestinationCityDocCount);
            StringTerms eddrStringTerms = destinationCityBucket.getAggregations().get(EDDR);
            addEddrDataValue(eddrStringTerms, result, totalDestinationCityDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private static Script getEDDRPercentageScript() {
        StringBuilder script = new StringBuilder();
        script.append("if(new java.util.Date(doc['").append(SubOrderDetailElasticColumn.EXPECTED_DELIVERY_DATE_RANGE_START.getColumnName())
                .append("'].value) > new java.util.Date(doc['").append(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName())
                .append("'].value)){ return \"").append(BEFORE_EDDR)
                .append("\";}; if(new java.util.Date(doc['").append(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName())
                .append("'].value) > new java.util.Date(doc['").append(SubOrderDetailElasticColumn.EXPECTED_DELIVERY_DATE.getColumnName())
                .append("'].value)){ return \"").append(AFTER_EDDR)
                .append("\";}; return \"").append(WITHIN_EDDR).append("\";");
        Script scriptObj = new Script(script.toString(), ScriptType.INLINE, null, null);
        return scriptObj;
    }

    private void addEddrDataValue(StringTerms eddrStringTerms, AggResult result, double totalDestinationCityDocCount) {
        for (Terms.Bucket eddrBucket : eddrStringTerms.getBuckets()) {
            result.addDataValue(eddrBucket.getKeyAsString(), getPercentage(totalDestinationCityDocCount, eddrBucket.getDocCount()));
        }
        Map<String, Double> dataValue = result.getDataValue();
        if (dataValue.get(BEFORE_EDDR) == null) {
            result.addDataValue(BEFORE_EDDR, 0.0);
        }
        if (dataValue.get(WITHIN_EDDR) == null) {
            result.addDataValue(WITHIN_EDDR, 0.0);
        }
        if (dataValue.get(AFTER_EDDR) == null) {
            result.addDataValue(AFTER_EDDR, 0.0);
        }
    }
}
