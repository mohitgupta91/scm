/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.scm.web.core.mao.FilterOptionsRepository;
import com.snapdeal.scm.web.core.mongo.documents.FilterOptions;
import com.snapdeal.scm.web.services.IFilterOptionService;

/**
 * @version 1.0, 25-Feb-2016
 * @author ashwini
 */
@Service
public class FilterOptionServiceImpl implements IFilterOptionService {

    @Autowired
    private FilterOptionsRepository repo;

    @Override
    public FilterOptions getFiltersBykey(String key) {
        return repo.findByFilterKey(key);
    }

}
