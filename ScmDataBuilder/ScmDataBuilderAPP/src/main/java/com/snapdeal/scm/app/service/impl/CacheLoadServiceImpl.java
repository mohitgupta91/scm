package com.snapdeal.scm.app.service.impl;

import com.snapdeal.scm.cache.AbstractCacheLoaderService;
import com.snapdeal.scm.cache.CacheGroup;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.cache.impl.*;
import com.snapdeal.scm.fh.cache.impl.QueryDTOFieldMappingCache;
import com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty;
import com.snapdeal.scm.mongo.mao.repository.ScmJmsPropertyMongoRepository;
import com.snapdeal.scm.poller.cache.impl.JobCache;
import com.snapdeal.scm.poller.cache.impl.PollerCache;
import com.snapdeal.scm.processor.cache.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * @author prateek
 */
public class CacheLoadServiceImpl extends AbstractCacheLoaderService {

    @Autowired
    private ScmJmsPropertyMongoRepository jmsConfRepo;

    @Value("${scm.host.name}")
    private String hostName;

    @Override
    public void loadAll(boolean forceFully) {

        ScmJmsMachineProperty jmsMachineProp = jmsConfRepo.findByCritria(hostName);
        if (null == jmsMachineProp) {
            throw new RuntimeException("Please Configure Machine Jms Property in Database !");
        }
        loadCacheByCacheGroup(forceFully, CacheGroup.DATA_ALL);
        if (jmsMachineProp.isDataPoller())
            loadCacheByCacheGroup(forceFully, CacheGroup.DATA_POLLER);
        if (jmsMachineProp.isFileHandler())
            loadCacheByCacheGroup(forceFully, CacheGroup.DATA_FILE_HANDLER);
        if (jmsMachineProp.isDataProcessor())
            loadCacheByCacheGroup(forceFully, CacheGroup.DATA_PROCESSOR);
    }

    @Override
    public List<Class<? extends ICache>> getCachesToLoad() {

        List<Class<? extends ICache>> cacheClasses = new ArrayList<>();
        cacheClasses.add(FulfillmentProviderCache.class);
        cacheClasses.add(CourierGroupCache.class);
        cacheClasses.add(PincodeMasterCache.class);
        cacheClasses.add(StatusCodesCache.class);
        cacheClasses.add(MetroCityCache.class);
        cacheClasses.add(QueryDTOFieldMappingCache.class);
        cacheClasses.add(JobCache.class);
        cacheClasses.add(PollerCache.class);
        cacheClasses.add(ScmPropertyCache.class);
        cacheClasses.add(CourierLocationSnapdealLocationMappingCache.class);
        cacheClasses.add(OriginCityExitMappingCache.class);
        cacheClasses.add(PincodeDCMappingCache.class);
        cacheClasses.add(SupcCache.class);
        cacheClasses.add(ComplaintTypeCache.class);
        return cacheClasses;
    }
}