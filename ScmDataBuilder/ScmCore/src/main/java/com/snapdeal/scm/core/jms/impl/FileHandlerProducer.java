package com.snapdeal.scm.core.jms.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.scm.core.poller.dto.impl.PollerQueueDto;
import com.snapdeal.scm.mongo.doc.JmsDetails;

/**
 * Created by vinay on 20/2/16.
 */
@Component
public class FileHandlerProducer extends ScmJmsProducerImpl<PollerQueueDto>{

    @Autowired
    private JmsDetails jmsDetails;

    @PostConstruct
    void postConstruct(){
        setDestinationQueue(jmsDetails.getDestPollerToFH());
    }
}
