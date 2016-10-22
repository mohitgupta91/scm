/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class TestMetricDataServiceG38 {

    @Autowired
    IMetricDataService metricDataService;

    @Test
    public void testGetLevelDataLevel1G38() {
        System.out.println("------------------- G38 L1-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("monthly"));
                put("durationtype-startdate", Arrays.asList("1429906330000"));
                put("durationtype-enddate", Arrays.asList("1461542400000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
            }
        }));
        request.setChartFilters(Collections.unmodifiableMap(new HashMap<String, Map<String, List<String>>>() {
            {
                put("ONE", null);
            }
        }));
        request.setStage("ONE");

        Metric metric = Metric.LAST_MILE_RTO_INITIATED_COUNT;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel2G38() {
        System.out.println("------------------- G38 L2-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("monthly"));
                put("durationtype-startdate", Arrays.asList("1429906330000"));
                put("durationtype-enddate", Arrays.asList("1461542400000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
            }
        }));
        request.setChartFilters(Collections.unmodifiableMap(new HashMap<String, Map<String, List<String>>>() {
            {
                put("ONE", null);
                put("TWO", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupby", Arrays.asList("fulfillmentmodel"));
                    }
                }));
            }
        }));
        request.setStage("TWO");

        Metric metric = Metric.LAST_MILE_RTO_INITIATED_COUNT;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel3G38() {
        System.out.println("------------------- G38 L3-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("monthly"));
                put("durationtype-startdate", Arrays.asList("1429906330000"));
                put("durationtype-enddate", Arrays.asList("1461542400000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("fulfillmentmodel", "FC_VOI");
            }
        }));
        request.setChartFilters(Collections.unmodifiableMap(new HashMap<String, Map<String, List<String>>>() {
            {
                put("ONE", null);
                put("TWO", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupby", Arrays.asList("fulfillmentmodel"));
                    }
                }));
                put("THREE", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupby", Arrays.asList("couriergroup"));
                    }
                }));
            }
        }));
        request.setStage("THREE");

        Metric metric = Metric.LAST_MILE_RTO_INITIATED_COUNT;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel4G38() {
        System.out.println("------------------- G38 L4-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("monthly"));
                put("durationtype-startdate", Arrays.asList("1429906330000"));
                put("durationtype-enddate", Arrays.asList("1461542400000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("fulfillmentmodel", "FC_VOI");
                put("couriergroup", "courierGroup1");
            }
        }));
        request.setChartFilters(Collections.unmodifiableMap(new HashMap<String, Map<String, List<String>>>() {
            {
                put("ONE", null);
                put("TWO", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupby", Arrays.asList("fulfillmentmodel"));
                    }
                }));
                put("THREE", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupby", Arrays.asList("couriergroup"));
                    }
                }));
                put("FOUR", null);
            }
        }));
        request.setStage("FOUR");

        Metric metric = Metric.LAST_MILE_RTO_INITIATED_COUNT;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLevelDataLevel5G38() {
        System.out.println("------------------- G38 L5-------------------");
        SearchRequest request = new SearchRequest();
        request.setFilters(Collections.unmodifiableMap(new HashMap<String, List<String>>() {
            {
                put("durationtype", Arrays.asList("monthly"));
                put("durationtype-startdate", Arrays.asList("1429906330000"));
                put("durationtype-enddate", Arrays.asList("1461542400000"));
            }
        }));
        request.setOptions(Collections.unmodifiableMap(new HashMap<String, String>() {
            {
                put("fulfillmentmodel", "FC_VOI");
                put("couriergroup", "courierGroup1");
                put("destinationstate", "state2");
            }
        }));
        request.setChartFilters(Collections.unmodifiableMap(new HashMap<String, Map<String, List<String>>>() {
            {
                put("ONE", null);
                put("TWO", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupby", Arrays.asList("fulfillmentmodel"));
                    }
                }));
                put("THREE", Collections.unmodifiableMap(new HashMap<String, List<String>>() {
                    {
                        put("groupby", Arrays.asList("couriergroup"));
                    }
                }));
                put("FOUR", null);
                put("FIVE", null);
            }
        }));
        request.setStage("FIVE");

        Metric metric = Metric.LAST_MILE_RTO_INITIATED_COUNT;

        try {
            SearchResponse levelData = metricDataService.getLevelData(request, metric);
            System.out.println(levelData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}