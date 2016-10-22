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
public class TestMetricDataServiceG10 {
	   @Autowired
	    IMetricDataService metricDataService;

	    @Test
	    public void testGetLevelDataLevel1G10() {
	        System.out.println("------------------- G10 L1-------------------");
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

	        Metric metric = Metric.UD_MONITORING;

	        try {
	            SearchResponse levelData = metricDataService.getLevelData(request, metric);
	            System.out.println(levelData);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    @Test
	    public void testGetLevelDataLevel2G10() {
	        System.out.println("------------------- G10 L2-------------------");
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
	                put("ndr", "ndr1");
	                put("couriergroup", "FEDEX");
	            }
	        }));
	        request.setStage("TWO");

	        Metric metric = Metric.UD_MONITORING;

	        try {
	            SearchResponse levelData = metricDataService.getLevelData(request, metric);
	            System.out.println(levelData);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    @Test
	    public void testGetLevelDataLevel3G10() {
	        System.out.println("------------------- G10 L3-------------------");
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
	                put("ndr", "ndr1");
	                put("couriergroup", "FEDEX");
	                put("destinationcity", "delhi");
	            }
	        }));
	        request.setStage("THREE");

	        Metric metric = Metric.UD_MONITORING;

	        try {
	            SearchResponse levelData = metricDataService.getLevelData(request, metric);
	            System.out.println(levelData);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}

