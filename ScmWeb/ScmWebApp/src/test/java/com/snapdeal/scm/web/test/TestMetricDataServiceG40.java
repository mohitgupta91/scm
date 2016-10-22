/**
 * Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 * JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.test;

import com.snapdeal.scm.web.ConfigurationScmWebApp;
import com.snapdeal.scm.web.core.enums.Metric;
import com.snapdeal.scm.web.core.request.SearchRequest;
import com.snapdeal.scm.web.core.response.SearchResponse;
import com.snapdeal.scm.web.services.IMetricDataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * @version 1.0, 17-Mar-2016
 * @author ashwini
 */
@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
@SuppressWarnings({"serial"})
public class TestMetricDataServiceG40 {

    @Autowired
    IMetricDataService metricDataService;

    @Before
    public void setup() {

    }

    @Test
    public void testGetLevelDataLevel1G40() {
        System.out.println("------------------- G40 L1-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(new HashMap<>());
        request.setOptions(new HashMap<>());
        request.setChartFilters(Collections.unmodifiableMap(new HashMap<String, Map<String, List<String>>>() {
            {
                put("ONE", null);
            }
        }));
        request.setStage("ONE");

        Metric metric = Metric.LAST_MILE_RTO_PENDENCY;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel2G40() {
        System.out.println("------------------- G40 L2-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(new HashMap<>());
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "rtoageing");
            }
        }));
        request.setChartFilters(Collections.unmodifiableMap(new HashMap<String, Map<String, List<String>>>() {
            {
                put("ONE", null);
                put("TWO", null);
            }
        }));
        request.setStage("TWO");

        Metric metric = Metric.LAST_MILE_RTO_PENDENCY;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel3G40() {
        System.out.println("------------------- G40 L3-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(new HashMap<>());
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "rtoageing");
                put("rtostates", "initiatednotattempted");
            }
        }));
        request.setChartFilters(Collections.unmodifiableMap(new HashMap<String, Map<String, List<String>>>() {
            {
                put("ONE", null);
                put("TWO", null);
                put("THREE", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupBy", Arrays.asList("fulfillmentmodel"));
                    }
                }));
            }
        }));
        request.setStage("THREE");

        Metric metric = Metric.LAST_MILE_RTO_PENDENCY;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel4G40() {
        System.out.println("------------------- G40 L4-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(new HashMap<>());
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "rtopendency");
                put("rtostates", "initiatednotattempted");
                put("fulfillmentmodel", "FC_VOI");
            }
        }));
        request.setChartFilters(Collections.unmodifiableMap(new HashMap<String, Map<String, List<String>>>() {
            {
                put("ONE", null);
                put("TWO", null);
                put("THREE", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupBy", Arrays.asList("fulfillmentmodel"));
                    }
                }));
                put("FOUR", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupBy", Arrays.asList("destinationcity"));
                    }
                }));
            }
        }));
        request.setStage("FOUR");

        Metric metric = Metric.LAST_MILE_RTO_PENDENCY;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel5G40() {
        System.out.println("------------------- G40 L5-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(new HashMap<>());
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("category", "rtopendency");
                put("rtoStates", "initiatednotattempted");
                put("fulfillmentmodel", "FC_VOI");
                put("destinationcity", "delhi");
            }
        }));
        request.setChartFilters(Collections.unmodifiableMap(new HashMap<String, Map<String, List<String>>>() {
            {
                put("ONE", null);
                put("TWO", null);
                put("THREE", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupBy", Arrays.asList("fulfillmentmodel"));
                    }
                }));
                put("FOUR", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupBy", Arrays.asList("destinationcity"));
                    }
                }));
                put("FIVE", null);
            }
        }));
        request.setStage("FIVE");

        Metric metric = Metric.LAST_MILE_RTO_PENDENCY;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}