package com.snapdeal.scm.mongo.doc;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

/**
 * @author prateek
 */
@Document(collection = "scm_property")
public class ScmProperty extends MongoDocument {

	private String			 		name;
	private String 					stringValue;
	private List<String> 			listValue;
	private Map<String, String> 	mapValue;

	public ScmProperty() {
	}

	public ScmProperty(String name, String stringValue) {
		this.name = name;
		this.stringValue = stringValue;
	}
	
	public ScmProperty(String name, List<String> listValue) {
		this.name = name;
		this.listValue = listValue;
	}

	public ScmProperty(String name, Map<String, String> mapValue) {
		this.name = name;
		this.mapValue = mapValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public List<String> getListValue() {
		return listValue;
	}

	public void setListValue(List<String> listValue) {
		this.listValue = listValue;
	}

	public Map<String, String> getMapValue() {
		return mapValue;
	}

	public void setMapValue(Map<String, String> mapValue) {
		this.mapValue = mapValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
