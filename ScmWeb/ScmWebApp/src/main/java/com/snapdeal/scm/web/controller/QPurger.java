package com.snapdeal.scm.web.controller;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Should only be use to purge a Q : technically reading all messages over the Q
 * 
 * @author pranav
 *
 */
@Controller
@RequestMapping("/admin")
public class QPurger{
	
	private static final Logger LOG = LoggerFactory.getLogger(QPurger.class);

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;


	@Value("${spring.activemq.user}")
	private String userName;


	@Value("${spring.activemq.password}")
	private String password;


	@RequestMapping("/purgeQ")
	@ResponseBody
	public String purgeQ( @RequestParam(name="destinationName", required=true) String queueName) throws JMSException{
		LOG.info("Purging Q at Destination="+queueName);
		ActiveMQConnectionFactory connectionFactory = new
				ActiveMQConnectionFactory(userName, password, brokerUrl);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		Destination queue = session.createQueue(queueName);
		MessageConsumer consumer = session.createConsumer(queue);
		int count=0;
		while (null!=consumer.receive(10000)){
			count++;
		}
		LOG.info("Total Read Message="+count);
		consumer.close();
		consumer=null;
		session.close();
		session=null;
		queue=null;
		connection.close();
		connection=null;
		connectionFactory=null;
		System.gc();
		return queueName + " Q is Purged";
	}
}
