/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.services;

import java.util.List;

import com.snapdeal.scm.web.core.sro.FilterSRO;

/**
 * @version 1.0, 25-Feb-2016
 * @author ashwini, prateek
 */
public interface IFilterService {

    public List<FilterSRO> getFilterForMatricByid(String maticId);
    
    public FilterSRO getFilterByKey(String filterKey);

}
