package com.snapdeal.scm.poller.service;

import java.util.Map;

import com.snapdeal.scm.poller.dto.EndTimeFutureExternalAPIResponse;

public interface IS3Service {

	public abstract void readS3Content ( Map<String, EndTimeFutureExternalAPIResponse> externalResponses) throws InterruptedException;

	public String processDirectory (String outPath, String jobNameStr);
	
	public String processFile(String fileNameString, String jobNameStr);
}