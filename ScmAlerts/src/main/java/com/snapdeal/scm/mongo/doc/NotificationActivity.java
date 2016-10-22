/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.mongo.doc;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.util.StringUtils;

import com.snapdeal.scm.alerts.utils.NotificationActivitySerializer;
import com.snapdeal.scm.mongo.doc.AlertNotification.NotificationStatus;

/**
 * @version 1.0, 14-Apr-2016
 * @author ashwini
 */
@JsonSerialize(using = NotificationActivitySerializer.class)
public class NotificationActivity {

    private NotificationStatus status;
    private String             comment;
    private String             updatedBy;
    private Date               updatedOn;

    public NotificationActivity() {
    }

    public NotificationActivity(NotificationStatus status, String comment, String updatedBy, Date updatedOn) {
        this.status = status;
        this.comment = comment;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
    }

    public NotificationActivity(String status, String comment, String updatedBy, Date updatedOn) {
        this.status = StringUtils.isEmpty(status) ? null : NotificationStatus.valueOf(status);
        this.comment = comment;
        this.updatedBy = updatedBy;
        this.updatedOn = updatedOn;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
        result = prime * result + ((updatedOn == null) ? 0 : updatedOn.hashCode());
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
        NotificationActivity other = (NotificationActivity) obj;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (updatedBy == null) {
            if (other.updatedBy != null)
                return false;
        } else if (!updatedBy.equals(other.updatedBy))
            return false;
        if (updatedOn == null) {
            if (other.updatedOn != null)
                return false;
        } else if (!updatedOn.equals(other.updatedOn))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NotificationActivity [status=");
        builder.append(status);
        builder.append(", comment=");
        builder.append(comment);
        builder.append(", updatedBy=");
        builder.append(updatedBy);
        builder.append(", updatedOn=");
        builder.append(updatedOn);
        builder.append("]");
        return builder.toString();
    }

}
