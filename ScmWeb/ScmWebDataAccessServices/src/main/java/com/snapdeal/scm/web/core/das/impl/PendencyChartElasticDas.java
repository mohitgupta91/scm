package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.snapdeal.base.utils.CollectionUtils;
import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.cache.impl.PriorityMetricsCache;
import com.snapdeal.scm.core.elastic.dto.ElasticDurationTypeDateRange;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.enums.PendencyMetricsStatus;
import com.snapdeal.scm.web.core.das.ILastMilePieChartElasticDas;
import com.snapdeal.scm.web.core.enums.ChartFilterKey;
import com.snapdeal.scm.web.core.enums.FilterKey;
import com.snapdeal.scm.web.core.enums.Stage;
import com.snapdeal.scm.web.core.sro.ChartDataSetSRO;
import com.snapdeal.scm.web.core.sro.ChartSRO;
import com.snapdeal.scm.web.core.sro.SearchResponseDataSRO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author chitransh
 */
public abstract class PendencyChartElasticDas extends AbstractElasticDasImpl implements ILastMilePieChartElasticDas {

    protected String                                              metricsId;
    protected int                                                 noOfLegs;
    protected SubOrderDetailElasticColumn                         dateRangeColumn;
    protected Map<Integer, String>                                descriptions;
    protected Map<Integer, SubOrderDetailElasticColumn>           columnExists;
    protected Multimap<Integer, SubOrderDetailElasticColumn>      columnsMissing;
    protected Multimap<Integer, SubOrderDetailElasticColumn>      columnsAggregated;
    protected Map<Integer, Class<? extends PriorityMetricsCache>> caches;

    public static final    String OVERALL       = "Overall";
    protected static final String HOURS_ELAPSED = "hours_elapsed";
    private static final   Logger LOG           = LoggerFactory.getLogger(PendencyChartElasticDas.class);

    /**
     * @param metricsId         the name of the metrics ex G8, G9....
     * @param noOfLegs          no og legs in the current metric
     * @param dateRangeColumn   the column on which the dat filter is going to be applied
     * @param descriptions      description of the various legs
     * @param columnExists      column that should exist for a given leg
     * @param columnsMissing    columns that should not exist for a given leg
     * @param columnsAggregated columns on which the aggregation should take place for a given leg
     * @param caches            caches corresponding to a given leg
     */
    public PendencyChartElasticDas(String metricsId, int noOfLegs, SubOrderDetailElasticColumn dateRangeColumn, Map<Integer, String> descriptions,
                                   Map<Integer, SubOrderDetailElasticColumn> columnExists, Multimap<Integer, SubOrderDetailElasticColumn> columnsMissing,
                                   Multimap<Integer, SubOrderDetailElasticColumn> columnsAggregated, Map<Integer, Class<? extends PriorityMetricsCache>> caches) {
        this.metricsId = metricsId;
        this.noOfLegs = noOfLegs;
        this.dateRangeColumn = dateRangeColumn;
        this.descriptions = descriptions;
        this.columnExists = columnExists;
        this.columnsMissing = columnsMissing;
        this.columnsAggregated = columnsAggregated;
        this.caches = caches;
    }

    /**
     * @return the column on which the grouping is going to take place; populated in chart filters
     */
    protected FilterKey groupByFilterFromChartFilter(ElasticFilter elasticFilter) {

        FilterKey groupBy = elasticFilter.getChartFilterKeyByStageAndChartFilter(elasticFilter.getStage(), ChartFilterKey.GROUP_BY);
        if (groupBy == null || groupBy.getSubOrderDetailElasticColumn() == null) {
            throw new IllegalArgumentException("Invalid request, no group by column provided");
        }
        return groupBy;
    }

    protected abstract BoolQueryBuilder additionalFilter(BoolQueryBuilder query);

    protected abstract SubOrderDetailElasticColumn groupByColumnForLevelTwoOnwards(ElasticFilter elasticFilter);

    /**
     * assumes that the first level of aggregation has the name <code>HOURS_ELAPSED</code>, which has a numeric value
     *
     * @param response is the response obtained from executing the query, which is aggregated in the order specified by <code>columns</code>
     * @param columns  are the columns on which the response has been aggregated e.g. first courier group, then courier type, then lane type and so on.....
     * @param leg      is the leg according to which the pendency status is determined for every record
     * @return returns a map of PendencyMetricsStatus along with their count, aggregated over all the metrices (uses recursion)
     */
    @SuppressWarnings("unchecked")
    protected Map<PendencyMetricsStatus, Long> parseResponseIntoMap(SearchResponse response, List<SubOrderDetailElasticColumn> columns, int leg) {

        Map<PendencyMetricsStatus, Long> map         = new HashMap<>();
        List<Terms.Bucket>               timeBuckets = response.getAggregations().<InternalTerms>get(HOURS_ELAPSED).getBuckets();

        timeBuckets.stream()
                .filter(bucket -> bucket.getDocCount() > 0)
                .forEach(bucket -> {
                    Long                                     timeInHoursElapsed = bucket.getKeyAsNumber().longValue();
                    Map<SubOrderDetailElasticColumn, String> fieldsMap          = new HashMap<>();
                    parseResponseIntoMap(bucket, fieldsMap, timeInHoursElapsed, columns, 0, map, leg);
                });
        return map;
    }

    /**
     * recursively populates the map to be returned, while processing for each of the <code>columns</code>
     *
     * @param parentBucket is the parent bucket, which will have further aggregations
     * @param fieldsMap    maintains a map of <code>SubOrderDetailElasticColumn</code> and their values, as we keep on recursing
     * @param hoursElapsed no of hours elapsed, which is used in determining the pendency status
     * @param columns      are the columns on which the response has been aggregated e.g. first courier group, then courier type, then lane type and so on.....
     * @param columnIndex  refers to the current index in the <code>columns</code>, which is being processed
     * @param map          stores the end value to be returned
     * @param leg          is the leg according to which the pendency status is determined for every record
     */
    @SuppressWarnings("unchecked")
    private void parseResponseIntoMap(Terms.Bucket parentBucket, Map<SubOrderDetailElasticColumn, String> fieldsMap, Long hoursElapsed,
                                      List<SubOrderDetailElasticColumn> columns, int columnIndex, Map<PendencyMetricsStatus, Long> map, int leg) {

        if (columnIndex >= columns.size()) {
            long                  bucketCount = parentBucket.getDocCount();
            PendencyMetricsStatus status      = getPendencyStatus(fieldsMap, hoursElapsed, leg);
            if (status != null) {
                map.put(status, map.getOrDefault(status, 0L) + bucketCount);
            } else {
                LOG.info("Unable to find any matching key for the record: {}. Skipping {} such records. Please provide a mapping for the same to avoid this in future.", fieldsMap.toString(), bucketCount);
            }
        } else {
            SubOrderDetailElasticColumn column  = columns.get(columnIndex);
            List<Terms.Bucket>          buckets = parentBucket.getAggregations().<InternalTerms>get(column.getColumnName()).getBuckets();

            for (Terms.Bucket bucket : buckets) {
                if (bucket.getDocCount() > 0) {
                    String fieldAsString = bucket.getKeyAsString();
                    fieldsMap.put(column, fieldAsString);
                    parseResponseIntoMap(bucket, fieldsMap, hoursElapsed, columns, columnIndex + 1, map, leg);
                }
            }
        }
    }

    /**
     * assumes that the first level of aggregation has the name <code>HOURS_ELAPSED</code>, which has a numeric value
     *
     * @param response      is the response obtained from executing the query, which is aggregated in the order specified by <code>columns</code>
     * @param columns       are the columns on which the response has been aggregated e.g. first courier group, then courier type, then lane type and so on.....
     * @param leg           is the leg according to which the pendency status is determined for every record
     * @param groupByColumn the column on which the outer grouping takes place, e.g. if this is courier group, then the resultant table will have rows with names GO_JAVAS, JV_EXPRESS
     * @return returns a table of <code>groupByFilter</code>  to PendencyMetricsStatus to their count, aggregated over all the metrices (uses recursion)
     */
    @SuppressWarnings("unchecked")
    protected Table<String, PendencyMetricsStatus, Long> parseResponseIntoTable(SearchResponse response, List<SubOrderDetailElasticColumn> columns,
                                                                                int leg, SubOrderDetailElasticColumn groupByColumn) {

        Table<String, PendencyMetricsStatus, Long> statusTable = HashBasedTable.create();
        List<Terms.Bucket>                         timeBuckets = response.getAggregations().<InternalTerms>get(HOURS_ELAPSED).getBuckets();

        timeBuckets.stream()
                .filter(bucket -> bucket.getDocCount() > 0)
                .forEach(bucket -> {
                    Long                                     timeInHoursElapsed = bucket.getKeyAsNumber().longValue();
                    Map<SubOrderDetailElasticColumn, String> fieldsMap          = new HashMap<>();
                    parseResponseIntoTable(bucket, fieldsMap, timeInHoursElapsed, columns, 0, statusTable, leg, groupByColumn);
                });
        return statusTable;
    }

    /**
     * recursively populates the table to be returned, while processing for each of the <code>columns</code>
     *
     * @param parentBucket  is the parent bucket, which will have further aggregations
     * @param fieldsMap     maintains a map of <code>SubOrderDetailElasticColumn</code> and their values, as we keep on recursing
     * @param hoursElapsed  no of hours elapsed, which is used in determining the pendency status
     * @param columns       are the columns on which the response has been aggregated e.g. first courier group, then courier type, then lane type and so on.....
     * @param columnIndex   refers to the current index in the <code>columns</code>, which is being processed
     * @param table         stores the end value to be returned
     * @param leg           is the leg according to which the pendency status is determined for every record
     * @param groupByColumn the column on which the outer grouping takes place, e.g. if this is courier group, then the resultant table will have rows with names GO_JAVAS, JV_EXPRESS
     */
    @SuppressWarnings("unchecked")
    private void parseResponseIntoTable(Terms.Bucket parentBucket, Map<SubOrderDetailElasticColumn, String> fieldsMap, Long hoursElapsed, List<SubOrderDetailElasticColumn> columns,
                                        int columnIndex, Table<String, PendencyMetricsStatus, Long> table, int leg, SubOrderDetailElasticColumn groupByColumn) {

        if (columnIndex >= columns.size()) {
            long                  bucketCount = parentBucket.getDocCount();
            long                  newCount    = bucketCount;
            PendencyMetricsStatus status      = getPendencyStatus(fieldsMap, hoursElapsed, leg);
            if (status != null) {
                String                           groupBy   = fieldsMap.get(groupByColumn);
                Map<PendencyMetricsStatus, Long> statusMap = table.row(groupBy);
                if (statusMap != null) {
                    newCount = bucketCount + statusMap.getOrDefault(status, 0L);
                }
                table.put(groupBy, status, newCount);
            } else {
                LOG.info("Unable to find any matching key for the record: {}. Skipping {} such records. Please provide a mapping for the same to avoid this in future.", fieldsMap.toString(), bucketCount);
            }
        } else {
            SubOrderDetailElasticColumn column  = columns.get(columnIndex);
            List<Terms.Bucket>          buckets = parentBucket.getAggregations().<InternalTerms>get(column.getColumnName()).getBuckets();

            for (Terms.Bucket bucket : buckets) {
                if (bucket.getDocCount() > 0) {
                    String fieldAsString = bucket.getKeyAsString();
                    fieldsMap.put(column, fieldAsString);
                    parseResponseIntoTable(bucket, fieldsMap, hoursElapsed, columns, columnIndex + 1, table, leg, groupByColumn);
                }
            }
        }
    }

    /**
     * @param columns      are the columns on which the response will be aggregated e.g. first courier group, then courier type, then lane type and so on.....
     * @param columnExists used while calculating the hoursElapsed <code>HOURS_ELAPSED</code> criteria, by subtracting from the current time
     * @return the nested aggregation criteria, in the order specified by <code>columns</code>. The first aggregation has name <code>HOURS_ELAPSED</code>.
     */
    protected AggregationBuilder buildAggregation(List<SubOrderDetailElasticColumn> columns, SubOrderDetailElasticColumn columnExists, Long interval) {

        AggregationBuilder aggregation = null;
        for (int i = columns.size() - 1; i >= 0; i--) {

            SubOrderDetailElasticColumn column           = columns.get(i);
            TermsBuilder                termsAggregation = AggregationBuilders.terms(column.getColumnName()).field(column.getColumnName());
            if (aggregation == null) {
                aggregation = termsAggregation;
            } else {
                aggregation = termsAggregation.subAggregation(aggregation);
            }
        }

        StringBuilder scriptString = new StringBuilder();
        scriptString.append("return (Math.floor((new java.util.Date().getTime() - new java.util.Date(doc['");
        scriptString.append(columnExists.getColumnName());
        scriptString.append("'].value).getTime())/(1000*60*60*" + interval + ")))*" + interval + ";");
        Script script = new Script(scriptString.toString());
        aggregation = AggregationBuilders.terms(HOURS_ELAPSED).script(script).subAggregation(aggregation);
        return aggregation;
    }

    /**
     * @param query          the original query constructed from elastic filter
     * @param columns        used in constructing the aggregation logic
     * @param existingColumn the column which must exist
     * @param missingColumns the columns which does not exist
     * @return the response of the query execution, as per the criteria mentioned above
     */
    protected SearchResponse addConditionAndExecuteQuery(BoolQueryBuilder query, List<SubOrderDetailElasticColumn> columns,
                                                         SubOrderDetailElasticColumn existingColumn, List<SubOrderDetailElasticColumn> missingColumns, Long interval) {

        query = additionalFilter(query);
        query = query.must(QueryBuilders.existsQuery(existingColumn.getName()));
        if (!CollectionUtils.isEmpty(missingColumns)) {
            for (SubOrderDetailElasticColumn missingColumn : missingColumns) {
                query = query.mustNot(QueryBuilders.existsQuery(missingColumn.getName()));
            }
        }

        AggregationBuilder aggregation = buildAggregation(columns, existingColumn, interval);
        return executeQuery(query, aggregation);
    }


    /**
     * @return the final result of the service
     */
    public com.snapdeal.scm.web.core.response.SearchResponse findMetricData(ElasticFilter elasticFilter, String optionKey) {

        com.snapdeal.scm.web.core.response.SearchResponse response = new com.snapdeal.scm.web.core.response.SearchResponse();
        Stage                                             stage    = elasticFilter.getStage();

        SearchResponseDataSRO dataSRO = new SearchResponseDataSRO();
        dataSRO.setType(optionKey);

        ElasticDurationTypeDateRange durationTypeRange = elasticFilter.getDurationTypeDateRange();
        durationTypeRange.setColumnName(dateRangeColumn.getColumnName());
        List<ChartDataSetSRO> chartDataSetSROs = findMetricDataInternal(stage, elasticFilter);
        dataSRO.setDataset(chartDataSetSROs);

        response.setCode(HttpStatus.OK.value());
        response.setData(dataSRO);
        response.setErrors(null);
        response.setMessage("Request processed successfully");
        return response;
    }

    /**
     * @return a list of <code>ChartDataSetSRO</code> as per the elastic filter and stage
     */
    private List<ChartDataSetSRO> findMetricDataInternal(Stage stage, ElasticFilter elasticFilter) {
        switch (stage) {
            case ONE:
                return firstLevelAggregation(elasticFilter);
            case TWO:
            case THREE:
            case FOUR:
                return genericAggregation(elasticFilter);
            default:
                throw new IllegalArgumentException("Invalid stage: " + stage + " metrics requested.");
        }
    }

    /**
     * @return the result for first stage
     */
    @SuppressWarnings("unchecked")
    protected List<ChartDataSetSRO> firstLevelAggregation(ElasticFilter elasticFilter) {

        ChartDataSetSRO chartDataSetSRO = new ChartDataSetSRO();
        chartDataSetSRO.setColumns(null);
        List<ChartSRO<PendencyMetricsStatus>> chartSROs = new LinkedList<>();

        LOG.info("Performing aggregation for stage/level: {}, for metrics: {}", elasticFilter.getStage(), metricsId);
        IntStream.rangeClosed(1, noOfLegs)
                .forEach(leg -> {
                    LOG.info("Performing aggregation for stage/level: {}, leg: {}, for metrics: {}", elasticFilter.getStage(), leg, metricsId);
                    ChartSRO                                       chartSRO          = new ChartSRO(descriptions.get(leg));
                    List<SubOrderDetailElasticColumn>              columnsAggregated = Lists.newArrayList(this.columnsAggregated.get(leg));
                    SubOrderDetailElasticColumn                    columnExists      = this.columnExists.get(leg);
                    List<SubOrderDetailElasticColumn>              columnMissing     = Lists.newArrayList(columnsMissing.get(leg));
                    BoolQueryBuilder                               query             = applyFilterAndDateRange(elasticFilter);
                    Long                                           gcd               = getInterval(leg);
                    org.elasticsearch.action.search.SearchResponse response          = addConditionAndExecuteQuery(query, columnsAggregated, columnExists, columnMissing, gcd);
                    Map<PendencyMetricsStatus, Long>               map               = parseResponseIntoMap(response, columnsAggregated, leg);
                    chartSRO.setChartData(map);
                    chartSROs.add(chartSRO);
                });

        Map<String, List<ChartSRO<PendencyMetricsStatus>>> series = new HashMap<>();
        series.put(OVERALL, chartSROs);
        chartDataSetSRO.setSeries((Map) series);

        List<String> columns = new LinkedList<>();
        columns.add(OVERALL);
        IntStream.rangeClosed(1, noOfLegs).forEach(leg -> columns.add(descriptions.get(leg)));
        chartDataSetSRO.setColumns(columns);

        return Lists.newArrayList(chartDataSetSRO);
    }

    /**
     * @return the result for second, third and fourth stage
     */
    @SuppressWarnings("unchecked")
    protected List<ChartDataSetSRO> genericAggregation(ElasticFilter elasticFilter) {

        ChartDataSetSRO             chartDataSetSRO = new ChartDataSetSRO();
        SubOrderDetailElasticColumn firstColumn     = groupByColumnForLevelTwoOnwards(elasticFilter);

        if (firstColumn == null) {
            throw new IllegalArgumentException("No valid chart filter provided.");
        }

        LOG.info("Performing aggregation for stage/level: {}, for metrics: {}", elasticFilter.getStage(), metricsId);
        Map<String, Map<String, Map<PendencyMetricsStatus, Long>>> groupByToLegToStatusMap = new HashMap<>();
        IntStream.rangeClosed(1, noOfLegs)
                .forEach(leg -> {
                    LOG.info("Performing aggregation for stage/level: {}, leg: {}, for metrics: {}", elasticFilter.getStage(), leg, metricsId);
                    List<SubOrderDetailElasticColumn>          columnsAggregated = copyWithFirstElement(firstColumn, Lists.newArrayList(this.columnsAggregated.get(leg)));
                    SubOrderDetailElasticColumn                columnExists      = this.columnExists.get(leg);
                    List<SubOrderDetailElasticColumn>          columnsMissing    = Lists.newArrayList(this.columnsMissing.get(leg));
                    BoolQueryBuilder                           query             = applyFilterAndDateRange(elasticFilter);
                    Long                                       interval          = getInterval(leg);
                    SearchResponse                             response          = addConditionAndExecuteQuery(query, columnsAggregated, columnExists, columnsMissing, interval);
                    Table<String, PendencyMetricsStatus, Long> table             = parseResponseIntoTable(response, columnsAggregated, leg, firstColumn);
                    String                                     legDescription    = descriptions.get(leg);

                    table.rowKeySet().forEach(outerColumnValue -> {

                        Map<String, Map<PendencyMetricsStatus, Long>> legToStatusMap = groupByToLegToStatusMap.getOrDefault(outerColumnValue, new HashMap<>());
                        Map<PendencyMetricsStatus, Long>              statusMap      = legToStatusMap.getOrDefault(legDescription, new HashMap<>());
                        Map<PendencyMetricsStatus, Long>              tableRow       = table.row(outerColumnValue);
                        tableRow.keySet().forEach(tableKey -> {
                            statusMap.put(tableKey, tableRow.get(tableKey) + statusMap.getOrDefault(tableKey, 0L));
                        });
                        legToStatusMap.put(legDescription, statusMap);
                        groupByToLegToStatusMap.put(outerColumnValue, legToStatusMap);
                    });
                });


        Map<String, List<ChartSRO<PendencyMetricsStatus>>> series = createChartDataSetSeries(groupByToLegToStatusMap);
        chartDataSetSRO.setSeries((Map) series);

        List<String> columns = new LinkedList<>();
        columns.add(firstColumn.getColumnName());
        IntStream.rangeClosed(1, noOfLegs).forEach(leg -> columns.add(descriptions.get(leg)));
        chartDataSetSRO.setColumns(columns);

        return Lists.newArrayList(chartDataSetSRO);
    }

    private Map<String, List<ChartSRO<PendencyMetricsStatus>>> createChartDataSetSeries(Map<String, Map<String, Map<PendencyMetricsStatus, Long>>> groupByToLegToStatusMap) {

        Map<String, List<ChartSRO<PendencyMetricsStatus>>> series = new HashMap<>();
        groupByToLegToStatusMap.keySet().forEach(groupBy -> {
            List<ChartSRO<PendencyMetricsStatus>>         chartSROs      = new LinkedList<>();
            Map<String, Map<PendencyMetricsStatus, Long>> legToStatusMap = groupByToLegToStatusMap.get(groupBy);
            descriptions.values().forEach(description -> {
                chartSROs.add(new ChartSRO<>(description, legToStatusMap.getOrDefault(description, new HashMap<>())));
            });
            series.put(groupBy, chartSROs);
        });
        return series;
    }

    /**
     * @return the pendency status on the basis of leg, keys as per the fields map and the cache entries
     */
    protected PendencyMetricsStatus getPendencyStatus(Map<SubOrderDetailElasticColumn, String> fieldsMap, Long hoursElapsed, int leg) {

        Class<? extends PriorityMetricsCache> cacheClass = caches.get(leg);
        if (cacheClass == null) {
            throw new IllegalArgumentException("Only legs 1-" + noOfLegs + " have been configured.");
        }
        return CacheManager.getInstance().getCache(cacheClass).getMetricsStatus(hoursElapsed, fieldsMap);
    }

    /**
     * @return the gcd of times on the basis of leg
     */
    protected Long getInterval(int leg) {

        Class<? extends PriorityMetricsCache> cacheClass = caches.get(leg);
        if (cacheClass == null) {
            throw new IllegalArgumentException("Only legs 1-" + noOfLegs + " have been configured.");
        }
        return CacheManager.getInstance().getCache(cacheClass).getInterval();
    }

    private List<SubOrderDetailElasticColumn> copyWithFirstElement(SubOrderDetailElasticColumn first, List<SubOrderDetailElasticColumn> list) {

        Set<SubOrderDetailElasticColumn> set = new LinkedHashSet<>();
        set.add(first);
        set.addAll(list);
        return new ArrayList<>(set);
    }
}
