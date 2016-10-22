package com.snapdeal.scm.cache.impl;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.mongo.mao.repository.SupcDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static com.snapdeal.scm.cache.CacheKey.SUPER_CATEGORY;

/**
 * Created by ashwini.kumar on 17/05/16.
 */
@Cache(cacheKey = SUPER_CATEGORY, MIN_REPEAT_TIME_IN_MINUTE = 60)
public class SuperCategoryCache implements ICache {

    @Autowired
    private SupcDetailsRepository supcDetailsRepository;

    private List<String> superCategories = new ArrayList<>();

    private void addSuperCategories(List<String> superCategories) {
        this.superCategories = superCategories;
    }

    public List<String> getAllSuperCategories() {
        return this.superCategories;
    }

    @Override
    public void load() {
        List<String> superCategories = supcDetailsRepository.findDistinctSuperCategory();
        addSuperCategories(superCategories);
    }
}
