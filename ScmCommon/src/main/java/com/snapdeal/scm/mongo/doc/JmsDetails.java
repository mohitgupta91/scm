package com.snapdeal.scm.mongo.doc;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * JmsDetails : Hold Common jms configuration details
 * 
 * @author pranav
 */
@Document(collection = "jms_details")
public class JmsDetails extends MongoDocument {

    private String jmsBrokerUrl;
    private String uname;
    private String pwd;
    private String destPollerToFH;
    private String destFHToProcessor;
    private String destAlerts;

    public JmsDetails(String jmsBrokerUrl, String uname, String pwd, String destPollerToFH,
    		String destFHToProcessor, String destAlerts) {
        super();
        this.jmsBrokerUrl = jmsBrokerUrl;
        this.uname = uname;
        this.pwd = pwd;
        this.destPollerToFH = destPollerToFH;
        this.destFHToProcessor = destFHToProcessor;
        this.destAlerts=destAlerts;
    }

    public JmsDetails() {
    }

    public String getJmsBrokerUrl() {
        return jmsBrokerUrl;
    }

    public void setJmsBrokerUrl(String jmsBrokerUrl) {
        this.jmsBrokerUrl = jmsBrokerUrl;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDestPollerToFH() {
        return destPollerToFH;
    }

    public void setDestPollerToFH(String destPollerToFH) {
        this.destPollerToFH = destPollerToFH;
    }

    public String getDestFHToProcessor() {
        return destFHToProcessor;
    }

    public void setDestFHToProcessor(String destFHToProcessor) {
        this.destFHToProcessor = destFHToProcessor;
    }

    @Override
    public String toString() {
        return "JmsDetails [jmsBrokerUrl=" + jmsBrokerUrl + ", uname=" + uname + ", pwd=" + pwd + ", destPollerToFH=" + destPollerToFH + ", destFHToProcessor=" + destFHToProcessor
                + "]";
    }

	public String getDestAlerts() {
		return destAlerts;
	}

	public void setDestAlerts(String destAlerts) {
		this.destAlerts = destAlerts;
	}
}
