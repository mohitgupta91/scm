package com.snapdeal.scm.web.services.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.web.core.mao.RolePermissionsRepository;
import com.snapdeal.scm.web.core.mongo.documents.RolePermissions;

@Cache(cacheKey= CacheKey.ROLE_PERMISSIONS, MIN_REPEAT_TIME_IN_HOUR=24)
public class RolePermissionsCache implements ICache {

	@Autowired
	RolePermissionsRepository rolePermissionRepository;
	
	Map<String,List<String>> rolePermissionsMap = new HashMap<String,List<String>>();
	
	public void load() {
		for(RolePermissions rolePermissions : rolePermissionRepository.findAll()) {
            if(rolePermissions.getRole() != null && rolePermissions.getPermissions() != null) {
                addRolePermissions(rolePermissions.getRole(),rolePermissions.getPermissions());
            }
        }
	}

	 private void addRolePermissions(String role, List<String> permissions) {
	    	List<String> list = new ArrayList<String>();
	    	list.addAll(permissions);
	    	rolePermissionsMap.put(role, list);
	    }

	public Map<String, List<String>> getRolePermissionsMap() {
		return rolePermissionsMap;
	}

	public void setRolePermissionsMap(Map<String, List<String>> rolePermissionsMap) {
		this.rolePermissionsMap = rolePermissionsMap;
	}
}
