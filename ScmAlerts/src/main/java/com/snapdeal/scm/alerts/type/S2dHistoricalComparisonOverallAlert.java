package com.snapdeal.scm.alerts.type;

import com.snapdeal.scm.alerts.GroupLogic;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.mongo.doc.AlertGroupLogic;
import com.snapdeal.scm.mongo.doc.AlertInstance;
import com.snapdeal.scm.mongo.doc.AlertNotification;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.AvgBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * S2dHistoricalComparisonOverallAlert : s2d historical comparison overall
 *
 * @author pranav, mohit
 */
@Component
public class S2dHistoricalComparisonOverallAlert extends BaseAlert {

    private static final Logger logger = LoggerFactory.getLogger(S2dHistoricalComparisonOverallAlert.class);

    private Map<String, String> groupLogicToElasticColMap = new HashMap<String, String>();

    @PostConstruct
    private void upldateMap() {
        groupLogicToElasticColMap.put("overall", null);
        groupLogicToElasticColMap.put("lane", "avg");
        groupLogicToElasticColMap.put("lanecourier", "avg");
        groupLogicToElasticColMap.put("courier", "avg");
    }

    public static final String AVG_S2D = "avg_s2d";

    @Override
    public void process(AlertInstance alert) {
        logger.info("processing S2dHistoricalComparisonOverallAlert with alert {}", alert);
        AlertGroupLogic grpLogic = super.getGroupLogic(alert.getAlertId(), alert.getGroupLogicName());
        if (null == grpLogic) {
            return;
        }
        GroupLogic grpLogicEnum = GroupLogic.fromGroupLogic(alert.getGroupLogicName().toLowerCase());
        if (!grpLogic.IsFileType()) {
            switch (grpLogicEnum) {
                case OVERALL:
                    runOverallAlertNotification(alert);
                    break;
                case LANE_TYPE:
                    runLaneTypeFileAlertNotification(alert);
                    break;
                case COURIER:
                    runCourierTypeFileAlertNotification(alert);
                    break;
                case COURIER_LANE:
                    runCourierLaneTypeFileAlertNotification(alert);
                default:
                    break;
            }
        } else {

        }
    }

    private void runCourierLaneTypeFileAlertNotification(AlertInstance alert) {
        AlertNotification notification = null;
        if (alert != null && alert.getFileHeader().size() > 0) {
            List<Map<String, String>> fileData = getFileValues(alert);
            for (Map<String, String> row : fileData) {
                AvgBuilder       avgS2d           = AggregationBuilders.avg(AVG_S2D).field(SubOrderDetailElasticColumn.SHIPPED_TO_DELIVERED.getColumnName());
                BoolQueryBuilder boolQueryHistory = QueryBuilders.boolQuery();

                String sourceCity       = row.get("sourceCity");
                String sourceState      = row.get("sourceState");
                String destinationCity  = row.get("destinationCity");
                String destinationState = row.get("destinationState");
                String courier          = row.get("courier");

                applyDateFilter(boolQueryHistory, alert.getHistoricalDateRangeStart(), alert.getHistoricalDateRangeEnd(), SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE);
                applyLaneCourierFilter(boolQueryHistory, sourceCity, sourceState, destinationCity, destinationState, courier);
                SearchResponse historicalSR = executeQuery(boolQueryHistory, avgS2d);
                System.out.println("Historical Data : " + historicalSR);

                double historicalValue = ((InternalAvg) historicalSR.getAggregations().get(AVG_S2D)).getValue();

                BoolQueryBuilder boolQueryCurrent = QueryBuilders.boolQuery();
                applyDateFilter(boolQueryCurrent, alert.getCurrentDateRangeStart(), alert.getCurrentDateRangeEnd(), SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE);
                applyLaneCourierFilter(boolQueryHistory, sourceCity, sourceState, destinationCity, destinationState, courier);
                SearchResponse currentSR = executeQuery(boolQueryCurrent, avgS2d);
                System.out.println("Current Data : " + currentSR);

                double currentValue = ((InternalAvg) currentSR.getAggregations().get(AVG_S2D)).getValue();

                System.out.println(currentValue);
                Double diffValue = Double.parseDouble(row.get("Percentage"));
                Double load      = Double.parseDouble(row.get("load"));
                double diff      = (currentValue - historicalValue) * 100;
                if (diff > 0 && historicalValue >= load && currentValue >= load) {
                    double diffPercentage = diff / historicalValue;
                    if (diffPercentage > diffValue) {
                        logger.info("Firing Alert for Instance : " + alert);
                        if (notification == null) {
                            notification = new AlertNotification();
                        }
                        notification.getDetails().add("difference : " + diffPercentage + " for sourceCity:" + sourceCity + " sourceState:" + sourceState + " destinationCity: "
                                                      + destinationCity + "destinationState: " + destinationState + " courier: " + courier);
                    }
                }
            }
            if (notification != null) {
                notification = saveNotification(alert, notification.getDetails());
                Map<String, Object> eventBody = createEventBody("", notification.getCreated(), notification.getDetails(), alert.getEmailId(), null);
                triggerUCMSNotificationEvent(alert.getAlertId(), alert.getGroupLogicName(), notification.getNotificationId(), eventBody);
            }
        }

    }

    private void runCourierTypeFileAlertNotification(AlertInstance alert) {
        AlertNotification notification = null;
        if (alert != null && alert.getFileHeader().size() > 0) {
            List<Map<String, String>> fileData = getFileValues(alert);
            for (Map<String, String> row : fileData) {
                AvgBuilder       avgS2d           = AggregationBuilders.avg(AVG_S2D).field(SubOrderDetailElasticColumn.SHIPPED_TO_DELIVERED.getColumnName());
                BoolQueryBuilder boolQueryHistory = QueryBuilders.boolQuery();

                String courier = row.get("courier");

                applyDateFilter(boolQueryHistory, alert.getHistoricalDateRangeStart(), alert.getHistoricalDateRangeEnd(), SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE);
                applyCourierFilter(boolQueryHistory, courier);
                SearchResponse historicalSR = executeQuery(boolQueryHistory, avgS2d);
                System.out.println("Historical Data : " + historicalSR);

                double historicalValue = ((InternalAvg) historicalSR.getAggregations().get(AVG_S2D)).getValue();

                BoolQueryBuilder boolQueryCurrent = QueryBuilders.boolQuery();
                applyDateFilter(boolQueryCurrent, alert.getCurrentDateRangeStart(), alert.getCurrentDateRangeEnd(), SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE);
                applyCourierFilter(boolQueryCurrent, courier);
                SearchResponse currentSR = executeQuery(boolQueryCurrent, avgS2d);
                System.out.println("Current Data : " + currentSR);

                double currentValue = ((InternalAvg) currentSR.getAggregations().get(AVG_S2D)).getValue();

                System.out.println(currentValue);
                Double diffValue = Double.parseDouble(row.get("Percentage"));
                Double load      = Double.parseDouble(row.get("load"));
                double diff      = currentValue - historicalValue;
                if (diff > 0 && historicalValue >= load && currentValue >= load) {
                    double diffPercentage = (diff / historicalValue) * 100;
                    if (diffPercentage > diffValue) {
                        logger.info("Firing Alert for Instance : " + alert);
                        if (notification == null) {
                            notification = new AlertNotification();
                        }
                        notification.getDetails().add("difference : " + diffPercentage + " for courier: " + courier);
                    }
                }
            }
            if (notification != null) {
                notification = saveNotification(alert, notification.getDetails());
                Map<String, Object> eventBody = createEventBody("", notification.getCreated(), notification.getDetails(), alert.getEmailId(), null);
                triggerUCMSNotificationEvent(alert.getAlertId(), alert.getGroupLogicName(), notification.getNotificationId(), eventBody);
            }
        }
    }

    private void runLaneTypeFileAlertNotification(AlertInstance alert) {
        AlertNotification notification = null;
        if (alert != null && alert.getFileHeader().size() > 0) {
            List<Map<String, String>> fileData = getFileValues(alert);
            for (Map<String, String> row : fileData) {
                AvgBuilder       avgS2d           = AggregationBuilders.avg(AVG_S2D).field(SubOrderDetailElasticColumn.SHIPPED_TO_DELIVERED.getColumnName());
                BoolQueryBuilder boolQueryHistory = QueryBuilders.boolQuery();

                String sourceCity       = row.get("sourceCity");
                String sourceState      = row.get("sourceState");
                String destinationCity  = row.get("destinationCity");
                String destinationState = row.get("destinationState");

                applyDateFilter(boolQueryHistory, alert.getHistoricalDateRangeStart(), alert.getHistoricalDateRangeEnd(), SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE);
                applyLaneFilter(boolQueryHistory, sourceCity, sourceState, destinationCity, destinationState);
                SearchResponse historicalSR = executeQuery(boolQueryHistory, avgS2d);
                System.out.println("Historical Data : " + historicalSR);

                double historicalValue = ((InternalAvg) historicalSR.getAggregations().get(AVG_S2D)).getValue();

                BoolQueryBuilder boolQueryCurrent = QueryBuilders.boolQuery();
                applyDateFilter(boolQueryCurrent, alert.getCurrentDateRangeStart(), alert.getCurrentDateRangeEnd(), SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE);
                applyLaneFilter(boolQueryCurrent, sourceCity, sourceState, destinationCity, destinationState);
                SearchResponse currentSR = executeQuery(boolQueryCurrent, avgS2d);
                System.out.println("Current Data : " + currentSR);

                double currentValue = ((InternalAvg) currentSR.getAggregations().get(AVG_S2D)).getValue();

                Double diffValue = Double.parseDouble(row.get("Percentage"));
                Double load      = Double.parseDouble(row.get("load"));
                double diff      = currentValue - historicalValue;
                if (diff > 0 && historicalValue >= load && currentValue >= load) {
                    double diffPercentage = (diff / historicalValue) * 100;
                    if (diffPercentage > diffValue) {
                        logger.info("Firing Alert for Instance : " + alert);
                        if (notification == null) {
                            notification = new AlertNotification();
                        }
                        notification.getDetails().add("difference : " + diffPercentage + " for sourceCity:" + sourceCity + " sourceState:" + sourceState + " destinationCity: "
                                                      + destinationCity + "destinationState: " + destinationState);
                    }
                }
            }
            if (notification != null) {
                notification = saveNotification(alert, notification.getDetails());
                Map<String, Object> eventBody = createEventBody("", notification.getCreated(), notification.getDetails(), alert.getEmailId(), null);
                triggerUCMSNotificationEvent(alert.getAlertId(), alert.getGroupLogicName(), notification.getNotificationId(), eventBody);
            }
        }
    }

    private void runOverallAlertNotification(AlertInstance alert) {
        AvgBuilder avgS2d = AggregationBuilders.avg(AVG_S2D).field(SubOrderDetailElasticColumn.SHIPPED_TO_DELIVERED.getColumnName());

        BoolQueryBuilder boolQueryHistory = QueryBuilders.boolQuery();
        applyDateFilter(boolQueryHistory, alert.getHistoricalDateRangeStart(), alert.getHistoricalDateRangeEnd(), SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE);
        SearchResponse historicalSR = executeQuery(boolQueryHistory, avgS2d);
        System.out.println("Historical Data : " + historicalSR);

        double historicalValue = ((InternalAvg) historicalSR.getAggregations().get(AVG_S2D)).getValue();

        System.out.println(historicalValue);

        BoolQueryBuilder boolQueryCurrent = QueryBuilders.boolQuery();
        applyDateFilter(boolQueryCurrent, alert.getCurrentDateRangeStart(), alert.getCurrentDateRangeEnd(), SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE);
        SearchResponse currentSR = executeQuery(boolQueryCurrent, avgS2d);
        System.out.println("Current Data : " + currentSR);

        double currentlValue = ((InternalAvg) currentSR.getAggregations().get(AVG_S2D)).getValue();

        System.out.println(currentlValue);

        double            diff         = currentlValue - historicalValue;
        AlertNotification notification = null;
        if (diff > 0) {
            double diffPercentage = (diff / historicalValue) * 100;
            if (diffPercentage > alert.getValue()) {
                logger.info("Firing Alert for Instance : " + alert);
                notification = new AlertNotification();
                notification.getDetails().add("diffPercentage : " + diffPercentage);
                notification = saveNotification(alert, notification.getDetails());
                Map<String, Object> eventBody = createEventBody("", notification.getCreated(), notification.getDetails(), alert.getEmailId(), null);
                triggerUCMSNotificationEvent(alert.getAlertId(), alert.getGroupLogicName(), notification.getNotificationId(), eventBody);
            }
        }
    }

    private Map<String, Object> createEventBody(String userName, Date createdTime, List<String> details, String emailId, String mobileNumber) {
        return Collections.unmodifiableMap(new HashMap<String, Object>() {
            {
                put("user", userName);
                put("createdTime", createdTime);
                put("data", String.join(",", details));
                put("emailId", emailId);
                put("mobileNumer", mobileNumber);
            }
        });
    }
}
