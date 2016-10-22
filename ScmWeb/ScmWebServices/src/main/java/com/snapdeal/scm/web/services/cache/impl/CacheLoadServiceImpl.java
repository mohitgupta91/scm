package com.snapdeal.scm.web.services.cache.impl;

import com.snapdeal.scm.cache.AbstractCacheLoaderService;
import com.snapdeal.scm.cache.CacheGroup;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.cache.impl.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author prateek
 */
public class CacheLoadServiceImpl extends AbstractCacheLoaderService {

    @Override
    public void loadAll(boolean forceFully) {
        loadCacheByCacheGroup(forceFully, CacheGroup.WEB_ALL);
    }

    @Override
    public List<Class<? extends ICache>> getCachesToLoad() {
        List<Class<? extends ICache>> cacheClasses = new ArrayList<>();
        cacheClasses.add(ScmPropertyCache.class);
        cacheClasses.add(PincodeMasterCache.class);
        cacheClasses.add(CourierGroupCache.class);
        cacheClasses.add(LaneGroupCache.class);
        cacheClasses.add(MetroCityCache.class);
        cacheClasses.add(RolePermissionsCache.class);
        cacheClasses.add(ShippedNotConMetricsCache.class);
        cacheClasses.add(ConNotReachedMetricsCache.class);
        cacheClasses.add(ReachedNotOFDMetricsCache.class);
        cacheClasses.add(OfdNotAttemptMetricsCache.class);
        cacheClasses.add(AttemptNotDelMetricsCache.class);
        cacheClasses.add(AttemptNotDelNonIntgrMetricsCache.class);
        cacheClasses.add(ShipNotAttemptNonIntgrMetricsCache.class);
        cacheClasses.add(UD1NotUD2MetricsCache.class);
        cacheClasses.add(UD2NotUD3MetricsCache.class);
        cacheClasses.add(UD3NotUD4MetricsCache.class);
        cacheClasses.add(UD4NotDelMetricsCache.class);
        cacheClasses.add(SuperCategoryCache.class);
        cacheClasses.add(FulfillmentProviderCache.class);

        return cacheClasses;
    }
}