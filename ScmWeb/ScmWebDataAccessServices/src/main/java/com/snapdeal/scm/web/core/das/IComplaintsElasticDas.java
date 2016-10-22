package com.snapdeal.scm.web.core.das;

import java.util.List;

import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;

/**
 * 
 * @author mohit
 *
 */
public interface IComplaintsElasticDas extends IAbstractElasticDas{

	@SuppressWarnings("rawtypes")
	List<AggResult> findMetricData(ElasticFilter elasticFilter);
	
}
