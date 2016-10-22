/**
 * 
 */
package com.snapdeal.scm.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.scm.cache.ICacheLoadService;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.poller.service.IAsyncJob;
import com.snapdeal.scm.poller.service.IPollerService;

/**
 * @author gaurav on 22-Feb-2016
 */
@Controller
@RequestMapping("/poller")
public class PollerController {

	@Autowired
	IAsyncJob asyncJob;

	@Autowired
	IPollerService pollerService;
	
	@Autowired
	ICacheLoadService cacheLoadService;

	private static final String DIRURL = "dirurl";

	private static final String FILEURL = "fileurl";

	private static final String JOBNAME = "jobname";

	private static final String TIMESTAMP = "timestamp";

	private static final String ENABLED = "enabled";


	@RequestMapping("/processDirectory")
	@ResponseBody
	public String processDirectory(@RequestParam(value = DIRURL, required = true) String outPath, 
			@RequestParam(value = JOBNAME, required = true) String jobNameStr) {
		QueryType queryType = QueryType.valueOf(jobNameStr);
		return new Boolean(asyncJob.processDirectory(outPath, queryType)).toString();
	}

	@RequestMapping("/processFile")
	@ResponseBody
	public String processFile(@RequestParam(value = FILEURL, required = true) String fileURL, 
			@RequestParam(value = JOBNAME, required = true) String jobNameStr) {
		QueryType queryType = QueryType.valueOf(jobNameStr);
		return new Boolean(asyncJob.processFile(fileURL, queryType)).toString();
	}

	@RequestMapping("/updateJob")
	@ResponseBody
	public String updateJob(@RequestParam(value = JOBNAME, required = true) String jobNameStr, 
			@RequestParam(value = TIMESTAMP, required = true) Long timestamp, @RequestParam(value = ENABLED, required = true) Boolean enabled) {
		try {
			pollerService.updateJob(jobNameStr, timestamp, enabled);
			cacheLoadService.loadAll(true);
		} catch (Exception e) {
			return e.getMessage();
		}
			return "Job is updated successfull and Cache is reloaded";
	}

	@RequestMapping("/health")
	@ResponseBody
	public String checkProcess() {
		return "Health is fine !";
	}
}
