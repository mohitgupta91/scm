package com.snapdeal.scm.web.auth;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Component;

import com.snapdeal.sacs.base.sro.v2.ResourcePermissionSRO;
import com.snapdeal.sacs.base.sro.v2.UserSRO;
import com.snapdeal.sacs.client.services.SACSClientService;
import com.snapdeal.sacs.client.sso.authentication.SSOSamlAuthenticationProvider;

@ConditionalOnProperty(value = "scm.sso.enabled",havingValue="true")
@Component("ssoAuthProvider")
public class ScmWebSSOAuthenticationProvider extends SSOSamlAuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(ScmWebSSOAuthenticationProvider.class);

	@Autowired
	private SACSClientService  sacsClientService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException{
		Authentication auth = null;
		try {
			SAMLCredential credential = generateCredential(authentication);
			UserSRO userSRO = getSSOUserSRO(credential);
			List<GrantedAuthority> authorities = null;
			authorities = new ArrayList<GrantedAuthority>();
			if(userSRO!=null){
				if (userSRO.getResourcePermissions() != null) {
					for (ResourcePermissionSRO resourceRole : userSRO.getResourcePermissions()) {
						if (resourceRole != null) {
							authorities.add(new SimpleGrantedAuthority(resourceRole.getName()));
						}
					}
				}
				auth = new UsernamePasswordAuthenticationToken(userSRO,credential,authorities);
			}else {
				throw new BadCredentialsException("User does not exist on SACS");
			}
		} catch (Exception e) {
			logger.info(e.getMessage(),e);
		}
		return auth;
	}
}