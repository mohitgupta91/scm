package com.snapdeal.scm.poller.service;

import java.util.concurrent.Future;

import com.snapdeal.scm.poller.dto.ExternalAPIResponse;

public interface IIntegrationService {
	
	Future<ExternalAPIResponse> getDataFromExternalAPI(String url);

}
