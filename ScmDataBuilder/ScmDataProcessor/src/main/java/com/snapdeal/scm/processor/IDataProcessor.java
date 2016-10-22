package com.snapdeal.scm.processor;

import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;

import javax.jms.JMSException;

/**
 * 
 * @author prateek
 *
 */
public interface IDataProcessor {

	void process(ProcessorQueueDto<IStandardDTO> processorQueueDto) throws JMSException;
}
