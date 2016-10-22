package com.snapdeal.scm.alerts.services.impl;


import com.snapdeal.base.exception.SerializationException;
import com.snapdeal.base.transport.serialization.service.ISerializationService;
import com.snapdeal.cx.ebs.commons.dto.Event;
import com.snapdeal.cx.ebs.commons.dto.Header;
import com.snapdeal.cx.ebs.producer.EventProducer;
import com.snapdeal.cx.ebs.producer.EventProducerConfig;
import com.snapdeal.scm.alerts.services.IUCMSEventTriggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Created by ashwini.kumar on 04/05/16.
 */
public class UCMSEventTriggerServiceImpl implements IUCMSEventTriggerService {

	private static final Logger LOG = LoggerFactory.getLogger(UCMSEventTriggerServiceImpl.class);

	private static volatile EventProducer eventProducer;

	@Value("${event.bus.broker.list}")
	private String eventBusBrokerList;

	@Value("${event.bus.topic}")
	private String eventBusTopic;

	@Value("${event.bus.header.system.name}")
	private String eventBusHeaderSystemName;

	@Autowired
	@Qualifier("jsonSerializationService")
	private ISerializationService jsonSerializationService;


	public synchronized void init() {
		LOG.info("Initialize the communication engine events");

		try {
			EventProducerConfig eventProducerConfig = null;
			List<String> brokers = Arrays.asList(eventBusBrokerList.split(","));
			for (String broker : brokers) {
				List<String> brokerTemp = Arrays.asList(broker.split(":"));
				if (brokerTemp.size() != 2) {
					LOG.error("Please send right broker address and broker port", eventBusBrokerList);
				} else {
					String brokerAddress = brokerTemp.get(0).trim();
					int brokerPort = Integer.parseInt(brokerTemp.get(1).trim());
					eventProducerConfig = new EventProducerConfig().addBroker(new InetSocketAddress(brokerAddress, brokerPort));
				}
			}

			LOG.info("eventProducerConfig ", eventProducerConfig.getBrokers());
			eventProducer = new EventProducer(eventProducerConfig);
		} catch (Exception e) {
			LOG.error("Following exception occurs in initialization {}", e);
		}

	}

	@Override
	@Async
	public void sendCommunicationEventAsync(Long notificationId, String eventType, Map<String, Object> eventBodyFields) {
		if (eventProducer == null) {
			synchronized (UCMSEventTriggerServiceImpl.class) {
				if (eventProducer == null) {
					init();
				}
			}
		}
		LOG.info("sending the async communication events for notification Id : {} and eventType :{}", notificationId, eventType);
		try {
			Event e = createEventWithHeader(notificationId, eventType);
			sendEvent(e, eventBodyFields);
		} catch (SerializationException se) {
			LOG.info("Exception in serialization {}", se.getMessage());
		} catch (Exception ex) {
			LOG.error("Some error occured during send communication event", ex);
		}
	}

	private void sendEvent(Event e, Map<String, Object> eventBodyFields) throws SerializationException {
		if (eventBodyFields != null) {
			Map<String, Map<String, Object>> eventBodyMap = Collections.singletonMap("notificationId", eventBodyFields);
			String jsonBody = jsonSerializationService.doSerialize(eventBodyMap);
			e.setBody(jsonBody.getBytes());
		}
		LOG.info("sending the events for topic : {}", eventBusTopic);
		eventProducer.send(eventBusTopic, e);
		LOG.info("Event has been sent successfully for alert Notification Id {}", e.getHeader().getPartitionKey());
	}

	private Event createEventWithHeader(Long notificationId, String eventType) {

		LOG.info("creating the event header for event type:{}", eventType);
		//SETTING HEADER
		Header h = new Header();
		h.addParam("eventType", eventType);
		h.addParam("isAggregatorBased", false);
		h.setPartitionKey(notificationId.toString());
		h.setSource(eventBusHeaderSystemName);

		Event e = new Event();
		e.setHeader(h);
		return e;
	}

	@PreDestroy
	public void preDestroy() {
		LOG.info("Destroying the event producer");
		if(null!=eventProducer)
			eventProducer.close();
	}
}
