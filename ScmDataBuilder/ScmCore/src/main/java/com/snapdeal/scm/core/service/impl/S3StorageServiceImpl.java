/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.snapdeal.scm.core.service.IS3StorageService;

/**
 * S3 storage Service
 * 
 * @version 1.0, 16-Feb-2016
 * @author ashwini
 */

@Service("s3StorageService")
public class S3StorageServiceImpl implements IS3StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(S3StorageServiceImpl.class);

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public S3Object getObjectFromS3(String key) {
        try {
            AmazonS3URI uri = new AmazonS3URI(key);
            GetObjectRequest rangeObjectRequest = new GetObjectRequest(uri.getBucket(), uri.getKey());
            S3Object s3object = s3Client.getObject(rangeObjectRequest);
            return s3object;

        } catch (AmazonServiceException ase) {
            LOG.info("Caught an AmazonServiceException, which" + " means your request made it " + "to Amazon S3, but was rejected with an error response" + " for some reason.");
            LOG.info("Error Message:    " + ase.getMessage());
            LOG.info("HTTP Status Code: " + ase.getStatusCode());
            LOG.info("AWS Error Code:   " + ase.getErrorCode());
            LOG.info("Error Type:       " + ase.getErrorType());
            LOG.info("Request ID:       " + ase.getRequestId());
            throw ase;
        } catch (AmazonClientException ace) {
            LOG.info("Caught an AmazonClientException, which means" + " the client encountered " + "an internal error while trying to " + "communicate with S3, "
                    + "such as not being able to access the network.");
            LOG.info("Error Message: " + ace.getMessage());
            throw ace;
        }
    }
}