package com.snapdeal.scm.cache;

/**
 * 
 * @author prateek
 *
 */
public interface ICacheLoadService {

	void loadAll(boolean forceFully);
	
	void loadCacheByCacheGroup(boolean forceFully, CacheGroup cacheGroup);
	
	void loadCacheByCacheName(boolean forceFully, String cacheName);

}
