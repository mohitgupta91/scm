package com.snapdeal.scm.web.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;

@ConditionalOnProperty(value = "scm.sso.enabled",havingValue="false")
@Configuration
public class InMemoryGlobalAuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

	@Value("${scm.inmemory.credentials}")
	private String credentials;
	
	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		String[] credentialsArray=credentials.split(",");
		
		UserDetailsManagerConfigurer<AuthenticationManagerBuilder, InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>>.UserDetailsBuilder inMemoryUser=null;
		for (String credential : credentialsArray) {
			String[] usernamePWdRole = credential.split("/");
			if(null==inMemoryUser)
				inMemoryUser = auth.inMemoryAuthentication().withUser(usernamePWdRole[0]).password(usernamePWdRole[1]).roles(usernamePWdRole[2]);
			else
				inMemoryUser=inMemoryUser.and().withUser(usernamePWdRole[0]).password(usernamePWdRole[1]).roles(usernamePWdRole[2]);
		}
	}
}

