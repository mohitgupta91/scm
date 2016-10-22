package com.snapdeal.scm.processor.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.Mongo;
import com.snapdeal.scm.SCMConfiguration;
import com.snapdeal.scm.mongo.mao.repository.SupcDetailsRepository;

@SpringApplicationConfiguration(SCMConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestMongoConfiguration {
	
	@Autowired
	private Mongo op;
	
	@Autowired
	private SupcDetailsRepository supcRepo;
	
	@SuppressWarnings("deprecation")
	@Test
	public void test(){
		System.out.println(op.getMongoOptions());
		System.out.println(op.getConnector().getConnectPoint());
		System.out.println(supcRepo.findBySupc("123455"));
		System.out.println(supcRepo.findDistinctSuperCategory());
	}
}
