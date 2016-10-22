/**
 * 
 */
package com.snapdeal.scm.poller.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author gaurav
 *
 */
public interface JobRepository extends MongoRepository<Job, String>{
	
	public Job findByJobName(String jobName);
	public Job findByJobID(String jobID);
	public List<Job> findByEnabledIsTrue();

}
