package com.snapdeal.scm.web;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.snapdeal.base.startup.config.AppEnvironmentContext;
import com.snapdeal.scm.web.core.mao.SecurityConfigRepository;

/**
 * SCM SSOSecurityConfig : Sacs Integration for sso
 * 
 * @author pranav,mohit
 *
 */
@ConditionalOnProperty(value = "scm.sso.enabled",havingValue="true")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled=true)
public class ScmWebAppSsoSecurityConfig {

	@ConditionalOnProperty(value = "scm.sso.enabled",havingValue="true")
	@Configuration
	@Order(1)
	@ImportResource("classpath:/security/spring_saml_sso_security.xml")
	public static class ScmWebSSOSecurityConfigStatic extends WebSecurityConfigurerAdapter {


		@Autowired
		@Qualifier(value="ssoAuthProvider")
		AuthenticationProvider authProvider;

		@Autowired
		MetadataGeneratorFilter metadataGeneratorFilter;

		@Autowired
		FilterChainProxy samlFilter;

		@Autowired
		SAMLEntryPoint samlEntryPoint;

		@Autowired
		private SecurityConfigRepository securityConfigMao;

		@Value("${sacs.webservice.scm.id}")
		private String appId;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			.httpBasic()
			.authenticationEntryPoint(samlEntryPoint);
			http
			//.addFilterBefore(metadataGeneratorFilter, ChannelProcessingFilter.class)
			.addFilterAfter(samlFilter, BasicAuthenticationFilter.class);
			http.requiresChannel()
			.anyRequest().requiresSecure();
			http.csrf().disable();
			
			ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authRegistry = http.authorizeRequests()
					.antMatchers("/favicon.ico").permitAll()
					.antMatchers("/templates/**").permitAll()
					.antMatchers("/admin/**").permitAll()
					.antMatchers("/css/**").permitAll()
					.antMatchers("/data/**").permitAll()
					.antMatchers("/imgs/**").permitAll()
					.antMatchers("/js/**").permitAll()
					.antMatchers("/saml/metadata").permitAll()
					.antMatchers("/error").permitAll()
					.antMatchers("/reloadCache").permitAll()
					.antMatchers("/static/**").permitAll();

			authRegistry.anyRequest().fullyAuthenticated();
			
			http.exceptionHandling().authenticationEntryPoint(delegatingAuthenticationEntryPoint());
		}

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
			ProviderManager pm=(ProviderManager)authenticationManager();
			pm.setEraseCredentialsAfterAuthentication(false);
			authenticationManagerBuilder=authenticationManagerBuilder.eraseCredentials(false);
			authenticationManagerBuilder.authenticationProvider(authProvider);
		}

		@Bean
		public AppEnvironmentContext appEnvironmentContext() {
			return new AppEnvironmentContext(appId);
		}
		
		public DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint() {
			DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint = new DelegatingAuthenticationEntryPoint(map());
			delegatingAuthenticationEntryPoint.setDefaultEntryPoint(samlEntryPoint);
			return delegatingAuthenticationEntryPoint;
		}

		public LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> map() {
			LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> map = new LinkedHashMap<>();
			map.put(ajaxRequestMatcher(), ajaxAuthenticationEntryPoint());
			return map;
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
}



