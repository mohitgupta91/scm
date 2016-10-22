package com.snapdeal.scm.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.snapdeal.base.utils.CollectionUtils;
import com.snapdeal.sacs.base.sro.v2.PermissionSRO;
import com.snapdeal.sacs.base.sro.v2.ResourcePermissionSRO;
import com.snapdeal.sacs.base.sro.v2.UserSRO;
import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.web.core.mao.SecurityConfigRepository;
import com.snapdeal.scm.web.services.cache.impl.RolePermissionsCache;
/**
 * Interceptor for handling all api calls
 * @author mohit
 *
 *
 */
public class ScmWebAppAPIInterceptor implements HandlerInterceptor{

	@Value("{scm.sso.enabled}")
	Boolean ssoEnabled; 
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String url = request.getContextPath();
		Object currentUser = getLoggedUser();
		
		if(currentUser != null && ssoEnabled){
			if(ssoEnabled){
				 UserSRO user = (UserSRO) currentUser;
				 Set<String> permissions = getAllPermissionsForRoles(user);
				 for(String path : permissions){
					 if(path.equals(url) || url.contains(path) || path.contains(url)){
						 return true;
					 }
				 }
			}
			else {
				return true;
			}
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}
		else{
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
	private Object getLoggedUser(){
		 Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        if (principal instanceof UserSRO) {
	            UserSRO user = (UserSRO) principal;
	            return user;
	        } else if (principal instanceof UserDetails) {
	            UserDetails user = (UserDetails) principal;
	            return user;
	        }
	        return null;
	}

	private static Set<String> getAllPermissionsForRoles(UserSRO user) {

		List<String> roles = new ArrayList<>();
		if (!CollectionUtils.isEmpty(user.getResourcePermissions())) {
			for (ResourcePermissionSRO sro : user.getResourcePermissions()) {
				if (!CollectionUtils.isEmpty(sro.getPermissions())) {
					for (PermissionSRO permissionSRO : sro.getPermissions()) {
						roles.add(permissionSRO.getName());
					}
				}
			}
			}
			if (CollectionUtils.isEmpty(roles)) {
				return Collections.emptySet();
			}
			Set<String> permissions = new HashSet<>();
			Map<String, List<String>> map = CacheManager.getInstance().getCache(RolePermissionsCache.class)
					.getRolePermissionsMap();

			for (String role : roles) {
				List<String> list = map.get(role);
				if (!CollectionUtils.isEmpty(list)) {
					permissions.addAll(list);
				}
			}
			return permissions;
		}
}
