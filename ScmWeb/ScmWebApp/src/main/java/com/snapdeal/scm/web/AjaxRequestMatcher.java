package com.snapdeal.scm.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Ajax Request Matcher
 * @author pranav
 *
 */
public class AjaxRequestMatcher implements RequestMatcher{

	@Override
	public boolean matches(HttpServletRequest request) {
		String ajaxRequest = ((HttpServletRequest) request).getHeader("X-Requested-With");
		if ("XMLHttpRequest".equals(ajaxRequest)) {
			return true;
		}
		return false;
	}
}
