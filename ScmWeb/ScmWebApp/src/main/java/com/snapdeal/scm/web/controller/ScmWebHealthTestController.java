package com.snapdeal.scm.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.mongo.mao.repository.PincodeMasterRepository;
import com.snapdeal.scm.web.services.IElasticService;

/**
 * Health Test Controller
 * 
 * @author mohit
 *
 */
@Controller
public class ScmWebHealthTestController {

	private static final Logger logger = LoggerFactory.getLogger(ScmWebHealthTestController.class);
	
	@Autowired
	PincodeMasterRepository repository;
	
	@Autowired
	IElasticService elasticService;
	
	@RequestMapping(value="/health_test.mvc",produces="application/json")
	@ResponseBody
	public Object healthTest()
	{
		logger.info("Server Health Test");
		try{
			Long count = repository.count();
			ElasticDataDTO elasticData = elasticService.getBySuborderId("3510021900");
			if(count == 0)
				throw new Exception();
		}
		catch(Exception e){
			return "Service not available";	
			}		
		return "Server up and running";
	}
	

}
