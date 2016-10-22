package com.snapdeal.scm.jms;

/**
 * ScmJmsConsumer : Jms Consumer must implement this interface
 * 
 * @author pranav
 *
 */
public interface ScmJmsConsumer {

	 public void receiveMessage(Object message) throws Exception;
}
