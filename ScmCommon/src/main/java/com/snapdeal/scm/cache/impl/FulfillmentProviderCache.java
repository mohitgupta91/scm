package com.snapdeal.scm.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.mongo.doc.FulfillmentProvider;
import com.snapdeal.scm.mongo.doc.FulfillmentProvider.FulfillmentType;
import com.snapdeal.scm.mongo.mao.repository.FulfillmentProviderRepository;

/**
 * 
 * @author prateek
 *
 */
@Cache(cacheKey= CacheKey.FULFILLMENT_PROVIDER, MIN_REPEAT_TIME_IN_HOUR=24)
public class FulfillmentProviderCache implements ICache{

	@Autowired
	private FulfillmentProviderRepository fulfillmentProviderRepository;
	
	private Map<String, String> centerCodeToPincodeMapping = new HashMap<String, String>();
	private Map<String, String> vendorCodeToPincodeMapping = new HashMap<String, String>();

	private void add(FulfillmentType  centerType, String code, String pincode){
		if(centerType.equals(FulfillmentType.CENTER))
			addCenterCodeToPincodeMapping(code, pincode);
		else if(centerType.equals(FulfillmentType.VENDOR)){
			addVendorCodeToPincodeMapping(code, pincode);
		}
	}
	
	public String getCenterCodeToPincodeMapping(String centerCode) {
		return centerCodeToPincodeMapping.get(centerCode);
	}
	
	public void addCenterCodeToPincodeMapping(String fpCode, String pincode) {
		centerCodeToPincodeMapping.put(fpCode, pincode);
	}

	public String getVendorCodeToPincodeMapping(String vendorCode) {
		return vendorCodeToPincodeMapping.get(vendorCode);
	}
	
	public void addVendorCodeToPincodeMapping(String vendorCode, String pincode) {
		vendorCodeToPincodeMapping.put(vendorCode, pincode);
	}

	public List<String> getAllFulfillmentCenter(){
		return new ArrayList<>(centerCodeToPincodeMapping.keySet());
	}

	@Override
	public void load() {
		for(FulfillmentProvider centerMaster : fulfillmentProviderRepository.findAll()) {
			add(centerMaster.getFulfillmentType(), centerMaster.getCode(), centerMaster.getPincode());
		}
	}
}