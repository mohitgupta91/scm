package com.snapdeal.scm.alerts.services;

import java.util.Map;

/**
 * Created by ashwini.kumar on 04/05/16.
 */
public interface IUCMSEventTriggerService {

    void sendCommunicationEventAsync(Long notificationId, String eventType, Map<String, Object> fields);

}
