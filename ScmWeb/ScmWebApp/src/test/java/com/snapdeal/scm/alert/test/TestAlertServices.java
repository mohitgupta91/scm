//package com.snapdeal.scm.alert.test;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.net.URISyntaxException;
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.boot.test.WebIntegrationTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import com.snapdeal.scm.mongo.dao.AlertMongoRepository;
//import com.snapdeal.scm.mongo.dao.AlertTypeGroupLogicMongoRepository;
//import com.snapdeal.scm.mongo.dao.AlertTypeMongoRepository;
//import com.snapdeal.scm.web.ConfigurationScmWebApp;
//import com.snapdeal.scm.web.services.IAlertService;
///**
// * 
// * @author mohit
// *
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
//@WebIntegrationTest
//public class TestAlertServices {
//
//	@Autowired
//	AlertMongoRepository alertRepo;
//	
//	@Autowired
//	AlertTypeMongoRepository alertTypeRepo;
//	
//	@Autowired
//	AlertTypeGroupLogicMongoRepository alertGroupLogicRepo;
//	
//	@Autowired
//	IAlertService alertService; 
//	
//	private RestTemplate restTemplate = new RestTemplate();  
//	
//	@Test
//	public void testCreateAlert(){
//		
//	}
//	
//	@Before
//	public void setUp(){
//
//	}
//	
//	@SuppressWarnings("unchecked")
//	@Test
//	public void testViewAlert(){
//		Map<String,Object> response;
//		try {
//			response = restTemplate.getForObject("https://localhost:8443/alert-list.mvc",Map.class);
//			assertNotNull(response);
//		} catch (RestClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	
//	@Test
//	public void testEditAlert(){
//		
//	}
//	
//	@SuppressWarnings("unchecked")
//	@Test
//	public void testViewAlertTypes() throws RestClientException, URISyntaxException{
//		Map<String,Object> response;
//		response = restTemplate.getForObject("https://localhost:8443/data/alerts/types",Map.class);
//		System.out.println("here:"+response);
//		assertNotNull(response);
//		assertNotNull(response.get("data"));
//		assertEqual()
//		System.out.println(response);
//	}
//	
//	@Test
//	public void testViewAlertTypeGroupLogic(){
//		
//	}
//	
//}
