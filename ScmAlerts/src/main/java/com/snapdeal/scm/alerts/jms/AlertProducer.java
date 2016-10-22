package com.snapdeal.scm.alerts.jms;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snapdeal.scm.core.jms.impl.ScmJmsProducerImpl;
import com.snapdeal.scm.mongo.doc.JmsDetails;

/**
 * AlertProducer : Alert producer jms end point
 * 
 * @author pranav, Ashwini
 */
@Component
public class AlertProducer extends ScmJmsProducerImpl<Long> {

    @Autowired
    private JmsDetails jmsDetails;

    @PostConstruct
    void postConstruct() {
        setDestinationQueue(jmsDetails.getDestAlerts());
    }
}
