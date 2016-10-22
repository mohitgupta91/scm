package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.web.core.das.ILastMilePieChartElasticDas;
import com.snapdeal.scm.web.core.enums.ChartFilterKey;
import com.snapdeal.scm.web.core.enums.FilterKey;
import com.snapdeal.scm.web.core.enums.OptionKey;
import com.snapdeal.scm.web.core.enums.Stage;
import com.snapdeal.scm.web.core.response.SearchResponse;
import com.snapdeal.scm.web.core.sro.ChartDataSetSRO;
import com.snapdeal.scm.web.core.sro.ChartSRO;
import com.snapdeal.scm.web.core.sro.SearchResponseDataSRO;
import org.apache.commons.collections.MapUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ashwini.kumar on 13/05/16.
 */
@Service("G40")
@SuppressWarnings("rawtypes")
public class RTOPendencyElasticDas extends AbstractElasticDasImpl implements ILastMilePieChartElasticDas {

    private static final Logger LOG = LoggerFactory.getLogger(RTOPendencyElasticDas.class);

    private static final String RTO_AGEING = "rtoageing";
    private static final String SHP_AGEING = "shpageing";
    private static final String D0         = "d0";
    private static final String D1         = "d1";
    private static final String D2         = "d2";
    private static final String D3         = "d3";

    private static final String INITIATE_NOT_ATTEMPTED  = "initiatednotattempted";
    private static final String ATTEMPTED_NOT_DELIVERED = "attemptednotdelivered";
    private static final String DELIVERED_NOT_ACCEPTED  = "deliverednotaccepted";

    /**
     * @return the final result of the service
     */
    public SearchResponse findMetricData(ElasticFilter elasticFilter, String optionKey) {

        LOG.info("Elastic Search Query Filter : " + elasticFilter);

        SearchRequestBuilder dasRequest = buildDasRequest(super.applyFilterAndDateRange(elasticFilter));

        Table<Stage, ChartFilterKey, List<FilterKey>> chartFilter = elasticFilter.getChartGroupValue();

        SearchResponseDataSRO dataSRO = null;

        switch (elasticFilter.getStage()) {
            case ONE:
                dataSRO = firstLevelAggregation(dasRequest);
                break;
            case TWO:
                dataSRO = secondLevelAggregation(dasRequest, elasticFilter.getOptionValues());
                break;
            case THREE:
                dataSRO = thirdLevelAggregation(dasRequest, elasticFilter.getOptionValues(), chartFilter.row(Stage.THREE));
                break;
            case FOUR:
                dataSRO = fourthLevelAggregation(dasRequest, elasticFilter.getOptionValues(), chartFilter.row(Stage.FOUR));
                break;
            case FIVE:
                dataSRO = fifthLevelAggregation(dasRequest, elasticFilter.getOptionValues());
                break;
            default:
                throw new IllegalArgumentException("Invalid stage: " + elasticFilter.getStage() + " metrics requested.");
        }

        SearchResponse response = new SearchResponse(HttpStatus.OK.value(), dataSRO, null, "Request processed successfully");
        return response;
    }

    private SearchResponseDataSRO firstLevelAggregation(SearchRequestBuilder dasRequest) {
        dasRequest.addAggregation(scriptAggrForRTO()).addAggregation(scriptAggrForSHP());
        LOG.info("Query Elastic First Level : {}", dasRequest);
        org.elasticsearch.action.search.SearchResponse searchResponse = dasRequest.get();
        List<ChartDataSetSRO>                          resultSet      = new ArrayList<>();
        Map<String, List<ChartSRO>>                    series         = new HashMap<>();

        List<ChartSRO> chartSROs = new ArrayList<>();

        List<String> columnsList = new ArrayList<>(Arrays.asList(RTO_AGEING, SHP_AGEING));

        columnsList.forEach(s -> {
            ChartSRO<String> chartSRORTO = new ChartSRO<>();
            chartSRORTO.setTitle(s);
            setCharData(chartSRORTO, searchResponse, null, s);
            setDefaultValues(chartSRORTO);
            chartSROs.add(chartSRORTO);
        });

        columnsList.add(0, OptionKey.CATEGORY.getOptionName());

        series.put(OptionKey.CATEGORY.getOptionName(), chartSROs);

        ChartDataSetSRO sro = new ChartDataSetSRO();
        sro.setColumns(columnsList);
        sro.setSeries(series);
        resultSet.add(sro);

        SearchResponseDataSRO dataSRO = new SearchResponseDataSRO();
        dataSRO.setType(OptionKey.CATEGORY.getOptionName());
        LOG.info("Elastic Search Query Response G40 stage: firstLavel is: {}", resultSet);
        dataSRO.setDataset(resultSet);
        return dataSRO;
    }

    private SearchResponseDataSRO secondLevelAggregation(SearchRequestBuilder dasRequest, Multimap<OptionKey, String> optionKey) {
        String category = new ArrayList<String>(optionKey.get(OptionKey.CATEGORY)).get(0);
        if (RTO_AGEING.equalsIgnoreCase(category)) {
            TermsBuilder termBuilderINA = termBuilderByScript(INITIATE_NOT_ATTEMPTED, initiateNotAttempted(), 0);
            termBuilderINA.subAggregation(scriptAggrForRTO());
            dasRequest.addAggregation(termBuilderINA);
            TermsBuilder termBuilderAND = termBuilderByScript(ATTEMPTED_NOT_DELIVERED, attemptedNotDelivered(), 0);
            termBuilderAND.subAggregation(scriptAggrForRTO());
            dasRequest.addAggregation(termBuilderAND);
            TermsBuilder termBuilderDNA = termBuilderByScript(DELIVERED_NOT_ACCEPTED, deliveredNotAccepted(), 0);
            termBuilderDNA.subAggregation(scriptAggrForRTO());
            dasRequest.addAggregation(termBuilderDNA);
        }
        if (SHP_AGEING.equalsIgnoreCase(category)) {
            TermsBuilder termBuilderINA = termBuilderByScript(INITIATE_NOT_ATTEMPTED, initiateNotAttempted(), 0);
            termBuilderINA.subAggregation(scriptAggrForSHP());
            dasRequest.addAggregation(termBuilderINA);
            TermsBuilder termBuildeAND = termBuilderByScript(ATTEMPTED_NOT_DELIVERED, attemptedNotDelivered(), 0);
            termBuildeAND.subAggregation(scriptAggrForSHP());
            dasRequest.addAggregation(termBuildeAND);
            TermsBuilder termBuilderDNA = termBuilderByScript(DELIVERED_NOT_ACCEPTED, deliveredNotAccepted(), 0);
            termBuilderDNA.subAggregation(scriptAggrForSHP());
            dasRequest.addAggregation(termBuilderDNA);
        }
        LOG.info("Query Elastic First Level : {}", dasRequest);
        org.elasticsearch.action.search.SearchResponse searchResponse = dasRequest.get();

        List<ChartDataSetSRO>       resultSet = new ArrayList<>();
        Map<String, List<ChartSRO>> series    = new HashMap<>();

        List<ChartSRO> chartSROs = new ArrayList<>();

        List<String> columnList = new ArrayList<>(Arrays.asList(INITIATE_NOT_ATTEMPTED, ATTEMPTED_NOT_DELIVERED, DELIVERED_NOT_ACCEPTED));

        columnList.forEach(s -> {
            ChartSRO<String> chartSROINA = new ChartSRO<>();
            chartSROINA.setTitle(s);
            for (Terms.Bucket pendencyBucket : super.termsBuckets(searchResponse, s)) {
                setCharData(chartSROINA, null, pendencyBucket, category);
            }
            setDefaultValues(chartSROINA);
            chartSROs.add(chartSROINA);
        });

        series.put(category, chartSROs);

        ChartDataSetSRO sro = new ChartDataSetSRO();
        sro.setColumns(Arrays.asList(OptionKey.RTO_STATES.getOptionName(), INITIATE_NOT_ATTEMPTED, ATTEMPTED_NOT_DELIVERED, DELIVERED_NOT_ACCEPTED));
        sro.setSeries(series);
        resultSet.add(sro);

        SearchResponseDataSRO dataSRO = new SearchResponseDataSRO();
        dataSRO.setType(OptionKey.RTO_STATES.getOptionName());
        LOG.info("Elastic Search Query Response G40 stage: firstLavel is: {}", resultSet);
        dataSRO.setDataset(resultSet);
        return dataSRO;
    }


    private SearchResponseDataSRO thirdLevelAggregation(SearchRequestBuilder dasRequest, Multimap<OptionKey, String> optionKey, Map<ChartFilterKey, List<FilterKey>> groupBy) {

        String       category             = new ArrayList<String>(optionKey.get(OptionKey.CATEGORY)).get(0);
        String       rtoStates            = new ArrayList<String>(optionKey.get(OptionKey.RTO_STATES)).get(0);
        FilterKey    groupByFilterKey     = groupBy.get(ChartFilterKey.GROUP_BY).get(0);
        String       filterKey            = groupByFilterKey.getSubOrderDetailElasticColumn().getColumnName();
        TermsBuilder filterKeyTermBuilder = super.termsBuilderWithMinDocCount(filterKey, filterKey, 0, 0);
        if (RTO_AGEING.equalsIgnoreCase(category)) {
            switch (rtoStates) {
                case INITIATE_NOT_ATTEMPTED:
                    TermsBuilder termBuilderINA = termBuilderByScript(INITIATE_NOT_ATTEMPTED, initiateNotAttempted(), 0);
                    termBuilderINA.subAggregation(scriptAggrForRTO());
                    filterKeyTermBuilder.subAggregation(termBuilderINA);
                    break;
                case ATTEMPTED_NOT_DELIVERED:
                    TermsBuilder termBuildeAND = termBuilderByScript(ATTEMPTED_NOT_DELIVERED, attemptedNotDelivered(), 0);
                    termBuildeAND.subAggregation(scriptAggrForRTO());
                    filterKeyTermBuilder.subAggregation(termBuildeAND);
                    break;
                case DELIVERED_NOT_ACCEPTED:
                    TermsBuilder termBuilderDNA = termBuilderByScript(DELIVERED_NOT_ACCEPTED, deliveredNotAccepted(), 0);
                    termBuilderDNA.subAggregation(scriptAggrForRTO());
                    filterKeyTermBuilder.subAggregation(termBuilderDNA);
                    break;
            }
        }
        if (SHP_AGEING.equalsIgnoreCase(category)) {
            switch (rtoStates) {
                case INITIATE_NOT_ATTEMPTED:
                    TermsBuilder termBuilderINA = termBuilderByScript(INITIATE_NOT_ATTEMPTED, initiateNotAttempted(), 0);
                    termBuilderINA.subAggregation(scriptAggrForSHP());
                    filterKeyTermBuilder.subAggregation(termBuilderINA);
                    break;
                case ATTEMPTED_NOT_DELIVERED:
                    TermsBuilder termBuilderAND = termBuilderByScript(ATTEMPTED_NOT_DELIVERED, attemptedNotDelivered(), 0);
                    termBuilderAND.subAggregation(scriptAggrForSHP());
                    filterKeyTermBuilder.subAggregation(termBuilderAND);
                    break;
                case DELIVERED_NOT_ACCEPTED:
                    TermsBuilder termBuilderDNA = termBuilderByScript(DELIVERED_NOT_ACCEPTED, deliveredNotAccepted(), 0);
                    termBuilderDNA.subAggregation(scriptAggrForSHP());
                    filterKeyTermBuilder.subAggregation(termBuilderDNA);
                    break;
            }
        }
        dasRequest.addAggregation(filterKeyTermBuilder);
        LOG.info("Query Elastic First Level : {}", dasRequest);
        org.elasticsearch.action.search.SearchResponse searchResponse = dasRequest.get();

        List<ChartDataSetSRO>       resultSet = new ArrayList<>();
        Map<String, List<ChartSRO>> series    = new HashMap<>();

        for (Terms.Bucket fulfillmentBucket : super.termsBuckets(searchResponse, filterKey)) {
            List<ChartSRO> chartSROs = new ArrayList<>();

            ChartSRO<String> chartSRO = new ChartSRO<>();
            chartSRO.setTitle(rtoStates);
            for (Terms.Bucket pendencyBucket : super.termsBuckets(fulfillmentBucket, rtoStates)) {
                setCharData(chartSRO, null, pendencyBucket, category);
            }
            setDefaultValues(chartSRO);
            chartSROs.add(chartSRO);

            series.put(fulfillmentBucket.getKeyAsString(), chartSROs);
        }

        ChartDataSetSRO sro = new ChartDataSetSRO();
        sro.setColumns(Arrays.asList(OptionKey.getOptionByName(groupByFilterKey.getFilterKey()).getOptionName(), rtoStates));
        sro.setSeries(series);
        resultSet.add(sro);

        SearchResponseDataSRO dataSRO = new SearchResponseDataSRO();
        dataSRO.setType(OptionKey.getOptionByName(groupByFilterKey.getFilterKey()).getOptionName());
        LOG.info("Elastic Search Query Response G40 stage: thirdLevel is: {}", resultSet);
        dataSRO.setDataset(resultSet);
        return dataSRO;
    }

    private SearchResponseDataSRO fourthLevelAggregation(SearchRequestBuilder dasRequest, Multimap<OptionKey, String> optionKey, Map<ChartFilterKey, List<FilterKey>> groupBy) {
        return thirdLevelAggregation(dasRequest, optionKey, groupBy);
    }

    private SearchResponseDataSRO fifthLevelAggregation(SearchRequestBuilder dasRequest, Multimap<OptionKey, String> optionKey) {

        Map<ChartFilterKey, List<FilterKey>> groupBy = new HashMap<ChartFilterKey, List<FilterKey>>();
        groupBy.put(ChartFilterKey.GROUP_BY, Arrays.asList(FilterKey.LANE));
        return thirdLevelAggregation(dasRequest, optionKey, groupBy);
    }


    public SearchRequestBuilder buildDasRequest(BoolQueryBuilder boolQuery) {
        boolQuery.must(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.RTO_DATE.getColumnName()));
        boolQuery.mustNot(QueryBuilders.existsQuery(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE.getColumnName()));
        SearchRequestBuilder searchRequestBuilder =
                getClient()
                        .prepareSearch(getIndexName())
                        .setTypes(getIndexType())
                        .setQuery(boolQuery)
                        .setSize(0);
        return searchRequestBuilder;
    }

    private AggregationBuilder scriptAggrForRTO() {
        StringBuilder scriptStringRTO = new StringBuilder();
        scriptStringRTO.append("if(!doc['");
        scriptStringRTO.append(SubOrderDetailElasticColumn.RTO_DATE.getColumnName());
        scriptStringRTO.append("'].empty){def rtiDuration = groovy.time.TimeCategory.minus(new java.util.Date(), new java.util.Date(doc['");
        scriptStringRTO.append(SubOrderDetailElasticColumn.RTO_DATE.getColumnName());
        scriptStringRTO.append("'].value));if(rtiDuration.days >= 0 && rtiDuration.days <= 5){ return '");
        scriptStringRTO.append(D0);
        scriptStringRTO.append("';} else if(rtiDuration.days >= 6 && rtiDuration.days <= 10){ return '");
        scriptStringRTO.append(D1);
        scriptStringRTO.append("';} else if(rtiDuration.days >= 11 && rtiDuration.days <= 15){ return '");
        scriptStringRTO.append(D2);
        scriptStringRTO.append("';} else { return '");
        scriptStringRTO.append(D3);
        scriptStringRTO.append("';}} ");
        Script scriptRTO = new Script(scriptStringRTO.toString());
        return termBuilderByScript(RTO_AGEING, scriptRTO, 0);
    }

    private AggregationBuilder scriptAggrForSHP() {
        StringBuilder scriptStringSHP = new StringBuilder();
        scriptStringSHP.append("if(!doc['");
        scriptStringSHP.append(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        scriptStringSHP.append("'].empty){def shpDuration = groovy.time.TimeCategory.minus(new java.util.Date(), new java.util.Date(doc['");
        scriptStringSHP.append(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        scriptStringSHP.append("'].value));if(shpDuration.days >= 0 && shpDuration.days <= 30){ return '");
        scriptStringSHP.append(D0);
        scriptStringSHP.append("';} else if(shpDuration.days >= 31 && shpDuration.days <= 45){ return '");
        scriptStringSHP.append(D1);
        scriptStringSHP.append("';} else if(shpDuration.days >= 46 && shpDuration.days <= 60){return '");
        scriptStringSHP.append(D2);
        scriptStringSHP.append("';} else {return '");
        scriptStringSHP.append(D3);
        scriptStringSHP.append("';}} ");
        Script scriptSHP = new Script(scriptStringSHP.toString());
        return termBuilderByScript(SHP_AGEING, scriptSHP, 0);
    }

    private Script initiateNotAttempted() {
        StringBuilder initiateNotAttempted = new StringBuilder();
        initiateNotAttempted.append("if(doc['");
        initiateNotAttempted.append(SubOrderDetailElasticColumn.TP_CURRENT_STATUS.getColumnName());
        initiateNotAttempted.append("'].value == 'RTI'){return '");
        initiateNotAttempted.append(INITIATE_NOT_ATTEMPTED);
        initiateNotAttempted.append("';} ");
        return new Script(initiateNotAttempted.toString());
    }

    private Script attemptedNotDelivered() {
        StringBuilder attemptedNotDelivered = new StringBuilder();
        attemptedNotDelivered.append("if(doc['");
        attemptedNotDelivered.append(SubOrderDetailElasticColumn.TP_CURRENT_STATUS.getColumnName());
        attemptedNotDelivered.append("'].value != 'RTI' && doc['");
        attemptedNotDelivered.append(SubOrderDetailElasticColumn.TP_CURRENT_STATUS.getColumnName());
        attemptedNotDelivered.append("'].value != 'RTC' && doc['");
        attemptedNotDelivered.append(SubOrderDetailElasticColumn.TP_CURRENT_STATUS.getColumnName());
        attemptedNotDelivered.append("'].value != 'RTN'){return '");
        attemptedNotDelivered.append(ATTEMPTED_NOT_DELIVERED);
        attemptedNotDelivered.append("';} ");
        return new Script(attemptedNotDelivered.toString());
    }

    private Script deliveredNotAccepted() {
        StringBuilder deliveredNotAccepted = new StringBuilder();
        deliveredNotAccepted.append("if(doc['");
        deliveredNotAccepted.append(SubOrderDetailElasticColumn.TP_CURRENT_STATUS.getColumnName());
        deliveredNotAccepted.append("'].value == 'RTC' && doc['");
        deliveredNotAccepted.append(SubOrderDetailElasticColumn.TP_CURRENT_STATUS.getColumnName());
        deliveredNotAccepted.append("'] != 'RTN'){return '");
        deliveredNotAccepted.append(DELIVERED_NOT_ACCEPTED);
        deliveredNotAccepted.append("';} ");
        return new Script(deliveredNotAccepted.toString());
    }

    private void setCharData(ChartSRO<String> chartSRO, org.elasticsearch.action.search.SearchResponse searchResponse, Terms.Bucket bucket, String key) {
        Map<String, Long>  chartData = new HashMap<>();
        List<Terms.Bucket> buckets   = null != searchResponse ? super.termsBuckets(searchResponse, key) : super.termsBuckets(bucket, key);
        for (Terms.Bucket rtoCountBucket : buckets) {
            chartData.put(rtoCountBucket.getKeyAsString(), rtoCountBucket.getDocCount());

        }
        chartSRO.setChartData(chartData);
    }

    private void setDefaultValues(ChartSRO<String> chartSRO) {
        Map<String, Long> chartData = chartSRO.getChartData();
        if (MapUtils.isEmpty(chartData)) {
            return;
        }
        chartData.putIfAbsent(D0, 0L);
        chartData.putIfAbsent(D1, 0L);
        chartData.putIfAbsent(D2, 0L);
        chartData.putIfAbsent(D3, 0L);
        chartSRO.setChartData(chartData);
    }
}
