package com.snapdeal.scm.mongo.doc;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * ScmJmsProperty : Hold App and its related Jms Configuration for a Machine
 * 
 * @author pranav
 */
@Document(collection = "scm_jms_machine_property")
public class ScmJmsMachineProperty extends MongoDocument {

    private String  machineHostName;
    private String  fhConcurrency;
    private String  processorConcurrency;
    private String  alertsConcurrency;
    private boolean isDataPoller;
    private boolean isFileHandler;
    private boolean isDataProcessor;
    private boolean isAlertConsumer;
    private boolean isAlertProducer;
    private boolean isProcessorScheduler;
    private int     maxRedeliveryAttempt;
    private long    redeliveryDelayInMS;

    public ScmJmsMachineProperty() {

    }

    public ScmJmsMachineProperty(String hostName) {
        this.machineHostName = hostName;
    }

    public String getMachineHostName() {
        return machineHostName;
    }

    public void setMachineHostName(String machineHostName) {
        this.machineHostName = machineHostName;
    }

    public String getFhConcurrency() {
        return fhConcurrency;
    }

    public void setFhConcurrency(String fhConcurrency) {
        this.fhConcurrency = fhConcurrency;
    }

    public String getProcessorConcurrency() {
        return processorConcurrency;
    }

    public void setProcessorConcurrency(String processorConcurrency) {
        this.processorConcurrency = processorConcurrency;
    }

    public boolean isDataPoller() {
        return isDataPoller;
    }

    public void setDataPoller(boolean isDataPoller) {
        this.isDataPoller = isDataPoller;
    }

    public boolean isFileHandler() {
        return isFileHandler;
    }

    public void setFileHandler(boolean isFileHandler) {
        this.isFileHandler = isFileHandler;
    }

    public boolean isDataProcessor() {
        return isDataProcessor;
    }

    public void setDataProcessor(boolean isDataProcessor) {
        this.isDataProcessor = isDataProcessor;
    }

    public int getMaxRedeliveryAttempt() {
        return maxRedeliveryAttempt;
    }

    public void setMaxRedeliveryAttempt(int maxRedeliveryAttempt) {
        this.maxRedeliveryAttempt = maxRedeliveryAttempt;
    }

    public long getRedeliveryDelayInMS() {
        return redeliveryDelayInMS;
    }

    public void setRedeliveryDelayInMS(long redeliveryDelayInMS) {
        this.redeliveryDelayInMS = redeliveryDelayInMS;
    }

	@Override
	public String toString() {
		return "ScmJmsMachineProperty [machineHostName=" + machineHostName
				+ ", fhConcurrency=" + fhConcurrency
				+ ", processorConcurrency=" + processorConcurrency
				+ ", alertsConcurrency=" + alertsConcurrency
				+ ", isDataPoller=" + isDataPoller + ", isFileHandler="
				+ isFileHandler + ", isDataProcessor=" + isDataProcessor
				+ ", isAlertConsumer=" + isAlertConsumer + ", isAlertProducer="
				+ isAlertProducer + ", isProcessorScheduler="
				+ isProcessorScheduler + ", maxRedeliveryAttempt="
				+ maxRedeliveryAttempt + ", redeliveryDelayInMS="
				+ redeliveryDelayInMS + "]";
	}

	public boolean isProcessorScheduler() {
		return isProcessorScheduler;
	}

	public void setProcessorScheduler(boolean isProcessorScheduler) {
		this.isProcessorScheduler = isProcessorScheduler;
	}

	public String getAlertsConcurrency() {
		return alertsConcurrency;
	}

	public void setAlertsConcurrency(String alertsConcurrency) {
		this.alertsConcurrency = alertsConcurrency;
	}

	public boolean isAlertConsumer() {
		return isAlertConsumer;
	}

	public void setAlertConsumer(boolean isAlertConsumer) {
		this.isAlertConsumer = isAlertConsumer;
	}

	public boolean isAlertProducer() {
		return isAlertProducer;
	}

	public void setAlertProducer(boolean isAlertProducer) {
		this.isAlertProducer = isAlertProducer;
	}
}
