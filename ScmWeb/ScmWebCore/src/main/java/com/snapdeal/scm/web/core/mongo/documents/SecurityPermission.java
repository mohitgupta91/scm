package com.snapdeal.scm.web.core.mongo.documents;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SecurityPermission : Hold Urls and Access Role
 * @author pranav
 *
 */
@Document(collection = "web_security_permission")
public class SecurityPermission {

	@Id
	private String id;
	private String  urlPattern;
	private List<String>  roleNames= new ArrayList<String>();
	@CreatedDate
	private Date    created;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrlPattern() {
		return urlPattern;
	}
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public List<String> getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}
	
	
}
