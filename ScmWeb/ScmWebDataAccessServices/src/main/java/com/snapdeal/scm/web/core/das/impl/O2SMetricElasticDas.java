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
 * O2SMetricElasticDas :  G2
 *
 * @author prateek
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Component("G2")
public class O2SMetricElasticDas extends AbstractElasticDasImpl implements ILastMileElasticDas {

    private static final Logger LOG = LoggerFactory.getLogger(O2SMetricElasticDas.class);

    public static final String O2S               = SubOrderDetailElasticColumn.ORDER_TO_SHIPPED.getColumnName();
    public static final String SHIPPED_COUNT     = "shippedcount";
    public static final String FULFILLMENT_MODEL = SubOrderDetailElasticColumn.FULFILLMENT_MODEL.getColumnName();
    public static final String AVG               = "avg";
    public static final String AVG_O2S           = "avg_o2s";
    public static final String SOURCE_STATE      = SubOrderDetailElasticColumn.SOURCE_STATE.getColumnName();
    public static final String SOURCE_CITY       = SubOrderDetailElasticColumn.SOURCE_CITY.getColumnName();
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
        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        BoolQueryBuilder boolQuery = super.applyFilterAndDateRange(elasticFilter);
        switch (elasticFilter.getStage()) {
            case ONE:
                aggResults = firstLevelAggregation(boolQuery, super.dateHistogramBuilder(SHIPPED_COUNT, durationTypeDateRange));
                break;
            case TWO:
                aggResults = secondLevelAggregation(boolQuery, super.dateHistogramBuilder(SHIPPED_COUNT, durationTypeDateRange), elasticFilter.getOptionValues());
                break;
            case THREE:
                aggResults = thirdLevelAggregation(boolQuery, super.dateHistogramBuilder(SHIPPED_COUNT, durationTypeDateRange), elasticFilter.getOptionValues());
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
                .subAggregation(super.avgBuilder(AVG_O2S, SubOrderDetailElasticColumn.ORDER_TO_SHIPPED.getColumnName()));
        SearchResponse searchResponse = super.executeQuery(boolQuery, termsBuilder);
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, SHIPPED_COUNT)) {
            InternalAvg         avgO2S = deliveredCountBucket.getAggregations().get(AVG_O2S);
            AggResult<DateTime> result = new AggResult(deliveredCountBucket.getKey());
            result.addDataValue(SHIPPED_COUNT, (double) deliveredCountBucket.getDocCount());
            result.addDataValue(O2S, getAverageValue(avgO2S.getValue()));
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> secondLevelAggregation(BoolQueryBuilder boolQuery,
                                                   AggregationBuilder<DateHistogramBuilder> metricBuilder, Multimap<OptionKey, String> options) {
        List<AggResult> resultSet       = new ArrayList<>();
        TermsBuilder    laneTypeBuilder = super.termsBuilderWithMinDocCount(FULFILLMENT_MODEL, SubOrderDetailElasticColumn.FULFILLMENT_MODEL.getColumnName(), 0, 0);
        List            optionList      = (List) options.get(OptionKey.CATEGORY);
        if (optionList == null || optionList.size() != 1) {
            return resultSet;
        }
        String optionTypeValue = (String) optionList.get(0);
        if (!optionTypeValue.equalsIgnoreCase(SHIPPED_COUNT)) {
            laneTypeBuilder.subAggregation(super.avgBuilder(AVG, optionTypeValue));
        }
        DateHistogramBuilder termsBuilder   = metricBuilder.subAggregation(laneTypeBuilder);
        SearchResponse       searchResponse = super.executeQuery(boolQuery, termsBuilder);
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, SHIPPED_COUNT)) {
            StringTerms         fulfillmentModelTerm    = deliveredCountBucket.getAggregations().get(FULFILLMENT_MODEL);
            List<Terms.Bucket>  fulfillmentModelBuckets = fulfillmentModelTerm.getBuckets();
            AggResult<DateTime> result                  = new AggResult(deliveredCountBucket.getKey());
            if (optionTypeValue.equalsIgnoreCase(SHIPPED_COUNT)) {
                for (Terms.Bucket fulfillmentModelBucket : fulfillmentModelBuckets) {
                    result.addDataValue(fulfillmentModelBucket.getKeyAsString(), (double) fulfillmentModelBucket.getDocCount());
                }
            } else {
                for (Terms.Bucket fulfillmentModelBucket : fulfillmentModelBuckets) {
                    InternalAvg avg = fulfillmentModelBucket.getAggregations().get(AVG);
                    result.addDataValue(fulfillmentModelBucket.getKeyAsString(), getAverageValue(avg.getValue()));
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
        if (!optionTypeValue.equalsIgnoreCase(SHIPPED_COUNT)) {
            laneTypeBuilder.subAggregation(super.avgBuilder(AVG, optionTypeValue));
        }
        DateHistogramBuilder termsBuilder   = metricBuilder.subAggregation(laneTypeBuilder);
        SearchResponse       searchResponse = super.executeQuery(boolQuery, termsBuilder);
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, SHIPPED_COUNT)) {
            StringTerms         laneTypeTerm    = deliveredCountBucket.getAggregations().get(COURIER_GRP);
            List<Terms.Bucket>  laneTypeBuckets = laneTypeTerm.getBuckets();
            AggResult<DateTime> result          = new AggResult(deliveredCountBucket.getKey());
            if (optionTypeValue.equalsIgnoreCase(SHIPPED_COUNT)) {
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
        AggregationBuilder<TermsBuilder> metricBuilder = super.termsBuilder(SOURCE_STATE, SubOrderDetailElasticColumn.SOURCE_STATE.getColumnName(), 0)
                .subAggregation(super.avgBuilder(AVG_O2S, SubOrderDetailElasticColumn.ORDER_TO_SHIPPED.getColumnName()));
        SearchResponse searchResponse = super.executeQuery(boolQuery, metricBuilder);
        for (Terms.Bucket bucket : super.termsBuckets(searchResponse, SOURCE_STATE)) {
            AggResult<String>   result    = new AggResult<String>(bucket.getKeyAsString());
            Map<String, Double> dataValue = result.getDataValue();
            dataValue.put(SHIPPED_COUNT, (double) bucket.getDocCount());
            InternalAvg o2sterm = bucket.getAggregations().get(AVG_O2S);
            dataValue.put(O2S, getAverageValue(o2sterm.getValue()));
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> fifthLevelAggregation(BoolQueryBuilder boolQuery, Multimap<OptionKey, String> options) {
        List<AggResult> resultSet = new ArrayList<AggResult>();
        AggregationBuilder<TermsBuilder> metricBuilder = super.termsBuilder(SOURCE_CITY, SubOrderDetailElasticColumn.SOURCE_CITY.getColumnName(), 0)
                .subAggregation(super.avgBuilder(AVG_O2S, SubOrderDetailElasticColumn.ORDER_TO_SHIPPED.getColumnName()));
        SearchResponse searchResponse = super.executeQuery(boolQuery, metricBuilder);
        for (Terms.Bucket bucket : super.termsBuckets(searchResponse, SOURCE_CITY)) {
            AggResult<String>   result    = new AggResult<String>(bucket.getKeyAsString());
            Map<String, Double> dataValue = result.getDataValue();
            dataValue.put(SHIPPED_COUNT, (double) bucket.getDocCount());
            InternalAvg o2sterm = bucket.getAggregations().get(AVG_O2S);
            dataValue.put(O2S, getAverageValue(o2sterm.getValue()));
            resultSet.add(result);
        }
        return resultSet;
    }
}