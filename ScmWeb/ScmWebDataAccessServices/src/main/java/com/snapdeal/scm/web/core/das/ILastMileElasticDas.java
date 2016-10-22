package com.snapdeal.scm.web.core.das;

import java.util.List;

import com.snapdeal.scm.core.elastic.dto.AggResult;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;

/**
 * ILastMileElasticDas : provide last mile elastic data service
 * 
 * @author pranav, prateek
 *
 */
@SuppressWarnings("rawtypes")
public interface ILastMileElasticDas extends IAbstractElasticDas{

	List<AggResult> findMetricData(ElasticFilter elasticFilter);
}
