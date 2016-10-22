package com.snapdeal.scm.processor;

import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;

/**
 * 
 * @author prateek
 *
 */
public interface IMongoProcessor<T extends IStandardDTO> {

	public void process(ProcessorQueueDto<T> processorQueueDto);
	public QueryType getQueryType();
}
