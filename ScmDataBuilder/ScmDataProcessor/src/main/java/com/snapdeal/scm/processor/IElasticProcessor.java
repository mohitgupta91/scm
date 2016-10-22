package com.snapdeal.scm.processor;

import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;

import javax.jms.JMSException;

/**
 * <p> Query: ""
 * <p> Key  : ""
 * <p> Data : ""
 * 
 * @author prateek
 *
 */
public interface IElasticProcessor<T extends IStandardDTO> {
	
	public void process(ProcessorQueueDto<T> processorQueueDto) throws JMSException;
	public QueryType getQueryType();

}