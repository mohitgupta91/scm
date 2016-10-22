package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.mongo.mao.repository.CourierGroupRepository;
import com.snapdeal.scm.mongo.doc.CourierGroup;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @author prateek
 *
 */
@Cache(cacheKey= CacheKey.COURIER_GROUP, MIN_REPEAT_TIME_IN_HOUR=24)
public class CourierGroupCache implements ICache {

	@Autowired
	CourierGroupRepository courierGroupRepository;
	
	Map<String, CourierDetail> courierCodeToDetail = new HashMap<>();
	List<String> listOfCourierGroup = new ArrayList<>();
	List<String> listOfCourierType = new ArrayList<>();
	List<String> listOfIntegrated = new ArrayList<>();
	

	/**
	 * @return the listOfIntegrated
	 */
	public List<String> getListOfIntegrated() {
		return listOfIntegrated;
	}

	/**
	 * @param listOfIntegrated the listOfIntegrated to set
	 */
	public void setListOfIntegrated(List<String> listOfIntegrated) {
		this.listOfIntegrated = listOfIntegrated;
	}

	public CourierDetail getCourierCodeToDetail(String courierCode) {
		return courierCodeToDetail.get(courierCode);
	}

	public void addCourierCodeToDetail(String courierCode, CourierDetail courierDetail) {
		courierCodeToDetail.put(courierCode, courierDetail);
	}

	public List<String> getAllCourierCode(){
		return new ArrayList<>(courierCodeToDetail.keySet());
	}

	public List<String> getListOfCourierGroup() {
		return listOfCourierGroup;
	}

	public void setListOfCourierGroup(List<String> listOfCourierGroup) {
		this.listOfCourierGroup = listOfCourierGroup;
	}

	public List<String> getListOfCourierType() {
		return listOfCourierType;
	}

	public void setListOfCourierType(List<String> listOfCourierType) {
		this.listOfCourierType = listOfCourierType;
	}

	public static class CourierDetail {

		String courierType;
		String courierGroup;
		Boolean integrated;

		/**
		 * @return the integrated
		 */
		public Boolean getIntegrated() {
			return integrated;
		}

		/**
		 * @param integrated the integrated to set
		 */
		public void setIntegrated(Boolean integrated) {
			this.integrated = integrated;
		}

		public CourierDetail() {

		}

		public CourierDetail(String courierType, String courierGroup, Boolean integrated) {
			super();
			this.courierType = courierType;
			this.courierGroup = courierGroup;
			this.integrated = integrated;
		}

		public String getCourierType() {
			return courierType;
		}

		public void setCourierType(String courierType) {
			this.courierType = courierType;
		}

		public String getCourierGroup() {
			return courierGroup;
		}

		public void setCourierGroup(String courierGroup) {
			this.courierGroup = courierGroup;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CourierDetail [courierType=" + courierType
					+ ", courierGroup=" + courierGroup + ", integrated="
					+ integrated + "]";
		}

	}

	@Override
	public void load() {
		Set<String> courierGroupSet = new HashSet<String>();
		Set<String> courierTypeSet = new HashSet<String>();

		for (CourierGroup courierGroup : courierGroupRepository.findAll()) {

			String couriergrp = courierGroup.getCourierGroup();
			String courierType = courierGroup.getCourierType();
			Boolean integrated = courierGroup.getIntegrated();
			String courierCode = courierGroup.getCourierCode();

			addCourierCodeToDetail(courierGroup.getCourierCode(),
					new CourierDetail(courierType, couriergrp, integrated));
			if(courierGroupSet.add(couriergrp))
				listOfCourierGroup.add(couriergrp);
			if(courierTypeSet.add(courierType))
				listOfCourierType.add(courierType);
			
			if (Boolean.TRUE.equals(integrated)){
				listOfIntegrated.add(courierCode);
			}
		}
	}
}
