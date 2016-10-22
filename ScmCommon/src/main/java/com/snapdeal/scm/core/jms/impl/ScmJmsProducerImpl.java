package com.snapdeal.scm.core.jms.impl;

import com.nurkiewicz.asyncretry.AsyncRetryExecutor;
import com.nurkiewicz.asyncretry.RetryExecutor;
import com.snapdeal.scm.core.jms.IScmJmsProducer;

import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by vinay on 19/2/16.
 */
public abstract class ScmJmsProducerImpl<T extends Serializable> implements IScmJmsProducer<T> {
	
	private static final Logger errorLOG = LoggerFactory.getLogger("DATAPROCESSOR-ERROR-LOGGER");

    private String destinationQueue;

    private RetryExecutor retryExecutor;

    private ScheduledExecutorService scheduler;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${scm.jms.producer.max.attempts:6}")
    private int maxAttempts;

    @Value("${scm.jms.producer.retry.delay.in.ms:5000}")
    private long retryDelayInMs;

    @Value("${scm.jms.producer.exponential.backoff:false}")
    private boolean exponentialBackoff;

    @Value("${scm.jms.producer.backoff.multiplier:2}")
    private int backOffMultiplier;

    @Value("${scm.jms.producer.max.delay.in.ms:120000}")
    private long maxDelayInMs;

    @PostConstruct
    void setup(){
        scheduler = Executors.newSingleThreadScheduledExecutor();
        AsyncRetryExecutor baseExecutor = new AsyncRetryExecutor(scheduler).
                retryOn(JmsException.class).
                withMaxDelay(maxDelayInMs).
                withUniformJitter().
                withMaxRetries(maxAttempts);

        retryExecutor = (exponentialBackoff)? baseExecutor.withExponentialBackoff(retryDelayInMs, backOffMultiplier)
                : baseExecutor.withFixedBackoff(retryDelayInMs);
    }

    @PreDestroy
    void cleanUp(){
        if (scheduler!=null && !scheduler.isShutdown()) scheduler.shutdown();
    }

    public ScmJmsProducerImpl(){
    }

    public ScmJmsProducerImpl(String destinationQueue){
        this.destinationQueue = destinationQueue;
    }

    protected String getDestinationQueue() {
        return Optional.ofNullable(destinationQueue).
                orElseThrow(() -> new IllegalStateException("No \'destinationQueue\' specified. Check configuration of ScmJmsProducer."));
    }

    protected void setDestinationQueue(String destinationQueue) {
        this.destinationQueue = destinationQueue;
    }

    /**
     * Async Method.
     * Publish a message to destination queue without delay.
     * Any JmsException while publishing is handled and action is retried 'maxAttempts' times.
     * @param message
     */
    @Override
    public void send(T message) {
        send(message, 0);
    }

    /**
     * Async Method.
     * Publish a message to destination queue with specified delay.
     * Any JmsException while publishing is handled and action is retried 'maxAttempts' times.
     * @param message
     * @param delayInMS
     */
    @Override
    public void send(T message, long delayInMS) {
        retryExecutor.doWithRetry((context) ->
                jmsTemplate.send(getDestinationQueue(), (session) -> {
                    ObjectMessage queueMessage = session.createObjectMessage(message);
                    if (delayInMS > 0)
                        queueMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delayInMS);
                    return queueMessage;
                }));
    }

    /** Sync Method.
	 * Publish a message to destination queue in sync.
	 * @param message
	 * @throws JMSException
	 */
	@Override
	public void sendInSync(T message) throws JMSException{
		sendInSync(message, 0);
	}
	
	/**
	 * Sync Method.
	 * Publish a message to destination queue in sync with specified delay.
	 * @param message
	 * @throws JMSException
	 */
	@Override
	public void sendInSync(T message, long delayInMS) throws JMSException{
		try {
			jmsTemplate.send(getDestinationQueue(), (session) -> {
				ObjectMessage queueMessage = session.createObjectMessage(message);
				if (delayInMS > 0)
					queueMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delayInMS);
				return queueMessage;
			});
		}catch (JmsException e){ //Note: JmsException is a RuntimeException
			errorLOG.error("JMSException in sendInSync destinationQueue : {}, exception : {}, errorCode : {}, message : {}", getDestinationQueue(), e.getMessage(), e.getErrorCode(), message.toString()); 
			throw new JMSException(e.getMessage(), e.getErrorCode());
		}
	}
}