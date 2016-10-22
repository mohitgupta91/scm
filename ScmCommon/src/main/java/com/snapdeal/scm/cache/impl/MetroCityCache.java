package com.snapdeal.scm.cache.impl;

import java.util.HashSet;
import java.util.Set;

import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.mongo.mao.repository.MetroCitiesRepository;
import com.snapdeal.scm.mongo.doc.MetroCity;

/**
 * 
 * @author prateek
 *
 */
@Cache(cacheKey= CacheKey.METRO_CITY, MIN_REPEAT_TIME_IN_HOUR=24)
public class MetroCityCache implements ICache {

	@Autowired
	private MetroCitiesRepository metroCitiesRepository;

	private Set<String> metroCities = new HashSet<>();

	private void addCity(MetroCity metroCity){
		if(metroCity.getType().equals(MetroCity.Type.METRO))
			metroCities.add(metroCity.getCity());
	}

	public boolean isMetroCity(String city){
		return metroCities.contains(city);
	}

	@Override
	public void load() {
		for(MetroCity metroCity : metroCitiesRepository.findAll()){
			addCity(metroCity);
		}
	}
}