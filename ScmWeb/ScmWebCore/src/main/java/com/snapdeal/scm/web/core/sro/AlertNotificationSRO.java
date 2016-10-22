/**
 * Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 * JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.core.sro;

import com.snapdeal.scm.mongo.doc.AlertNotification;
import com.snapdeal.scm.mongo.doc.NotificationActivity;
import com.snapdeal.scm.utils.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * @version 1.0, 14-Apr-2016
 * @author ashwini
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertNotificationSRO {

    private Long notificationId;
    private String notificationKey;
    private Long alertId;
    private String alertTitle;
    private Long alertInstanceId;
    private String alertInstanceTitle;
    private String status;
    private String assignedTo;
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private List<NotificationActivity> notificationActivity;

    public AlertNotificationSRO() {
    }

    public AlertNotificationSRO(AlertNotification notification, String alertTitle, String instanceTitle) {
        this.notificationId = notification.getNotificationId();
        this.notificationKey = new StringBuilder().append(notification.getAlertId()).append("_").append(notification.getInstanceId()).append("_").append(
                notification.getNotificationId()).toString();
        this.alertId = notification.getAlertId();
        this.alertTitle = alertTitle;
        this.alertInstanceId = notification.getInstanceId();
        this.alertInstanceTitle = instanceTitle;
        this.status = notification.getCurrentStatus();
        this.assignedTo = notification.getAssignedTo();
        this.createdOn = DateUtils.convertDateToString(notification.getCreated());
        this.createdBy = notification.getAlertCreatedBy();
        this.updatedOn = null!=notification.getUpdated()? DateUtils.convertDateToString(notification.getUpdated()):null;
        this.notificationActivity = notification.getNotificationActivity();
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public List<NotificationActivity> getNotificationActivity() {
        return notificationActivity;
    }

    public void setNotificationActivity(List<NotificationActivity> notificationActivity) {
        this.notificationActivity = notificationActivity;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AlertNotificationSRO [notificationId=");
        builder.append(notificationId);
        builder.append(", notificationKey=");
        builder.append(notificationKey);
        builder.append(", alertId=");
        builder.append(alertId);
        builder.append(", alertTitle=");
        builder.append(alertTitle);
        builder.append(", instanceId=");
        builder.append(alertInstanceId);
        builder.append(", instanceTitle=");
        builder.append(alertInstanceTitle);
        builder.append(", status=");
        builder.append(status);
        builder.append(", assignedTo=");
        builder.append(assignedTo);
        builder.append(", createdOn=");
        builder.append(createdOn);
        builder.append(", createdBy=");
        builder.append(createdBy);
        builder.append(", updatedOn=");
        builder.append(updatedOn);
        builder.append(", notificationActivity=");
        builder.append(notificationActivity);
        builder.append("]");
        return builder.toString();
    }

    public Long getAlertInstanceId() {
        return alertInstanceId;
    }

    public void setAlertInstanceId(Long alertInstanceId) {
        this.alertInstanceId = alertInstanceId;
    }

    public String getAlertInstanceTitle() {
        return alertInstanceTitle;
    }

    public void setAlertInstanceTitle(String alertInstanceTitle) {
        this.alertInstanceTitle = alertInstanceTitle;
    }

}
