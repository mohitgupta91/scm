package com.snapdeal.scm.poller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty;
import com.snapdeal.scm.poller.service.IPollerTask;
import com.snapdeal.scm.poller.service.impl.PollerTaskImpl;

/**
 * PollerConfiguration :  Poller Configuration
 * 
 * @author pranav
 *
 */
@Configuration
@EnableScheduling
public class PollerConfiguration {
	
	@Autowired
	private ScmJmsMachineProperty machineProp;

	// do not use reference of this bean anywhere.
	@Bean
	public IPollerTask startScheduler(){
		if(!machineProp.isDataPoller())
			return null;
		return new PollerTaskImpl();
	}
}
