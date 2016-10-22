/**
 * 
 */
package com.snapdeal.scm.poller.service;

import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.poller.dto.JobFuture;
import com.snapdeal.scm.poller.model.Job;

/**
 * @author gaurav on 26-Feb-2016
 *
 */
public interface IAsyncJob {
	
	public JobFuture runJob(Job job, long endTime);
	
	public boolean processDirectory(String outPath, QueryType queryType);
	
	public boolean processFile(String fileNameString, QueryType queryType);

}
