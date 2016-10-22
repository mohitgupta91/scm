package com.snapdeal.scm.processor.jms;

import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;
import com.snapdeal.scm.jms.ScmJmsConsumer;
import com.snapdeal.scm.processor.IDataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FileHandlerConsumer : This Will receive message from File-Handler app
 * 
 * @author pranav, prateek
 *
 */
@Component("fileHandlerConsumer")
public class FileHandlerConsumer implements ScmJmsConsumer {

	private static final Logger logger = LoggerFactory.getLogger(FileHandlerConsumer.class);
	private static final Logger errorLOG = LoggerFactory.getLogger("DATAPROCESSOR-ERROR-LOGGER");

	@Autowired
	private IDataProcessor dataprocessor;

	@SuppressWarnings("unchecked")
	@Override
	public void receiveMessage(Object message) throws Exception {
		Long startTime = System.currentTimeMillis();
		logger.debug("Received message from File Handler<" + message + ">");	
		if (message instanceof ProcessorQueueDto)  {
			dataprocessor.process((ProcessorQueueDto<IStandardDTO>) message);
			logger.info("Total time taken to process {} records is : {}", ((ProcessorQueueDto<IStandardDTO>) message).size(), System.currentTimeMillis() - startTime);
		}
		else
			errorLOG.error("dto " + message.getClass() + " is not instance of ProcessorQueueDto.class");
	}
}
