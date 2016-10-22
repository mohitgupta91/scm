package com.snapdeal.scm.web.core.das;

import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.web.core.response.SearchResponse;

/**
 * @author chitransh
 */
public interface ILastMilePieChartElasticDas {

    SearchResponse findMetricData(ElasticFilter elasticFilter, String optionKey);
}
