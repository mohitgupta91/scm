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
public class TestMetricDataServiceG6 {

    @Autowired
    IMetricDataService metricDataService;

    @Test
    public void testGetLevelDataLevel1G6() {
        System.out.println("------------------- G6 L1-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("daily"));
                put("durationtype-startdate", Arrays.asList("1454265000000"));
                put("durationtype-enddate", Arrays.asList("1458153000000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
            }
        }));
        request.setStage("ONE");

        Metric metric = Metric.LAST_MILE_S2D_PENDENCY_TREND;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel2G6() {
        System.out.println("------------------- G6 L2-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("daily"));
                put("durationtype-startdate", Arrays.asList("1454265000000"));
                put("durationtype-enddate", Arrays.asList("1454437800000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "delivered");
            }
        }));
        request.setStage("TWO");

        Metric metric = Metric.LAST_MILE_S2D_PENDENCY_TREND;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel3G6() {
        System.out.println("------------------- G6 L3-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("daily"));
                put("durationtype-startdate", Arrays.asList("1454265000000"));
                put("durationtype-enddate", Arrays.asList("1454437800000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "delivered");
                put("lanetype", "SameCity");
            }
        }));
        request.setStage("THREE");

        Metric metric = Metric.LAST_MILE_S2D_PENDENCY_TREND;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel4G6() {
        System.out.println("------------------- G6 L4-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("daily"));
                put("durationtype-startdate", Arrays.asList("1454265000000"));
                put("durationtype-enddate", Arrays.asList("1454437800000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "delivered");
                put("lanetype", "SameCity");
                put("couriergroup", "CG");
            }
        }));
        request.setStage("FOUR");

        Metric metric = Metric.LAST_MILE_S2D_PENDENCY_TREND;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel5G6() {
        System.out.println("------------------- G6 L5-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("daily"));
                put("durationtype-startdate", Arrays.asList("1454265000000"));
                put("durationtype-enddate", Arrays.asList("1454437800000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "delivered");
                put("lanetype", "SameCity");
                put("couriergroup", "CG");
                put("destinationstate", "Delhi");
            }
        }));
        request.setStage("FIVE");

        Metric metric = Metric.LAST_MILE_S2D_PENDENCY_TREND;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}