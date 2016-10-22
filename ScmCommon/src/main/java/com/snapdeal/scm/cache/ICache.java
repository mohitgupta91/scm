package com.snapdeal.scm.cache;

/**
 * Every Cache class should implement this interface and
 * add entry of its .class in {@link CacheLoadServiceImpl} based on its cacheGroup
 * 
 * <p> For e.g.
 * <p> for {@link CacheGroup#PROCESSOR} add entry in getProcessorGroupCacheToLoad method
 * <p> for {@link CacheGroup#POLL} add entry in getPollerGroupCacheToLoad method
 * <p>
 * <pre> {@code
 *  private List<Class<? extends ICache>> addProcessorGroupCacheToLoad() {
 *  	List<Class<? extends ICache>> cacheClasses = new ArrayList<>();
 *  	// Processor Group Cache
 *  	cacheClasses.add(CenterMasterCache.class);
 *  	cacheClasses.add(CourierGroupCache.class);
 *  	cacheClasses.add(PincodeMasterCache.class);
 *  	cacheClasses.add(StatusCodesCache.class);
 *  	return cacheClasses;
 *  }
 *	<pre>
 *
 * @author prateek
 *
 */
public interface ICache {

	/**
	 * contain logic to build cache
	 */
	void load();
	
}