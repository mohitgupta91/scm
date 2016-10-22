/**
 * 
 */
package com.snapdeal.scm.poller.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.jms.impl.FileHandlerProducer;
import com.snapdeal.scm.core.poller.dto.impl.PollerQueueDto;
import com.snapdeal.scm.poller.dto.EndTimeFutureExternalAPIResponse;
import com.snapdeal.scm.poller.dto.ExternalAPIResponse;
import com.snapdeal.scm.poller.model.Job;
import com.snapdeal.scm.poller.model.JobRepository;
import com.snapdeal.scm.poller.service.IS3Service;
import com.snapdeal.scm.poller.util.Constants;

/**
 * @author gaurav
 */
@Service
public class S3ServiceImpl implements IS3Service {

	private static final Logger LOG = LoggerFactory.getLogger(S3ServiceImpl.class);
	
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private FileHandlerProducer fileHandlerProducer;

    @Autowired
    private JobRepository repository;

    /* (non-Javadoc)
     * @see com.snapdeal.scm.poller.service.impl.IS3Service#readS3Content(java.util.List)
     */
    @Override
    public void readS3Content(Map<String, EndTimeFutureExternalAPIResponse> externalResponses) {
    	
    }
    /*public void readS3Content(Map<String, EndTimeFutureExternalAPIResponse> externalResponses) {
        ExternalAPIResponse externalAPIResponse = null;
        for (String jobNameStr : externalResponses.keySet()) {
            EndTimeFutureExternalAPIResponse endTimeFutureExternalAPIResponse = externalResponses.get(jobNameStr);
            Future<ExternalAPIResponse> future = endTimeFutureExternalAPIResponse.getExternalAPIResponse();
            try {
                externalAPIResponse = future.get();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            if (externalAPIResponse != null && externalAPIResponse.getResponseCode().equals(com.snapdeal.scm.poller.util.Constants.OK)) {

                Job job = repository.findByJobName(jobNameStr);
                job.setTimestamp(endTimeFutureExternalAPIResponse.getEndTimestamp());
                repository.save(job);

                // We have paths available now.
                List<String> outPaths = externalAPIResponse.getAllOutPaths();

                for (String outPath : outPaths) {
                    // Read through each path and get all file names.
                    //					List<String> filenames = null;
                    processDirectory(outPath, jobNameStr);
                }
            }
        }

    }*/

	@Override
	public String processDirectory(String outPath, String jobNameStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String processFile(String fileNameString, String jobNameStr) {
		// TODO Auto-generated method stub
		return null;
	}

   /* @Override
    public String processDirectory(String outPath, String jobNameStr) {
        StringBuilder returnVal = new StringBuilder();
        AmazonS3URI uri = new AmazonS3URI(outPath);

        ObjectListing objListing = s3Client.listObjects(uri.getBucket(), uri.getKey());
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
                String result = processFile(fileNameString, jobNameStr);
                returnVal.append(result);
                returnVal.append(System.getProperty("line.separator"));
            }

        }
        return returnVal.toString();
    }
*/
   /* @Override
    public String processFile(String fileNameString, String jobNameStr) {
        QueryType jobName = QueryType.valueOf(jobNameStr);

        final PollerQueueDto pollerQueueDto = new PollerQueueDto(fileNameString, jobName);*/

        // Push this to Queue.
/*        try {
            jmsTemplate.send(jmsDetails.getDestPollerToFH(), new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createObjectMessage(pollerQueueDto);
                }
            });
        } catch (JmsException exception) {
            return "File Upload Failed " + fileNameString + "  " + exception.getMessage();
        }*/
       /* 
        try {
			fileHandlerProducer.sendInSync(pollerQueueDto);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        LOG.info(pollerQueueDto.toString());
        return "File Uploaded Successfully " + fileNameString;
    }
*/
}
