package com.snapdeal.scm.cache.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.mongo.doc.PincodeMaster;
import com.snapdeal.scm.mongo.mao.repository.PincodeMasterRepository;


/**
 * 
 * @author prateek
 *
 */
@Cache(cacheKey= CacheKey.PINCODE_MASTER, MIN_REPEAT_TIME_IN_HOUR = 24)
public class PincodeMasterCache implements ICache {

	@Autowired
	PincodeMasterRepository pincodeMasterRepository;

	Map<String, PincodeDetail> pincodeDetails = new HashMap<>();
	List<String> listOfRegions = new ArrayList<String>();
	List<String> listOfTiers = new ArrayList<String>();
	Map<String, List<String>> mapOfCities = new HashMap<String, List<String>>();
	List<String> listOfStates = new ArrayList<String>();

	public void addPincodeDetail(String pincode, PincodeDetail pincodeDetail) {
		pincodeDetails.put(pincode, pincodeDetail);
	}

	public PincodeDetail getPincodeDetail(String pincode) {
		return pincodeDetails.get(pincode);
	}

	public List<String> getListOfRegions() {
		return listOfRegions;
	}

	public void setListOfRegions(List<String> listOfRegions) {
		this.listOfRegions = listOfRegions;
	}

	public List<String> getListOfTiers() {
		return listOfTiers;
	}

	public void setListOfTiers(List<String> listOfTiers) {
		this.listOfTiers = listOfTiers;
	}

	public Map<String, List<String>> getMapOfCities() {
		return mapOfCities;
	}

	public void setMapOfCities(Map<String, List<String>> mapOfCities) {
		this.mapOfCities = mapOfCities;
	}

	public List<String> getListOfStates() {
		return listOfStates;
	}

	public void setListOfStates(List<String> listOfStates) {
		this.listOfStates = listOfStates;
	}

	public static class PincodeDetail {
		String city;
		String state;
		String zone;
		String scCity;
		String scState;
		String scZone;
		String bdeZone;

		public PincodeDetail() {

		}

		public PincodeDetail(String city, String state, String zone,
				String scCity, String scState, String scZone, String bdeZone) {
			this.city = city;
			this.state = state;
			this.zone = zone;
			this.scCity = scCity;
			this.scState = scState;
			this.scZone = scZone;
			this.bdeZone = bdeZone;
		}


		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getScCity() {
			return scCity;
		}

		public void setScCity(String scCity) {
			this.scCity = scCity;
		}

		public String getScState() {
			return scState;
		}

		public void setScState(String scState) {
			this.scState = scState;
		}

		public String getScZone() {
			return scZone;
		}

		public void setScZone(String scZone) {
			this.scZone = scZone;
		}

		public String getBdeZone() {
			return bdeZone;
		}

		public void setBdeZone(String bdeZone) {
			this.bdeZone = bdeZone;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getZone() {
			return zone;
		}

		public void setZone(String zone) {
			this.zone = zone;
		}
	}

	@Override
	public void load() {
		Set<String> regionsSet = new HashSet<String>();
		Set<String> tiersSet = new HashSet<String>();
		Set<String> citiesSet = new HashSet<String>();
		Set<String> statesSet = new HashSet<String>();
		for (PincodeMaster pincodeMaster : pincodeMasterRepository.findAll()) {
			String city = pincodeMaster.getCity();
			String state = pincodeMaster.getState();
			String zone = pincodeMaster.getZone();
			String bdeZone = pincodeMaster.getBdeZone();
			addPincodeDetail(pincodeMaster.getPincode(), new PincodeDetail
					(city, state, zone, pincodeMaster.getScCity(), pincodeMaster.getScState(),
							pincodeMaster.getScZone(), bdeZone));
			if(regionsSet.add(bdeZone))
				listOfRegions.add(bdeZone);
			if(tiersSet.add(zone))
				listOfTiers.add(zone);
			if(citiesSet.add(city)){
				List<String> cities = mapOfCities.get(state);
				if(cities == null) {
					cities = new ArrayList<String>();
					mapOfCities.put(state, cities);
				}
				cities.add(city);
			}
			if(statesSet.add(state))
				listOfStates.add(state);
		}
		Collections.sort(listOfStates);
		Collections.sort(listOfRegions);
		Collections.sort(listOfTiers);
		mapOfCities.forEach((state, cities) -> {
			Collections.sort(cities); 
		});
		
	}
}