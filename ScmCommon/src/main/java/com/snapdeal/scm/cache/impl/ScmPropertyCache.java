package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.enums.ScmPropertyEnum;
import com.snapdeal.scm.mongo.doc.ScmProperty;
import com.snapdeal.scm.mongo.mao.repository.ScmPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author prateek
 */
@Cache(cacheKey = CacheKey.SCM_PROPERTY, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class ScmPropertyCache implements ICache {

    @Autowired
    ScmPropertyRepository scmPropertyRepository;

    private final Map<String, String>              stringValues     = new HashMap<>();
    private final Map<String, List<String>>        listValues       = new HashMap<>();
    private final Map<String, Map<String, String>> mapValues        = new HashMap<>();
    private final Map<String, String>              enumStringValues = new HashMap<>();
    private final Map<String, List<String>>        enumListValues   = new HashMap<>();
    private final Map<String, Map<String, String>> enumMapValues    = new HashMap<>();


    private Date lastUpdatedTime = new Date(0L);

    public void addProperty(ScmProperty scmProperty) {

        if (scmProperty.getStringValue() != null) {
            stringValues.put(scmProperty.getName(), scmProperty.getStringValue());
        } else if (scmProperty.getListValue() != null) {
            listValues.put(scmProperty.getName(), scmProperty.getListValue());
        } else if (scmProperty.getMapValue() != null) {
            mapValues.put(scmProperty.getName(), scmProperty.getMapValue());
        }
        if (scmProperty.getUpdated().after(lastUpdatedTime)) {
            lastUpdatedTime = scmProperty.getUpdated();
        }
    }

    private void loadPropertiesFromEnum() {

        for (ScmPropertyEnum scmPropertyEnum : ScmPropertyEnum.values()) {
            if (scmPropertyEnum.getStringValue() != null) {
                enumStringValues.put(scmPropertyEnum.getName(), scmPropertyEnum.getStringValue());
            }
            if (scmPropertyEnum.getListValue() != null) {
                enumListValues.put(scmPropertyEnum.getName(), scmPropertyEnum.getListValue());
            }
            if (scmPropertyEnum.getMapValue() != null) {
                enumMapValues.put(scmPropertyEnum.getName(), scmPropertyEnum.getMapValue());
            }
        }
    }

    public String getStringValue(String name) {
        String valueFromMongo = stringValues.get(name);
        return valueFromMongo != null ? valueFromMongo : enumStringValues.get(name);
    }

    public Map<String, String> getMap(String name) {
        Map<String, String> mapFromMongo = mapValues.get(name);
        return mapFromMongo != null ? mapFromMongo : enumMapValues.get(name);
    }

    public List<String> getList(String name) {
        List<String> listFromMongo = listValues.get(name);
        return listFromMongo != null ? listFromMongo : enumListValues.get(name);
    }

    public String getStringValue(ScmPropertyEnum property) {
        return getStringValue(property.getName());
    }

    public Map<String, String> getMap(ScmPropertyEnum property) {
        return getMap(property.getName());
    }

    public List<String> getList(ScmPropertyEnum property) {
        return getList(property.getName());
    }

    public String getMapValue(String name, String key) {
        return getMapValue(name, key, null);
    }

    public String getMapValue(String name, String key, String defaultValue) {
        return Optional.ofNullable(getMap(name))
                .map(values -> values.get(key)).orElse(defaultValue);
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    @Override
    public void load() {
        Optional.ofNullable(scmPropertyRepository.findAll())
                .ifPresent(properties -> properties.forEach(this::addProperty));
        loadPropertiesFromEnum();
    }
}
