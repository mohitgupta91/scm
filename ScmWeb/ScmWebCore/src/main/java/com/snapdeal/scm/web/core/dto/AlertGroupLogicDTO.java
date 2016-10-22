package com.snapdeal.scm.web.core.dto;

import com.snapdeal.scm.mongo.doc.AlertGroupLogic;

public class AlertGroupLogicDTO {
	private String groupLogicName;
	private Boolean isFileType;

	public AlertGroupLogicDTO(){}
	
	public AlertGroupLogicDTO(AlertGroupLogic alertTypeGroupLogic){
		this.groupLogicName = alertTypeGroupLogic.getGroupLogicName();
		this.isFileType = alertTypeGroupLogic.IsFileType();
	}
	
	public Boolean getIsFileType() {
		return isFileType;
	}

	public void setIsFileType(Boolean isFileType) {
		this.isFileType = isFileType;
	}

	@Override
	public String toString() {
		return "AlertTypeGroupLogic [logicName=" + groupLogicName
				+ ", isFileType=" + isFileType + "]";
	}

	public String getGroupLogicName() {
		return groupLogicName;
	}

	public void setGroupLogicName(String groupLogicName) {
		this.groupLogicName = groupLogicName;
	}
}
