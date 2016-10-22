package com.snapdeal.scm.web.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.web.ConfigurationScmWebApp;
import com.snapdeal.scm.web.core.enums.Metric;
import com.snapdeal.scm.web.core.request.SearchRequest;
import com.snapdeal.scm.web.core.response.SearchResponse;
import com.snapdeal.scm.web.services.IMetricDataService;

/**
 * 
 * @author mohit
 *
 *
 */
@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
public class TestMetricDataServiceG8 {

	   @Autowired
	    IMetricDataService metricDataService;

	    @Test
	    public void testGetLevelDataLevel1G8() {
	        System.out.println("------------------- G8 L1-------------------");
	        SearchRequest request = new SearchRequest();
	        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
	            {
	                put("durationtype", Arrays.asList("daily"));
	                put("durationtype-startdate", Arrays.asList("1429906330000"));
	                put("durationtype-enddate", Arrays.asList("1461542400000"));
	            }
	        }));
	        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
	            {
	            }
	        }));
	        request.setStage("ONE");

	        Metric metric = Metric.PENDENCY_INTEGRATED_COURIERS;

	        try {
	            SearchResponse levelData = metricDataService.getLevelData(request, metric);
	            System.out.println(levelData);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    @Test
	    public void testGetLevelDataLevel2G8() {
	        System.out.println("------------------- G8 L2-------------------");
	        SearchRequest request = new SearchRequest();
	        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
	            {
	                put("durationtype", Arrays.asList("daily"));
	                put("durationtype-startdate", Arrays.asList("1429906330000"));
	                put("durationtype-enddate", Arrays.asList("1461542400000"));
	            }
	        }));
	        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
	            {
	                put("lanetype", "same-city");
	            }
	        }));
	        request.setStage("TWO");

	        Metric metric = Metric.PENDENCY_INTEGRATED_COURIERS;

	        try {
	            SearchResponse levelData = metricDataService.getLevelData(request, metric);
	            System.out.println(levelData);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    @Test
	    public void testGetLevelDataLevel3G8() {
	        System.out.println("------------------- G8 L3-------------------");
	        SearchRequest request = new SearchRequest();
	        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
	            {
	                put("durationtype", Arrays.asList("daily"));
	                put("durationtype-startdate", Arrays.asList("1429906330000"));
	                put("durationtype-enddate", Arrays.asList("1461542400000"));
	            }
	        }));
	        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
	            {
	                put("lanetype", "same-city");
	                put("couriergroup", "FEDEX");
	                put("lane","lane1");
	            }
	        }));
	        request.setStage("THREE");

	        Metric metric = Metric.PENDENCY_INTEGRATED_COURIERS;

	        try {
	            SearchResponse levelData = metricDataService.getLevelData(request, metric);
	            System.out.println(levelData);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    @Test
	    public void testGetLevelDataLevel4G8() {
	        System.out.println("------------------- G8 L4-------------------");
	        SearchRequest request = new SearchRequest();
	        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
	            {
	                put("durationtype", Arrays.asList("daily"));
	                put("durationtype-startdate", Arrays.asList("1429906330000"));
	                put("durationtype-enddate", Arrays.asList("1461542400000"));
	            }
	        }));
	        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
	            {
	                put("lanetype", "same-city");
	                put("couriergroup", "FEDEX");
	                put("lane","lane1");
	                put("origincity","Delhi");
	            }
	        }));
	        request.setStage("FOUR");

	        Metric metric = Metric.PENDENCY_INTEGRATED_COURIERS;

	        try {
	            SearchResponse levelData = metricDataService.getLevelData(request, metric);
	            System.out.println(levelData);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}

