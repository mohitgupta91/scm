package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticDurationTypeDateRange;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.web.core.das.ILastMileElasticDas;
import com.snapdeal.scm.web.core.enums.ChartFilterKey;
import com.snapdeal.scm.web.core.enums.FilterKey;
import com.snapdeal.scm.web.core.enums.OptionKey;
import com.snapdeal.scm.web.core.enums.Stage;
import com.snapdeal.scm.web.core.utils.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
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
 * RTOPercentageMetricElasticDas : G39
 *
 * @author Ashwini
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G39")
public class RTOPercentageMetricElasticDas extends AbstractElasticDasImpl implements ILastMileElasticDas {

    private static final Logger LOG = LoggerFactory.getLogger(RTOPercentageMetricElasticDas.class);

    private static final String STATUS_COUNT       = "statuscount";
    private static final String DATE_RANGE         = "daterange";
    private static final String RTO_COUNT          = "rtopercentage";
    private static final String UDL_COUNT          = "udlpercentage";
    private static final String ITR_COUNT          = "itrpercentage";
    private static final String DESTINATION_STATE  = SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName();
    private static final String DESTINATION_CITY   = SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName();
    private static final String TP_UDL_STATUS_DATE = SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE.getColumnName();
    private static final String RTO_DATE           = SubOrderDetailElasticColumn.RTO_DATE.getColumnName();
    private static final String TP_DEL_STATUS_DATE = SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName();

    @Override
    public List<AggResult> findMetricData(ElasticFilter elasticFilter) {
        LOG.info("Elastic Search Query Filter : " + elasticFilter);
        List<AggResult>              aggResults            = new ArrayList<>();
        ElasticDurationTypeDateRange durationTypeDateRange = elasticFilter.getDurationTypeDateRange();
        if (null == durationTypeDateRange || durationTypeDateRange.getFromDate() == null || durationTypeDateRange.getToDate() == null) {
            return aggResults;
        }
        durationTypeDateRange.setColumnName(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        BoolQueryBuilder boolQuery = super.applyFilterAndDateRange(elasticFilter);
        boolQuery.mustNot(QueryBuilders.existsQuery(TP_DEL_STATUS_DATE));

        Table<Stage, ChartFilterKey, List<FilterKey>> chartFilter = elasticFilter.getChartGroupValue();
        switch (elasticFilter.getStage()) {
            case ONE:
                aggResults = firstLevelAggregation(boolQuery, super.dateHistogramBuilder(STATUS_COUNT, durationTypeDateRange));
                break;
            case TWO:
                aggResults = secondLevelAggregation(boolQuery, super.dateHistogramBuilder(STATUS_COUNT, durationTypeDateRange), chartFilter.row(Stage.TWO));
                break;
            case THREE:
                aggResults = thirdLevelAggregation(boolQuery, super.dateHistogramBuilder(STATUS_COUNT, durationTypeDateRange), chartFilter.row(Stage.THREE), elasticFilter.getOptionValues());
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
        dateHistogramBuilder.subAggregation(super.termBuilderByScript(DATE_RANGE, getStatusCountScript(), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (InternalHistogram.Bucket rtoCountBucket : super.dateHistogramBuckets(searchResponse, STATUS_COUNT)) {
            AggResult<DateTime> result            = new AggResult(rtoCountBucket.getKey());
            double              totalDocCount     = (double) rtoCountBucket.getDocCount();
            StringTerms         statusStringTerms = rtoCountBucket.getAggregations().get(DATE_RANGE);
            addStatusDataValue(statusStringTerms, result, totalDocCount);
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> secondLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder, Map<ChartFilterKey, List<FilterKey>> groupBy) {
        String filterKey = groupBy.get(ChartFilterKey.GROUP_BY).get(0).getSubOrderDetailElasticColumn().getColumnName();

        dateHistogramBuilder.subAggregation(
                super.termsBuilderWithMinDocCount(filterKey, filterKey, 0, 0).subAggregation(super.termBuilderByScript(DATE_RANGE, getStatusCountScript(), 0)));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, dateHistogramBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (InternalHistogram.Bucket rtoCountBucket : super.dateHistogramBuckets(searchResponse, STATUS_COUNT)) {
            AggResult<DateTime>     result        = new AggResult(rtoCountBucket.getKey());
            StringTerms             filterKeyTerm = rtoCountBucket.getAggregations().get(filterKey);
            List<AggResult<String>> aggResults    = new ArrayList<AggResult<String>>();
            result.setAggResults(aggResults);
            for (Bucket filterKeyBucket : filterKeyTerm.getBuckets()) {
                double            totalDocCount     = (double) filterKeyBucket.getDocCount();
                AggResult<String> aggResult         = new AggResult(filterKeyBucket.getKey());
                StringTerms       statusStringTerms = filterKeyBucket.getAggregations().get(DATE_RANGE);
                addStatusDataValue(statusStringTerms, aggResult, totalDocCount);
                aggResults.add(aggResult);
            }
            resultSet.add(result);
        }
        return resultSet;
    }

    private List<AggResult> thirdLevelAggregation(BoolQueryBuilder boolQuery, DateHistogramBuilder dateHistogramBuilder, Map<ChartFilterKey, List<FilterKey>> groupBy, Multimap<OptionKey, String> options) {
        String  filterKey   = groupBy.get(ChartFilterKey.GROUP_BY).get(0).getSubOrderDetailElasticColumn().getColumnName();
        Boolean cummulative = Boolean.valueOf(groupBy.get(ChartFilterKey.CUMMULATIVE).get(0).getFilterKey());

        List<AggResult> resultSet = new ArrayList<AggResult>();

        String optionTypeValue = getCategoryOption(options);
        if (StringUtils.isEmpty(optionTypeValue)) {
            return resultSet;
        }

        AbstractAggregationBuilder aggBuilder = null;
        if (cummulative) {
            aggBuilder = super.termsBuilder(filterKey, filterKey, 0).subAggregation(super.termBuilderByScript(DATE_RANGE, getStatusCountScript(), 0));
        } else {
            aggBuilder = dateHistogramBuilder.subAggregation(super.termsBuilderWithMinDocCount(filterKey, filterKey, 0, 0).subAggregation(super.termBuilderByScript(DATE_RANGE, getStatusCountScript(), 0)));
        }

        SearchResponse searchResponse = super.executeQuery(boolQuery, aggBuilder);

        if (cummulative) {
            for (Bucket filterKeyBucket : super.termsBuckets(searchResponse, filterKey)) {
                double            totalDocCount     = (double) filterKeyBucket.getDocCount();
                AggResult<String> aggResult         = new AggResult(filterKeyBucket.getKey());
                StringTerms       statusStringTerms = filterKeyBucket.getAggregations().get(DATE_RANGE);
                addCategoryOptionDataValue(statusStringTerms, aggResult, totalDocCount, optionTypeValue);
                resultSet.add(aggResult);
            }
        } else {
            for (InternalHistogram.Bucket deliveredCountBucket : super.dateHistogramBuckets(searchResponse, STATUS_COUNT)) {
                AggResult<DateTime>     result        = new AggResult(deliveredCountBucket.getKey());
                StringTerms             filterKeyTerm = deliveredCountBucket.getAggregations().get(filterKey);
                List<AggResult<String>> aggResults    = new ArrayList<AggResult<String>>();
                result.setAggResults(aggResults);
                for (Bucket filterKeyBucket : filterKeyTerm.getBuckets()) {
                    double            totalDocCount     = (double) filterKeyBucket.getDocCount();
                    AggResult<String> aggResult         = new AggResult(filterKeyBucket.getKey());
                    StringTerms       statusStringTerms = filterKeyBucket.getAggregations().get(DATE_RANGE);
                    addCategoryOptionDataValue(statusStringTerms, aggResult, totalDocCount, optionTypeValue);
                    aggResults.add(aggResult);
                }
                resultSet.add(result);
            }
        }

        return resultSet;
    }

    private List<AggResult> fourthLevelAggregation(BoolQueryBuilder boolQuery) {
        TermsBuilder termsBuilder = super.termsBuilder(DESTINATION_STATE, SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName(), 0).subAggregation(
                super.termBuilderByScript(DATE_RANGE, getStatusCountScript(), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, termsBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (Bucket destinationStateBucket : super.termsBuckets(searchResponse, DESTINATION_STATE)) {
            double            totalDocCount     = (double) destinationStateBucket.getDocCount();
            AggResult<String> aggResult         = new AggResult(destinationStateBucket.getKey());
            StringTerms       statusStringTerms = destinationStateBucket.getAggregations().get(DATE_RANGE);
            addStatusDataValue(statusStringTerms, aggResult, totalDocCount);
            resultSet.add(aggResult);
        }
        return resultSet;
    }

    private List<AggResult> fifthLevelAggregation(BoolQueryBuilder boolQuery) {
        TermsBuilder termsBuilder = super.termsBuilder(DESTINATION_CITY, SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName(), 0).subAggregation(
                super.termBuilderByScript(DATE_RANGE, getStatusCountScript(), 0));
        SearchResponse  searchResponse = super.executeQuery(boolQuery, termsBuilder);
        List<AggResult> resultSet      = new ArrayList<AggResult>();
        for (Bucket destinationCityBucket : super.termsBuckets(searchResponse, DESTINATION_CITY)) {
            double            totalDocCount     = (double) destinationCityBucket.getDocCount();
            AggResult<String> aggResult         = new AggResult(destinationCityBucket.getKey());
            StringTerms       statusStringTerms = destinationCityBucket.getAggregations().get(DATE_RANGE);
            addStatusDataValue(statusStringTerms, aggResult, totalDocCount);
            resultSet.add(aggResult);
        }
        return resultSet;
    }

    private Script getStatusCountScript() {
        StringBuilder script = new StringBuilder();
        script.append("if(doc['").append(RTO_DATE).append("'].empty){ return '").append(RTO_COUNT).append("';} else if(doc['").append(TP_UDL_STATUS_DATE).append(
                "'].empty){ return '").append(UDL_COUNT).append("';} else {return '").append(ITR_COUNT).append("';}");
        Script scriptObj = new Script(script.toString(), ScriptType.INLINE, null, null);
        return scriptObj;
    }

    private void addStatusDataValue(StringTerms statusStringTerms, AggResult result, double totalDocCount) {
        for (Terms.Bucket statusBucket : statusStringTerms.getBuckets()) {
            result.addDataValue(statusBucket.getKeyAsString(), getPercentage(totalDocCount, statusBucket.getDocCount()));
        }
        Map<String, Double> dataValue = result.getDataValue();
        if (dataValue.get(RTO_COUNT) == null) {
            result.addDataValue(RTO_COUNT, 0.0);
        }
        if (dataValue.get(UDL_COUNT) == null) {
            result.addDataValue(UDL_COUNT, 0.0);
        }
        if (dataValue.get(ITR_COUNT) == null) {
            result.addDataValue(ITR_COUNT, 0.0);
        }
    }

    private void addCategoryOptionDataValue(StringTerms eddrStringTerms, AggResult aggResult, double totalLaneTypeDocCount, String optionTypeValue) {
        for (Terms.Bucket eddrBucket : eddrStringTerms.getBuckets()) {
            if (eddrBucket.getKeyAsString().equalsIgnoreCase(optionTypeValue)) {
                aggResult.addDataValue(eddrBucket.getKeyAsString(), getPercentage(totalLaneTypeDocCount, eddrBucket.getDocCount()));
            }
        }
        if (aggResult.getDataValue().get(optionTypeValue) == null) {
            aggResult.addDataValue(optionTypeValue, 0.0);
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