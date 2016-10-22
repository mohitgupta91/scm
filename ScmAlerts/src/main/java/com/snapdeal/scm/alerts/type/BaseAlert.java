package com.snapdeal.scm.alerts.type;

import com.snapdeal.scm.alerts.services.IUCMSEventTriggerService;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.enums.SequenceKey;
import com.snapdeal.scm.mongo.dao.*;
import com.snapdeal.scm.mongo.doc.*;
import com.snapdeal.scm.mongo.doc.AlertNotification.NotificationStatus;
import com.snapdeal.scm.mongo.mao.ISequenceMao;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * BaseAlert: All alert must extend the base alert
 *
 * @author pranav
 */
@Component
public abstract class BaseAlert {

    private static final Logger logger = LoggerFactory.getLogger(BaseAlert.class);

    public static final String UTC        = "UTC";
    public static final String IST_OFFSET = "+05:30";

    @Autowired
    private AlertNotificationRepository notificationRepo;

    @Autowired
    private AlertInstanceRepository alertRepo;

    @Autowired
    private AlertGroupLogicRepository grpLogicRepo;

    @Autowired
    private InstanceGroupingParamRepository instanceParamRepo;

    @Autowired
    private ISequenceMao sequenceDao;

    private IUCMSEventTriggerService ucmsTriggerService;

    @Autowired
    private EventMappingRepository eventMappingRepository;

    @Autowired
    @Qualifier("transportClient")
    private Client client;

    @Value("${scm.elastic.index.name.type}")
    private String indexType;

    @Value("${scm.elastic.index.name}")
    private String indexName;

    public Client getClient() {
        return client;
    }

    public String getIndexType() {
        return indexType;
    }

    public String getIndexName() {
        return indexName;
    }

    public abstract void process(AlertInstance alert);

    public AlertInstance getAlertById(Long alertId) {
        return alertRepo.findByAlertId(alertId);
    }

    public BoolQueryBuilder applyDateFilter(BoolQueryBuilder boolQuery, int xDays, int yDays, SubOrderDetailElasticColumn colName) {
        LocalDate localDate = LocalDate.now();
        LocalDate xDayDate  = localDate.minusDays(xDays);
        LocalDate yDayDate  = localDate.minusDays(yDays);
        Date      dStart    = Date.from(xDayDate.atStartOfDay().atZone(ZoneId.ofOffset(UTC, ZoneOffset.of(IST_OFFSET))).toInstant());
        Date      dEnd      = Date.from(yDayDate.atStartOfDay().atZone(ZoneId.ofOffset(UTC, ZoneOffset.of(IST_OFFSET))).toInstant());

        if (null == boolQuery) {
            boolQuery = QueryBuilders.boolQuery();
        }
        boolQuery.must(QueryBuilders.rangeQuery(colName.getColumnName()).from(dStart).to(dEnd).includeUpper(true).includeLower(false));
        return boolQuery;
    }

    public BoolQueryBuilder applyLaneFilter(BoolQueryBuilder boolQuery, String sourceCity, String sourceState, String destinationCity, String destinationState) {
        if (null == boolQuery) {
            boolQuery = QueryBuilders.boolQuery();
        }
        boolQuery.must(QueryBuilders.termQuery(SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName(), destinationCity));
        boolQuery.must(QueryBuilders.termQuery(SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName(), destinationState));
        boolQuery.must(QueryBuilders.termQuery(SubOrderDetailElasticColumn.SOURCE_CITY.getColumnName(), sourceCity));
        boolQuery.must(QueryBuilders.termQuery(SubOrderDetailElasticColumn.SOURCE_STATE.getColumnName(), sourceState));
        logger.info("Query : \n" + boolQuery.toString());
        return boolQuery;
    }

    public BoolQueryBuilder applyLaneCourierFilter(BoolQueryBuilder boolQuery, String sourceCity, String sourceState, String destinationCity, String destinationState,
                                                   String courier) {
        if (null == boolQuery) {
            boolQuery = QueryBuilders.boolQuery();
        }
        boolQuery.must(QueryBuilders.termQuery(SubOrderDetailElasticColumn.DESTINATION_CITY.getColumnName(), destinationCity));
        boolQuery.must(QueryBuilders.termQuery(SubOrderDetailElasticColumn.DESTINATION_STATE.getColumnName(), destinationState));
        boolQuery.must(QueryBuilders.termQuery(SubOrderDetailElasticColumn.SOURCE_CITY.getColumnName(), sourceCity));
        boolQuery.must(QueryBuilders.termQuery(SubOrderDetailElasticColumn.SOURCE_STATE.getColumnName(), sourceState));
        boolQuery.must(QueryBuilders.termQuery(SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName(), courier));
        logger.info("Query : \n" + boolQuery.toString());
        return boolQuery;
    }

    public BoolQueryBuilder applyCourierFilter(BoolQueryBuilder boolQuery, String courier) {
        if (null == boolQuery) {
            boolQuery = QueryBuilders.boolQuery();
        }
        boolQuery.must(QueryBuilders.termQuery(SubOrderDetailElasticColumn.COURIER_GROUP.getColumnName(), courier));
        logger.info("Query : \n" + boolQuery.toString());
        return boolQuery;
    }

    public SearchHits getAllHitsForQuery(String query) {
        SearchResponse response = client.prepareSearch(indexName).setTypes(indexType).setSource(query.toString()).execute().actionGet();
        return response.getHits();
    }

    public SearchResponse executeQuery(BoolQueryBuilder boolQuery, AbstractAggregationBuilder aggregationBuilders) {
        SearchRequestBuilder searchRequestBuilder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType()).setQuery(boolQuery).setSize(0).addAggregation(
                aggregationBuilders);
        logger.info("Query : \n" + searchRequestBuilder.toString());
        return searchRequestBuilder.get();
    }

    public AlertGroupLogic getGroupLogic(Long alertId, String groupLogicName) {
        if (null == alertId || null == groupLogicName) {
            return null;
        }
        return grpLogicRepo.findByAlertIdAndGroupLogicName(alertId, groupLogicName);
    }

    public AlertNotification saveNotification(AlertInstance instance, List<String> details) {
        AlertNotification notification = createNotification(instance, details);
        notification = notificationRepo.save(notification);
        return notification;
    }

    private AlertNotification createNotification(AlertInstance instance, List<String> details) {
        AlertNotification notification = new AlertNotification();
        notification.setNotificationId(sequenceDao.getNextSequenceId(SequenceKey.ALERT_NOTIFICATION.name()));
        notification.setAlertId(instance.getAlertId());
        notification.setInstanceId(instance.getAlertInstanceId());
        notification.setCurrentStatus(NotificationStatus.ASSIGNED);
        notification.setDetails(details);
        notification.setAlertCreatedBy(instance.getCreatedBy());
        notification.setAssignedTo(instance.getEmailId());
        notification.setNotificationActivity(Collections.unmodifiableList(new ArrayList<NotificationActivity>() {
            private static final long serialVersionUID = 1L;

            {
                add(new NotificationActivity(NotificationStatus.ASSIGNED, null, "system", new Date()));
            }
        }));
        return notification;
    }

    public List<Map<String, String>> getFileValues(AlertInstance alert) {
        List<InstanceGroupingParam> paramList = instanceParamRepo.findByAlertInstanceId(alert.getAlertInstanceId());
        List<Map<String, String>>   fileData  = new ArrayList<Map<String, String>>();
        for (InstanceGroupingParam param : paramList) {
            fileData.add(param.getGroupingParam());
        }
        return fileData;
    }

    public void triggerUCMSNotificationEvent(Long alertId, String groupLogicName, Long notificationId, Map<String, Object> eventBody) {
        NotificationEventMapping eventMapping = eventMappingRepository.findByAlertIdAndGroupLogicName(alertId, groupLogicName);
        if (null != eventMapping) {
            ucmsTriggerService.sendCommunicationEventAsync(notificationId, eventMapping.getEventKey(), eventBody);
        } else {
            logger.info("no Event Exist for alertId : [{}] and GroupLogic : [{}]", alertId, groupLogicName);
        }
    }
}
