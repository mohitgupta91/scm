/**
 *
 */
package com.snapdeal.scm.poller.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.JMSException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.jms.impl.FileHandlerProducer;
import com.snapdeal.scm.core.poller.dto.impl.PollerQueueDto;
import com.snapdeal.scm.poller.cache.impl.PollerCache;
import com.snapdeal.scm.poller.dto.ExternalAPIResponse;
import com.snapdeal.scm.poller.dto.JobFuture;
import com.snapdeal.scm.poller.model.Job;
import com.snapdeal.scm.poller.service.IAsyncJob;
import com.snapdeal.scm.poller.util.Constants;

/**
 * @author gaurav, Ashwini on 26-Feb-2016
 */
@Service
public class AsyncJob implements IAsyncJob {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncJob.class);

    @Value("${dp.api.enabled}")
    private String isDPAPIEnabled;

    @Value("${amazon.s3.url}")
    private String dpS3URL;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private FileHandlerProducer fileHandlerProducer;

    @Override
    @Async
    public JobFuture runJob(Job job, long endTimestamp) {
        JobFuture jobFuture = new JobFuture();

        boolean returnValue = false;

        PollerCache pollarCache = CacheManager.getInstance().getCache(PollerCache.class);
        QueryType queryType = QueryType.valueOf(job.getJobName());
        DateTime lastTime = new DateTime(job.getTimestamp());

        jobFuture.setQueryType(queryType);
        ExternalAPIResponse externalAPIResponse = new ExternalAPIResponse();
        if (Boolean.parseBoolean(isDPAPIEnabled)) {
            long timestamp = job.getTimestamp();
            String delayStorage = pollarCache.getProperty(Constants.DELAY_STORAGE);
            if (null != delayStorage) {
                timestamp = timestamp - Long.parseLong(delayStorage);
            }
            String apiURL = pollarCache.getAPIURL();
            String jobURL = String.format(apiURL, job.getJobID(), timestamp, endTimestamp);
            LOG.info("Job Url for job {}-{} is {}.", job.getJobID(), job.getJobName(), jobURL);
            externalAPIResponse = getDataFromExternalAPI(jobURL);
        } else {
            String dynamicPath = (new StringBuilder()).append(lastTime.getYear()).append("/").append(lastTime.getMonthOfYear()).append("/").append(lastTime.getDayOfMonth()).append(
                    "/").append(lastTime.getHourOfDay()).toString();
            externalAPIResponse.setAllOutPaths(Arrays.asList((new StringBuilder()).append(dpS3URL).append("/").append(job.getId()).append("/").append(dynamicPath).toString()));
            externalAPIResponse.setResponseCode(Constants.OK);
        }
        if (externalAPIResponse != null
                && externalAPIResponse.getResponseCode().equals(Constants.OK)
                ) {
            List<String> outPaths = externalAPIResponse.getAllOutPaths();
            if (null != outPaths && outPaths.size() > 0) {
                returnValue = readS3Content(externalAPIResponse, queryType);
                jobFuture.setStatus(new AsyncResult<Boolean>(returnValue));
            } else {
                LOG.info("Response for {} is OK but no outpaths received.", job);
            }
        }
        return jobFuture;
    }

    public ExternalAPIResponse getDataFromExternalAPI(String url) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        ExternalAPIResponse externalAPIResponse = restTemplate.getForObject(url, ExternalAPIResponse.class);

        LOG.info("Response from: {} is: {}.", url, externalAPIResponse);

        return (externalAPIResponse);
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        PollerCache pollarCache = CacheManager.getInstance().getCache(PollerCache.class);
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(Integer.parseInt(pollarCache.getProperty(Constants.REST_READ_TIMEOUT)));
        factory.setConnectTimeout(Integer.parseInt(pollarCache.getProperty(Constants.REST_CONNECT_TIMEOUT)));
        return factory;
    }

    private boolean readS3Content(ExternalAPIResponse externalAPIResponse, QueryType queryType) {
        boolean returnValue = true;

        if (externalAPIResponse != null && externalAPIResponse.getResponseCode().equals(Constants.OK)) {

            // We have paths available now.
            List<String> outPaths = externalAPIResponse.getAllOutPaths();
            if (null != outPaths && outPaths.size() > 0) {
                boolean directoryProcessed = false;
                for (String outPath : outPaths) {
                    LOG.info("Processing directory: {}. ", outPath);

                    directoryProcessed = processDirectory(outPath, queryType);
                    if (!directoryProcessed) {
                        LOG.error("Failed processing directory: {}.", outPath);
                        returnValue = false;
                        break;
                    }
                }
            } else {
                returnValue = false;
            }
        }
        return returnValue;
    }

    @Override
    public boolean processDirectory(String outPath, QueryType queryType) {
        boolean returnVal = true;
        String updatedOutPath = outPath.replaceAll("s3n://", "s3://");
        AmazonS3URI uri = new AmazonS3URI(updatedOutPath);

        ObjectListing objListing = null;
        try {
            objListing = s3Client.listObjects(uri.getBucket(), uri.getKey());
        } catch (Exception e) { // throws AmazonClientException, AmazonServiceException;
            returnVal = false;
            LOG.error("Directory listing failed: {} for queryType: {}.", updatedOutPath, queryType);
        }

        if (returnVal) {
            List<S3ObjectSummary> filenames = objListing.getObjectSummaries();

            String fileNameString = null;
            for (S3ObjectSummary filename : filenames) {
                fileNameString = String.format(Constants.S3_STRING, filename.getBucketName() + Constants.DIRECTORY_SEPARATOR, filename.getKey());
                Pattern pattern = Pattern.compile(Constants.CRC_PATTERN_STRING);

                Matcher matcher = pattern.matcher(fileNameString);
                boolean matches = matcher.matches();
                // put a regex as filename should NOT be of type part-00009.crc as directory contains crc files also.

                // if regex passes
                if (!matches) {
                    returnVal = processFile(fileNameString, queryType);

                    if (!returnVal) {
                        LOG.error("Directory listing failed as file failed: {} for queryType: {}.", fileNameString, queryType);
                        break;
                    }
                }
            }
        }
        return returnVal;
    }
    
    @Override
    public boolean processFile(String fileNameString, QueryType queryType) {
        boolean returnValue = false;
        String updatedFileNameString = fileNameString.replaceAll("s3n://", "s3://");
        final PollerQueueDto pollerQueueDto = new PollerQueueDto(updatedFileNameString, queryType);
        LOG.info(pollerQueueDto.toString());

        try {
            fileHandlerProducer.sendInSync(pollerQueueDto);
            returnValue = true;
        } catch (JMSException e) {
            LOG.error("File Failed: {} and exception is {}.", pollerQueueDto.toString(), e);
        }
        return returnValue;
    }
}
