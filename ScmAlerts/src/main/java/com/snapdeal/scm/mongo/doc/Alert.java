package com.snapdeal.scm.mongo.doc;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;

/**
 * AlertType : keep entry for all alert type
 * 
 * @author pranav
 */
@Document(collection = "alert")
public class Alert extends MongoDocument {

	@NotNull
    private Long alertId;
    private String title;
    private String implClass;

    public Alert(long seqId) {
    	this.setAlertId(seqId);
	}
    
    public Alert() {
	}

    public String getImplClass() {
        return implClass;
    }

    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Alert [alerId=" + getAlertId() + ", title=" + title + ", implClass="
				+ implClass + "]";
	}

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}
	
}
