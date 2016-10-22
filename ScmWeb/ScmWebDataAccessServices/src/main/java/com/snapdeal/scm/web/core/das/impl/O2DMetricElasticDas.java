package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.Multimap;
import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticDurationTypeDateRange;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.web.core.das.ILastMileElasticDas;
import com.snapdeal.scm.web.core.enums.OptionKey;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * O2DMetricElasticDas : G1
 *
 * @author pranav, prateek
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Component("G1")
public class O2DMetricElasticDas extends AbstractElasticDasImpl implements ILastMileElasticDas {

    private static final Logger LOG = LoggerFactory.getLogger(O2DMetricElasticDas.class);

    public static final String O2D               = SubOrderDetailElasticColumn.ORDER_TO_DELIVERED.getColumnName();
    public static final String S2D               = SubOrderDetailElasticColumn.SHIPPED_TO_DELIVERED.getColumnName();
    public static final String DELIVERED_COUNT   = "deliveredcount";
    public static final String LANE_TYPE         = SubOrderDetailElasticColumn.LANE_TYPE.getColumnName();
    public static final String AVG               = "avg";
    public static final String AVG_O2D           = "avg_o2d";
    public static final String AVG_S2D           = "avg_s2d";
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
                aggResults = secondLevelAggregation(boolQuery, super.dateHistogramBuilder(DELIVERED_COUNT, durationTypeDateRange), elasticFilter.getOptionValues());
                break;
            case THREE:
                aggResults = thirdLevelAggregation(boolQuery, super.dateHistogramBuilder(DELIVERED_COUNT, durationTypeDateRange), elasticFilter.getOptionValues());
                break;
            case FOUR:
                aggResults = fourthLevelAggregation(boolQuery, elasticFilter.getOptionValues());
                break;
            case FIVE:
                aggResults = fifthLevelAggregation(boolQuery, elasticFilter.getOptionValues());
                break;
            default:
                break;
        }
        LOG.debug("Elastic Search Query Response : " + aggResults);
        return aggResults;
    }

    private List<AggResult> firstLevelAggregation(BoolQueryBuilder boolQuery, AggregationBuilder<DateHistogramBuilder> metricBuilder) {
        List<AggResult> resultSet = new ArrayList<>();
        DateHistogramBuilder termsBuilder = metricBuilder
                .subAggregation(super.avgBuilder(AVG_O2D, SubOrderDetailElasticColumn.ORDER_TO_DELIVERED.getColumnName()))
                .subAggregation(super.avgBuilder(AVG_S2D, SubOrderDetailElasticColumn.SHIPPED_TO_DELIVERED.getColumnName()));
        SearchResponse searchResponse = super.executeQuery(boolQuery, termsBuilder);
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, DELIVERED_COUNT)) {
            InternalAvg         avgO2D = deliveredCountBucket.getAggregations().get(AVG_O2D);
            InternalAvg         avgS2D = deliveredCountBucket.getAggregations().get(AVG_S2D);
            AggResult<DateTime> result = new AggResult(deliveredCountBucket.getKey());
            result.addDataValue(DELIVERED_COUNT, (double) deliveredCountBucket.getDocCount());
            result.addDataValue(O2D, getAverageValue(avgO2D.getValue()));
            result.addDataValue(S2D, getAverageValue(avgS2D.getValue()));
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> secondLevelAggregation(BoolQueryBuilder boolQuery,
                                                   AggregationBuilder<DateHistogramBuilder> metricBuilder, Multimap<OptionKey, String> options) {
        List<AggResult> resultSet       = new ArrayList<>();
        TermsBuilder    laneTypeBuilder = super.termsBuilderWithMinDocCount(LANE_TYPE, SubOrderDetailElasticColumn.LANE_TYPE.getColumnName(), 0, 0);
        List            optionList      = (List) options.get(OptionKey.CATEGORY);
        if (optionList == null || optionList.size() != 1) {
            return resultSet;
        }
        String optionTypeValue = (String) optionList.get(0);
        if (!optionTypeValue.equalsIgnoreCase(DELIVERED_COUNT)) {
            laneTypeBuilder.subAggregation(super.avgBuilder(AVG, optionTypeValue));
        }
        DateHistogramBuilder termsBuilder   = metricBuilder.subAggregation(laneTypeBuilder);
        SearchResponse       searchResponse = super.executeQuery(boolQuery, termsBuilder);
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, DELIVERED_COUNT)) {
            StringTerms         laneTypeTerm    = deliveredCountBucket.getAggregations().get(LANE_TYPE);
            List<Terms.Bucket>  laneTypeBuckets = laneTypeTerm.getBuckets();
            AggResult<DateTime> result          = new AggResult(deliveredCountBucket.getKey());
            if (optionTypeValue.equalsIgnoreCase(DELIVERED_COUNT)) {
                for (Terms.Bucket laneTypeBucket : laneTypeBuckets) {
                    result.addDataValue(laneTypeBucket.getKeyAsString(), (double) laneTypeBucket.getDocCount());
                }
            } else {
                for (Terms.Bucket laneTypeBucket : laneTypeBuckets) {
                    InternalAvg avg = laneTypeBucket.getAggregations().get(AVG);
                    result.addDataValue(laneTypeBucket.getKeyAsString(), getAverageValue(avg.getValue()));
                }
            }
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> thirdLevelAggregation(BoolQueryBuilder boolQuery,
                                                  AggregationBuilder<DateHistogramBuilder> metricBuilder, Multimap<OptionKey, String> options) {
        List<AggResult> resultSet       = new ArrayList<>();
        TermsBuilder    laneTypeBuilder = super.termsBuilderWithMinDocCount(COURIER_GRP, SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName(), 0, 0);
        List            optionList      = (List) options.get(OptionKey.CATEGORY);
        if (optionList == null || optionList.size() != 1) {
            return resultSet;
        }
        String optionTypeValue = (String) optionList.get(0);
        if (!optionTypeValue.equalsIgnoreCase(DELIVERED_COUNT)) {
            laneTypeBuilder.subAggregation(super.avgBuilder(AVG, optionTypeValue));
        }
        DateHistogramBuilder termsBuilder   = metricBuilder.subAggregation(laneTypeBuilder);
        SearchResponse       searchResponse = super.executeQuery(boolQuery, termsBuilder);
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, DELIVERED_COUNT)) {
            StringTerms         laneTypeTerm    = deliveredCountBucket.getAggregations().get(COURIER_GRP);
            List<Terms.Bucket>  laneTypeBuckets = laneTypeTerm.getBuckets();
            AggResult<DateTime> result          = new AggResult(deliveredCountBucket.getKey());
            if (optionTypeValue.equalsIgnoreCase(DELIVERED_COUNT)) {
                for (Terms.Bucket laneTypeBucket : laneTypeBuckets) {
                    result.addDataValue(laneTypeBucket.getKeyAsString(), (double) laneTypeBucket.getDocCount());
                }
            } else {
                for (Terms.Bucket laneTypeBucket : laneTypeBuckets) {
                    InternalAvg avg = laneTypeBucket.getAggregations().get(AVG);
                    result.addDataValue(laneTypeBucket.getKeyAsString(), getAverageValue(avg.getValue()));
                }
            }
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> fourthLevelAggregation(BoolQueryBuilder boolQuery, Multimap<OptionKey, String> options) {
        List<AggResult> resultSet = new ArrayList<AggResult>();
        AggregationBuilder<TermsBuilder> metricBuilder = super.termsBuilder(DESTINATION_STATE, SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName(), 0)
                .subAggregation(super.avgBuilder(AVG_O2D, SubOrderDetailElasticColumn.ORDER_TO_DELIVERED.getColumnName()))
                .subAggregation(super.avgBuilder(AVG_S2D, SubOrderDetailElasticColumn.SHIPPED_TO_DELIVERED.getColumnName()));
        SearchResponse searchResponse = super.executeQuery(boolQuery, metricBuilder);
        for (Terms.Bucket bucket : super.termsBuckets(searchResponse, DESTINATION_STATE)) {
            AggResult<String>   result    = new AggResult<String>(bucket.getKeyAsString());
            Map<String, Double> dataValue = result.getDataValue();
            dataValue.put(DELIVERED_COUNT, (double) bucket.getDocCount());
            InternalAvg o2dterm = bucket.getAggregations().get(AVG_O2D);
            InternalAvg s2dterm = bucket.getAggregations().get(AVG_S2D);
            dataValue.put(O2D, getAverageValue(o2dterm.getValue()));
            dataValue.put(S2D, getAverageValue(s2dterm.getValue()));
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> fifthLevelAggregation(BoolQueryBuilder boolQuery, Multimap<OptionKey, String> options) {
        List<AggResult> resultSet = new ArrayList<AggResult>();
        AggregationBuilder<TermsBuilder> metricBuilder = super.termsBuilder(DESTINATION_CITY, SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName(), 0)
                .subAggregation(super.avgBuilder(AVG_O2D, SubOrderDetailElasticColumn.ORDER_TO_DELIVERED.getColumnName()))
                .subAggregation(super.avgBuilder(AVG_S2D, SubOrderDetailElasticColumn.SHIPPED_TO_DELIVERED.getColumnName()));
        SearchResponse searchResponse = super.executeQuery(boolQuery, metricBuilder);
        for (Terms.Bucket bucket : super.termsBuckets(searchResponse, DESTINATION_CITY)) {
            AggResult<String>   result    = new AggResult<String>(bucket.getKeyAsString());
            Map<String, Double> dataValue = result.getDataValue();
            dataValue.put(DELIVERED_COUNT, (double) bucket.getDocCount());
            InternalAvg o2dterm = bucket.getAggregations().get(AVG_O2D);
            InternalAvg s2dterm = bucket.getAggregations().get(AVG_S2D);
            dataValue.put(O2D, getAverageValue(o2dterm.getValue()));
            dataValue.put(S2D, getAverageValue(s2dterm.getValue()));
            resultSet.add(result);
        }
        return resultSet;
    }
}