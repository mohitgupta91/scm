package com.snapdeal.scm.web.services.cache.impl;

import com.snapdeal.scm.cache.IAutoCacheReloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.snapdeal.scm.cache.ICacheLoadService;

/**
 * @author prateek
 */
@Service("AutoCacheReloadServiceImpl")
public class AutoCacheReloadServiceImpl implements IAutoCacheReloadService {

    private static final Logger LOG = LoggerFactory.getLogger(AutoCacheReloadServiceImpl.class);

    @Autowired
    ICacheLoadService cacheLoadService;

    @Override
    @Scheduled(cron = "${auto.cache.reload.cron}")
    public void refresh() {
        try {
            LOG.info("Starting SCM Cache Reload");
            cacheLoadService.loadAll(false);
            ;
            LOG.info("Cache loaded Successfully");
        } catch (Exception e) {
            LOG.error("Error while cache reload.", e);
        }
    }
}
