package com.snapdeal.scm.mongo.doc;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author prateek
 */
@Document(collection = "supc_details")
public class SupcDetails extends MongoDocument {

    private String  supc;
    private String  superCategory;
    private String mto;

    public SupcDetails() {

    }

    public SupcDetails(String supc, String superCategory, String mto) {
        this.supc = supc;
        this.superCategory = superCategory;
        this.mto = mto;
    }

    public SupcDetails(String supc) {
    	this.supc = supc;
	}

	public String getSupc() {
        return supc;
    }

    public void setSupc(String supc) {
        this.supc = supc;
    }

    public String getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(String superCategory) {
        this.superCategory = superCategory;
    }

    public String getMto() {
        return mto;
    }

    public void setMto(String mto) {
        this.mto = mto;
    }

    @Override
    public String toString() {
        return "SupcDetails [supc=" + supc + ", superCategory=" + superCategory + ", mto=" + mto + "]";
    }
}
