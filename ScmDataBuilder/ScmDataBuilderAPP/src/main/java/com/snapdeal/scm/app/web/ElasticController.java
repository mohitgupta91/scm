package com.snapdeal.scm.app.web;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snapdeal.scm.processor.IElasticService;

/**
 * 
 * @author prateek
 *
 */
@RestController
public class ElasticController {

	private static final Logger LOG            = LoggerFactory.getLogger(ElasticController.class);
    public static final String  URL  = "/elasticscript/{script}";
    
    @Autowired
	private IElasticService elasticServicce;
    
    @RequestMapping(URL)
    public void getFilters(@NotNull @PathVariable(value = "script") String script) {
    	LOG.info("Executing elastic script : {}", script);
    	elasticServicce.runScript(null, script, null);
    }
}
