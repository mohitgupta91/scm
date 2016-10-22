/**
 * 
 */
package com.snapdeal.scm.poller.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.cache.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.scm.cache.ICache;
import com.snapdeal.scm.poller.model.Job;
import com.snapdeal.scm.poller.model.JobRepository;

/**
 * @author gaurav
 *
 */
@Cache(cacheKey= CacheKey.JOBS, cacheReload=false, MIN_REPEAT_TIME_IN_MINUTE=60)
@Service
public class JobCache implements ICache{
	
	@Autowired
	private JobRepository repository;
	
	Map<String, Job> jobNameToDetail = new HashMap<>();
	
	List<Job> allJobs = new ArrayList<Job>();
	List<Job> enabledJobs = new ArrayList<Job>();
	
	public List<Job> getJobs() {
		if (allJobs == null || allJobs.size() == 0) {
			load();
		}
		return allJobs;
	}
	
	public List<Job> getEnabledJobs() {
		if (allJobs == null || allJobs.size() == 0) {
			load();
		}
		return enabledJobs;
	}
	
	/* (non-Javadoc)
	 * @see com.snapdeal.scm.poller.cache.impl.IJobCache#getJobByJobName(java.lang.String)
	 */
	public Job getJobByJobName(String jobName) {
		return jobNameToDetail.get(jobName);
	}
	
	/* (non-Javadoc)
	 * @see com.snapdeal.scm.poller.cache.impl.IJobCache#addJobToJobDetail(java.lang.String, com.snapdeal.scm.poller.model.Job)
	 */
	public void addJobToJobDetail(String jobName, Job job) {
		jobNameToDetail.put(jobName, job);
	}

	/* (non-Javadoc)
	 * @see com.snapdeal.scm.poller.cache.impl.IJobCache#load()
	 */
	@Override
	public void load() {
		List<Job> jobs = repository.findAll();
		allJobs = jobs;
		for (Job job : jobs) {
			jobNameToDetail.put(job.getJobName(), job);
			if (job.isEnabled()) {
				enabledJobs.add(job);
			}
		}
	}
}
