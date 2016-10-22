package com.snapdeal.scm;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;

import com.snapdeal.scm.mongo.mao.repository.ScmJmsPropertyMongoRepository;
import com.snapdeal.scm.mongo.doc.JmsDetails;
import com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty;

/**
 * ScmJmsConfiguration : Jms Configuration
 * 
 * @author pranav
 *
 */
@Configuration
@ConditionalOnClass(JmsTemplate.class)
@EnableJms
@SpringBootApplication(exclude = {ActiveMQAutoConfiguration.class })
public class ScmJmsConfiguration {
	
	@Autowired
	private ScmJmsPropertyMongoRepository jmsConfRepo;
	
	@Value("${scm.host.name}")
	private String hostName;

	@Configuration
	@ConditionalOnClass(ActiveMQConnectionFactory.class)
	@ConditionalOnMissingBean(ConnectionFactory.class)
	protected static class ActiveMQConnectionFactoryCreator {

		@Autowired
		private ScmJmsMachineProperty jmsMachineProp;
		
		@Autowired
		private JmsDetails  jmsDetails;

		@Bean
		public ConnectionFactory connectionFactory() {
			ActiveMQConnectionFactory jmsConnectionFactory = new ActiveMQConnectionFactory(jmsDetails.getUname(),
					jmsDetails.getPwd(), jmsDetails.getJmsBrokerUrl());

			RedeliveryPolicy rPolicy= jmsConnectionFactory.getRedeliveryPolicy();

			int maxRedeliveryAttempt = jmsMachineProp.getMaxRedeliveryAttempt();

			if (maxRedeliveryAttempt > 0) {
				rPolicy.setInitialRedeliveryDelay(jmsMachineProp.getRedeliveryDelayInMS());
				rPolicy.setMaximumRedeliveries(maxRedeliveryAttempt);
			}
			else {
				rPolicy.setMaximumRedeliveries(0);
			}

			jmsConnectionFactory.setRedeliveryPolicy(rPolicy);
			return jmsConnectionFactory;
		}
	}


	@Configuration
	@ConditionalOnMissingBean(JmsTemplate.class)
	protected static class JmsTemplateCreator {

		@Autowired
		ConnectionFactory connectionFactory;

		@Bean
		public JmsTemplate jmsTemplate() {
			JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
			jmsTemplate.setPubSubDomain(false);
			return jmsTemplate;
		}

		@Bean
		public JmsTransactionManager enableJmsTransaction(){
			JmsTransactionManager tManager=new JmsTransactionManager(connectionFactory);
			return tManager;
		}
	}
	
	@Bean
	public ScmJmsMachineProperty getScmJmsMachineProperty(){
		 ScmJmsMachineProperty jmsMachineProp = jmsConfRepo.findByCritria(hostName);
		 if(null==jmsMachineProp){
			 throw new RuntimeException("Please Configure Machine Jms Property in Database !");
		 }
		 return jmsMachineProp;
	}
	
	@Bean
	public JmsDetails getJmsDetails(){
		JmsDetails  jmsDetails= jmsConfRepo.findJmsConf();
		if(null==jmsDetails)
			throw new RuntimeException("Please Configure Jms Property in Database !");
		String brokerUrl = jmsDetails.getJmsBrokerUrl();
		String user = jmsDetails.getUname();
		String password =jmsDetails.getPwd();
		if(null == brokerUrl || null == user || null == password || brokerUrl.trim().length()<=0
				|| user.trim().length()<=0 || password.trim().length()<=0)
			throw new RuntimeException("Jms Property is not configured properly in Database !");
		 return jmsDetails;
	}
}
