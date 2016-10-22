/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.fh.cache.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.mongo.document.DtoQueryFieldMap;
import com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping;
import com.snapdeal.scm.das.mongo.dao.QueryDTOFieldMappingRepository;

/**
 * @version 1.0, 17-Feb-2016
 * @author ashwini
 */
@Cache(cacheKey = CacheKey.DTO_QUERY_FIELD_MAP, cacheReload = false, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class QueryDTOFieldMappingCache implements ICache {

    @Autowired
    private QueryDTOFieldMappingRepository dtoQueryFieldRepo;

    Map<QueryType, QueryDTOFieldMapping> jobNameDTOfieldMap = new HashMap<>();

    public void addMapping(QueryDTOFieldMapping mapping) {
        jobNameDTOfieldMap.put(mapping.getJobName(), mapping);
    }

    public List<DtoQueryFieldMap> getFieldMappingByJobName(QueryType jobName) {
        QueryDTOFieldMapping mapping = jobNameDTOfieldMap.get(jobName);
        return (null == mapping) ? null : mapping.getFieldMapping();
    }

    public String getjobMappedDtoClass(QueryType jobName) {
        QueryDTOFieldMapping mapping = jobNameDTOfieldMap.get(jobName);
        return (null == mapping) ? null : mapping.getJobClass();
    }

    public QueryDTOFieldMapping getMapping(QueryType jobName) {
        return jobNameDTOfieldMap.get(jobName);
    }

    public Boolean mappingExist(QueryType jobName) {
        QueryDTOFieldMapping mapping = jobNameDTOfieldMap.get(jobName);
        return (null == mapping) ? false : true;
    }

    @Override
    public void load() {
        List<QueryDTOFieldMapping> mappings = dtoQueryFieldRepo.findAll();

        mappings.forEach(mapping -> {
            addMapping(mapping);
        });
    }
}
