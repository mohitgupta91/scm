package com.snapdeal.scm.mongo.doc;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by ashwini.kumar on 06/05/16.
 */
@Document(collection = "notification_event_mapping")
public class NotificationEventMapping {

    private Long alertId;
    private String groupLogicName;
    private String eventKey;

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getGroupLogicName() {
        return groupLogicName;
    }

    public void setGroupLogicName(String groupLogicName) {
        this.groupLogicName = groupLogicName;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NotificationEventMapping{");
        sb.append("alertId=").append(alertId);
        sb.append(", groupLogicName='").append(groupLogicName).append('\'');
        sb.append(", eventKey='").append(eventKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
