package com.snapdeal.scm.web.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticDurationTypeDateRange;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.web.ConfigurationScmWebApp;
import com.snapdeal.scm.web.core.das.ILastMileElasticDas;
import com.snapdeal.scm.web.core.enums.FilterKey;
import com.snapdeal.scm.web.core.enums.OptionKey;
import com.snapdeal.scm.web.core.enums.Stage;
import com.snapdeal.scm.web.services.IElasticService;

/**
 * TestWebElasticIntegration : Elastic Web Integration
 * 
 * @author pranav, prateek
 *
 */
@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
@SuppressWarnings({"rawtypes","unused"})
public class TestWebElasticIntegration {


	@Autowired
	IElasticService elasticService;
	
	@Autowired
	@Qualifier("G3")
	private ILastMileElasticDas deliveredEddrElasticDas;

	/**
	 * Test aggregate on minute/Day/week/month
	 */
	@Test
	public void testAggregateByMinute(){
		AggregationBuilder aggregation =
		AggregationBuilders
		.dateHistogram("agg")
		.field("created_on")
		.interval(DateHistogramInterval.MINUTE);
		String  query="{ \"query\": { \"match_all\": {}}}";
		Histogram agg = elasticService.runAggregate(aggregation, query);
		for (Histogram.Bucket entry : agg.getBuckets()) {
			DateTime key = (DateTime) entry.getKey();    // Key
			String keyAsString = entry.getKeyAsString(); // Key as String
			long docCount = entry.getDocCount();         // Doc count

			System.out.println("key : " + keyAsString+ ", date : " +key + ", doc_count : " + docCount);
		}
	}

	/**
	 * Test Elastic Storage
	 */
	@Test
	public void testElasticService() {
		ElasticDataDTO dto = elasticService.getBySuborderId("supc_test_elastic");
		System.out.println(dto);
	}

	@Test
	public void testQuery(){
		String  query="{ \"query\": { \"match_all\": {}}}";
		elasticService.printQueryResponse(query);
	}

	@Test
	public void findFirstLevelMetricData(){
		ElasticFilter ef= new ElasticFilter();
		Map<FilterKey, String> filters= ef.getFilterValues();
		ef.setStage(Stage.ONE);
		Multimap<OptionKey, String> multimap = ArrayListMultimap.create();
		ef.setOptionValues(multimap);
		filters.put(FilterKey.MTO, "mto1");
		SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
		Date startDate=null,endDate=null;
		try {
			startDate = simpleDateFormat.parse("2016-02-22T07:16:59.826Z");
			endDate = simpleDateFormat.parse("2016-03-03T07:16:59.826Z");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ef.setDurationTypeDateRange(new ElasticDurationTypeDateRange("tp_del_status_date", startDate, endDate, true, true,DateHistogramInterval.DAY));
		List<AggResult> aggregateByO2D = elasticService.findMetricData(ef);
		for (AggResult lastMileFirstLevelResultSet : aggregateByO2D) {
			System.out.println(lastMileFirstLevelResultSet);
		}
	}

	@Test
	public void findSecondLevelMetricData(){
		ElasticFilter ef= new ElasticFilter();
		EnumMap<FilterKey, String> filters= ef.getFilterValues();
		ef.setStage(Stage.TWO);
		Multimap<OptionKey, String> multimap = ArrayListMultimap.create();
		multimap.put(OptionKey.CATEGORY, "delivered_count");
		ef.setOptionValues(multimap);
		filters.put(FilterKey.MTO, "mto1");
		SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
		Date startDate=null,endDate=null;
		try {
			startDate = simpleDateFormat.parse("2016-02-22T07:16:59.826Z");
			endDate = simpleDateFormat.parse("2016-03-13T07:16:59.826Z");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ef.setDurationTypeDateRange(new ElasticDurationTypeDateRange("tp_del_status_date", startDate, endDate, true, true,DateHistogramInterval.DAY));
		List<AggResult> aggregateByO2D = elasticService.findMetricData(ef);
		for (AggResult lastMileFirstLevelResultSet : aggregateByO2D) {
			System.out.println(lastMileFirstLevelResultSet);
		}
	}
	
	@Test
	public void findThirdLevelMetricData(){
		System.out.println("------>Third Level Metric Data Start<--------");
		ElasticFilter ef= new ElasticFilter();
		EnumMap<FilterKey, String> filters= ef.getFilterValues();
		ef.setStage(Stage.THREE);
		Multimap<OptionKey, String> multimap = ArrayListMultimap.create();
		multimap.put(OptionKey.CATEGORY, "order_to_delivered");
		ef.setOptionValues(multimap);
		filters.put(FilterKey.MTO, "mto1");
		filters.put(FilterKey.COURIER_GROUP, "couriergroup1");
		SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
		Date startDate=null,endDate=null;
		try {
			startDate = simpleDateFormat.parse("2016-02-22T07:16:59.826Z");
			endDate = simpleDateFormat.parse("2016-03-13T07:16:59.826Z");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ef.setDurationTypeDateRange(new ElasticDurationTypeDateRange("tp_del_status_date", startDate, endDate, true, true,DateHistogramInterval.DAY));
		List<AggResult> aggregateByO2D = elasticService.findMetricData(ef);
		for (AggResult lastMileFirstLevelResultSet : aggregateByO2D) {
			System.out.println(lastMileFirstLevelResultSet);
		}
		System.out.println("------>Third Level Metric Data END<--------");
	}
	
	@Test
	public void findForthLevelMetricData(){
		ElasticFilter ef= new ElasticFilter();
		EnumMap<FilterKey, String> filters= ef.getFilterValues();
		ef.setStage(Stage.FOUR);
		Multimap<OptionKey, String> multimap = ArrayListMultimap.create();
		multimap.put(OptionKey.CATEGORY, "order_to_delivered");
		ef.setOptionValues(multimap);
		filters.put(FilterKey.SHIPPING_MODE, "air");
		filters.put(FilterKey.MTO, "mto1,nonmto");
		SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
		Date startDate=null,endDate=null;
		try {
			startDate = simpleDateFormat.parse("2016-02-22T07:16:59.826Z");
			endDate = simpleDateFormat.parse("2016-03-13T07:16:59.826Z");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ef.setDurationTypeDateRange(new ElasticDurationTypeDateRange("tp_del_status_date", startDate, endDate, true, true,DateHistogramInterval.DAY));
		List<AggResult> aggregateByO2D = elasticService.findMetricData(ef);
		for (AggResult lastMileFirstLevelResultSet : aggregateByO2D) {
			System.out.println(lastMileFirstLevelResultSet);
		}
	}
	
	@Test
	public void findFifthLevelMetricData(){
		ElasticFilter ef= new ElasticFilter();
		EnumMap<FilterKey, String> filters= ef.getFilterValues();
		ef.setStage(Stage.FIVE);
		Multimap<OptionKey, String> multimap = ArrayListMultimap.create();
		multimap.put(OptionKey.CATEGORY, "order_to_delivered");
		ef.setOptionValues(multimap);
		filters.put(FilterKey.SHIPPING_MODE, "air");
		filters.put(FilterKey.MTO, "mto1,nonmto");
		SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
		Date startDate=null,endDate=null;
		try {
			startDate = simpleDateFormat.parse("2016-02-22T07:16:59.826Z");
			endDate = simpleDateFormat.parse("2016-03-13T07:16:59.826Z");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ef.setDurationTypeDateRange(new ElasticDurationTypeDateRange("tp_del_status_date", startDate, endDate, true, true,DateHistogramInterval.DAY));
		List<AggResult> aggregateByO2D = elasticService.findMetricData(ef);
		for (AggResult lastMileFirstLevelResultSet : aggregateByO2D) {
			System.out.println(lastMileFirstLevelResultSet);
		}
	}
	
	@Test
	public void findThirdMetricFirstLevel(){
		System.out.println("<-----------Third Metric First Level Start------->");
		ElasticFilter ef= new ElasticFilter();
		Map<FilterKey, String> filters= ef.getFilterValues();
		ef.setStage(Stage.ONE);
		Multimap<OptionKey, String> multimap = ArrayListMultimap.create();
		ef.setOptionValues(multimap);
		SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
		Date startDate=null,endDate=null;
		try {
			startDate = simpleDateFormat.parse("2016-01-22T07:16:59.826Z");
			endDate = simpleDateFormat.parse("2016-03-23T07:16:59.826Z");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ef.setDurationTypeDateRange(new ElasticDurationTypeDateRange("sp_awb_uploaded_status_date", startDate, endDate, true, true,DateHistogramInterval.DAY));
		List<AggResult> aggregateByO2D = deliveredEddrElasticDas.findMetricData(ef);
		for (AggResult lastMileFirstLevelResultSet : aggregateByO2D) {
			System.out.println(lastMileFirstLevelResultSet);
		}
		
		System.out.println("<-----------Third Metric First Level End------->");
	}
	
	@Test
	public void findThirdMetricSecondLevel(){
		System.out.println("<-----------Third Metric Second Level Start------->");
		ElasticFilter ef= new ElasticFilter();
		Map<FilterKey, String> filters= ef.getFilterValues();
		ef.setStage(Stage.TWO);
		Multimap<OptionKey, String> multimap = ArrayListMultimap.create();
		ef.setOptionValues(multimap);
		SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
		Date startDate=null,endDate=null;
		try {
			startDate = simpleDateFormat.parse("2016-01-22T07:16:59.826Z");
			endDate = simpleDateFormat.parse("2016-03-23T07:16:59.826Z");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ef.setDurationTypeDateRange(new ElasticDurationTypeDateRange("sp_awb_uploaded_status_date", startDate, endDate, true, true,DateHistogramInterval.DAY));
		List<AggResult> aggregateByO2D = deliveredEddrElasticDas.findMetricData(ef);
		for (AggResult lastMileFirstLevelResultSet : aggregateByO2D) {
			System.out.println(lastMileFirstLevelResultSet);
		}
		
		System.out.println("<-----------Third Metric Second Level End------->");
	}
	
	@Test
	public void findThirdMetricThirdLevel(){
		System.out.println("<-----------Third Metric Third Level Start------->");
		ElasticFilter ef= new ElasticFilter();
		Map<FilterKey, String> filters= ef.getFilterValues();
		ef.setStage(Stage.THREE);
		Multimap<OptionKey, String> multimap = ArrayListMultimap.create();
		ef.setOptionValues(multimap);
		SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
		Date startDate=null,endDate=null;
		try {
			startDate = simpleDateFormat.parse("2016-01-22T07:16:59.826Z");
			endDate = simpleDateFormat.parse("2016-03-23T07:16:59.826Z");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ef.setDurationTypeDateRange(new ElasticDurationTypeDateRange("sp_awb_uploaded_status_date", startDate, endDate, true, true,DateHistogramInterval.DAY));
		List<AggResult> aggregateByO2D = deliveredEddrElasticDas.findMetricData(ef);
		for (AggResult lastMileFirstLevelResultSet : aggregateByO2D) {
			System.out.println(lastMileFirstLevelResultSet);
		}
		
		System.out.println("<-----------Third Metric Third Level End------->");
	}
	
	
	@Test
	public void getFieldValueBySuborderId(){
		System.out.println("<----------Get Field Value for a suborder id : Start------->");
		System.out.println("Value is : "+deliveredEddrElasticDas.getFieldValueBySuborderId("indexForSearch", "tp_del_status_date"));
		System.out.println("<----------Get Field Value for a suborder id : End------->");
	}
}
