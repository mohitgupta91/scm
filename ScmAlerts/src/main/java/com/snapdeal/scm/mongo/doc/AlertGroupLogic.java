package com.snapdeal.scm.mongo.doc;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;

/**
 * AlertTypeGroupLogic: hold grouping logic for ui to be displayed for earch alert type
 * 
 * @author pranav
 */
@Document(collection = "alert_group_logic")
public class AlertGroupLogic extends MongoDocument {

	@NotNull
    private Long alertId;
	private String  groupLogicName;
	private Boolean isFileType;
	
	public AlertGroupLogic(){}

	public AlertGroupLogic(Long alertId, Date createdOn, Date updatedOn,
			boolean isFileType, String groupLogicName) {
		this.setAlertId(alertId);
		super.setCreated(createdOn);
		super.setUpdated(updatedOn);
		this.groupLogicName=groupLogicName;
		this.setFileType(isFileType);
	}

	public String getGroupLogicName() {
		return groupLogicName;
	}

	public void setGroupLogicName(String groupLogicName) {
		this.groupLogicName = groupLogicName;
	}

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

	@Override
	public String toString() {
		return "AlertGroupLogic [alertId=" + alertId + ", groupLogicName="
				+ groupLogicName + ", isFileType=" + IsFileType() + "]";
	}

	public Boolean IsFileType() {
		return isFileType;
	}

	public void setFileType(Boolean isFileType) {
		this.isFileType = isFileType;
	}
}
