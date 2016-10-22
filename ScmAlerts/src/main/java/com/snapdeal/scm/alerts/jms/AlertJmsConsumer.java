package com.snapdeal.scm.alerts.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.scm.alerts.type.BaseAlert;
import com.snapdeal.scm.jms.ScmJmsConsumer;
import com.snapdeal.scm.mongo.dao.AlertInstanceRepository;
import com.snapdeal.scm.mongo.dao.AlertRepository;
import com.snapdeal.scm.mongo.doc.AlertInstance;
import com.snapdeal.scm.mongo.doc.Alert;

/**
 * AlertJmsConsumer : JMS alert consumer. This will receive alerts event to be processed.
 * 
 * @author pranav
 */
public class AlertJmsConsumer implements ScmJmsConsumer {

    private static final Logger      logger = LoggerFactory.getLogger(AlertJmsConsumer.class);

    @Autowired
    private ApplicationContext       context;

    @Autowired
    private AlertInstanceRepository     alertInstanceRepo;

    @Autowired
    private AlertRepository alertRepo;

    @Transactional
    @Override
    public void receiveMessage(Object message) throws Exception {
        logger.info("Received Alert Request  <" + message + ">");
        if (message instanceof Long) {
            AlertInstance alert = alertInstanceRepo.findByAlertId((Long) message);
            Alert alertType = alertRepo.findByAlertId(alert.getAlertId());
            BaseAlert alertProcessor = (BaseAlert) context.getBean(Class.forName(alertType.getImplClass()));
            alertProcessor.process(alert);
        } else {
            logger.info("Not Valid Message : {}", message);
        }
    }
}