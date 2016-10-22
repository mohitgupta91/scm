package com.snapdeal.scm.fh.service;

import com.snapdeal.scm.core.poller.dto.impl.PollerQueueDto;

/**
 * IFileHandlerService : File Handler Service
 * 
 * @author pranav
 */
public interface IFileHandlerService {

    public void processFile(PollerQueueDto dto) throws Exception;
}
