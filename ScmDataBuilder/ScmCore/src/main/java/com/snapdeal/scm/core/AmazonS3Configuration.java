/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Register Amazon S3 client
 * 
 * @version 1.0, 16-Feb-2016
 * @author ashwini
 */

@Configuration
public class AmazonS3Configuration {

    private static AmazonS3 s3Client = null;

    @Value("${amazon.s3.access.id}")
    String accessId;

    @Value("${amazon.s3.secret.key}")
    String secretKey;

    @Value("${amazon.s3.region}")
    String region;

    @Bean
    public AmazonS3 getS3Client() {
        if (s3Client == null) {
            AWSCredentials s3Credentials = new BasicAWSCredentials(accessId, secretKey);
            s3Client = new AmazonS3Client(s3Credentials);
            s3Client.setRegion(Region.getRegion(Regions.fromName(region)));
        }
        return s3Client;
    }
}
