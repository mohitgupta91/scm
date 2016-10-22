/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.web.ConfigurationScmWebApp;
import com.snapdeal.scm.web.core.mao.FilterOptionsRepository;
import com.snapdeal.scm.web.core.mao.MetricDetailsRepository;
import com.snapdeal.scm.web.core.mongo.documents.FilterOptions;
import com.snapdeal.scm.web.core.mongo.documents.MetricDetails;

/**
 * @version 1.0, 02-Mar-2016
 * @author ashwini
 */
@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
@EnableMongoRepositories
@EnableMongoAuditing
@SuppressWarnings("serial")
public class TestMetricDetail {
    @Autowired
    private MetricDetailsRepository mRepo;

    @Autowired
    private FilterOptionsRepository fRepo;

    @Before
    public void setUp() {
        mRepo.deleteAll();
        fRepo.deleteAll();
        MetricDetails details1 = new MetricDetails();
        details1.setMetricID("avgo2dperformancedelivered");
        details1.setFilterKey(Collections.unmodifiableMap(new HashMap<String, Boolean>() {
            {
                put("durationtype", true);
                put("durationtype-startdate", true);
                put("durationtype-enddate", true);
                put("mode", false);
                put("type", false);
                put("metro", false);
                put("paidndd", false);
                put("fulfillmentmodel", false);
                put("lanetype", false);
                put("lanegroup", false);
                put("lane", false);
                put("originstate", false);
                put("origincity", false);
                put("destinationregion", false);
                put("destinationstate", false);
                put("destinationcity", false);
                put("destinationtier", false);
                put("couriergroup", false);
                put("couriertype", false);
                put("delivereddate", false);
            }
        }));
        details1.setCreated(new Date());
        details1.setUpdated(new Date());
        mRepo.save(details1);

        MetricDetails details2 = new MetricDetails();
        details2.setMetricID("avgo2sperformanceshipped");
        details2.setFilterKey(Collections.unmodifiableMap(new HashMap<String, Boolean>() {
            {
                put("durationtype", true);
                put("durationtype-startdate", true);
                put("durationtype-enddate", true);
                put("mode", false);
                put("type", false);
                put("metro", false);
                put("paidndd", false);
                put("fulfillmentmodel", false);
                put("lanetype", false);
                put("lanegroup", false);
                put("lane", false);
                put("originstate", false);
                put("origincity", false);
                put("destinationregion", false);
                put("destinationstate", false);
                put("destinationcity", false);
                put("destinationtier", false);
                put("couriergroup", false);
                put("couriertype", false);
                put("shippeddate", false);
            }
        }));
        details2.setCreated(new Date());
        details2.setUpdated(new Date());
        mRepo.save(details2);

        MetricDetails details3 = new MetricDetails();
        details3.setMetricID("deliveryadherance");
        details3.setFilterKey(Collections.unmodifiableMap(new HashMap<String, Boolean>() {
            {
                put("durationtype", true);
                put("durationtype-startdate", true);
                put("durationtype-enddate", true);
                put("mode", false);
                put("type", false);
                put("metro", false);
                put("paidndd", false);
                put("fulfillmentmodel", false);
                put("lanetype", false);
                put("lanegroup", false);
                put("lane", false);
                put("originstate", false);
                put("origincity", false);
                put("destinationregion", false);
                put("destinationstate", false);
                put("destinationcity", false);
                put("destinationtier", false);
                put("couriergroup", false);
                put("couriertype", false);
                put("delivereddate", false);
            }
        }));
        details3.setCreated(new Date());
        details3.setUpdated(new Date());
        mRepo.save(details3);

        MetricDetails details4 = new MetricDetails();
        details4.setMetricID("attemptadherance");
        details4.setFilterKey(Collections.unmodifiableMap(new HashMap<String, Boolean>() {
            {
                put("durationtype", true);
                put("durationtype-startdate", true);
                put("durationtype-enddate", true);
                put("mode", false);
                put("type", false);
                put("metro", false);
                put("paidndd", false);
                put("fulfillmentmodel", false);
                put("lanetype", false);
                put("lanegroup", false);
                put("lane", false);
                put("originstate", false);
                put("origincity", false);
                put("destinationregion", false);
                put("destinationstate", false);
                put("destinationcity", false);
                put("destinationtier", false);
                put("couriergroup", false);
                put("couriertype", false);
                put("delivereddate", false);
            }
        }));
        details4.setCreated(new Date());
        details4.setUpdated(new Date());
        mRepo.save(details4);

        FilterOptions fs1 = new FilterOptions();
        fs1.setFilterKey("durationtype");
        fs1.setType("singleselect");
        fs1.setOptions(Arrays.asList(new String[] { "Daily", "Weekly" }));
        fs1.setCreated(new Date());
        fs1.setUpdated(new Date());
        fRepo.save(fs1);

        FilterOptions fs2 = new FilterOptions();
        fs2.setFilterKey("durationtype-startdate");
        fs2.setType("date");
        fs2.setRange("90-0");
        fs2.setCreated(new Date());
        fs2.setUpdated(new Date());
        fRepo.save(fs2);

        FilterOptions fs3 = new FilterOptions();
        fs3.setFilterKey("durationtype-enddate");
        fs3.setType("date");
        fs3.setRange("90-0");
        fs3.setCreated(new Date());
        fs3.setUpdated(new Date());
        fRepo.save(fs3);

        FilterOptions fs4 = new FilterOptions();
        fs4.setFilterKey("mode");
        fs4.setType("singleselect");
        fs4.setOptions(Arrays.asList(new String[] { "air", "surface" }));
        fs4.setCreated(new Date());
        fs4.setUpdated(new Date());
        fRepo.save(fs4);

        FilterOptions fs5 = new FilterOptions();
        fs5.setFilterKey("type");
        fs5.setType("singleselect");
        fs5.setOptions(Arrays.asList(new String[] { "mto", "non-mto" }));
        fs5.setCreated(new Date());
        fs5.setUpdated(new Date());
        fRepo.save(fs5);

        FilterOptions fs6 = new FilterOptions();
        fs6.setFilterKey("metro");
        fs6.setType("singleselect");
        fs6.setOptions(Arrays.asList(new String[] { "metro", "non-metro" }));
        fs6.setCreated(new Date());
        fs6.setUpdated(new Date());
        fRepo.save(fs6);

        FilterOptions fs7 = new FilterOptions();
        fs7.setFilterKey("paidndd");
        fs7.setType("singleselect");
        fs7.setOptions(Arrays.asList(new String[] { "ndd", "sdd", "nndd", "normal" }));
        fs7.setCreated(new Date());
        fs7.setUpdated(new Date());
        fRepo.save(fs7);

        FilterOptions fs8 = new FilterOptions();
        fs8.setFilterKey("fulfillmentmodel");
        fs8.setType("singleselect");
        fs8.setOptions(Arrays.asList(new String[] { "dropship", "vendor_self", "oneship", "fc_voi", "o2o", "ocplus" }));
        fs8.setCreated(new Date());
        fs8.setUpdated(new Date());
        fRepo.save(fs8);

        FilterOptions fs9 = new FilterOptions();
        fs9.setFilterKey("lanetype");
        fs9.setType("singleselect");
        fs9.setOptions(Arrays.asList(new String[] { "same-city", "metro", "zone", "roi" }));
        fs9.setCreated(new Date());
        fs9.setUpdated(new Date());
        fRepo.save(fs9);

        FilterOptions fs10 = new FilterOptions();
        fs10.setFilterKey("lanegroup");
        fs10.setType("singleselect");
        fs10.setCreated(new Date());
        fs10.setUpdated(new Date());
        fRepo.save(fs10);

        FilterOptions fs11 = new FilterOptions();
        fs11.setFilterKey("lane");
        fs11.setType("singleselect");
        fs11.setDependsOn("lanegroup");
        fs11.setCreated(new Date());
        fs11.setUpdated(new Date());
        fRepo.save(fs11);

        FilterOptions fs12 = new FilterOptions();
        fs12.setFilterKey("originstate");
        fs12.setType("singleselect");
        fs12.setCreated(new Date());
        fs12.setUpdated(new Date());
        fRepo.save(fs12);

        FilterOptions fs13 = new FilterOptions();
        fs13.setFilterKey("destinationregion");
        fs13.setType("singleselect");
        fs13.setCreated(new Date());
        fs13.setUpdated(new Date());
        fRepo.save(fs13);

        FilterOptions fs14 = new FilterOptions();
        fs14.setFilterKey("destinationtier");
        fs14.setType("singleselect");
        fs14.setCreated(new Date());
        fs14.setUpdated(new Date());
        fRepo.save(fs14);

        FilterOptions fs15 = new FilterOptions();
        fs15.setFilterKey("couriergroup");
        fs15.setType("singleselect");
        fs15.setCreated(new Date());
        fs15.setUpdated(new Date());
        fRepo.save(fs15);

        FilterOptions fs16 = new FilterOptions();
        fs16.setFilterKey("couriertype");
        fs16.setType("singleselect");
        fs16.setCreated(new Date());
        fs16.setUpdated(new Date());
        fRepo.save(fs16);

        FilterOptions fs17 = new FilterOptions();
        fs17.setFilterKey("delivereddate");
        fs17.setType("date");
        fs17.setRange("90-0");
        fs17.setCreated(new Date());
        fs17.setUpdated(new Date());
        fRepo.save(fs17);

        FilterOptions fs18 = new FilterOptions();
        fs18.setFilterKey("destinationstate");
        fs18.setType("singleselect");
        fs18.setCreated(new Date());
        fs18.setUpdated(new Date());
        fRepo.save(fs18);

        FilterOptions fs19 = new FilterOptions();
        fs19.setFilterKey("destinationcity");
        fs19.setType("singleselect");
        fs19.setDependsOn("destinationstate");
        fs19.setCreated(new Date());
        fs19.setUpdated(new Date());
        fRepo.save(fs19);

        FilterOptions fs20 = new FilterOptions();
        fs20.setFilterKey("origincity");
        fs20.setType("singleselect");
        fs20.setDependsOn("originstate");
        fs20.setCreated(new Date());
        fs20.setUpdated(new Date());
        fRepo.save(fs20);

        FilterOptions fs21 = new FilterOptions();
        fs21.setFilterKey("shippeddate");
        fs21.setType("date");
        fs21.setRange("90-0");
        fs21.setCreated(new Date());
        fs21.setUpdated(new Date());
        fRepo.save(fs21);
    }

    @Test
    public void saveDate() {

    }
}
