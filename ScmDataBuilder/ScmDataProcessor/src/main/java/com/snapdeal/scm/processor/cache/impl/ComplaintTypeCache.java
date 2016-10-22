package com.snapdeal.scm.processor.cache.impl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.core.mongo.document.ComplaintType;
import com.snapdeal.scm.das.mongo.dao.ComplaintTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by harshit.nimbark on 5/30/16.
 */
@Cache(cacheKey= CacheKey.COMPLAINT_TYPE, MIN_REPEAT_TIME_IN_HOUR=24)
public class ComplaintTypeCache implements ICache {

    @Autowired
    ComplaintTypeRepository complaintTypeRepository;

    private BiMap<String, String> complaintTypeCodeBiMap  = HashBiMap.create();

    public String getCodeByComplaintType(String complaintType){
        return complaintTypeCodeBiMap.get(complaintType);
    }

    public String getComplaintTypeByCode(String code){
        return complaintTypeCodeBiMap.get(code);
    }

    @Override
    public void load() {
        for (ComplaintType complaintType : complaintTypeRepository.findAll()){
            complaintTypeCodeBiMap.put(complaintType.getComplaintCategory(), complaintType.getCode());
        }
    }
}
