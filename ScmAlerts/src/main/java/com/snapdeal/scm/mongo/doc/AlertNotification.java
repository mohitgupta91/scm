/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.mongo.doc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;

/**
 * @version 1.0, 13-Apr-2016
 * @author ashwini
 */
@Document(collection = "alert_notification")
public class AlertNotification extends MongoDocument {

	public enum NotificationStatus {
        ASSIGNED, REVIEWED, SUBMITTED
    }

    private Long                       notificationId;
    private Long                       alertId;
    private Long                       instanceId;
    private String                     currentStatus;
    private List<String>               details = new ArrayList<String>();
    private String                     alertCreatedBy;
    private String                     assignedTo;
    private List<NotificationActivity> notificationActivity = new ArrayList<NotificationActivity>();

    public AlertNotification() {
        this.setCreated(new Date());
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

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

    public void setCurrentStatus(NotificationStatus currentStatus) {
        this.currentStatus = currentStatus.name();
    }

    public List<String> getDetails() {
  		return details;
  	}

  	public void setDetails(List<String> details) {
  		this.details = details;
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

    public List<NotificationActivity> getNotificationActivity() {
        return notificationActivity;
    }

    public void setNotificationActivity(List<NotificationActivity> notificationActivity) {
        this.notificationActivity = notificationActivity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alertCreatedBy == null) ? 0 : alertCreatedBy.hashCode());
        result = prime * result + ((alertId == null) ? 0 : alertId.hashCode());
        result = prime * result + ((assignedTo == null) ? 0 : assignedTo.hashCode());
        result = prime * result + ((currentStatus == null) ? 0 : currentStatus.hashCode());
        result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
        result = prime * result + ((notificationId == null) ? 0 : notificationId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AlertNotification other = (AlertNotification) obj;
        if (alertCreatedBy == null) {
            if (other.alertCreatedBy != null)
                return false;
        } else if (!alertCreatedBy.equals(other.alertCreatedBy))
            return false;
        if (alertId == null) {
            if (other.alertId != null)
                return false;
        } else if (!alertId.equals(other.alertId))
            return false;
        if (assignedTo == null) {
            if (other.assignedTo != null)
                return false;
        } else if (!assignedTo.equals(other.assignedTo))
            return false;
        if (currentStatus == null) {
            if (other.currentStatus != null)
                return false;
        } else if (!currentStatus.equals(other.currentStatus))
            return false;
        if (instanceId == null) {
            if (other.instanceId != null)
                return false;
        } else if (!instanceId.equals(other.instanceId))
            return false;
        if (notificationId == null) {
            if (other.notificationId != null)
                return false;
        } else if (!notificationId.equals(other.notificationId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AlertNotification [notificationId=");
        builder.append(notificationId);
        builder.append(", alertId=");
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
        builder.append(", notificationActivity=");
        builder.append(notificationActivity);
        builder.append("]");
        return builder.toString();
    }

}
