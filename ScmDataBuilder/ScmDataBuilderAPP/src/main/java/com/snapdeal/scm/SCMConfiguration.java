package com.snapdeal.scm;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.snapdeal.scm.app.service.impl.CacheLoadServiceImpl;
import com.snapdeal.scm.cache.ICacheLoadService;

/**
 * SCMConfiguration : Configuration Spring boot to boot
 * 
 * @author pranav, prateek
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class})
@ComponentScan({ "com.snapdeal.scm" })
@PropertySources({ @PropertySource("classpath:application.properties"), @PropertySource("classpath:app.properties") })
@EnableScheduling
public class SCMConfiguration {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SCMConfiguration.class, args);

		// Below code is not required in prod
		/*
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
       MessageCreator messageCreator = new MessageCreator() {
        MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                PollerQueueDto dto = new PollerQueueDto("s3://sdstg/rms/scm/query.file", QueryType.SHIPPING_SOI_SD);
                return session.createObjectMessage(dto);
            }
        };

        System.out.println("Sending a new message to File Handler.");
        for (int i = 0; i < 1; i++)
            jmsTemplate.send("data-poller", messageCreator);*/
		/*   
        MessageCreator  messageCreator = new MessageCreator() {
        for (int i = 0; i < 10; i++)
            jmsTemplate.send("data-poller-test", messageCreator);

        messageCreator = new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("Message posted for alerts !");
            }
        };
        System.out.println("Sending a new message.");
        jmsTemplate.send("alerts", messageCreator);*/

	}

	@Bean
	@PostConstruct
	public ICacheLoadService runCacheFirst() {
		return new CacheLoadServiceImpl();
	}

	@Bean
	public MultipartResolver multipartResolver() {
		return new CommonsMultipartResolver();
	}
}
