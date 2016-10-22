package com.snapdeal.scm.web.core.mongo.documents;

import java.util.List;
import java.util.Map;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.web.core.sro.OptionSRO;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by heer on 25/2/16.
 */

@Document(collection = "metric_details")
public class MetricDetails extends MongoDocument {

    private String                       metricID;
    private Map<String, Boolean>         filterKey;
    private Map<String, List<OptionSRO>> options;

    public String getMetricID() {
        return metricID;
    }

    public void setMetricID(String metricID) {
        this.metricID = metricID;
    }

    public Map<String, Boolean> getFilterKey() {
        return filterKey;
    }

    public void setFilterKey(Map<String, Boolean> filterKey) {
        this.filterKey = filterKey;
    }

    public Map<String, List<OptionSRO>> getOptions() {
        return options;
    }

    public void setOptions(Map<String, List<OptionSRO>> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MetricDetails other = (MetricDetails) obj;
        if (filterKey == null) {
            if (other.filterKey != null)
                return false;
        } else if (!filterKey.equals(other.filterKey))
            return false;
        if (metricID == null) {
            if (other.metricID != null)
                return false;
        } else if (!metricID.equals(other.metricID))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filterKey == null) ? 0 : filterKey.hashCode());
        result = prime * result + ((metricID == null) ? 0 : metricID.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "MetricDetails [metricID=" + metricID + ", filterKey=" + filterKey + ", options=" + options + "]";
    }
}
