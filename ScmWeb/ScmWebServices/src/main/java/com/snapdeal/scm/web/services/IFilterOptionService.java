/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.services;

import com.snapdeal.scm.web.core.mongo.documents.FilterOptions;

/**
 * @version 1.0, 25-Feb-2016
 * @author ashwini
 */
public interface IFilterOptionService {

    public FilterOptions getFiltersBykey(String key);

}
