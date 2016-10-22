package com.snapdeal.scm.app.web;

import com.snapdeal.scm.cache.ICacheLoadService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * 
 * @author bhatia.gaurav
 * 
 */
@Controller
public class DataHomeController {

	private static final Logger LOG = LoggerFactory.getLogger(DataHomeController.class);
	
	@Autowired
	ICacheLoadService cacheLoadService;

	@RequestMapping(value = "/reloadCache", method = RequestMethod.GET )
	@ResponseBody
	public String reloadConfiguration(@RequestParam(required=false) String message) {
		if (StringUtils.isBlank(message)) {
			message = "";
		} else {
			message += "\n";
		}
		long time = System.currentTimeMillis();
		try {
			cacheLoadService.loadAll(true);
			long timeNow = System.currentTimeMillis() - time;
			return message + "Successful cache reloaded in " + timeNow + "(millis)";
		} catch (Exception e) {
			LOG.error("Error while cache reload.", e);
			return message + "Reloading SCM cache : " + e.getMessage();
		}
	}
}
