package com.snapdeal.scm.alerts;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import com.snapdeal.scm.alerts.jms.AlertJmsConsumer;
import com.snapdeal.scm.alerts.task.AlertScheduledTask;
import com.snapdeal.scm.jms.ScmJmsConsumer;
import com.snapdeal.scm.jms.ScmMessageListenerAdapter;
import com.snapdeal.scm.mongo.mao.repository.ScmJmsPropertyMongoRepository;
import com.snapdeal.scm.mongo.doc.JmsDetails;
import com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty;

/**
 * AlertsConfiguration : Alerts can be configured inside web or data
 * 
 * @author pranav
 */
@Configuration
@ComponentScan("com.snapdeal.base.transport.serialization.service")
@PropertySources(@PropertySource("classpath:alert.properties"))
public class AlertConfiguration {

	@Autowired
	private ScmJmsPropertyMongoRepository jmsConfRepo;

	@Autowired
	private JmsTransactionManager         tManager;

	@Autowired
	private ScmJmsMachineProperty         jmsMachineProp;

	@Autowired
	private JmsDetails                    jmsDetails;

	@Autowired
	@Qualifier("alertsConsumer")
	private ScmJmsConsumer                jmsConsumer;

	@Autowired
	private ConnectionFactory             connectionFactory;

	@Autowired
	private ApplicationContext context;

	@Bean(name="alertsJmsMsgListener")
	public DefaultMessageListenerContainer jmsListenerFH() {
		if (!jmsMachineProp.isAlertConsumer())
			return null;
		if (jmsMachineProp.isAlertConsumer()) {
			if (null == jmsDetails.getDestAlerts() || null == jmsMachineProp.getAlertsConcurrency() || jmsDetails.getDestAlerts().trim().length() == 0
					|| jmsMachineProp.getAlertsConcurrency().trim().length() == 0) {
				throw new RuntimeException("Alert Processor Flag is true but jms settings are missing !");
			}
		}
		DefaultMessageListenerContainer jmsListener = new DefaultMessageListenerContainer();
		jmsListener.setConnectionFactory(connectionFactory);
		jmsListener.setTransactionManager(tManager);
		jmsListener.setDestinationName(jmsDetails.getDestAlerts());
		jmsListener.setPubSubDomain(false);
		jmsListener.setConcurrency(jmsMachineProp.getAlertsConcurrency());
		jmsListener.setSessionTransacted(true);
		MessageListenerAdapter adapter = new ScmMessageListenerAdapter(jmsConsumer);
		adapter.setDefaultListenerMethod("receiveMessage");
		jmsListener.setMessageListener(adapter);
		return jmsListener;
	}


	@Bean(name="alertsConsumer")
	public ScmJmsConsumer getAlertjmsConsumer(){
		if (!jmsMachineProp.isAlertConsumer())
			return null;
		return new AlertJmsConsumer();
	}

	@Bean
	public AlertScheduledTask getAlertTaskScheduler(){
		if (!jmsMachineProp.isAlertProducer())
			return null;
		return new AlertScheduledTask();
	}
}
