package com.snapdeal.scm.processor.cache.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.core.configuration.ConfigUtils;
import com.snapdeal.scm.core.configuration.Property;
import com.snapdeal.scm.mongo.doc.SupcDetails;
import com.snapdeal.scm.mongo.mao.repository.SupcDetailsRepository;

/**
 * 
 * @author prateek
 *
 */
@Cache(cacheKey = CacheKey.SUPC_CACHE, cacheReload = false)
public class SupcCache implements ICache{

	@Autowired
	private SupcDetailsRepository supcDetailsRepository;

//	private Map<String, SupcDetails> supcDetailsMap = new LinkedHashMap<>();
	private LoadingCache<String, SupcDetails> supcDetailsGuavaCache;

//	public void addSupcDetails(SupcDetails supcDetail) {
//		supcDetailsMap.put(supcDetail.getSupc(), supcDetail);
//	}
//
//	public void addAllSupcDetails(List<SupcDetails> supcDetails) {
//		supcDetails.forEach(supcDetail -> addSupcDetails(supcDetail));
//	}
//
//	public SupcDetails getSupcDetails(String supc){
//		SupcDetails supcDetails = supcDetailsMap.get(supc);
//		if(Objects.isNull(supcDetails)){
//			supcDetails = findSupcDetails(supc);
//			addSupcDetails(supcDetails);
//		}
//		return supcDetails;
//	}


	private SupcDetails findSupcDetails(String supc) {
		return supcDetailsRepository.findBySupc(supc);
	}

	public SupcDetails getSupcDetailsFromGuavacache(String supc){
		SupcDetails supcDetails = null;
		try {
			supcDetails = supcDetailsGuavaCache.get(supc);
		} catch (ExecutionException e) {
			supcDetails = findSupcDetails(supc);
		}
		return supcDetails;
	}
	
	public void addAllSupcDetailsInGuavaCache(List<SupcDetails> supcDetails) {
		supcDetails.forEach((supcDetail) -> supcDetailsGuavaCache.put(supcDetail.getSupc(), supcDetail));
	}

	@Override
	public void load() {
		int ttl = ConfigUtils.getIntegerScalar(Property.SUPC_CACHE_TTL_SECOND);
		int cacheSize = ConfigUtils.getIntegerScalar(Property.SUPC_CACHE_SIZE);
		supcDetailsGuavaCache = CacheBuilder.newBuilder().maximumSize(cacheSize)
				.expireAfterWrite(ttl, TimeUnit.SECONDS).build(new CacheLoader<String, SupcDetails>() {
					@Override
					public SupcDetails load(String supc) throws Exception {
						return findSupcDetails(supc);
					}
				});
	}
}
