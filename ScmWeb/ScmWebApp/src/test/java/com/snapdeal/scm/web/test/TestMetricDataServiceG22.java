package com.snapdeal.scm.web.test;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
 */

@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
public class TestMetricDataServiceG22 {
	@Autowired
	IMetricDataService metricDataService;
	
    @SuppressWarnings("serial")
	@Test
    public void testGetLevelDataLevel1G22() {
        System.out.println("------------------- G22 L1-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("daily"));
                put("durationtype-startdate", Arrays.asList("1456770600000"));
                put("durationtype-enddate", Arrays.asList("1459103400000"));
            }
        }));
        request.setOptions(new HashMap<String, String>());
        request.setStage("ONE");
        System.out.println(request);
        Metric metric = Metric.COMPLAINTS_POST_DELIVERY;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetLevelDataLevel2G22() {
        System.out.println("------------------- G21 L2-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("daily"));
                put("durationtype-startdate", Arrays.asList("1456770600000"));
                put("durationtype-enddate", Arrays.asList("1459103400000"));
            }
        }));
        request.setOptions(new HashMap<String, String>());
        request.setStage("TWO");

        Metric metric = Metric.COMPLAINTS_POST_DELIVERY;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}