package com.snapdeal.scm.poller.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.scm.poller.model.Job;
import com.snapdeal.scm.poller.model.JobRepository;
import com.snapdeal.scm.poller.service.IPollerService;
/**
 * @author gaurav on 16-May-2016
 *
 */
@Service
public class PollerServiceImpl implements IPollerService {

	@Autowired
	JobRepository repository;

	@Override
	public boolean updateJob(String jobNameStr, Long timestamp, Boolean enabled) throws Exception{
		if (StringUtils.isNotBlank(jobNameStr) && null!=timestamp && null!=enabled) {
			Job job = repository.findByJobName(jobNameStr);
			if (null == job) {
				throw new Exception("Job Name is not correct");
			}
			job.setTimestamp(timestamp);
			job.setEnabled(enabled);
			repository.save(job);
		} else {
			throw new Exception("Mandatory parameters are blank jobNameStr: " + jobNameStr + " timestamp: " + timestamp + " enabled: " + enabled);
		}
		return true;
	}

}














