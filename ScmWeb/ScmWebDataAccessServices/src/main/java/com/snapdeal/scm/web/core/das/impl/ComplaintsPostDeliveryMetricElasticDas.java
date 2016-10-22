package com.snapdeal.scm.web.core.das.impl;

import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.CLOSED_STATUS_DATE;
import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.COMPLAINT_CATEGORY;
import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.COMPLAINT_ORIGIN;
import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.COMPLAINT_RESOLUTION;
import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.COURIER_GROUP;
import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.FULFILLMENT_MODEL;
import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.enums.ComplaintResolution;
import com.snapdeal.scm.web.core.das.ILastMilePieChartElasticDas;
import com.snapdeal.scm.web.core.response.SearchResponse;
import com.snapdeal.scm.web.core.sro.PointDataSetSRO;
import com.snapdeal.scm.web.core.sro.PointSRO;
import com.snapdeal.scm.web.core.sro.SearchResponseDataSRO;

@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G22")
public class ComplaintsPostDeliveryMetricElasticDas extends
                                                    AbstractElasticDasImpl implements ILastMilePieChartElasticDas {

    private static final Logger LOG      = Logger.getLogger(ComplaintsPreDeliveryMetricElasticDas.class);
    private static final String COURIERS = "couriers";
    private static final String FM_MODE  = "fullfilmentmode";

    @Override
    public SearchResponse findMetricData(ElasticFilter elasticFilter,
                                         String optionKey) {

        elasticFilter.getDurationTypeDateRange().setColumnName(
                TP_DEL_STATUS_DATE.getColumnName());
        return findMetricDataInternal(elasticFilter, optionKey);
    }

    private SearchResponse findMetricDataInternal(ElasticFilter elasticFilter,
                                                  String optionKey) {
        switch (elasticFilter.getStage()) {
            case ONE:
                return firstLevelAggregation(elasticFilter, optionKey);
            case TWO:
                return secondLevelAggregation(elasticFilter, optionKey);
            case THREE:
                return thirdLevelAggregation(elasticFilter, optionKey);
            case FOUR:
                return fourthLevelAggregation(elasticFilter, optionKey);
            default:
                throw new IllegalArgumentException("Invalid stage: "
                                                   + elasticFilter.getStage().name()
                                                   + " metrics requested for G22 metrics.");
        }
    }

    private SearchResponse firstLevelAggregation(ElasticFilter elasticFilter, String optionKey) {
        SearchResponse response = new SearchResponse();
        response.setCode(HttpStatus.OK.value());
        BoolQueryBuilder boolQuery = buildQueryForCurrentMetrics(elasticFilter);

        ListMultimap<String, PointSRO> seriesMultiMap;
        try {
            seriesMultiMap = executeAndParseDailySubAggregatedQuery(boolQuery, elasticFilter, COMPLAINT_CATEGORY);
        } catch (Exception e) {
            LOG.info(
                    "Exception while processing response for stage: ONE, metrics: G22",
                    e);
            response.setMessage("Something went wrong! Please look at the logs for more details");
            return response;
        }

        Map<String, List<PointSRO>> seriesMap = new HashMap<>();
        seriesMultiMap.keySet().forEach(key -> {
            seriesMap.put(key, seriesMultiMap.get(key));
        });

        PointDataSetSRO pointDataSetSRO = new PointDataSetSRO();
        pointDataSetSRO.setSeries(seriesMap);
        pointDataSetSRO.setTitle("Category of complaints wise split");
        response.setData(new SearchResponseDataSRO(optionKey, Collections.singletonList(pointDataSetSRO)));
        return response;
    }

    private SearchResponse secondLevelAggregation(ElasticFilter elasticFilter,
                                                  String optionKey) {
        SearchResponse response = new SearchResponse();
        response.setCode(HttpStatus.OK.value());

        BoolQueryBuilder boolQueryForPending  = buildQueryForCurrentMetrics(elasticFilter).must(QueryBuilders.termQuery(COMPLAINT_RESOLUTION.getColumnName(), ComplaintResolution.PENDING.name()));
        BoolQueryBuilder boolQueryForRefunded = buildQueryForCurrentMetrics(elasticFilter).must(QueryBuilders.termQuery(COMPLAINT_RESOLUTION.getColumnName(), ComplaintResolution.REFUNDED.name()));
        BoolQueryBuilder boolQueryForReturned = buildQueryForCurrentMetrics(elasticFilter).must(QueryBuilders.termQuery(COMPLAINT_RESOLUTION.getColumnName(), ComplaintResolution.RETURNED.name()));

        PointDataSetSRO rPendingDataSetSRO  = getDataSetForRefundReplacedPendingForSecondLevel(boolQueryForPending, elasticFilter,"Origin of complaints- Return Pending");
        PointDataSetSRO rRefundedDataSetSRO = getDataSetForRefundReplacedPendingForSecondLevel(boolQueryForRefunded, elasticFilter,"Origin of complaints- Return Refunded");
        PointDataSetSRO rReturnedDataSetSRO = getDataSetForRefundReplacedPendingForSecondLevel(boolQueryForReturned, elasticFilter,"Origin of complaints- Return Pending");

        if (rPendingDataSetSRO == null || rRefundedDataSetSRO == null || rReturnedDataSetSRO == null) {
            response.setMessage("Something went wrong! Please look at the logs for more details");
            return response;
        }

        response.setData(new SearchResponseDataSRO(optionKey, Lists.newArrayList(rPendingDataSetSRO,rRefundedDataSetSRO,rReturnedDataSetSRO)));
        return response;
    }

    private PointDataSetSRO getDataSetForRefundReplacedPendingForSecondLevel(
            BoolQueryBuilder boolQuery, ElasticFilter elasticFilter,String comments) {
        PointDataSetSRO pointDataSetSRO = null;
        try {
            ListMultimap<String, PointSRO> multimap = executeAndParseDailySubAggregatedQuery(boolQuery, elasticFilter, COMPLAINT_ORIGIN);
            Map<String, List<PointSRO>>    seriesMap                     = new HashMap<>();

            if (multimap != null && !multimap.isEmpty()) {
                for (String key : multimap.keySet()) {
                    seriesMap.put(key, multimap.get(key));
                }
            }
            pointDataSetSRO = new PointDataSetSRO(comments, seriesMap);
        } catch (Exception e) {
            LOG.info("Exception while processing response for stage: TWO metrics: G22", e);
        }
        return pointDataSetSRO;
    }


    private SearchResponse thirdLevelAggregation(ElasticFilter elasticFilter, String optionKey) {
    	SearchResponse response = new SearchResponse();
        response.setCode(HttpStatus.OK.value());

        BoolQueryBuilder                               boolQuery              = buildQueryForCurrentMetrics(elasticFilter);
        String                                         courierGroupColumnName = COURIER_GROUP.getColumnName();
        TermsBuilder                                   termsAggregation       = AggregationBuilders.terms(courierGroupColumnName).field(courierGroupColumnName);
        org.elasticsearch.action.search.SearchResponse elasticResponse        = executeQuery(boolQuery, termsAggregation);

        Map<String, Long>  courierToComplaintsMap = new HashMap<>();
        List<Terms.Bucket> courierBuckets         = elasticResponse.getAggregations().<InternalTerms>get(courierGroupColumnName).getBuckets();
        courierBuckets.stream()
                .filter(courierBucket -> courierBucket.getDocCount() > 0)
                .forEach(courierBucket ->
                {
                    String courierGroup = courierBucket.getKeyAsString();
                    courierToComplaintsMap.put(courierGroup, courierToComplaintsMap.getOrDefault(courierGroup, 0L) + courierBucket.getDocCount());
                });

        PointDataSetSRO pointDataSetSRO = new PointDataSetSRO();
        List<PointSRO>  list            = new LinkedList<>();
        courierToComplaintsMap.keySet()
                .forEach(courierGroup -> {
                    list.add(new PointSRO(courierGroup, courierToComplaintsMap.get(courierGroup).doubleValue()));
                });

        Map<String, List<PointSRO>> seriesMap = new HashMap<>();
        seriesMap.put("count", list);
        pointDataSetSRO.setSeries(seriesMap);
        pointDataSetSRO.setTitle("Courier wise split for " + optionKey + " orders");
        response.setData(new SearchResponseDataSRO(optionKey, Collections.singletonList(pointDataSetSRO)));
        return response;
    }

    private SearchResponse fourthLevelAggregation(ElasticFilter elasticFilter,
                                                  String optionKey) {
    	SearchResponse response = new SearchResponse();
        response.setCode(HttpStatus.OK.value());

        BoolQueryBuilder                               boolQuery              = buildQueryForCurrentMetrics(elasticFilter);
        String                                         fmColumnName 		  = FULFILLMENT_MODEL.getColumnName();
        TermsBuilder                                   termsAggregation       = AggregationBuilders.terms(fmColumnName).field(fmColumnName);
        org.elasticsearch.action.search.SearchResponse elasticResponse        = executeQuery(boolQuery, termsAggregation);

        Map<String, Long>  fmToComplaintsMap = new HashMap<>();
        List<Terms.Bucket> fmBuckets         = elasticResponse.getAggregations().<InternalTerms>get(fmColumnName).getBuckets();
        fmBuckets.stream()
                .filter(courierBucket -> courierBucket.getDocCount() > 0)
                .forEach(courierBucket ->
                {
                    String courierGroup = courierBucket.getKeyAsString();
                    fmToComplaintsMap.put(courierGroup, fmToComplaintsMap.getOrDefault(courierGroup, 0L) + courierBucket.getDocCount());
                });

        PointDataSetSRO pointDataSetSRO = new PointDataSetSRO();
        List<PointSRO>  list            = new LinkedList<>();
        fmToComplaintsMap.keySet()
                .forEach(courierGroup -> {
                    list.add(new PointSRO(courierGroup, fmToComplaintsMap.get(courierGroup).doubleValue()));
                });

        Map<String, List<PointSRO>> seriesMap = new HashMap<>();
        seriesMap.put("count", list);
        pointDataSetSRO.setSeries(seriesMap);
        pointDataSetSRO.setTitle("FM wise split for " + optionKey + " orders");
        response.setData(new SearchResponseDataSRO(optionKey, Collections.singletonList(pointDataSetSRO)));
        return response;
    }

    private BoolQueryBuilder buildQueryForCurrentMetrics(
            ElasticFilter elasticFilter) {

        return super.applyFilterAndDateRange(elasticFilter)
                // .mustNot(existsField(RTO_DATE))
                .must(existsField(TP_DEL_STATUS_DATE))
                .must(existsField(CLOSED_STATUS_DATE));
    }
}
