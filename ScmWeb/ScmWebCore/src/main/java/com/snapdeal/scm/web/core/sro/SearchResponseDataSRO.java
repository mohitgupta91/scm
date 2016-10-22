package com.snapdeal.scm.web.core.sro;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author hasrh, chitransh
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponseDataSRO implements Serializable {

    private static final long serialVersionUID = -7191710901031348258L;

    private String type;
    private List<? extends IDataSetSRO> dataset;

    public SearchResponseDataSRO() {
    }

    public SearchResponseDataSRO(String type, List<? extends IDataSetSRO> dataset) {
        this.type = type;
        this.dataset = dataset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<? extends IDataSetSRO> getDataset() {
        return dataset;
    }

    public void setDataset(List<? extends IDataSetSRO> dataset) {
        this.dataset = dataset;
    }

    @Override
    public String toString() {
        return "SearchResponseDataSRO{" + "type='" + type + '\'' +
                ", dataset=" + dataset +
                '}';
    }
}
