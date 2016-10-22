package com.snapdeal.scm.processor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticScriptDataDTO;

/**
 * IElasticService : Elastic service
 * 
 * @author pranav, prateek
 *
 */
public interface IElasticService {

	void indexDocuments(Collection<ElasticDataDTO> schemaDto);

	void runScript(List<ElasticDataDTO> dtos, String script, Map<String, Object> params);
	
	Object getFieldValueBySuborderId(String id, String fieldName);

	void runDynamicScript(Collection<ElasticScriptDataDTO> collection);
}
