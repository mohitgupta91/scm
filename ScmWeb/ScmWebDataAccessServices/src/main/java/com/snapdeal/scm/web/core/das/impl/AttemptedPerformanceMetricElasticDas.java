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
 * AttemptedPerformanceMetricElasticDas: G7
 *
 * @author prateek
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G7")
public class AttemptedPerformanceMetricElasticDas extends AbstractElasticDasImpl implements ILastMileElasticDas {

    private static final Logger LOG = LoggerFactory.getLogger(AttemptedPerformanceMetricElasticDas.class);

    public static final  String                  ATTEMPTED_COUNT   = "attemptedcount";
    public static final  String                  PERFORMANCE       = "performance";
    public static final  String                  FA0               = "sum_attempted_percentage_d0";
    public static final  String                  FA1               = "sum_attempted_percentage_d1";
    public static final  String                  FA2               = "sum_attempted_percentage_d2";
    public static final  String                  FA3               = "sum_attempted_percentage_d3";
    public static final  String                  FA4               = "sum_attempted_percentage_greater_than_d3";
    public static final  String                  ITR               = "sum_attempted_percentage_itr";
    public static final  String                  RTO               = "sum_attempted_percentage_rto";
    public static final  String                  LANE_TYPE         = SubOrderDetailElasticColumn.LANE_TYPE.getColumnName();
    public static final  String                  DESTINATION_STATE = SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName();
    public static final  String                  DESTINATION_CITY  = SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName();
    public static final  String                  COURIER_GRP       = SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName();
    private static final String                  EXTRA             = "extra";
    private static final String                  FULL              = "full";
    private static       HashMap<String, Script> scripts           = new HashMap<>();

    static {
        scripts.put(FA0, getAttemptedScript(" == 0", FA0));
        scripts.put(FA1, getAttemptedScript(" == 1", FA1));
        scripts.put(FA2, getAttemptedScript(" == 2", FA2));
        scripts.put(FA3, getAttemptedScript(" == 3", FA3));
        scripts.put(FA4, getAttemptedScript(" > 3", FA4));
        scripts.put(RTO, getRTOScript());
        scripts.put(ITR, getITRScript());
        scripts.put(FULL, getPerformancePercentageScript());
    }

    @Override
    public List<AggResult> findMetricData(ElasticFilter elasticFilter) {
        LOG.info("Elastic Search Query Filter : " + elasticFilter);
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
                aggResults = firstLevelAggregation(boolQuery, super.dateHistogramBuilder(ATTEMPTED_COUNT, durationTypeDateRange));
                break;
            case TWO:
                aggResults = secondLevelAggregation(boolQuery, super.dateHistogramBuilder(ATTEMPTED_COUNT, durationTypeDateRange), elasticFilter.getOptionValues());
                break;
            case THREE:
                aggResults = thirdLevelAggregation(boolQuery, super.dateHistogramBuilder(ATTEMPTED_COUNT, durationTypeDateRange), elasticFilter.getOptionValues());
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
        LOG.info("Elastic Search Query Response : " + aggResults);
        return aggResults;
    }

    private List<AggResult> firstLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder) {
        dateHistogramBuilder.subAggregation(super.termBuilderByScript(PERFORMANCE, scripts.get(FULL), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, ATTEMPTED_COUNT)) {
            AggResult<DateTime> result        = new AggResult(deliveredCountBucket.getKey());
            double              totalDocCount = (double) deliveredCountBucket.getDocCount();
            result.addDataValue(ATTEMPTED_COUNT, totalDocCount);
            StringTerms eddrStringTerms = deliveredCountBucket.getAggregations().get(PERFORMANCE);
            addEddrDataValue(eddrStringTerms, result, totalDocCount);
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
        if (!optionTypeValue.equalsIgnoreCase(ATTEMPTED_COUNT)) {
            laneTypeBuilder.subAggregation(super.termBuilderByScript(PERFORMANCE, scripts.get(optionTypeValue), 0));
        }
        dateHistogramBuilder.subAggregation(laneTypeBuilder);
        SearchResponse searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, ATTEMPTED_COUNT)) {
            AggResult<DateTime> result       = new AggResult(deliveredCountBucket.getKey());
            StringTerms         laneTypeTerm = deliveredCountBucket.getAggregations().get(LANE_TYPE);
            for (Terms.Bucket laneTypeBucket : laneTypeTerm.getBuckets()) {
                double totalLaneTypeDocCount = (double) laneTypeBucket.getDocCount();
                if (optionTypeValue.equalsIgnoreCase(ATTEMPTED_COUNT)) {
                    result.addDataValue(ATTEMPTED_COUNT, totalLaneTypeDocCount);
                } else {
                    StringTerms laneTypeStringTerms = laneTypeBucket.getAggregations().get(PERFORMANCE);
                    addCategoryOptionDataValue(laneTypeBucket.getKeyAsString(), laneTypeStringTerms, result, totalLaneTypeDocCount, optionTypeValue);
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
        if (!optionTypeValue.equalsIgnoreCase(ATTEMPTED_COUNT)) {
            courierGroupBuilder.subAggregation(super.termBuilderByScript(PERFORMANCE, scripts.get(optionTypeValue), 0));
        }
        dateHistogramBuilder.subAggregation(courierGroupBuilder);
        SearchResponse searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, ATTEMPTED_COUNT)) {
            AggResult<DateTime> result           = new AggResult(deliveredCountBucket.getKey());
            StringTerms         courierGroupTerm = deliveredCountBucket.getAggregations().get(COURIER_GRP);
            for (Terms.Bucket courierGroupBucket : courierGroupTerm.getBuckets()) {
                double totalCourierGroupDocCount = (double) courierGroupBucket.getDocCount();
                if (optionTypeValue.equalsIgnoreCase(ATTEMPTED_COUNT)) {
                    result.addDataValue(ATTEMPTED_COUNT, totalCourierGroupDocCount);
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
            AggResult<String> result                    = new AggResult<String>(courierGroupBucket.getKeyAsString());
            double            totalCourierGroupDocCount = (double) courierGroupBucket.getDocCount();
            result.addDataValue(ATTEMPTED_COUNT, totalCourierGroupDocCount);
            StringTerms courierStringTerms = courierGroupBucket.getAggregations().get(PERFORMANCE);
            addEddrDataValue(courierStringTerms, result, totalCourierGroupDocCount);
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
            result.addDataValue(ATTEMPTED_COUNT, totalDestinationStateDocCount);
            StringTerms destinationStateStringTerms = destinationStateBucket.getAggregations().get(PERFORMANCE);
            addEddrDataValue(destinationStateStringTerms, result, totalDestinationStateDocCount);
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
            result.addDataValue(ATTEMPTED_COUNT, totalDestinationCityDocCount);
            StringTerms destinationCityStringTerms = destinationCityBucket.getAggregations().get(PERFORMANCE);
            addEddrDataValue(destinationCityStringTerms, result, totalDestinationCityDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private static Script getPerformancePercentageScript() {
        StringBuilder script = new StringBuilder();
        script.append("if(!doc['").append(SubOrderDetailElasticColumn.RTO_DATE.getColumnName())
                .append("'].empty && doc['").append(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName())
                .append("'].empty){ return \"").append(RTO)
                .append("\";} else if(!doc['").append(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE.getColumnName())
                .append("'].empty){def deliveredDuration = groovy.time.TimeCategory.minus(new java.util.Date(doc['").append(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE.getColumnName())
                .append("'].value), new java.util.Date(doc['").append(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName())
                .append("'].value));if(deliveredDuration.days == 0 ){ return \"").append(FA0)
                .append("\";} else if(deliveredDuration.days == 1 ){ return \"").append(FA1)
                .append("\";} else if(deliveredDuration.days == 2 ){ return \"").append(FA2)
                .append("\";} else if(deliveredDuration.days == 3 ){ return \"").append(FA3)
                .append("\";} else if(deliveredDuration.days > 3 ){ return \"").append(FA4)
                .append("\";}} else{ return \"").append(ITR).append("\";};");
        return new Script(script.toString(), ScriptType.INLINE, null, null);
    }

    private void addEddrDataValue(StringTerms stringTerms, AggResult result, double totalDestinationCityDocCount) {
        for (Terms.Bucket eddrBucket : stringTerms.getBuckets()) {
            result.addDataValue(eddrBucket.getKeyAsString(), getPercentage(totalDestinationCityDocCount, eddrBucket.getDocCount()));
        }
        Map<String, Double> dataValue = result.getDataValue();
        if (dataValue.get(FA0) == null) {
            result.addDataValue(FA0, 0.0);
        }
        if (dataValue.get(FA1) == null) {
            result.addDataValue(FA1, 0.0);
        }
        if (dataValue.get(FA2) == null) {
            result.addDataValue(FA2, 0.0);
        }
        if (dataValue.get(FA3) == null) {
            result.addDataValue(FA3, 0.0);
        }
        if (dataValue.get(FA4) == null) {
            result.addDataValue(FA4, 0.0);
        }
        if (dataValue.get(ITR) == null) {
            result.addDataValue(ITR, 0.0);
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

    private static Script getAttemptedScript(String count, String key) {
        StringBuilder script = new StringBuilder();
        script.append("if(!doc['").append(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE.getColumnName())
                .append("'].empty && doc['").append(SubOrderDetailElasticColumn.RTO_DATE.getColumnName())
                .append("'].empty){def deliveredDuration = groovy.time.TimeCategory.minus(new java.util.Date(doc['").append(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE.getColumnName())
                .append("'].value), new java.util.Date(doc['").append(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName())
                .append("'].value));if(deliveredDuration.days ").append(count).append(" ){ return \"").append(key)
                .append("\";}}; return \"").append(EXTRA).append("\";");
        return new Script(script.toString(), ScriptType.INLINE, null, null);
    }

    private static Script getITRScript() {
        StringBuilder script = new StringBuilder();
        script.append("if(doc['").append(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE.getColumnName())
                .append("'].empty && doc['").append(SubOrderDetailElasticColumn.RTO_DATE.getColumnName())
                .append("'].empty){ return \"").append(ITR)
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

    private String getCategoryOption(Multimap<OptionKey, String> options) {
        List optionList = (List) options.get(OptionKey.CATEGORY);
        if (optionList == null || optionList.size() != 1) {
            return null;
        }
        return (String) optionList.get(0);
    }
}