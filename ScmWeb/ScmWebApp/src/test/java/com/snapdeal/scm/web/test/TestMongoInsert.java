package com.snapdeal.scm.web.test;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.web.ConfigurationScmWebApp;
import com.snapdeal.scm.web.core.mao.SecurityConfigRepository;
import com.snapdeal.scm.web.core.mongo.documents.SecurityPermission;

/**
 * TestMongoInsert : Mongo Insert
 * 
 * @author pranav
 *
 */
@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
@EnableMongoRepositories
@EnableMongoAuditing
public class TestMongoInsert {
	
	@Autowired
	private SecurityConfigRepository securityConfigMao;

	@Test
	public void saveSecurityPermissions(){
		SecurityPermission sp=  new SecurityPermission();
		sp.setCreated(new Date());
		sp.getRoleNames().add("admin");
		sp.setUrlPattern("/lastmile/shiptodeliver/avgo2dperformancedelivered");
		securityConfigMao.save(sp);
	}
	
	@Test
	public void testSecurityData(){
		Set<String> permissions= new HashSet<>();
		List<SecurityPermission> secPermissions = securityConfigMao.findAll();
		for (SecurityPermission entry : secPermissions) {
			permissions.add(entry.getUrlPattern());
		}
		System.out.println(permissions);
	}
}
