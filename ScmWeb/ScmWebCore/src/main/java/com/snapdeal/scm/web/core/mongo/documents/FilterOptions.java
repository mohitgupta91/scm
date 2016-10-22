package com.snapdeal.scm.web.core.mongo.documents;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

/**
 * Created by sahil on 25/2/16.
 */
@Document(collection = "filter_options")
public class FilterOptions extends MongoDocument {
    private String                    filterKey;
    private String                    dependsOn;
    private List<String>              options;
    private Map<String, List<String>> optionMapping;
    private String                    defaults;
    private String                    type;
    private String                    range;

    public FilterOptions() {
    }

    public FilterOptions(String filterKey, String dependsOn, List<String> options, Map<String, List<String>> optionMapping, String defaults, String type, String range) {
        super();
        this.filterKey = filterKey;
        this.dependsOn = dependsOn;
        this.options = options;
        this.optionMapping = optionMapping;
        this.defaults = defaults;
        this.type = type;
        this.range = range;
    }

    public String getFilterKey() {
        return filterKey;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }

    public String getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Map<String, List<String>> getOptionMapping() {
        return optionMapping;
    }

    public void setOptionMapping(Map<String, List<String>> optionMapping) {
        this.optionMapping = optionMapping;
    }

    public String getDefaults() {
        return defaults;
    }

    public void setDefaults(String defaults) {
        this.defaults = defaults;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "FilterOptions [filterKey=" + filterKey + ", dependsOn=" + dependsOn + ", options=" + options + ", optionMapping=" + optionMapping + ", defaults=" + defaults
                + ", type=" + type + ", range=" + range + "]";
    }

}