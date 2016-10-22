package com.snapdeal.scm.processor;

import javax.jms.ConnectionFactory;

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
import com.snapdeal.scm.processor.scheduler.ISubOrderTpProcessorScheduler;
import com.snapdeal.scm.processor.scheduler.impl.SubOrderTpProcessorScheduler;

/**
 * DataProcessorConfiguration : Configuration related to data processor
 * 
 * @author pranav
 */
@Configuration
public class DataProcessorConfiguration {

    @Autowired
    @Qualifier("fileHandlerConsumer")
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

    @Bean(name="dataProcessorJmsMsgListener")
    public DefaultMessageListenerContainer jmsListenerDP(ConnectionFactory connectionFactory) {
        if (!jmsMachineProp.isDataProcessor())
            return null;
        if (jmsMachineProp.isDataProcessor()) {
            if (null == jmsDetails.getDestFHToProcessor() || null == jmsMachineProp.getProcessorConcurrency() || jmsDetails.getDestFHToProcessor().trim().length() == 0
                    || jmsMachineProp.getProcessorConcurrency().trim().length() == 0) {
                throw new RuntimeException("DataProcessor Flag is true but jms settings are missing !");
            }
        }
        DefaultMessageListenerContainer jmsListener = new DefaultMessageListenerContainer();
        jmsListener.setConnectionFactory(connectionFactory);
        jmsListener.setTransactionManager(tManager);
        jmsListener.setDestinationName(jmsDetails.getDestFHToProcessor());
        jmsListener.setPubSubDomain(false);
        jmsListener.setConcurrency(jmsMachineProp.getProcessorConcurrency());
        jmsListener.setSessionTransacted(true);

        MessageListenerAdapter adapter = new ScmMessageListenerAdapter(jmsConsumer);
        adapter.setDefaultListenerMethod("receiveMessage");

        jmsListener.setMessageListener(adapter);
        return jmsListener;
    }
    
    @Bean
	public ISubOrderTpProcessorScheduler subOrderTpMongoScheduler(){
    	SubOrderTpProcessorScheduler subOrderTpMongoScheduler = null;
		ScmJmsMachineProperty jmsMachineProp = jmsConfRepo.findByCritria(hostName);
		if(jmsMachineProp.isProcessorScheduler())
			subOrderTpMongoScheduler = new SubOrderTpProcessorScheduler();
		return subOrderTpMongoScheduler;
	}
}
