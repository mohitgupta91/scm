package com.snapdeal.scm.web;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.snapdeal.scm.web.core.mao.SecurityConfigRepository;

/**
 * ScmWebAppInMemorySecurityConfig : In Memory Security Config
 * 
 * @author pranav,mohit
 */
@ConditionalOnProperty(value = "scm.sso.enabled", havingValue = "false")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ScmWebAppInMemorySecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityConfigRepository securityConfigMao;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authRegistry = http.authorizeRequests()
				.antMatchers("/favicon.ico").permitAll()
				.antMatchers("/templates/**").permitAll()
				.antMatchers("/admin/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/data/**").permitAll()
				.antMatchers("/imgs/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/mvc/health-test.mvc").permitAll()
				.antMatchers("/static/**").permitAll()
				.antMatchers("/error").permitAll()
				.antMatchers("/login").permitAll();
			
		authRegistry.anyRequest().authenticated();
		authRegistry.and().formLogin().loginPage("/login")
		.failureUrl("/login?error").and().logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).and()
		.exceptionHandling().accessDeniedPage("/access?error");
		http.exceptionHandling().authenticationEntryPoint(delegatingAuthenticationEntryPoint());
	}

	public DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint() {
		DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint = new DelegatingAuthenticationEntryPoint(map());
		delegatingAuthenticationEntryPoint.setDefaultEntryPoint(loginUrlAuthenticationEntryPoint());
		return delegatingAuthenticationEntryPoint;
	}

	public LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> map() {
		LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> map = new LinkedHashMap<>();
		map.put(ajaxRequestMatcher(), ajaxAuthenticationEntryPoint());
		return map;
	}

	public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
		LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");
		return loginUrlAuthenticationEntryPoint;
	}

	public AjaxEntryPoint ajaxAuthenticationEntryPoint() {
		AjaxEntryPoint ajaxAuthenticationEntryPoint = new AjaxEntryPoint();
		return ajaxAuthenticationEntryPoint;
	}

	public AjaxRequestMatcher ajaxRequestMatcher() {
		AjaxRequestMatcher ajaxRequestMatcher = new AjaxRequestMatcher();
		return ajaxRequestMatcher;
	}
}