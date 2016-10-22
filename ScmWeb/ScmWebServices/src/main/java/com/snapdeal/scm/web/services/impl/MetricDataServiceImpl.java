package com.snapdeal.scm.web.services.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.*;
import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticDateRangeFilter;
import com.snapdeal.scm.core.elastic.dto.ElasticDurationTypeDateRange;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.mongo.doc.MetricsInfo;
import com.snapdeal.scm.mongo.mao.repository.MetricsInfoRepository;
import com.snapdeal.scm.web.core.das.ILastMileElasticDas;
import com.snapdeal.scm.web.core.das.ILastMilePieChartElasticDas;
import com.snapdeal.scm.web.core.dto.DateRangeDTO;
import com.snapdeal.scm.web.core.enums.*;
import com.snapdeal.scm.web.core.mao.MetricDetailsRepository;
import com.snapdeal.scm.web.core.mongo.documents.FilterOptions;
import com.snapdeal.scm.web.core.mongo.documents.MetricDetails;
import com.snapdeal.scm.web.core.request.SearchRequest;
import com.snapdeal.scm.web.core.response.SearchResponse;
import com.snapdeal.scm.web.core.sro.PointDataSetSRO;
import com.snapdeal.scm.web.core.sro.PointSRO;
import com.snapdeal.scm.web.core.sro.SearchResponseDataSRO;
import com.snapdeal.scm.web.core.utils.Constants;
import com.snapdeal.scm.web.core.utils.StringUtils;
import com.snapdeal.scm.web.services.IFilterOptionService;
import com.snapdeal.scm.web.services.IMetricDataService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.joda.time.*;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author gaurav, vinay, pranav, ashwini, chitransh
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Service("metricDataService")
public class MetricDataServiceImpl implements IMetricDataService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy");
    private static final Logger            LOG                 = LoggerFactory.getLogger(MetricDataServiceImpl.class);

    @Autowired
    private IFilterOptionService filterOptionService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MetricsInfoRepository metricsInfoRepository;

    @Autowired
    private MetricDetailsRepository metricDetailsRepository;

    @Override
    public SearchResponse getLevelData(SearchRequest request, Metric metric) throws Exception {

        Stage     currentStage     = Stage.fromCode(request.getStage());
        OptionKey currentOptionKey = Metric.getOption(metric, currentStage);
        if (currentOptionKey == null) {
            LOG.info("Invalid Stage Provided");
            throw new DataValidationException("Invalid Stage Provided");
        }

        ElasticFilter elasticFilter = new ElasticFilter();
        elasticFilter.setStage(currentStage);
        setFiltersAndOptions(request, elasticFilter);
        elasticFilter.setChartGroupValue(setChartFilterValue(request.getChartFilters()));

        String optionKey = currentOptionKey.getOptionName();
        if (currentOptionKey.equals(OptionKey.MULTIPLE)) {
            ChartFilterKey chartFilterKey = Metric.getChartFilter(metric, currentStage);
            FilterKey      optionFilter   = elasticFilter.getChartFilterKeyByStageAndChartFilter(currentStage, chartFilterKey);
            if (null == optionFilter) {
                LOG.info("No Chart Filter Provided");
                throw new DataValidationException("No Chart Filter Provided");
            }
            optionKey = optionFilter.getFilterKey();
        }

        MetricsInfo metricsInfo = metricsInfoRepository.findByMetricsId(metric.getMetricsId());
        if (metricsInfo != null) {
            boolean       usedForPieChart = metricsInfo.isUsedForPieChart();
            MetricDetails metricDetails   = metricDetailsRepository.findByMetricID(metric.getCode());
            DateRangeDTO  dateRangeDTO    = setDurationTypeDateRange(request, elasticFilter, !usedForPieChart, metricDetails);
            if (usedForPieChart) {
                try {
                    ILastMilePieChartElasticDas bean = (ILastMilePieChartElasticDas) context.getBean(Class.forName(metricsInfo.getImplClass()));
                    return bean.findMetricData(elasticFilter, optionKey);
                } catch (BeansException e) {
                    throw new IllegalArgumentException("Unable to find class: " + metricsInfo.getImplClass() + " while instantiating the bean for metrics: " + metric.getGridCode());
                }
            } else {
                try {
                    ILastMileElasticDas bean        = (ILastMileElasticDas) context.getBean(Class.forName(metricsInfo.getImplClass()));
                    List<AggResult>     elasticData = bean.findMetricData(elasticFilter);
                    return aggregatedResultsForDateWiseSearch(elasticData, optionKey, dateRangeDTO);
                } catch (BeansException e) {
                    throw new IllegalArgumentException("Unable to find class: " + metricsInfo.getImplClass() + " while instantiating the bean for metrics: " + metric.getGridCode());
                }
            }
        } else {
            throw new DataValidationException("Given metric not supported yet.");
        }
    }

    private SearchResponse aggregatedResultsForDateWiseSearch(List<AggResult> elasticData, String optionKey, DateRangeDTO dateRangeDTO) throws DataValidationException {

        SearchResponse                              response      = new SearchResponse();
        Map<String, ListMultimap<String, PointSRO>> dataSetSROMap = new HashMap<>();

        elasticData.forEach(aggResult -> {
            if (aggResult.getDataValue() != null && !aggResult.getDataValue().isEmpty()) {
                aggResult.getDataValue().forEach(
                        (key, value) -> populateDataSets(dataSetSROMap, optionKey, (String) key, aggResult.getKey(), (Double) value));
            } else {
                List<AggResult<String>> childAggResults = aggResult.getAggResults();
                if (childAggResults != null && !childAggResults.isEmpty()) {
                    childAggResults.forEach((childAggResult) -> {
                        if (childAggResult.getDataValue() != null && !childAggResult.getDataValue().isEmpty()) {
                            childAggResult.getDataValue().forEach(
                                    (key, value) -> populateDataSets(dataSetSROMap, childAggResult.getKey(), (String) key, aggResult.getKey(), (Double) value));
                        }
                    });
                }
            }
        });

        AtomicBoolean isDateWiseResult = new AtomicBoolean(false);
        if (!CollectionUtils.isEmpty(elasticData) && (elasticData.get(0).getKey() instanceof DateTime)) {
            isDateWiseResult.set(true);
        }

        SearchResponseDataSRO responseDataSRO = new SearchResponseDataSRO();
        List<PointDataSetSRO> dataSetSROs     = new ArrayList<>();
        dataSetSROMap.forEach((key, value) -> dataSetSROs.add(new PointDataSetSRO(key, getNormalizedSeriesMap(value, isDateWiseResult, dateRangeDTO))));
        responseDataSRO.setDataset(dataSetSROs);
        responseDataSRO.setType(optionKey);
        response.setCode(HttpStatus.OK.value());
        response.setErrors(null);
        response.setMessage("Request processed successfully");
        response.setData(responseDataSRO);
        return response;
    }

    private void populateDataSets(Map<String, ListMultimap<String, PointSRO>> dataSetSROMap, String titleKey, String seriesLabel, Object pointX, Double pointY) {

        ListMultimap<String, PointSRO> seriesMultiMap = dataSetSROMap.get(titleKey);
        if (seriesMultiMap == null) {
            seriesMultiMap = ArrayListMultimap.create();
        }
        populateSeriesData(seriesMultiMap, seriesLabel, pointX, pointY);
        dataSetSROMap.put(titleKey, seriesMultiMap);
    }

    private Map<String, List<PointSRO>> getNormalizedSeriesMap(ListMultimap<String, PointSRO> seriesMultiMap, AtomicBoolean isDateWiseResult, DateRangeDTO dateRangeDTO) {
        Map<String, List<PointSRO>> seriesDataMap = new HashMap<>();
        boolean                     normalizeData = false; //Adding as we don't want normalization as of now.
        if (normalizeData && isDateWiseResult.get()) {
            DateTime              startDate = dateRangeDTO.getStart();
            Interval              interval  = new Interval(startDate, dateRangeDTO.getEnd());
            BaseSingleFieldPeriod basePeriod;
            int                   expectedResultSize;
            DateTime              effectiveStartDate;
            switch (dateRangeDTO.getDurationType()) {
                case HOURLY:
                    expectedResultSize = Hours.hoursIn(interval).getHours() + 1;
                    basePeriod = Hours.ONE;
                    effectiveStartDate = startDate.withFields(new LocalTime(startDate.getHourOfDay(), 0, 0, 0));
                    break;
                case DAILY:
                    expectedResultSize = Days.daysIn(interval).getDays() + 1;
                    basePeriod = Days.ONE;
                    effectiveStartDate = startDate.withFields(new LocalTime(0, 0, 0, 0));
                    break;
                case WEEKLY:
                    expectedResultSize = Weeks.weeksIn(interval).getWeeks() + 1;
                    basePeriod = Weeks.ONE;
                    effectiveStartDate = startDate.withDayOfWeek(DateTimeConstants.MONDAY);
                    break;
                case MONTHLY:
                    expectedResultSize = Months.monthsIn(interval).getMonths() + 1;
                    basePeriod = Months.ONE;
                    effectiveStartDate = startDate.withDayOfMonth(1);
                    break;
                case YEARLY:
                    expectedResultSize = Years.yearsIn(interval).getYears() + 1;
                    basePeriod = Years.ONE;
                    effectiveStartDate = startDate.withDayOfYear(1);
                    break;
                default:
                    expectedResultSize = Days.daysIn(interval).getDays() + 1;
                    basePeriod = Days.ONE;
                    effectiveStartDate = startDate.withFields(new LocalTime(0, 0, 0, 0));
            }

            if (expectedResultSize == seriesMultiMap.values().size()) {
                seriesMultiMap.asMap().forEach((key, val) -> {
                    List<PointSRO> tempList = new ArrayList<>(val);
                    seriesDataMap.put(key, tempList);
                });
            } else {
                seriesMultiMap.asMap().forEach((key, val) -> {
                    Map<String, PointSRO> pointMap = new HashMap<>();
                    val.forEach((pointSRO -> {
                        pointMap.put(pointSRO.getX(), pointSRO);
                    }));
                    List<PointSRO> tempList     = new ArrayList<>();
                    DateTime       tempDateTime = effectiveStartDate;
                    for (int i = 0; i < expectedResultSize; i++, tempDateTime = tempDateTime.plus(basePeriod)) {
                        String pointX = DATE_TIME_FORMATTER.print(tempDateTime);
                        tempList.add((pointMap.get(pointX) != null) ? pointMap.get(pointX) : new PointSRO(pointX, 0.0));
                    }
                    seriesDataMap.put(key, tempList);
                });
            }
        } else {
            seriesMultiMap.asMap().forEach((key, val) -> {
                List<PointSRO> tempList = new ArrayList<>(val);
                seriesDataMap.put(key, tempList);
            });
        }
        return seriesDataMap;
    }

    private void populateSeriesData(ListMultimap<String, PointSRO> seriesMultiMap, String seriesLabel, Object pointX, Double pointY) {
        String pointKey = null;
        if (pointX instanceof String) {
            pointKey = (String) pointX;
        } else if (pointX instanceof DateTime) {
            pointKey = DATE_TIME_FORMATTER.print((DateTime) pointX);
        }

        if (StringUtils.isNotEmpty(pointKey) && pointY != null) {
            seriesMultiMap.put(seriesLabel, new PointSRO(pointKey, pointY));
        } else {
            LOG.info("Null coordinates for seriesLabel {}: pointX {}, pointY {}", seriesLabel, pointKey, pointY);
        }
    }

    private String firstFilterValue(Map<String, List<String>> filters, FilterKey filterKey) {

        List<String> values = filters.get(filterKey.getFilterKey());
        if (!CollectionUtils.isEmpty(values)) {
            return StringUtils.getTextOrNull(values.get(0));
        }
        return null;
    }

    private DateRangeDTO setDurationTypeDateRange(SearchRequest request, ElasticFilter elasticFilter, boolean isDurationTypeMandatory, MetricDetails metricDetails) throws DataValidationException {

        Map<String, List<String>> filters    = request.getFilters();
        Map<String, Boolean>      filterKeys = metricDetails.getFilterKey();
        if (filterKeys.containsKey(FilterKey.DURATION_TYPE.getFilterKey()) || filterKeys.containsKey(FilterKey.DURATION_TYPE_START_TIME.getFilterKey()) || filterKeys.containsKey(FilterKey.DURATION_TYPE_END_TIME.getFilterKey())) {
            String durationTypeString = firstFilterValue(filters, FilterKey.DURATION_TYPE);
            String startDateString    = firstFilterValue(filters, FilterKey.DURATION_TYPE_START_TIME);
            String endDateString      = firstFilterValue(filters, FilterKey.DURATION_TYPE_END_TIME);

            if (startDateString == null || endDateString == null) {
                throw new DataValidationException("durationStartTime or durationEndTime can't be null");
            }

            if (isDurationTypeMandatory && durationTypeString == null) {
                throw new DataValidationException("durationType or durationEndTime can't be null");
            }

            DurationType durationType = DurationType.getType(durationTypeString);
            Date         startDate    = new Date(Long.parseLong(startDateString));
            Date         endDate      = new Date(Long.parseLong(endDateString));
            if (startDate.after(endDate)) {
                throw new DataValidationException("durationStartTime is greater than durationEndTime");
            }

            DateHistogramInterval interval = durationType == null ? null : durationType.getHistogramInterval();

            if (durationType != null) {
                interval = durationType.getHistogramInterval();
            }
            if (isDurationTypeMandatory && interval == null) {
                interval = DateHistogramInterval.DAY;
            }
            elasticFilter.setDurationTypeDateRange(new ElasticDurationTypeDateRange(startDate, (new DateTime(endDate)).plusDays(1).toDate(), interval, false, true));
            return new DateRangeDTO(new DateTime(startDate), new DateTime(endDate), durationType);
        } else {
            return null;
        }
    }

    private void setFiltersAndOptions(SearchRequest request, ElasticFilter elasticFilter) {
        EnumMap<FilterKey, String> filterValuesEnumMap = new EnumMap<>(FilterKey.class);
        request.getFilters().forEach((key, value) -> {
            FilterKey     fk            = FilterKey.fromFilterKey(key);
            FilterOptions filterOptions = filterOptionService.getFiltersBykey(key);
            if (fk != null && fk.getSubOrderDetailElasticColumn() != null && !CollectionUtils.isEmpty(value)) {
                if (filterOptions == null || !Constants.DATE.equalsIgnoreCase(filterOptions.getType())) {
                    String val = Joiner.on(StringUtils.COMMA_STRING).skipNulls().join(value);
                    if (StringUtils.isNotEmpty(val)) {
                        filterValuesEnumMap.put(fk, val);
                    }
                } else {
                    String dateString = StringUtils.getTextOrNull(value.get(0));
                    if (dateString != null) {
                        Date dStart = new Date(Long.parseLong(dateString));
                        Date dEnd   = new Date((new DateTime(dStart)).plusDays(1).getMillis());
                        elasticFilter.getDateRangeFilters().add(new ElasticDateRangeFilter(fk, dStart, dEnd, true, false));
                    }
                }
            }
        });

        Multimap<OptionKey, String> optionKeyMap = ArrayListMultimap.create();

        request.getOptions().forEach((key, val) -> {
            FilterKey filter = FilterKey.fromFilterKey(key);
            if (null != filter) {
                if (!StringUtils.isEmpty(val)) {
                    filterValuesEnumMap.put(filter, val);
                } else {
                    LOG.info("Empty value provided for option {}", key);
                }
            } else {
                OptionKey optionKey = OptionKey.getOptionByName(key);
                if (optionKey != null) {
                    optionKeyMap.put(optionKey, val);
                }
            }
        });

        elasticFilter.setOptionValues(optionKeyMap);
        elasticFilter.setFilterValues(filterValuesEnumMap);
    }

    private Table<Stage, ChartFilterKey, List<FilterKey>> setChartFilterValue(Map<String, Map<String, List<String>>> chartValues) {
        Table<Stage, ChartFilterKey, List<FilterKey>> chartGroupValue = HashBasedTable.create();
        if (!MapUtils.isEmpty(chartValues)) {
            chartValues.forEach((stage, value) -> {
                if (!MapUtils.isEmpty(value)) {
                    value.forEach((key, dataValue) -> {
                        List<FilterKey> listKey = dataValue.stream().map(filterKey -> {
                            return FilterKey.fromFilterKey(filterKey);
                        }).collect(Collectors.toList());
                        ChartFilterKey chartFilterKey = ChartFilterKey.getChartFilterByName(key);
                        chartGroupValue.put(Stage.fromCode(stage), chartFilterKey, listKey);
                    });
                }
            });
        }
        return chartGroupValue;
    }
}
