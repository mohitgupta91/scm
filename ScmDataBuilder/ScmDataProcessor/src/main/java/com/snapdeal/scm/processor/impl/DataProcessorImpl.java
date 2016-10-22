package com.snapdeal.scm.processor.impl;

import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.dto.validator.IDTOValidator;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;
import com.snapdeal.scm.processor.IDataProcessor;
import com.snapdeal.scm.processor.IElasticProcessor;
import com.snapdeal.scm.processor.IMongoProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;

/**
 * Process data based on Job Name and insert the same in Master Schema(Table).
 * 
 * @author prateek, pranav
 */
@Service("DataProcessorImpl")
public class DataProcessorImpl implements IDataProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(DataProcessorImpl.class);

	@Autowired
	private ProcessorFactory	processorFactory;
	
	@Autowired
	private IDTOValidator		dtoValidator;

	@Override
	public void process(ProcessorQueueDto<IStandardDTO> processorQueueDto) throws JMSException {
		if(!dtoValidator.validate(processorQueueDto)){
			return;
		}
		IElasticProcessor<IStandardDTO> elasticProcess = processorFactory.getElasticProcessor(processorQueueDto.getQueryType());
		if(elasticProcess != null) {
			LOG.info("Calling elastic processor :{} of queryType :{} and filePath :{}", elasticProcess, processorQueueDto.getQueryType(), processorQueueDto.getFilePath());
			elasticProcess.process(processorQueueDto);
			return;
		}
		IMongoProcessor<IStandardDTO> mongoProcessor = processorFactory.getMongoProcessor(processorQueueDto.getQueryType());
		if(mongoProcessor != null){
			LOG.info("Calling Mongo processor :{} of queryType :{} and filePath :{}", mongoProcessor, processorQueueDto.getQueryType(), processorQueueDto.getFilePath());
			mongoProcessor.process(processorQueueDto);
			return;
		}	
		LOG.error("NO Processor found for queryType: {} and filePath :{}", processorQueueDto.getQueryType(), processorQueueDto.getFilePath());
	}
}
