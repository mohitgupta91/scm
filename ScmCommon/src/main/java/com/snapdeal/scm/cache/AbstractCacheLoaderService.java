package com.snapdeal.scm.cache;

import com.snapdeal.scm.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author chitransh
 */
public abstract class AbstractCacheLoaderService implements ICacheLoadService {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    IAutoCacheReloadService autoCacheReloadService;

    protected Map<String, Date> cacheNameToLastReloadTime                   = new HashMap<>();
    protected Map<String, Class<? extends ICache>> cacheNameToCacheClass    = new LinkedHashMap<>();
    protected Map<CacheGroup, List<String>> cacheGroupToCacheNames          = new HashMap<>();
    protected boolean isInitialCacheLoadDone                                = false;
    private static final Logger LOG                                         = LoggerFactory.getLogger(AbstractCacheLoaderService.class);

    @PostConstruct
    public final void initialize() {

        splitUpCaches();
        loadAll(false);
        isInitialCacheLoadDone = true;
        autoCacheReloadService.refresh();
    }

    public abstract void loadAll(boolean forceFully);

    public abstract List<Class<? extends ICache>> getCachesToLoad();

    @Override
    public void loadCacheByCacheName(boolean forceFully, String cacheName) {

        Class<? extends ICache> clazz = cacheNameToCacheClass.get(cacheName);
        if (clazz == null) {
            LOG.info("No Cache found with name : {}", cacheName);
            return;
        }
        loadCache(forceFully, clazz);
    }

    private void splitUpCaches() {

        getCachesToLoad().forEach(
                clazz ->
                {
                    if (!clazz.isAnnotationPresent(Cache.class)) {
                        throw new IllegalArgumentException("@Cache annotation should be present for " + "cache class: " + clazz.getName());
                    }

                    Cache cacheAnnotation = clazz.getAnnotation(Cache.class);
                    CacheKey cacheKey = cacheAnnotation.cacheKey();
                    String cacheName = cacheKey.getCacheName();
                    CacheGroup[] cacheGroups = cacheKey.getCacheGroups();

                    cacheNameToCacheClass.put(cacheName, clazz);
                    Arrays.asList(cacheGroups).forEach(cacheGroup -> {
                                List<String> cacheNames = cacheGroupToCacheNames.get(cacheGroup);
                                cacheNames = cacheNames == null ? new LinkedList<>() : cacheNames;
                                cacheNames.add(cacheName);
                                cacheGroupToCacheNames.put(cacheGroup, cacheNames);
                            }
                    );
                });
    }

    @Override
    public void loadCacheByCacheGroup(boolean forceFully, CacheGroup cacheGroup) {

        LOG.info("Loading Cache with group name : {}", cacheGroup);
        List<String> cacheNames = cacheGroupToCacheNames.get(cacheGroup);
        if (cacheNames == null || cacheNames.isEmpty()) {
            LOG.info("No Cache found with group name : {}", cacheGroup);
            return;
        }
        cacheNames.forEach(cacheName -> loadCacheByCacheName(forceFully, cacheName));
    }

    protected boolean isCacheReloadAllowed(boolean forceFully, Cache cacheAnnotation) {

        Date lastUpdatedTimeInMin = cacheNameToLastReloadTime.get(cacheAnnotation.cacheKey().getCacheName());
        if (lastUpdatedTimeInMin == null || forceFully)
            return true;

        if (!cacheAnnotation.cacheReload()) {
            LOG.info("Not reloading one time loadable cache : {} ", cacheAnnotation.cacheKey().getCacheName());
            return false;
        }

        Date currentTime = DateUtils.getCurrentTime();
        int minReloadTimeInMinutes = cacheAnnotation.MIN_REPEAT_TIME_IN_MINUTE() + 60 * cacheAnnotation.MIN_REPEAT_TIME_IN_HOUR();

        if (DateUtils.getDifferenceInMinutes(currentTime, lastUpdatedTimeInMin) < minReloadTimeInMinutes) {
            LOG.info("Last Cache Load happened at:{} i.e. just under {} minutes. Not Reloading " + cacheAnnotation.cacheKey().getCacheName() + " Cache again. ", lastUpdatedTimeInMin, minReloadTimeInMinutes);
            return false;
        }
        return true;
    }

    protected synchronized <T extends ICache> void loadCache(boolean forceFully, final Class<T> cacheClass) {

        final Cache cacheAnnotation = cacheClass.getAnnotation(Cache.class);
        LOG.info("Loading {} Cache", cacheAnnotation.cacheKey().getCacheName());
        Date currentTime = DateUtils.getCurrentTime();

        if (!isCacheReloadAllowed(forceFully, cacheAnnotation)) {
            return;
        }

        try {
            ICache cache = cacheClass.newInstance();
            applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(cache, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
            cache.load();
            CacheManager.getInstance().setCache(cache);
            cacheNameToLastReloadTime.put(cacheAnnotation.cacheKey().getCacheName(), currentTime);
            LOG.info("{} cache loaded successfully", cacheAnnotation.cacheKey().getCacheName());
        } catch (Exception e) {
            LOG.info("Error while loading cache : {} : {}", cacheAnnotation.cacheKey(), e);
            if (!isInitialCacheLoadDone)
                throw new RuntimeException("Exception while loading Initial Cache Load... " + cacheClass);
        }
    }
}
