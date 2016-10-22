
package com.snapdeal.scm.fh.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.scm.core.poller.dto.impl.PollerQueueDto;
import com.snapdeal.scm.fh.service.IFileHandlerService;
import com.snapdeal.scm.jms.ScmJmsConsumer;

/**
 * DataPollerConsumer : JMS data poller consumer. This will receive message from data poller
 * 
 * @author pranav, Ashwini
 */
@Component("dataPollerConsumer")
public class DataPollerConsumer implements ScmJmsConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DataPollerConsumer.class);
    private static final Logger errorLOG = LoggerFactory.getLogger("FILEHANDLER-ERROR-LOGGER");

    @Autowired
    private IFileHandlerService filehandlerService;

    @Transactional
    @Override
    public void receiveMessage(Object message) throws Exception {
        logger.info("Received message from data poller : <" + message + ">");
        if (message instanceof PollerQueueDto) {
            filehandlerService.processFile((PollerQueueDto) message);
        } else {
        	errorLOG.error("dto " + message.getClass() + " is not instance of PollerQueueDto.class");
        }
    }
}