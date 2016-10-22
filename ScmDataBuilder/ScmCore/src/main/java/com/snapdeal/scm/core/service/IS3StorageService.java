/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.core.service;

import com.amazonaws.services.s3.model.S3Object;

/**
 * S3 storage Service
 * 
 * @version 1.0, 16-Feb-2016
 * @author ashwini
 */

public interface IS3StorageService {

    /**
     * getS3Object from S3 Storage Service
     * 
     * @param key
     * @return S3Object
     * @throws RunTimeException
     */
    public S3Object getObjectFromS3(String key);

}
