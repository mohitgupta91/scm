package com.snapdeal.scm.mongo.doc;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by ashwini.kumar on 06/05/16.
 */
@Document(collection = "notification_event")
public class NotificationEvent {

    private String eventKey;
    private Map<String, String> templateKeys;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public Map<String, String> getTemplateKeys() {
        return templateKeys;
    }

    public void setTemplateKeys(Map<String, String> templateKeys) {
        this.templateKeys = templateKeys;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NotificationEvent{");
        sb.append("eventKey='").append(eventKey).append('\'');
        sb.append(", templateKeys=").append(templateKeys);
        sb.append('}');
        return sb.toString();
    }
}
