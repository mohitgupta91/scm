package com.snapdeal.scm.processor.cache.impl;

import java.util.ArrayList;
import java.util.List;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.core.mongo.document.StatusCodes;
import com.snapdeal.scm.core.mongo.document.StatusCodes.StatusCodeType;
import com.snapdeal.scm.das.mongo.dao.StatusCodesRepository;

/**
 * 
 * @author prateek
 *
 */
@Cache(cacheKey= CacheKey.STATUS_CODES, MIN_REPEAT_TIME_IN_HOUR=24)
public class StatusCodesCache implements ICache{

	@Autowired
	StatusCodesRepository statusCodesRepository;

	List<String> shippingPackageStatusCodes = new ArrayList<String>();
	List<String> shippingSOIStatusCodes = new ArrayList<String>();
	List<String> trackingPackageStatusCodes = new ArrayList<String>();

	private void add(StatusCodeType statusCodeType, String statusCode) {
		if(statusCodeType.equals(StatusCodeType.SHIPPING_ORDER_ITEM)){
			addShippingSOIStatusCodes(statusCode);
		} else if(statusCodeType.equals(StatusCodeType.SHIPPING_PACKAGE)){
			addShippingPackageStatusCodes(statusCode);
		} else if(statusCodeType.equals(StatusCodeType.TRACKING_PACKAGE)){
			addTrackingPackageStatusCodes(statusCode);
		}
	}

	public List<String> getShippingPackageStatusCodes() {
		return shippingPackageStatusCodes;
	}

	public void addShippingPackageStatusCodes(String shippingPackageStatusCode) {
		shippingPackageStatusCodes.add(shippingPackageStatusCode);
	}

	public List<String> getShippingSOIStatusCodes() {
		return shippingSOIStatusCodes;
	}

	public void addShippingSOIStatusCodes(String shippingSOIStatusCode) {
		shippingSOIStatusCodes.add(shippingSOIStatusCode);
	}

	public List<String> getTrackingPackageStatusCodes() {
		return trackingPackageStatusCodes;
	}

	public void addTrackingPackageStatusCodes(String trackingPackageStatusCode) {
		trackingPackageStatusCodes.add(trackingPackageStatusCode);
	}

	@Override
	public void load() {
		for (StatusCodes statusCodes : statusCodesRepository.findAll()) {
			add(statusCodes.getStatusCodeType(), statusCodes.getStatusCode());
		}
	}

}
