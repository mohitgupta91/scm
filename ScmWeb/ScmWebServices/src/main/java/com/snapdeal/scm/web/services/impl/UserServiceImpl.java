package com.snapdeal.scm.web.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.snapdeal.base.utils.CollectionUtils;
import com.snapdeal.sacs.base.sro.v2.PermissionSRO;
import com.snapdeal.sacs.base.sro.v2.ResourcePermissionSRO;
import com.snapdeal.sacs.base.sro.v2.UserSRO;
import com.snapdeal.scm.web.core.das.IRolePermissionsDAS;
import com.snapdeal.scm.web.core.mao.SecurityConfigRepository;
import com.snapdeal.scm.web.core.mongo.documents.SecurityPermission;
import com.snapdeal.scm.web.core.response.UserResponse;
import com.snapdeal.scm.web.services.IUserService;
import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.web.services.cache.impl.RolePermissionsCache;

/***
 * @author mohit
 */

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	IRolePermissionsDAS rolePermissionsDAS;

	@Autowired
	private SecurityConfigRepository securityConfigMao;

	public UserResponse getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext()
        		.getAuthentication();
        Object principal = auth.getPrincipal();
        UserResponse user = new UserResponse();
        
        if (principal instanceof UserSRO) {
        	UserSRO userDetails = (UserSRO) principal;
        	user.setEmail(userDetails.getEmail());
        	user.setName(userDetails.getName());
        
        	if(!CollectionUtils.isEmpty(userDetails.getResourcePermissions())){
        		List<String> roles = new ArrayList<>();
        		for(ResourcePermissionSRO sro : userDetails.getResourcePermissions()){
        				if(!CollectionUtils.isEmpty(sro.getPermissions())){
							for(PermissionSRO permissionSRO : sro.getPermissions()){
								roles.add(permissionSRO.getName());
							}
						}
        		}
        		user.setPermissions(getAllPermissionsForRoles(roles));
        	}
        	user.setCode(HttpStatus.OK.value());
        	        
        } else if (principal instanceof UserDetails) {
        	UserDetails userDetails = (UserDetails) principal;
        	user.setName(userDetails.getUsername());
        	user.setCode(HttpStatus.OK.value());
        }
        return user;
    }

	@Override
	public UserResponse giveTestPermissions() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		Object principal = auth.getPrincipal();
		UserResponse user = new UserResponse();
		if (principal instanceof UserSRO) {
			UserSRO userDetails = (UserSRO) principal;
			user.setEmail(userDetails.getEmail());
			user.setName(userDetails.getName());
		} else if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) principal;
			user.setName(userDetails.getUsername());
			user.setEmail(userDetails.getUsername());
		}
		List<SecurityPermission> secPermissions = securityConfigMao.findAll();
		for (SecurityPermission entry : secPermissions) {
			user.getPermissions().add(entry.getUrlPattern());
		}
		return user;
	}

	private static Set<String> getAllPermissionsForRoles(List<String> roles) {

        if(CollectionUtils.isEmpty(roles)){
            return Collections.emptySet();
        }
        Set<String> permissions = new HashSet<>();
        Map<String,List<String>> map = CacheManager.getInstance().getCache(RolePermissionsCache.class).getRolePermissionsMap();
		
        for(String role : roles){
            List<String> list = map.get(role);
            if(!CollectionUtils.isEmpty(list)){
                permissions.addAll(list);
                }
            }
        return permissions;
    }
}
