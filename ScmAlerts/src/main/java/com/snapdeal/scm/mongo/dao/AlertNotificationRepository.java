/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.mongo.doc.AlertNotification;
import com.snapdeal.scm.mongo.doc.NotificationActivity;

/**
 * @version 1.0, 13-Apr-2016
 * @author ashwini
 */
public interface AlertNotificationRepository extends MongoRepository<AlertNotification, String> {

    public AlertNotification findByNotificationId(Long notificationId);

    default void saveAndUpdate(Long notificationId, NotificationActivity activity) {
        AlertNotification alertNotification = findByNotificationId(notificationId);
        if (null != alertNotification) {
            if (null != activity.getStatus()) {
                alertNotification.setCurrentStatus(activity.getStatus());
            }
            alertNotification.getNotificationActivity().add(activity);
        }
        save(alertNotification);
    }

    public List<AlertNotification> findByAlertCreatedByOrAssignedTo(String alertCreatedBy, String assignedTo);
}
