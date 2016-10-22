package com.snapdeal.scm.poller.service;


/**
 * @author gaurav on 16-May-2016
 *
 */
public interface IPollerService {
	public boolean updateJob(String jobNameStr, Long timestamp, Boolean enabled)throws Exception;
}
