package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.cache.impl.ScmPropertyCache;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.enums.ScmPropertyEnum;
import com.snapdeal.scm.utils.DateUtils;
import com.snapdeal.scm.web.core.das.ILastMilePieChartElasticDas;
import com.snapdeal.scm.web.core.response.SearchResponse;
import com.snapdeal.scm.web.core.sro.PointDataSetSRO;
import com.snapdeal.scm.web.core.sro.PointSRO;
import com.snapdeal.scm.web.core.sro.SearchResponseDataSRO;
import org.apache.log4j.Logger;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.*;

/**
 * @author mohit, chitransh
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G21")
public class ComplaintsPreDeliveryMetricElasticDas extends AbstractElasticDasImpl implements ILastMilePieChartElasticDas {

    private static final Logger LOG      = Logger.getLogger(ComplaintsPreDeliveryMetricElasticDas.class);

    @Override
    public SearchResponse findMetricData(ElasticFilter elasticFilter, String optionKey) {

        elasticFilter.getDurationTypeDateRange().setColumnName(SP_AWB_UPLOADED_STATUS_DATE.getColumnName());
        return findMetricDataInternal(elasticFilter, optionKey);
    }

    private SearchResponse findMetricDataInternal(ElasticFilter elasticFilter, String optionKey) {

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
                throw new IllegalArgumentException("Invalid stage: " + elasticFilter.getStage().name() + " metrics requested for G21 metrics.");
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
            LOG.info("Exception while processing response for stage: ONE, metrics: G21", e);
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

    private SearchResponse secondLevelAggregation(ElasticFilter elasticFilter, String optionKey) {

        SearchResponse response = new SearchResponse();
        response.setCode(HttpStatus.OK.value());

        BoolQueryBuilder boolQueryForITROrders = buildQueryForCurrentMetrics(elasticFilter).mustNot(existsField(TP_FIRST_UDL_STATUS_DATE));
        BoolQueryBuilder boolQueryForUDOrders  = buildQueryForCurrentMetrics(elasticFilter).must(existsField(TP_FIRST_UDL_STATUS_DATE));

        PointDataSetSRO itrDataSetSRO = getDataSetForITROrdersForSecondLevel(boolQueryForITROrders, elasticFilter);
        PointDataSetSRO udDataSetSRO  = getDataSetForUDOrdersForSecondLevel(boolQueryForUDOrders, elasticFilter);

        if (itrDataSetSRO == null || udDataSetSRO == null) {
            response.setMessage("Something went wrong! Please look at the logs for more details");
            return response;
        }

        response.setData(new SearchResponseDataSRO(optionKey, Lists.newArrayList(itrDataSetSRO, udDataSetSRO)));
        return response;
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

    private SearchResponse fourthLevelAggregation(ElasticFilter elasticFilter, String optionKey) {

        SearchResponse response = new SearchResponse();
        response.setCode(HttpStatus.OK.value());

        List<Long> ageingBuckets   = getAgeingBuckets();
        long       firstBucket     = ageingBuckets.get(0);
        long       secondBucket    = ageingBuckets.get(1);
        long       thirdBucket     = ageingBuckets.get(2);
        long       fourthBucket    = ageingBuckets.get(3);
        String     firstBucketKey  = firstBucket + "-" + secondBucket + " days";
        String     secondBucketKey = secondBucket + "-" + thirdBucket + " days";
        String     thirdBucketKey  = thirdBucket + "-" + fourthBucket + " days";
        String     fourthBucketKey = fourthBucket + "+ days";

        BoolQueryBuilder boolQuery = buildQueryForCurrentMetrics(elasticFilter);
        DateHistogramBuilder histogramBuilder = dateHistogramBuilder("date_buckets", elasticFilter.getDurationTypeDateRange())
                .interval(DateHistogramInterval.DAY);
        org.elasticsearch.action.search.SearchResponse elasticResponse = executeQuery(boolQuery, histogramBuilder);

        Date                           currentTime  = DateUtils.getCurrentTime();
        List<InternalHistogram.Bucket> datedBuckets = elasticResponse.getAggregations().<InternalHistogram>get("date_buckets").getBuckets();
        Map<String, Long>              map          = new HashMap<>();

        for (InternalHistogram.Bucket datedBucket : datedBuckets) {
            long docCount = datedBucket.getDocCount();
            if (docCount > 0) {
                try {
                    Date date = DateUtils.ELASTIC_DATE_FORMAT.parse(datedBucket.getKeyAsString());
                    if (DateUtils.isDiffInBetween(currentTime, date, firstBucket, secondBucket, TimeUnit.DAYS)) {
                        map.put(firstBucketKey, map.getOrDefault(firstBucketKey, 0L) + docCount);
                    } else if (DateUtils.isDiffInBetween(currentTime, date, secondBucket, thirdBucket, TimeUnit.DAYS)) {
                        map.put(secondBucketKey, map.getOrDefault(secondBucketKey, 0L) + docCount);
                    } else if (DateUtils.isDiffInBetween(currentTime, date, thirdBucket, fourthBucket, TimeUnit.DAYS)) {
                        map.put(thirdBucketKey, map.getOrDefault(thirdBucketKey, 0L) + docCount);
                    } else if (DateUtils.isDiffGreaterThan(currentTime, date, fourthBucket, TimeUnit.DAYS)) {
                        map.put(fourthBucketKey, map.getOrDefault(fourthBucketKey, 0L) + docCount);
                    }
                } catch (ParseException e) {
                    LOG.info("Exception while processing response for stage: FOURTH, metrics: G21", e);
                    response.setMessage("Something went wrong! Please look at the logs for more details");
                    return response;
                }
            }
        }

        SearchResponseDataSRO dataSRO = new SearchResponseDataSRO();
        dataSRO.setType(optionKey);
        PointDataSetSRO             pointDataSetSRO = new PointDataSetSRO();
        Map<String, List<PointSRO>> seriesMap       = new HashMap<>();
        map.keySet().forEach(bucket -> seriesMap.put(bucket, Collections.singletonList(new PointSRO("value", map.get(bucket).doubleValue()))));
        pointDataSetSRO.setSeries(seriesMap);
        pointDataSetSRO.setTitle("Ageing of orders");
        response.setData(new SearchResponseDataSRO(optionKey, Collections.singletonList(pointDataSetSRO)));
        return response;
    }

    private BoolQueryBuilder buildQueryForCurrentMetrics(ElasticFilter elasticFilter) {

        return super.applyFilterAndDateRange(elasticFilter)
                .mustNot(existsField(RTO_DATE))
                .must(existsField(SP_AWB_UPLOADED_STATUS_DATE))
                .mustNot(existsField(CLOSED_STATUS_DATE));
    }

    private PointDataSetSRO getDataSetForITROrdersForSecondLevel(BoolQueryBuilder boolQueryForITROrders, ElasticFilter elasticFilter) {

        PointDataSetSRO pointDataSetSRO = null;
        try {
            ListMultimap<String, PointSRO> inTransitOrdersMultimap = executeAndParseDailySubAggregatedQuery(boolQueryForITROrders, elasticFilter, COMPLAINT_ORIGIN);
            Map<String, List<PointSRO>>    itrSeriesMap            = new HashMap<>();

            if (inTransitOrdersMultimap != null && !inTransitOrdersMultimap.isEmpty()) {
                for (String key : inTransitOrdersMultimap.keySet()) {
                    itrSeriesMap.put(key, inTransitOrdersMultimap.get(key));
                }
            }
            pointDataSetSRO = new PointDataSetSRO("Origin of complaints- ITR", itrSeriesMap);
        } catch (Exception e) {
            LOG.info("Exception while processing response for stage: TWO, ITR Orders, metrics: G21", e);
        }
        return pointDataSetSRO;
    }

    private PointDataSetSRO getDataSetForUDOrdersForSecondLevel(BoolQueryBuilder boolQueryForUDOrders, ElasticFilter elasticFilter) {

        PointDataSetSRO pointDataSetSRO = null;
        try {
            Map<String, List<PointSRO>> udSeriesMap = new HashMap<>();

            ListMultimap<String, PointSRO> undeliveredOrdersMultimap = executeAndParseDailySubAggregatedQuery(boolQueryForUDOrders, elasticFilter, COMPLAINT_ORIGIN);
            if (undeliveredOrdersMultimap != null && !undeliveredOrdersMultimap.isEmpty()) {
                for (String key : undeliveredOrdersMultimap.keySet()) {
                    udSeriesMap.put(key, undeliveredOrdersMultimap.get(key));
                }
            }
            pointDataSetSRO = new PointDataSetSRO("Origin of complaints- UD", udSeriesMap);
        } catch (Exception e) {
            LOG.info("Exception while processing response for stage: TWO, UD Orders, metrics: G21", e);
        }
        return pointDataSetSRO;
    }

    private List<Long> getAgeingBuckets() {

        List<String> buckets = CacheManager.getInstance().getCache(ScmPropertyCache.class).getList(ScmPropertyEnum.PRE_COMPLAINT_AGEING_BUCKETS);
        if (buckets == null || buckets.size() != 4) {
            throw new UnsupportedOperationException("The ageing brackets provided for metrics G21 are not in correct format");
        }
        List<Long> bucketsList = new ArrayList<>(4);
        buckets.forEach(bucketString -> {
            try {
                bucketsList.add(Long.parseLong(bucketString));
            } catch (NumberFormatException e) {
                throw new UnsupportedOperationException("The ageing brackets provided for metrics G21 are not in correct number format");
            }
        });
        return bucketsList;
    }
}
