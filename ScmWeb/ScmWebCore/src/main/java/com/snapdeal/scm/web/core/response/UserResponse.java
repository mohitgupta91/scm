package com.snapdeal.scm.web.core.response;

import java.util.HashSet;
import java.util.Set;

/***
 * 
 * @author mohit
 *
 */
@SuppressWarnings("rawtypes")
public class UserResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String email;
	private Set<String> permissions = new HashSet<String>();

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
	
	
}
