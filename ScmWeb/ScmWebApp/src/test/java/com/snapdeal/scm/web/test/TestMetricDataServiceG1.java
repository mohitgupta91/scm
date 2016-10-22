/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
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
 * @version 1.0, 17-Mar-2016
 * @author ashwini
 */
@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
@SuppressWarnings({ "serial" })
public class TestMetricDataServiceG1 {

    @Autowired
    IMetricDataService metricDataService;

    @Test
    public void testGetLevelDataLevel1M1() {
        System.out.println("------------------- G1 L1-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("Weekly"));
                put("durationtype-startdate", Arrays.asList("1454265000000"));
                put("durationtype-enddate", Arrays.asList("1457980200000"));
            }
        }));
        request.setOptions(new HashMap<String, String>());
        request.setStage("ONE");

        Metric metric = Metric.LAST_MILE_O2D_DELIVERED;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel2M1() {
        System.out.println("------------------- G1 L2-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("Weekly"));
                put("durationtype-startdate", Arrays.asList("1454265000000"));
                put("durationtype-enddate", Arrays.asList("1457980200000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "avg_o2d");
            }
        }));
        request.setStage("TWO");

        Metric metric = Metric.LAST_MILE_O2D_DELIVERED;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel3M1() {
        System.out.println("------------------- G1 L3-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("Weekly"));
                put("durationtype-startdate", Arrays.asList("1454265000000"));
                put("durationtype-enddate", Arrays.asList("1457980200000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "avg_o2d");
                put("lanetype", "same-city");
            }
        }));
        request.setStage("THREE");

        Metric metric = Metric.LAST_MILE_O2D_DELIVERED;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel4M1() {
        System.out.println("------------------- G1 L4-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("Weekly"));
                put("durationtype-startdate", Arrays.asList("1454265000000"));
                put("durationtype-enddate", Arrays.asList("1457980200000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "avg_o2d");
                put("lanetype", "same-city");
                put("couriergroup", "FEDEX");
            }
        }));
        request.setStage("FOUR");

        Metric metric = Metric.LAST_MILE_O2D_DELIVERED;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel5M1() {
        System.out.println("------------------- G1 L5-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("Weekly"));
                put("durationtype-startdate", Arrays.asList("1454265000000"));
                put("durationtype-enddate", Arrays.asList("1457980200000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "avg_o2d");
                put("lanetype", "same-city");
                put("couriergroup", "FEDEX");
                put("destinationstate", "DELHI");
            }
        }));
        request.setStage("FIVE");

        Metric metric = Metric.LAST_MILE_O2D_DELIVERED;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
