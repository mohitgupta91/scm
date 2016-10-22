package com.snapdeal.scm.mongo.doc;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author prateek
 */
@Document(collection = "pincode_master")
public class PincodeMaster extends MongoDocument {

    String pincode;
    String city;
    String state;
    String zone;
    String scCity;
    String scState;
    String scZone;
    String bdeZone;

    public PincodeMaster() {

    }
    
    public PincodeMaster(String pincode, String city, String state,
			String zone, String scCity, String scState, String scZone,
			String bdeZone) {
		this.pincode = pincode;
		this.city = city;
		this.state = state;
		this.zone = zone;
		this.scCity = scCity;
		this.scState = scState;
		this.scZone = scZone;
		this.bdeZone = bdeZone;
	}


	public PincodeMaster(String pincode) {
    	this.pincode = pincode;
	}

	public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getScCity() {
        return scCity;
    }

    public void setScCity(String scCity) {
        this.scCity = scCity;
    }

    public String getScState() {
        return scState;
    }

    public void setScState(String scState) {
        this.scState = scState;
    }

    public String getScZone() {
        return scZone;
    }

    public void setScZone(String scZone) {
        this.scZone = scZone;
    }

    public String getBdeZone() {
        return bdeZone;
    }

    public void setBdeZone(String bdeZone) {
        this.bdeZone = bdeZone;
    }

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	@Override
	public String toString() {
		return "PincodeMaster [pincode=" + pincode + ", city=" + city
				+ ", state=" + state + ", zone=" + zone + ", scCity=" + scCity
				+ ", scState=" + scState + ", scZone=" + scZone + ", bdeZone="
				+ bdeZone + "]";
	}
}
