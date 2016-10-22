package com.snapdeal.scm.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All Cache classes which implement {@link ICache} interface and contain
 * {@link @Cache} annotation are managed by this {@link CacheManager}
 *
 * @author prateek
 */
public class CacheManager {

    public static interface CacheChangeListner {

        public void reloadCache();
    }

    private CacheManager() {

    }

    private static final Logger LOG = LoggerFactory.getLogger(CacheManager.class);

    private static CacheManager _instance = new CacheManager();

    private final Map<String, ICache> _caches =
            new ConcurrentHashMap<String, ICache>();

    private static final List<CacheChangeListner> _listners =
            new ArrayList<CacheChangeListner>();

    public static CacheManager getInstance() {

        return _instance;
    }

    public ICache getCache(final String cacheName) {

        final ICache cache = _caches.get(cacheName);
    /*
     * if (cache == null) { throw new
     * IllegalArgumentException("cache not loaded. cache:" + cacheName); }
     */
        return cache;
    }

    /**
     * The same method is added since getCache() is overloaded and can create
     * problems if called from JSP. The getCacheByName() should be used in jsp
     * instead of getCache() to avoid such problems.
     *
     * @param cacheName
     * @return
     */

    public Object getCacheByName(final String cacheName) {

        return getCache(cacheName);
    }

    @SuppressWarnings("unchecked")
    public <T extends ICache> T getCache(final Class<T> cacheClass) {

        if (cacheClass.isAnnotationPresent(Cache.class)) {
            return (T) getCache(cacheClass.getAnnotation(Cache.class).cacheKey().getCacheName());
        } else {
            throw new IllegalArgumentException(
                    "@Cache annotation should be present for cache class:"
                            + cacheClass.getName());
        }
    }

    public synchronized void setCache(final ICache cache) {

        final Class<? extends ICache> cacheClass = cache.getClass();
        if (cacheClass.isAnnotationPresent(Cache.class)) {
            for (final Method m : cacheClass.getDeclaredMethods()) {
                if ("freeze".equals(m.getName())) {
                    try {
                        m.invoke(cache);
                    } catch (final Exception e) {
                        LOG.error("unable to freeze cache:" + cacheClass.getName(), e);
                    }
                }
            }
            final Cache annotation = cacheClass.getAnnotation(Cache.class);
            _caches.put(annotation.cacheKey().getCacheName(), cache);
            signalListners();
        } else {
            throw new IllegalArgumentException(
                    "@Cache annotation should be present for cache class:"
                            + cache.getClass().getName());
        }

    }

    private void signalListners() {

        for (final CacheChangeListner listner : _listners) {
            try {
                listner.reloadCache();
            } catch (final Exception e) {
                LOG.error(
                        "Error while signalling {} listner for cache change. Error: {}",
                        listner.getClass().getName(), e.getMessage());
            }
        }
    }

    public static void registerListener(final CacheChangeListner listner) {

        _listners.add(listner);
    }
}
