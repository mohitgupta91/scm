package com.snapdeal.scm.fh;

import javax.jms.ConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import com.snapdeal.scm.jms.ScmJmsConsumer;
import com.snapdeal.scm.jms.ScmMessageListenerAdapter;
import com.snapdeal.scm.mongo.doc.JmsDetails;
import com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty;
import com.snapdeal.scm.mongo.mao.repository.ScmJmsPropertyMongoRepository;

/**
 * DataProcessorConfiguration : Configuration related to data processor
 * 
 * @author pranav
 */
@Configuration
public class FileHandlerConfiguration {

    @Autowired
    @Qualifier("dataPollerConsumer")
    private ScmJmsConsumer                jmsConsumer;

    @Autowired
    private ScmJmsPropertyMongoRepository jmsConfRepo;

    @Value("${scm.host.name}")
    private String                        hostName;

    @Autowired
    private JmsTransactionManager         tManager;

    @Autowired
    private ScmJmsMachineProperty         jmsMachineProp;

    @Autowired
    private JmsDetails                    jmsDetails;
    
    private static final Logger   logger   = LoggerFactory.getLogger(FileHandlerConfiguration.class);

    @Bean(name="fileHandlerJmsMsgListener")
    public DefaultMessageListenerContainer jmsListenerFH(ConnectionFactory connectionFactory) {
    	logger.info("----Entering fileHandlerJmsMsgListener----, jmsMachineProp = {} , jmsDetails = {}", jmsMachineProp, jmsDetails);
    	if (!jmsMachineProp.isFileHandler()) {
    		logger.info("----Returning NULL----");
    		return null;
    	}
        if (jmsMachineProp.isFileHandler()) {
            if (null == jmsDetails.getDestPollerToFH() || null == jmsMachineProp.getFhConcurrency() || jmsDetails.getDestPollerToFH().trim().length() == 0
                    || jmsMachineProp.getFhConcurrency().trim().length() == 0) {
            	logger.info("----Returning ERROR----");
                throw new RuntimeException("FileHandler Flag is true but jms settings are missing !");
            }
        }
        logger.info("----Starting fileHandlerJmsMsgListener----");
        DefaultMessageListenerContainer jmsListener = new DefaultMessageListenerContainer();
        jmsListener.setConnectionFactory(connectionFactory);
        jmsListener.setTransactionManager(tManager);
        jmsListener.setDestinationName(jmsDetails.getDestPollerToFH());
        jmsListener.setPubSubDomain(false);
        jmsListener.setConcurrency(jmsMachineProp.getFhConcurrency());
        jmsListener.setSessionTransacted(true);
        MessageListenerAdapter adapter = new ScmMessageListenerAdapter(jmsConsumer);
        adapter.setDefaultListenerMethod("receiveMessage");
        jmsListener.setMessageListener(adapter);
        logger.debug("----jmsListener----{}",jmsListener);
        return jmsListener;
    }
}
