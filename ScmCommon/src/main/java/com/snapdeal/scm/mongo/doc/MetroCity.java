package com.snapdeal.scm.mongo.doc;

import org.springframework.data.mongodb.core.mapping.Document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;

/**
 * 
 * @author prateek
 *
 */
@Document(collection = "metro_city")
public class MetroCity extends MongoDocument {
	
	private Type type;
	private String city;
	
	public MetroCity() {
	}

	public MetroCity(Type type, String city) {
		this.type = type;
		this.city = city;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "MetroCity [type=" + type + ", city=" + city + "]";
	}
	
	public enum Type {
		METRO;
	}
}
