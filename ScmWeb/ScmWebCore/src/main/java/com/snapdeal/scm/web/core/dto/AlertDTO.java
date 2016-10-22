package com.snapdeal.scm.web.core.dto;

import com.snapdeal.scm.mongo.doc.Alert;

/**
 * AlertTypeDTO : Alert Type
 * 
 * @author pranav
 *
 */
public class AlertDTO {
	
	private Long alertId;
	private String title;
	
	public AlertDTO(){
		
	}
	
	public AlertDTO(Alert alertType){
		this.alertId=alertType.getAlertId();
		this.title = alertType.getTitle();
	}

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "AlertDTO [alertId=" + alertId + ", title=" + title + "]";
	}
}
