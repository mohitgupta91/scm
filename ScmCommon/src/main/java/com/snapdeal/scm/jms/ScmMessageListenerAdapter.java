package com.snapdeal.scm.jms;

import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * ScmMessageListenerAdapter : Adapter that holds consumer reference while setting up spring message listeners
 * 
 * Created by vinay on 29/2/16.
 */
public class ScmMessageListenerAdapter extends MessageListenerAdapter {

    public ScmMessageListenerAdapter(Object delegate){
        super(delegate);
    }

    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        try {
            super.onMessage(message, session);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
