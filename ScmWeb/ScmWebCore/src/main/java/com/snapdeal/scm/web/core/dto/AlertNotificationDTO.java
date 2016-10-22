/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.core.dto;

import java.util.List;

/**
 * @version 1.0, 14-Apr-2016
 * @author ashwini
 */
public class AlertNotificationDTO {

    private Long   alertId;
    private Long   instanceId;
    private String currentStatus;
    private List<String> details;
    private String alertCreatedBy;
    private String assignedTo;
    private String createdBy;

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(long alertId) {
        this.alertId = alertId;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }


    public String getAlertCreatedBy() {
        return alertCreatedBy;
    }

    public void setAlertCreatedBy(String alertCreatedBy) {
        this.alertCreatedBy = alertCreatedBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AlertNotificationDTO [alertId=");
        builder.append(alertId);
        builder.append(", instanceId=");
        builder.append(instanceId);
        builder.append(", currentStatus=");
        builder.append(currentStatus);
        builder.append(", details=");
        builder.append(details);
        builder.append(", alertCreatedBy=");
        builder.append(alertCreatedBy);
        builder.append(", assignedTo=");
        builder.append(assignedTo);
        builder.append(", createdBy=");
        builder.append(createdBy);
        builder.append("]");
        return builder.toString();
    }

}
