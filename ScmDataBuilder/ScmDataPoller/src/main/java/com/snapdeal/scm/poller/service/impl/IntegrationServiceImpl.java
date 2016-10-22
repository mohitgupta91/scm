/**
 * 
 */
package com.snapdeal.scm.poller.service.impl;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.snapdeal.scm.poller.dto.ExternalAPIResponse;
import com.snapdeal.scm.poller.service.IIntegrationService;
import com.snapdeal.scm.poller.util.Constants;

/**
 * @author gaurav
 *
 */
@Service
public class IntegrationServiceImpl implements IIntegrationService{

	private static final Logger LOG = LoggerFactory.getLogger(IntegrationServiceImpl.class);
	
	@Override
	@Async
	public Future<ExternalAPIResponse> getDataFromExternalAPI(String url) {
		return null;
	}
	/*public Future<ExternalAPIResponse> getDataFromExternalAPI(String url) {
		LOG.info("In ExternalAPIResponse");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter()); 

		ExternalAPIResponse externalAPIResponse = restTemplate.getForObject(url, ExternalAPIResponse.class);
		
		LOG.info("Response from " + url  + " is " + externalAPIResponse);
		if (externalAPIResponse != null && externalAPIResponse.getResponseCode().equals(Constants.OK)) {
			// We have paths available now.
		}
		return new AsyncResult<ExternalAPIResponse>(externalAPIResponse);
	}*/

}