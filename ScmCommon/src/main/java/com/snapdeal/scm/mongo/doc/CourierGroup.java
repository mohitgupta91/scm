package com.snapdeal.scm.mongo.doc;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author prateek
 */
@Document(collection = "courier_group")
public class CourierGroup extends MongoDocument {

	private String courierCode;
	private String courierType;
	private String courierGroup;
	private Boolean integrated;

	public CourierGroup() {

	}

	public CourierGroup(String courierCode, String courierType, String courierGroup, Boolean integrated) {
		this.courierCode = courierCode;
		this.courierType = courierType;
		this.courierGroup = courierGroup;
		this.integrated = integrated;
	}

	public CourierGroup(String courierCode) {
		this.courierCode = courierCode;
	}

	public String getCourierCode() {
		return courierCode;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}

	public String getCourierType() {
		return courierType;
	}

	public void setCourierType(String courierType) {
		this.courierType = courierType;
	}

	public String getCourierGroup() {
		return courierGroup;
	}

	public void setCourierGroup(String courierGroup) {
		this.courierGroup = courierGroup;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	 @Override
	 public String toString() {
		return "CourierGroup [courierCode=" + courierCode + ", courierType="
				+ courierType + ", courierGroup=" + courierGroup
				+ ", integrated=" + integrated + "]";
	 }

	 /**
	  * @return the integrated
	  */
	 public Boolean getIntegrated() {
		 return integrated;
	 }

	 /**
	  * @param integrated the integrated to set
	  */
	 public void setIntegrated(Boolean integrated) {
		 this.integrated = integrated;
	 }
}
