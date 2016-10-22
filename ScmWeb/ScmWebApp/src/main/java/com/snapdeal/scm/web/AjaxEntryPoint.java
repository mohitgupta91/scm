package com.snapdeal.scm.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * SCMInMemoryEntryPoint : In Memory Authentication for XHR
 * @author pranav
 *
 */
public class AjaxEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
					throws IOException, ServletException {
		if (authException!=null) {
			response.getWriter().write("Not Authenticated !");
			response.setStatus(401);
			return;
		} 
	}
	

}