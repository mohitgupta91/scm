package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.snapdeal.scm.core.elastic.dto.ElasticDurationTypeDateRange;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.utils.DateUtils;
import com.snapdeal.scm.utils.MapUtils;
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

import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.ALLOCATION_DATE;
import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.COURIER_GROUP;

/**
 * @author chitransh
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G11")
public class CourierAllocationElasticDas extends AbstractElasticDasImpl implements ILastMilePieChartElasticDas {

    private static final Logger LOG = Logger.getLogger(CourierAllocationElasticDas.class);

    @Override
    public SearchResponse findMetricData(ElasticFilter elasticFilter, String optionKey) {

        elasticFilter.getDurationTypeDateRange().setColumnName(ALLOCATION_DATE.getColumnName());
        return findMetricDataInternal(elasticFilter, optionKey);
    }

    private SearchResponse findMetricDataInternal(ElasticFilter elasticFilter, String optionKey) {

        switch (elasticFilter.getStage()) {
            case ONE:
                return firstLevelAggregation(elasticFilter, optionKey);
            case TWO:
                return secondLevelAggregation(elasticFilter, optionKey);
            default:
                throw new IllegalArgumentException("Invalid stage: " + elasticFilter.getStage().name() + " metrics requested for G21 metrics.");
        }
    }

    private SearchResponse firstLevelAggregation(ElasticFilter elasticFilter, String optionKey) {

        SearchResponse response = new SearchResponse();
        response.setCode(HttpStatus.OK.value());

        BoolQueryBuilder             boolQuery      = applyFilterAndDateRange(elasticFilter);
        String                       subAggregation = COURIER_GROUP.getColumnName();
        ElasticDurationTypeDateRange dateRange      = elasticFilter.getDurationTypeDateRange();

        DateHistogramBuilder histogramBuilder = dateHistogramBuilder("date_buckets", dateRange)
                .interval(DateHistogramInterval.DAY)
                .subAggregation(termsBuilder(subAggregation, subAggregation, 0));

        org.elasticsearch.action.search.SearchResponse elasticResponse           = executeQuery(boolQuery, histogramBuilder);
        Table<String, String, Double>                  dateToCourierToCountTable = HashBasedTable.create();

        List<InternalHistogram.Bucket> datedBuckets = elasticResponse.getAggregations().<InternalHistogram>get("date_buckets").getBuckets();
        for (InternalHistogram.Bucket datedBucket : datedBuckets) {
            try {
                String             dateAsString   = DateUtils.getDateStringFromElastic(datedBucket.getKeyAsString());
                List<Terms.Bucket> courierBuckets = datedBucket.getAggregations().<InternalTerms>get(subAggregation).getBuckets();
                courierBuckets.stream()
                        .filter(courierBucket -> courierBucket.getDocCount() > 0)
                        .forEach(courierBucket -> dateToCourierToCountTable.put(dateAsString, courierBucket.getKeyAsString(), ((Long) courierBucket.getDocCount()).doubleValue()));
            } catch (ParseException e) {
                LOG.info("Exception while processing response for stage: ONE, metrics: G11", e);
                response.setMessage("Something went wrong! Please look at the logs for more details");
                return response;
            }
        }

        // note: we need to filter for top 10 results; rest should go into "OTHERS" bucket
        Table<String, String, Double> dateToCourierToCountTableWithTopResults = HashBasedTable.create();
        dateToCourierToCountTable.rowKeySet().forEach(date -> {
            Map<String, Double> courierToCountMap = dateToCourierToCountTable.row(date);
            Map<String, Double> filteredMap       = MapUtils.sortMapByValueWithTopResults(courierToCountMap, 10, "OTHERS");
            filteredMap.keySet().forEach(courier -> dateToCourierToCountTableWithTopResults.put(date, courier, filteredMap.get(courier)));
        });

        // now we need to invert it and put in courier to date to count format for UI rendering
        Map<String, List<PointSRO>> seriesMap = new HashMap<>();
        dateToCourierToCountTableWithTopResults.rowKeySet().forEach(date -> {
            Map<String, Double> courierToCountMap = dateToCourierToCountTableWithTopResults.row(date);
            courierToCountMap.keySet().forEach(courier -> {
                Double         count = courierToCountMap.get(courier);
                List<PointSRO> list  = seriesMap.getOrDefault(courier, new LinkedList<>());
                list.add(new PointSRO(date, count));
                seriesMap.put(courier, list);
            });
        });

        PointDataSetSRO pointDataSetSRO = new PointDataSetSRO();
        pointDataSetSRO.setSeries(seriesMap);
        pointDataSetSRO.setTitle("Day wise load allocation for couriers");
        response.setData(new SearchResponseDataSRO(optionKey, Collections.singletonList(pointDataSetSRO)));
        return response;
    }

    private SearchResponse secondLevelAggregation(ElasticFilter elasticFilter, String optionKey) {

        SearchResponse response = new SearchResponse();
        response.setCode(HttpStatus.OK.value());

        BoolQueryBuilder                               boolQuery              = applyFilterAndDateRange(elasticFilter);
        String                                         courierGroupColumnName = COURIER_GROUP.getColumnName();
        TermsBuilder                                   termsAggregation       = AggregationBuilders.terms(courierGroupColumnName).field(courierGroupColumnName);
        org.elasticsearch.action.search.SearchResponse elasticResponse        = executeQuery(boolQuery, termsAggregation);

        List<PointSRO>     list           = new LinkedList<>();
        List<Terms.Bucket> courierBuckets = elasticResponse.getAggregations().<InternalTerms>get(courierGroupColumnName).getBuckets();
        courierBuckets.stream()
                .filter(courierBucket -> courierBucket.getDocCount() > 0)
                .forEach(courierBucket ->
                         {
                             String courierGroup = courierBucket.getKeyAsString();
                             Long   docCount     = courierBucket.getDocCount();
                             list.add(new PointSRO(courierGroup, docCount.doubleValue()));
                         });

        PointDataSetSRO pointDataSetSRO = new PointDataSetSRO();
        pointDataSetSRO.setSeries(ImmutableMap.<String, List<PointSRO>>builder().put("count", list).build());
        pointDataSetSRO.setTitle("Courier wise split for " + optionKey + " orders");
        response.setData(new SearchResponseDataSRO(optionKey, Collections.singletonList(pointDataSetSRO)));
        return response;
    }
}
