/**
 * 
 */
package com.snapdeal.scm.poller.model;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gaurav
 */
@Document(collection = "poller_job")
public class Job extends MongoDocument {

    String jobName;

    String jobID;

    long timestamp;

    boolean enabled;

    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return the jobID
     */
    public String getJobID() {
        return jobID;
    }

    /**
     * @param jobID the jobID to set
     */
    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Job [jobName=" + jobName + ", jobID=" + jobID  + ", timestamp=" + timestamp + ", enabled=" + enabled + "]";
    }

}
