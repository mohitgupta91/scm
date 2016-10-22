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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DeliveredPerformanceMetricElasticDas : G5
 *
 * @author prateek
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G5")
public class DeliveredPerformanceMetricElasticDas extends AbstractElasticDasImpl implements ILastMileElasticDas {

    private static final Logger LOG = LoggerFactory.getLogger(DeliveredEDDRMetricElasticDas.class);

    private static final String                  SHIPPED_COUNT     = "shippedcount";
    private static final String                  PERFORMANCE       = "performance";
    private static final String                  D0                = "sum_delivered_percentage_d0";
    private static final String                  D1                = "sum_delivered_percentage_d1";
    private static final String                  D2                = "sum_delivered_percentage_d2";
    private static final String                  D3                = "sum_delivered_percentage_d3";
    private static final String                  D4                = "sum_delivered_percentage_gretaer_than_d3";
    private static final String                  ITR               = "sum_delivered_percentage_itr";
    private static final String                  UDL               = "sum_delivered_percentage_udl";
    private static final String                  RTO               = "sum_delivered_percentage_rto";
    private static final String                  LANE_TYPE         = SubOrderDetailElasticColumn.LANE_TYPE.getColumnName();
    private static final String                  DESTINATION_STATE = SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName();
    private static final String                  DESTINATION_CITY  = SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName();
    private static final String                  COURIER_GRP       = SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName();
    private static final String                  EXTRA             = "extra";
    private static final String                  FULL              = "full";
    private static       HashMap<String, Script> scripts           = new HashMap<>();

    static {
        scripts.put(D0, getDeliveredScript(" == 0", D0));
        scripts.put(D1, getDeliveredScript(" == 1", D1));
        scripts.put(D2, getDeliveredScript(" == 2", D2));
        scripts.put(D3, getDeliveredScript(" == 3", D3));
        scripts.put(D4, getDeliveredScript(" > 3", D4));
        scripts.put(RTO, getRTOScript());
        scripts.put(UDL, getUDLScript());
        scripts.put(ITR, getITRScript());
        scripts.put(FULL, getPerformancePercentageScript());
    }

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
                aggResults = fourthLevelAggregation(boolQuery);
                break;
            case FIVE:
                aggResults = fifthLevelAggregation(boolQuery);
                break;
            case SIX:
                aggResults = sixthLevelAggregation(boolQuery);
                break;
            default:
                break;
        }
        LOG.debug("Elastic Search Query Response : " + aggResults);
        return aggResults;
    }

    private List<AggResult> firstLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder) {
        dateHistogramBuilder.subAggregation(super.termBuilderByScript(PERFORMANCE, scripts.get(FULL), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (InternalHistogram.Bucket shippedCountBucket : super.dateHistogramBuckets(searchResponse, SHIPPED_COUNT)) {
            AggResult<DateTime> result        = new AggResult(shippedCountBucket.getKey());
            double              totalDocCount = (double) shippedCountBucket.getDocCount();
            result.addDataValue(SHIPPED_COUNT, totalDocCount);
            StringTerms shippedStringTerms = shippedCountBucket.getAggregations().get(PERFORMANCE);
            addPerformanceDataValue(shippedStringTerms, result, totalDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> secondLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder, Multimap<OptionKey, String> options) {
        List<AggResult> resultSet       = new ArrayList<AggResult>();
        TermsBuilder    laneTypeBuilder = super.termsBuilderWithMinDocCount(LANE_TYPE, SubOrderDetailElasticColumn.LANE_TYPE.getColumnName(), 0, 0);
        String          optionTypeValue = getCategoryOption(options);
        if (StringUtils.isEmpty(optionTypeValue)) {
            return resultSet;
        }
        if (!optionTypeValue.equalsIgnoreCase(SHIPPED_COUNT)) {
            laneTypeBuilder.subAggregation(super.termBuilderByScript(PERFORMANCE, scripts.get(optionTypeValue), 0));
        }
        dateHistogramBuilder.subAggregation(laneTypeBuilder);
        SearchResponse searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        for (InternalHistogram.Bucket shippedCountBucket : super.dateHistogramBuckets(searchResponse, SHIPPED_COUNT)) {
            AggResult<DateTime> result       = new AggResult(shippedCountBucket.getKey());
            StringTerms         laneTypeTerm = shippedCountBucket.getAggregations().get(LANE_TYPE);
            for (Terms.Bucket laneTypeBucket : laneTypeTerm.getBuckets()) {
                double totalLaneTypeDocCount = (double) laneTypeBucket.getDocCount();
                if (optionTypeValue.equalsIgnoreCase(SHIPPED_COUNT)) {
                    result.addDataValue(laneTypeBucket.getKeyAsString(), totalLaneTypeDocCount);
                } else {
                    StringTerms laneStringTerms = laneTypeBucket.getAggregations().get(PERFORMANCE);
                    addCategoryOptionDataValue(laneTypeBucket.getKeyAsString(), laneStringTerms, result, totalLaneTypeDocCount, optionTypeValue);
                }
            }
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> thirdLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder, Multimap<OptionKey, String> options) {
        List<AggResult> resultSet           = new ArrayList<AggResult>();
        TermsBuilder    courierGroupBuilder = super.termsBuilderWithMinDocCount(COURIER_GRP, SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName(), 0, 0);
        String          optionTypeValue     = getCategoryOption(options);
        if (StringUtils.isEmpty(optionTypeValue)) {
            return resultSet;
        }
        if (!optionTypeValue.equalsIgnoreCase(SHIPPED_COUNT)) {
            courierGroupBuilder.subAggregation(super.termBuilderByScript(PERFORMANCE, scripts.get(optionTypeValue), 0));
        }
        dateHistogramBuilder.subAggregation(courierGroupBuilder);
        SearchResponse searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        for (InternalHistogram.Bucket shippedCountBucket : super.dateHistogramBuckets(searchResponse, SHIPPED_COUNT)) {
            AggResult<DateTime> result           = new AggResult(shippedCountBucket.getKey());
            StringTerms         courierGroupTerm = shippedCountBucket.getAggregations().get(COURIER_GRP);
            for (Terms.Bucket courierGroupBucket : courierGroupTerm.getBuckets()) {
                double totalCourierGroupDocCount = (double) courierGroupBucket.getDocCount();
                if (optionTypeValue.equalsIgnoreCase(SHIPPED_COUNT)) {
                    result.addDataValue(courierGroupBucket.getKeyAsString(), totalCourierGroupDocCount);
                } else {
                    StringTerms courierStringTerms = courierGroupBucket.getAggregations().get(PERFORMANCE);
                    addCategoryOptionDataValue(courierGroupBucket.getKeyAsString(), courierStringTerms, result, totalCourierGroupDocCount, optionTypeValue);
                }
            }
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> fourthLevelAggregation(BoolQueryBuilder boolQuery) {
        TermsBuilder termsBuilder = super.termsBuilder(COURIER_GRP, SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName(), 0)
                .subAggregation(super.termBuilderByScript(PERFORMANCE, getPerformancePercentageScript(), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, termsBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (Terms.Bucket courierGroupBucket : super.termsBuckets(searchResponse, COURIER_GRP)) {
            AggResult<String> result                  = new AggResult<String>(courierGroupBucket.getKeyAsString());
            double            totalCourierGrpDocCount = (double) courierGroupBucket.getDocCount();
            result.addDataValue(SHIPPED_COUNT, totalCourierGrpDocCount);
            StringTerms eddrStringTerms = courierGroupBucket.getAggregations().get(PERFORMANCE);
            addPerformanceDataValue(eddrStringTerms, result, totalCourierGrpDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> fifthLevelAggregation(BoolQueryBuilder boolQuery) {
        TermsBuilder termsBuilder = super.termsBuilder(DESTINATION_STATE, SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName(), 0)
                .subAggregation(super.termBuilderByScript(PERFORMANCE, getPerformancePercentageScript(), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, termsBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (Terms.Bucket destinationStateBucket : super.termsBuckets(searchResponse, DESTINATION_STATE)) {
            AggResult<String> result                        = new AggResult<String>(destinationStateBucket.getKeyAsString());
            double            totalDestinationStateDocCount = (double) destinationStateBucket.getDocCount();
            result.addDataValue(SHIPPED_COUNT, totalDestinationStateDocCount);
            StringTerms eddrStringTerms = destinationStateBucket.getAggregations().get(PERFORMANCE);
            addPerformanceDataValue(eddrStringTerms, result, totalDestinationStateDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> sixthLevelAggregation(BoolQueryBuilder boolQuery) {
        TermsBuilder termsBuilder = super.termsBuilder(DESTINATION_CITY, SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName(), 0)
                .subAggregation(super.termBuilderByScript(PERFORMANCE, getPerformancePercentageScript(), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, termsBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (Terms.Bucket destinationCityBucket : super.termsBuckets(searchResponse, DESTINATION_CITY)) {
            AggResult<String> result                       = new AggResult<String>(destinationCityBucket.getKeyAsString());
            double            totalDestinationCityDocCount = (double) destinationCityBucket.getDocCount();
            result.addDataValue(SHIPPED_COUNT, totalDestinationCityDocCount);
            StringTerms eddrStringTerms = destinationCityBucket.getAggregations().get(PERFORMANCE);
            addPerformanceDataValue(eddrStringTerms, result, totalDestinationCityDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private static Script getPerformancePercentageScript() {
        StringBuilder script = new StringBuilder();
        script.append("if(!doc['").append(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName())
                .append("'].empty){def deliveredDuration = groovy.time.TimeCategory.minus(new java.util.Date(doc['").append(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName())
                .append("'].value), new java.util.Date(doc['").append(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName())
                .append("'].value));if(deliveredDuration.days == 0 ){ return \"").append(D0)
                .append("\";} else if(deliveredDuration.days == 1 ){ return \"").append(D1)
                .append("\";} else if(deliveredDuration.days == 2 ){ return \"").append(D2)
                .append("\";} else if(deliveredDuration.days == 3 ){ return \"").append(D3)
                .append("\";} else if(deliveredDuration.days > 3 ){ return \"").append(D4)
                .append("\";}} else if(!doc['").append(SubOrderDetailElasticColumn.RTO_DATE.getColumnName())
                .append("'].empty){ return \"").append(RTO)
                .append("\";} else if(!doc['").append(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE.getColumnName())
                .append("'].empty){ return \"").append(UDL)
                .append("\";} else{ return \"").append(ITR).append("\";};");
        Script scriptObj = new Script(script.toString(), ScriptType.INLINE, null, null);
        return scriptObj;
    }

    private static Script getITRScript() {
        StringBuilder script = new StringBuilder();
        script.append("if(doc['").append(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName())
                .append("'].empty && doc['").append(SubOrderDetailElasticColumn.RTO_DATE.getColumnName())
                .append("'].empty && doc['").append(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE.getColumnName())
                .append("'].empty){ return \"").append(ITR)
                .append("\";} else{ return \"").append(EXTRA)
                .append("\";};");
        return new Script(script.toString(), ScriptType.INLINE, null, null);
    }

    private static Script getUDLScript() {
        StringBuilder script = new StringBuilder();
        script.append("if(doc['").append(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName())
                .append("'].empty && doc['").append(SubOrderDetailElasticColumn.RTO_DATE.getColumnName())
                .append("'].empty && !doc['").append(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE.getColumnName())
                .append("'].empty){ return \"").append(UDL)
                .append("\";} else{ return \"").append(EXTRA)
                .append("\";};");
        return new Script(script.toString(), ScriptType.INLINE, null, null);
    }

    private static Script getRTOScript() {
        StringBuilder script = new StringBuilder();
        script.append("if(doc['").append(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName())
                .append("'].empty && !doc['").append(SubOrderDetailElasticColumn.RTO_DATE.getColumnName())
                .append("'].empty){ return \"").append(RTO)
                .append("\";} else{ return \"").append(EXTRA)
                .append("\";};");
        return new Script(script.toString(), ScriptType.INLINE, null, null);
    }

    private static Script getDeliveredScript(String count, String key) {
        StringBuilder script = new StringBuilder();
        script.append("if(!doc['").append(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName())
                .append("'].empty){def deliveredDuration = groovy.time.TimeCategory.minus(new java.util.Date(doc['").append(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName())
                .append("'].value), new java.util.Date(doc['").append(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName())
                .append("'].value));if(deliveredDuration.days ").append(count).append("){ return \"").append(key)
                .append("\";}}; return \"").append(EXTRA).append("\";");
        return new Script(script.toString(), ScriptType.INLINE, null, null);
    }

    private void addPerformanceDataValue(StringTerms stringTerms, AggResult result, double totalDestinationCityDocCount) {
        for (Terms.Bucket eddrBucket : stringTerms.getBuckets()) {
            result.addDataValue(eddrBucket.getKeyAsString(), getPercentage(totalDestinationCityDocCount, eddrBucket.getDocCount()));
        }
        Map<String, Double> dataValue = result.getDataValue();
        if (dataValue.get(D0) == null) {
            result.addDataValue(D0, 0.0);
        }
        if (dataValue.get(D1) == null) {
            result.addDataValue(D1, 0.0);
        }
        if (dataValue.get(D2) == null) {
            result.addDataValue(D2, 0.0);
        }
        if (dataValue.get(D3) == null) {
            result.addDataValue(D3, 0.0);
        }
        if (dataValue.get(D4) == null) {
            result.addDataValue(D4, 0.0);
        }
        if (dataValue.get(ITR) == null) {
            result.addDataValue(ITR, 0.0);
        }
        if (dataValue.get(UDL) == null) {
            result.addDataValue(UDL, 0.0);
        }
        if (dataValue.get(RTO) == null) {
            result.addDataValue(RTO, 0.0);
        }
    }

    private void addCategoryOptionDataValue(String key, StringTerms eddrStringTerms, AggResult<DateTime> aggResult, double totalLaneTypeDocCount, String optionTypeValue) {
        boolean isAdded = false;
        for (Terms.Bucket eddrBucket : eddrStringTerms.getBuckets()) {
            if (eddrBucket.getKeyAsString().equalsIgnoreCase(optionTypeValue)) {
                aggResult.addDataValue(key, getPercentage(totalLaneTypeDocCount, eddrBucket.getDocCount()));
                isAdded = true;
            }
        }
        if (!isAdded) {
            aggResult.addDataValue(key, 0.0);
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