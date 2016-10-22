package com.snapdeal.scm.core.jms;

import javax.jms.JMSException;
import java.io.Serializable;

/**
 * Created by vinay on 19/2/16.
 */
public interface IScmJmsProducer<T extends Serializable> {

    void send(T message);

    void send(T message, long delayInMS);

    void sendInSync(T message) throws JMSException;

	void sendInSync(T message, long delayInMS) throws JMSException;

}
