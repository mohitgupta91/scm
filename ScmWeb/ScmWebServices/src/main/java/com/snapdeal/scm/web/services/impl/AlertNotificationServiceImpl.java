/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.scm.mongo.dao.AlertInstanceRepository;
import com.snapdeal.scm.mongo.dao.AlertNotificationRepository;
import com.snapdeal.scm.mongo.dao.AlertRepository;
import com.snapdeal.scm.mongo.doc.Alert;
import com.snapdeal.scm.mongo.doc.AlertInstance;
import com.snapdeal.scm.mongo.doc.AlertNotification;
import com.snapdeal.scm.mongo.doc.NotificationActivity;
import com.snapdeal.scm.web.core.dto.AlertNotificationUpdateDTO;
import com.snapdeal.scm.web.core.sro.AlertNotificationSRO;
import com.snapdeal.scm.web.services.IAlertNotificationService;

/**
 * @version 1.0, 13-Apr-2016
 * @author ashwini
 */
@Service
public class AlertNotificationServiceImpl implements IAlertNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(AlertNotificationServiceImpl.class);

    @Autowired
    private AlertNotificationRepository notificationRepo;

    @Autowired
    private AlertRepository             alertRepo;

    @Autowired
    private AlertInstanceRepository     instanceRepo;

    @Override
    public void updateNotification(AlertNotificationUpdateDTO dto) {
        NotificationActivity activity = new NotificationActivity(dto.getStatus(), dto.getComment(), dto.getUpdatedBy(), new Date());
        notificationRepo.saveAndUpdate(dto.getNotificationId(), activity);
    }

    @Override
    public AlertNotificationSRO viewNotification(Long notificationId) {
        AlertNotification notification = notificationRepo.findByNotificationId(notificationId);
        return convertNotificationToSRO(notification);
    }

    @Override
    public List<AlertNotificationSRO> listAlertNotification(String userName) {
        List<AlertNotification> notifications = notificationRepo.findByAlertCreatedByOrAssignedTo(userName, userName);
        List<AlertNotificationSRO> sro = notifications.stream().map(notification -> convertNotificationToSRO(notification)).collect(Collectors.toList());
        return sro;
    }

    @Override
    public List<AlertNotificationSRO> listAlertNotification() {
        List<AlertNotification> notifications = notificationRepo.findAll();
        List<AlertNotificationSRO> sro = notifications.stream().map(notification -> convertNotificationToSRO(notification)).collect(Collectors.toList());
        return sro;
    }

    private AlertNotificationSRO convertNotificationToSRO(AlertNotification notification) {
        Alert alert = alertRepo.findByAlertId(notification.getAlertId());
        String alertTitle = null != alert ? alert.getTitle() : null;
        AlertInstance instance = instanceRepo.findByAlertInstanceId(notification.getInstanceId());
        String instanceTitle = null != instance ? instance.getAlertInstanceTitle() : null;
        return new AlertNotificationSRO(notification, alertTitle, instanceTitle);
    }

}
