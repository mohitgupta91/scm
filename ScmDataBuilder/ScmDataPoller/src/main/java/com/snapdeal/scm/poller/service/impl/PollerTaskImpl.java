/**
 * 
 */
package com.snapdeal.scm.poller.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.poller.dto.JobFuture;
import com.snapdeal.scm.poller.model.Job;
import com.snapdeal.scm.poller.model.JobRepository;
import com.snapdeal.scm.poller.service.IAsyncJob;
import com.snapdeal.scm.poller.service.IPollerTask;

/**
 * @author bhatia.gaurav@snapdeal.com
 */
public class PollerTaskImpl implements IPollerTask {

    private static final Logger LOG = LoggerFactory.getLogger(PollerTaskImpl.class);

    @Autowired
    IAsyncJob asyncJob;

    @Autowired
    private JobRepository repository;

    /* (non-Javadoc)
     * @see com.snapdeal.scm.poller.service.impl.IPollerTask#scheduleTask()
     */
    @Override
    @Scheduled(fixedDelayString = "${poller.task.fixed.delay.prop}")
    public void scheduleTask() {
//        PollerCache pollarCache = CacheManager.getInstance().getCache(PollerCache.class);
//        JobCache jobCache = CacheManager.getInstance().getCache(JobCache.class);

        List<Job> jobs = repository.findByEnabledIsTrue();

        LOG.info("Starting job {}.", jobs.toString());

        List<JobFuture> runJobs = new ArrayList<JobFuture>();
        long endTimestamp = Calendar.getInstance().getTimeInMillis();

        for (Job job : jobs) {
            LOG.info(job.toString());
            runJobs.add(asyncJob.runJob(job, endTimestamp));
        }

        
        for (JobFuture jobFuture : runJobs) {
        	QueryType queryType = null;
            Boolean status = null;
            Job job = null;

            queryType = jobFuture.getQueryType();
            try {
            	if (null != jobFuture && null != jobFuture.getStatus()) {
            		status = jobFuture.getStatus().get();
				} else {
					status = false;
					LOG.error("Job failed as jobFuture status is null: {}",  queryType);
				}
            } catch (InterruptedException | ExecutionException e) {
                status = false;
                LOG.error("Job failed: {} {}", queryType, e);
            }

            if (status) {
                job = repository.findByJobName(queryType.toString());
                job.setTimestamp(endTimestamp);
                repository.save(job);
            }
        }
    }
}
