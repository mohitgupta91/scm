package com.snapdeal.scm.core.jms.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;
import com.snapdeal.scm.mongo.doc.JmsDetails;

/**
 * DataProcessorProducer : Data processor producer jms end point
 * 
 * @author pranav
 *
 */
@Component
public class DataProcessorProducer extends ScmJmsProducerImpl<ProcessorQueueDto>{

	@Autowired
	private JmsDetails jmsDetails;

	@PostConstruct
    void postConstruct(){
        setDestinationQueue(jmsDetails.getDestFHToProcessor());
    }
}
