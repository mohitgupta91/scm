package com.snapdeal.scm.common.domain.mongo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * Base class of all mongo document
 * 
 * @author pranav
 *
 */
public class MongoDocument {

	@Id
    private String            id;
    @CreatedDate
    private Date              created;
    @LastModifiedDate
    private Date              updated;
    
    public MongoDocument() {
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	

}
