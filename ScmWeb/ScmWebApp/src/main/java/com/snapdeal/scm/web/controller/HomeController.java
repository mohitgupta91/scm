package com.snapdeal.scm.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.scm.cache.ICacheLoadService;
import com.snapdeal.scm.web.services.configuration.ConfigUtils;
import com.snapdeal.scm.web.services.configuration.Property;

/**
 * 
 * @author mohit
 * 
 */
@Controller
public class HomeController {

	private static final long SERVER_STARTUP_TS = System.currentTimeMillis();
	
	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
    ICacheLoadService  cacheLoadService;

	@RequestMapping("/")
	public String home(Model model) {
		Long version = ConfigUtils.getLongScalar(Property.VERSION);
		if (!(version > SERVER_STARTUP_TS))
			version = SERVER_STARTUP_TS;
	
		model.addAttribute("web_server_url", ConfigUtils.getStringScalar(Property.WEB_SERVER_STATIC_URL));
		model.addAttribute("version", version);
		model.addAttribute("max_page_range",  ConfigUtils.getIntegerScalar(Property.MAX_PAGE_RANGE_ALLOWED));
		model.addAttribute("max_date_range", ConfigUtils.getIntegerScalar(Property.MAX_DATE_RANGE_ALLOWED));
		return "home";
	}
	
	@RequestMapping(value = "/reloadCache", method = RequestMethod.GET )
	@ResponseBody
	public void reloadConfiguration() {
		try {
			cacheLoadService.loadAll(true);
		} catch (Exception e) {
			LOG.error("Error while cache reload.",e);
		}
	}
	
	@RequestMapping("/controltower/**")
	public String controlTowerHome(Model model)
	{
		Long version = ConfigUtils.getLongScalar(Property.VERSION);
		if (!(version > SERVER_STARTUP_TS))
			version = SERVER_STARTUP_TS;
	
		model.addAttribute("web_server_url", ConfigUtils.getStringScalar(Property.WEB_SERVER_STATIC_URL));
		model.addAttribute("version", version);
		model.addAttribute("max_page_range",  ConfigUtils.getIntegerScalar(Property.MAX_PAGE_RANGE_ALLOWED));
		model.addAttribute("max_date_range", ConfigUtils.getIntegerScalar(Property.MAX_DATE_RANGE_ALLOWED));
		return "home";
		
	}
}
