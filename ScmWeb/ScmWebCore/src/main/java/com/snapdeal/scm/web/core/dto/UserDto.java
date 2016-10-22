package com.snapdeal.scm.web.core.dto;

import java.util.Set;
import com.snapdeal.sacs.base.sro.v2.ResourcePermissionSRO;
/***
 * 
 * @author mohit
 *
 */
public class UserDto {

	private String username;
	
	private String email;
	
	private Set<ResourcePermissionSRO> permissions;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<ResourcePermissionSRO> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<ResourcePermissionSRO> permissions) {
		this.permissions = permissions;
	}
	
}
