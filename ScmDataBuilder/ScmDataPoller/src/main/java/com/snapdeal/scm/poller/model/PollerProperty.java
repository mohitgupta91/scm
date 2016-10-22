/**
 * 
 */
package com.snapdeal.scm.poller.model;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author gaurav
 *
 */
@Document(collection = "poller_property")
public class PollerProperty extends MongoDocument {
	
	String name;
	
	String value;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PollarProperty [name=" + name + ", value=" + value + "]";
	}
	
	

}
