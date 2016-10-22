package com.snapdeal.scm.web.dto;

import java.util.Set;
/***
 * 
 * @author mohit
 *
 */
public class UserResponse {

	private String name;
	private String email;
	private Set<String> permissions;

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
