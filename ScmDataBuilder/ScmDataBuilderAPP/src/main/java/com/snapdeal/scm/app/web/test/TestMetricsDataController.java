package com.snapdeal.scm.app.web.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.GsonBuilder;
import com.snapdeal.scm.core.dto.ServiceResponse;
import com.snapdeal.scm.utils.DateUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author chitransh
 */
@RestController
@RequestMapping(value = "/test/data/metrics/")
public class TestMetricsDataController {

    protected List<String>       courierGroups;
    protected Set<AddressDetail> addressDetails;
    protected Set<String>        complaintCategory;
    private   Set<String>        complaintOrigin;
   
    @Autowired
    @Qualifier("nodeClient")
    private Client client;

    @Value("${scm.elastic.index.name}")
    private String indexName;

    @Value("${scm.elastic.index.name.type}")
    private String indexType;

    @Value("${scm.elastic.retryonconflict}")
    private int retryOnConflict;

    private static final Logger LOG = LoggerFactory.getLogger(TestMetricsDataController.class);

    public TestMetricsDataController() {

        this.courierGroups = ImmutableList.of("BLUEDART", "GATI", "GO_JAVAS", "VULCAN");
        this.addressDetails = new ImmutableSet.Builder<AddressDetail>()
                .add(new AddressDetail("ncr-", "ncr", "NORTH", "TIER 1", "NODE", "NORTH"))
                .add(new AddressDetail("andhrapradesh", "hyderabad-ap", "SOUTH", "TIER 1", "NODE", "SOUTH"))
                .add(new AddressDetail("bihar", "patna-br", "NORTH", "TIER 1", "NODE", "NORTH"))
                .add(new AddressDetail("karnataka", "bangalore-ka", "SOUTH", "TIER 1", "NODE", "SOUTH"))
                .add(new AddressDetail("westbengal", "kolkata-wb", "NORTH", "TIER 1", "NODE", "NORTH"))
                .build();
        this.complaintCategory = new ImmutableSet.Builder<String>().add("Product stuck as SLA", "AWB not in use", "Cancellation of non cancellable order", "Courier before SLA").build();
        this.complaintOrigin = new ImmutableSet.Builder<String>().add("CEO Escalations", "Social", "App based", "Web", "Others").build();
    }

    private void insertIntoElastic(List<MockSubOrderDetailsDTO> list) {

        LOG.info("Persisting {} records into elastic....");

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        list.forEach(data -> {
            String source = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create().toJson(data);
            bulkRequest.add(client.prepareIndex(indexName, indexType).setSource(source));
        });

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            String failureMessage = bulkResponse.buildFailureMessage();
            LOG.info("Encountered failures while trying to insert data into elastic: {}", failureMessage);
        }
        LOG.info("Successfully inserted {} records into elastic", list.size());
    }

    @RequestMapping(value = "g8", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse createDataForG8() {

        ServiceResponse response = new ServiceResponse();

        try {
            LOG.info("Creating data set for G8, leg one....");
            insertIntoElastic(createDataSetForG8LegOne());

            LOG.info("Creating data set for G8, leg two....");
            insertIntoElastic(createDataSetForG8LegTwo());

            LOG.info("Creating data set for G8, leg three....");
            insertIntoElastic(createDataSetForG8LegThree());

            LOG.info("Creating data set for G8, leg four....");
            insertIntoElastic(createDataSetForG8LegFour());

            LOG.info("Creating data set for G8, leg five....");
            insertIntoElastic(createDataSetForG8LegFive());

            response.setSuccessful(true);
            return response;

        } catch (Exception e) {
            LOG.info("Exception occurred while trying to insert test data into elastic: ", e);
            response.setSuccessful(false);
            return response;
        }
    }

    @RequestMapping(value = "g9", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse createDataForG9() {

        ServiceResponse response = new ServiceResponse();

        try {
            LOG.info("Creating data set for G9, leg one....");
            insertIntoElastic(createDataSetForG9LegOne());

            LOG.info("Creating data set for G9, leg two....");
            insertIntoElastic(createDataSetForG9LegTwo());

            response.setSuccessful(true);
            return response;

        } catch (Exception e) {
            LOG.info("Exception occurred while trying to insert test data into elastic: ", e);
            response.setSuccessful(false);
            return response;
        }
    }

    @RequestMapping(value = "g10", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse createDataForG10() {

        ServiceResponse response = new ServiceResponse();

        try {
            LOG.info("Creating data set for G10, leg one....");
            insertIntoElastic(createDataSetForG10LegOne());

            LOG.info("Creating data set for G10, leg two....");
            insertIntoElastic(createDataSetForG10LegTwo());

            LOG.info("Creating data set for G10, leg three....");
            insertIntoElastic(createDataSetForG10LegThree());

            LOG.info("Creating data set for G10, leg four....");
            insertIntoElastic(createDataSetForG10LegFour());

            response.setSuccessful(true);
            return response;

        } catch (Exception e) {
            LOG.info("Exception occurred while trying to insert test data into elastic: ", e);
            response.setSuccessful(false);
            return response;
        }
    }

    @RequestMapping(value = "g7", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse createDataForG7() {

        ServiceResponse response = new ServiceResponse();

        try {
            LOG.info("Creating data set for G7....");
            insertIntoElastic(createDataSetForG5());
            response.setSuccessful(true);
            return response;

        } catch (Exception e) {
            LOG.info("Exception occurred while trying to insert test data into elastic: ", e);
            response.setSuccessful(false);
            return response;
        }
    }

    @RequestMapping(value = "g22", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse createDataForG22() {

        ServiceResponse response = new ServiceResponse();

        try {
            LOG.info("Creating data set for G22....");
            insertIntoElastic(createDataSetForG22());
            response.setSuccessful(true);
            return response;

        } catch (Exception e) {
            LOG.info("Exception occurred while trying to insert test data into elastic: ", e);
            response.setSuccessful(false);
            return response;
        }
    }

    private List<MockSubOrderDetailsDTO> createDataSetForG22() {
    	List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
                data.setRefund_initiated("Initiated");;
         });
        list.addAll(createBaseDataSet());
        return list;
	}

	@RequestMapping(value = "g5", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse createDataForG5() {

        ServiceResponse response = new ServiceResponse();

        try {
            LOG.info("Creating data set for G5....");
            insertIntoElastic(createDataSetForG7());
            response.setSuccessful(true);
            return response;

        } catch (Exception e) {
            LOG.info("Exception occurred while trying to insert test data into elastic: ", e);
            response.setSuccessful(false);
            return response;
        }
    }

    @RequestMapping(value = "g21", method = RequestMethod.GET)
    @ResponseBody
    public ServiceResponse createDataForG21() {

        ServiceResponse response = new ServiceResponse();

        try {
            LOG.info("Creating data set for G21....");
            insertIntoElastic(createBaseDataSet());
            response.setSuccessful(true);
            return response;

        } catch (Exception e) {
            LOG.info("Exception occurred while trying to insert test data into elastic: ", e);
            response.setSuccessful(false);
            return response;
        }
    }

    
    
    private List<MockSubOrderDetailsDTO> createDataSetForG7() {
        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setAttempted_status_date(new Date(date.getTime() + 24 * 60 * 60 * 1000));
        });

        List<MockSubOrderDetailsDTO> list2 = createBaseDataSet();
        list2.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setAttempted_status_date(new Date(date.getTime() + 2 * 24 * 60 * 60 * 1000));
        });
        List<MockSubOrderDetailsDTO> list3 = createBaseDataSet();
        list3.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setAttempted_status_date(new Date(date.getTime() + 3 * 24 * 60 * 60 * 1000));
        });
        List<MockSubOrderDetailsDTO> list4 = createBaseDataSet();
        list4.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setAttempted_status_date(new Date(date.getTime() + 5 * 24 * 60 * 60 * 1000));
        });


        List<MockSubOrderDetailsDTO> list7 = createBaseDataSet();
        list7.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setAttempted_status_date(null);
        });
        return list;
    }

    private List<MockSubOrderDetailsDTO> createDataSetForG5() {
        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_del_status_date(new Date(date.getTime() + 24 * 60 * 60 * 1000));
        });
        List<MockSubOrderDetailsDTO> list2 = createBaseDataSet();
        list2.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_del_status_date(new Date(date.getTime() + 2 * 24 * 60 * 60 * 1000));
        });
        List<MockSubOrderDetailsDTO> list3 = createBaseDataSet();
        list3.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_del_status_date(new Date(date.getTime() + 3 * 24 * 60 * 60 * 1000));
        });
        List<MockSubOrderDetailsDTO> list4 = createBaseDataSet();
        list4.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_del_status_date(new Date(date.getTime() + 5 * 24 * 60 * 60 * 1000));
        });

        List<MockSubOrderDetailsDTO> list5 = createBaseDataSet();
        list5.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_udl_status_date(new Date(date.getTime() + 5 * 24 * 60 * 60 * 1000));
        });
        List<MockSubOrderDetailsDTO> list6 = createBaseDataSet();
        list6.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_udl_status_date(new Date(date.getTime() + 5 * 24 * 60 * 60 * 1000));
            data.setRto_date(new Date(date.getTime() + 6 * 24 * 60 * 60 * 1000));
        });

        List<MockSubOrderDetailsDTO> list7 = createBaseDataSet();
        list7.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_del_status_date(null);
        });

        list.addAll(list2);
        list.addAll(list3);
        list.addAll(list4);
        list.addAll(list5);
        list.addAll(list6);
        list.addAll(list7);
        return list;
    }

    public List<MockSubOrderDetailsDTO> createDataSetForG8LegOne() {
 
        return createBaseDataSet();
    }

    public List<MockSubOrderDetailsDTO> createDataSetForG8LegTwo() {

        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_connected_status_date(date);
        });
        return list;
    }

    public List<MockSubOrderDetailsDTO> createDataSetForG8LegThree() {

        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_connected_status_date(date);
            data.setTp_rad_status_date(date);
        });
        return list;
    }

    public List<MockSubOrderDetailsDTO> createDataSetForG8LegFour() {

        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_connected_status_date(date);
            data.setTp_rad_status_date(date);
            data.setTp_ofd_status_date(date);
        });
        return list;
    }

    public List<MockSubOrderDetailsDTO> createDataSetForG8LegFive() {

        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_connected_status_date(date);
            data.setTp_rad_status_date(date);
            data.setTp_ofd_status_date(date);
            data.setAttempted_status_date(date);
        });
        return list;
    }

    private List<MockSubOrderDetailsDTO> createDataSetForG9LegOne() {

        return createBaseDataSet();
    }

    private List<MockSubOrderDetailsDTO> createDataSetForG9LegTwo() {

        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_connected_status_date(date);
            data.setTp_rad_status_date(date);
            data.setTp_ofd_status_date(date);
            data.setAttempted_status_date(date);
        });
        return list;
    }

    private List<MockSubOrderDetailsDTO> createBaseDataSet() {

        List<MockSubOrderDetailsDTO> list              = new LinkedList<>();
        Date                         orderCreationDate = DateUtils.getCurrentTime();
        Date                         expectedDate      = DateUtils.addToDate(orderCreationDate, 60, TimeUnit.HOURS);

        Date       goodDate     = DateUtils.subtractFromDate(orderCreationDate, 4, TimeUnit.HOURS);
        Date       normalDate   = DateUtils.subtractFromDate(orderCreationDate, 25, TimeUnit.HOURS);
        Date       criticalDate = DateUtils.subtractFromDate(orderCreationDate, 50, TimeUnit.HOURS);
        List<Date> dates        = ImmutableList.of(goodDate, normalDate, criticalDate);
        
        dates.forEach(date -> {
            courierGroups.forEach(courierGroup -> {
                addressDetails.forEach(destinationAddress -> {
                    addressDetails.forEach(sourceAddress -> {
                        complaintCategory.forEach(categoryOfComplaint -> {
                            complaintOrigin.forEach(originOfComplaint -> {
                                MockSubOrderDetailsDTO dto = new MockSubOrderDetailsDTO();
                                // basic attributes
                                dto.setMto("mto");
                                dto.setLane_type("zone");
                                dto.setMetro("metro");
                                dto.setOn_hold(false);
                                dto.setItem_price("10");
                                dto.setPayment_mode("NDD");
                                dto.setSuper_category("Fashion");
                                dto.setFulfillment_model("DROPSHIP");
                                dto.setOrder_to_shipped("10");
                                dto.setShipped_to_delivered("10");
                                dto.setOrder_to_delivered("10");
                                dto.setShipping_mode("Air");
                                dto.setDelivery_type("STD");
                                dto.setCourier_type("NORMAL");
                                dto.setCourier_group(courierGroup);
                                dto.setCourier_code(courierGroup);
                                dto.setSoi_channel_code(null);
                                dto.setSoi_user_type(null);
                                dto.setTp_current_status(null);
                                // address attributes
                                dto.setSource_zone(sourceAddress.zone);
                                dto.setSource_city(sourceAddress.city);
                                dto.setSource_node(sourceAddress.node);
                                dto.setSource_tier(sourceAddress.tier);
                                dto.setSource_state(sourceAddress.state);
                                dto.setSource_region(sourceAddress.region);
                                dto.setTp_courier_origin_city(sourceAddress.city);
                                dto.setTp_courier_origin_state(sourceAddress.state);
                                dto.setCourier_origin_city(sourceAddress.city);
                                dto.setCourier_origin_state(sourceAddress.state);
                                dto.setDestination_city(destinationAddress.city);
                                dto.setDestination_zone(destinationAddress.zone);
                                dto.setDestination_node(destinationAddress.zone);
                                dto.setDestination_tier(destinationAddress.tier);
                                dto.setDestination_state(destinationAddress.state);
                                dto.setDestination_region(destinationAddress.region);
                                dto.setTp_courier_destination_city(destinationAddress.city);
                                dto.setTp_courier_destination_state(destinationAddress.state);
                                dto.setCourier_destination_city(destinationAddress.city);
                                dto.setCourier_destination_state(destinationAddress.state);
                                dto.setLane(sourceAddress.city + "::" + destinationAddress.city);
                                // dates
                                dto.setExpected_delivery_date(expectedDate);
                                dto.setExpected_delivery_date_range_start(expectedDate);
                                // ud reasons
                                dto.setUd_reason(null);
                                dto.setTp_first_udl_reason(null);
                                dto.setTp_second_udl_reason(null);
                                dto.setTp_third_udl_reason(null);
                                dto.setTp_fourth_udl_reason(null);
                                // important dates
                                dto.setOrder_created_date(date);
                                dto.setUpdated_on(date);
                                dto.setSp_awb_uploaded_status_date(date);
                                dto.setTp_connected_status_date(null);
                                dto.setTp_rad_status_date(null);
                                dto.setTp_ofd_status_date(null);
                                dto.setAttempted_status_date(null);
                                dto.setClosed_status_date(null);
                                dto.setRto_date(null);
                                //complaint category
                                dto.setComplaint_category(categoryOfComplaint);
                                dto.setComplaint_origin(originOfComplaint);
                                dto.setRefund_initiated(null);
                                list.add(dto);
	                        });
	                    });
	                });
	            });
	        });
        });    
        return list;
    }

    private List<MockSubOrderDetailsDTO> createDataSetForG10LegOne() {

        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_connected_status_date(date);
            data.setTp_rad_status_date(date);
            data.setTp_ofd_status_date(date);
            data.setAttempted_status_date(date);
            data.setTp_first_udl_reason("Customer not available");
            data.setUd_reason("Customer not available");
            data.setTp_udl_status_date(date);
        });
        return list;
    }

    private List<MockSubOrderDetailsDTO> createDataSetForG10LegTwo() {

        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_connected_status_date(date);
            data.setTp_rad_status_date(date);
            data.setTp_ofd_status_date(date);
            data.setAttempted_status_date(date);
            data.setTp_first_udl_reason("Customer not available");
            data.setTp_udl_status_date(date);
            data.setTp_second_udl_reason("Customer not available");
            data.setUd_reason("Customer not available");
            data.setTp_second_udl_status_date(date);
        });
        return list;
    }

    private List<MockSubOrderDetailsDTO> createDataSetForG10LegThree() {

        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_connected_status_date(date);
            data.setTp_rad_status_date(date);
            data.setTp_ofd_status_date(date);
            data.setAttempted_status_date(date);
            data.setTp_first_udl_reason("Customer not available");
            data.setTp_udl_status_date(date);
            data.setTp_second_udl_reason("Customer not available");
            data.setTp_second_udl_status_date(date);
            data.setTp_third_udl_reason("Customer refused to accept consignment");
            data.setUd_reason("Customer refused to accept consignment");
            data.setTp_third_udl_status_date(date);
        });
        return list;
    }

    private List<MockSubOrderDetailsDTO> createDataSetForG10LegFour() {

        List<MockSubOrderDetailsDTO> list = createBaseDataSet();
        list.forEach(data -> {
            Date date = data.getSp_awb_uploaded_status_date();
            data.setTp_connected_status_date(date);
            data.setTp_rad_status_date(date);
            data.setTp_ofd_status_date(date);
            data.setAttempted_status_date(date);
            data.setTp_first_udl_reason("Customer not available");
            data.setTp_udl_status_date(date);
            data.setTp_second_udl_reason("Customer not available");
            data.setTp_second_udl_status_date(date);
            data.setTp_third_udl_reason("Customer refused to accept consignment");
            data.setTp_third_udl_status_date(date);
            data.setTp_fourth_udl_reason("Unserviceable pincode");
            data.setTp_fourth_udl_status_date(date);
            data.setUd_reason("Unserviceable pincode");
        });
        return list;
    }

    private static class AddressDetail {

        private String city;
        private String state;
        private String zone;
        private String tier;
        private String node;
        private String region;

        public AddressDetail(String city, String state, String zone, String tier, String node, String region) {
            this.city = city;
            this.state = state;
            this.zone = zone;
            this.tier = tier;
            this.node = node;
            this.region = region;
        }
    }
}
