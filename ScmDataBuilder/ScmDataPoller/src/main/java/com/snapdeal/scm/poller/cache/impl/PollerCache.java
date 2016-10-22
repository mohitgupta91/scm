/**
 * 
 */
package com.snapdeal.scm.poller.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.poller.model.PollerProperty;
import com.snapdeal.scm.poller.model.PollarPropertyRepository;
import com.snapdeal.scm.poller.util.Constants;

/**
 * @author gaurav
 *
 */
@Cache(cacheKey= CacheKey.POLLER, MIN_REPEAT_TIME_IN_MINUTE=60)
public class PollerCache implements ICache{
	
	@Autowired
	private PollarPropertyRepository repository;
	
	Map<String, String> pollerProperties = new HashMap<>();
	
	List<PollerProperty> allPollarProperties = new ArrayList<PollerProperty>();
	
	/* (non-Javadoc)
	 * @see com.snapdeal.scm.poller.cache.impl.IPollerCache#getAPIURL()
	 */
	public String getAPIURL() {
		return pollerProperties.get(Constants.API_URL_STR) ;
	}
	
	public String getProperty(String key) {
		return pollerProperties.get(key);
	}
	
	/* (non-Javadoc)
	 * @see com.snapdeal.scm.poller.cache.impl.IPollerCache#load()
	 */
	@Override
	public void load() {
		List<PollerProperty> properties = repository.findAll();
		for (PollerProperty property : properties) {
			pollerProperties.put(property.getName(), property.getValue());
		}
	}
}
