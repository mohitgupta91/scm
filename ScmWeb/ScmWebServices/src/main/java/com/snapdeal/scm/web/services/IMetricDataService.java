/**
 * 
 */
package com.snapdeal.scm.web.services;

import com.snapdeal.scm.web.core.enums.Metric;
import com.snapdeal.scm.web.core.request.SearchRequest;
import com.snapdeal.scm.web.core.response.SearchResponse;

/**
 * @author gaurav on 01-Mar-2016
 *
 */
public interface IMetricDataService {
	
	SearchResponse getLevelData(SearchRequest request, Metric metric) throws Exception;

}
