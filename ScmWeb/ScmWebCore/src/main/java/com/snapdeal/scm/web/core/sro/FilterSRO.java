/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.core.sro;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0, 25-Feb-2016
 * @author ashwini
 */

public class FilterSRO {

    private String                    name;
    private List<String>              options;
    private Map<String, List<String>> dependentOptions;
    private String                    dependsOn;
    private String                    dataUrl;
    private String                    defaults;
    private String                    type;
    private Boolean                   mandatory = Boolean.FALSE;
    private String                    range;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Map<String, List<String>> getDependentOptions() {
        return dependentOptions;
    }

    public void setDependentOptions(Map<String, List<String>> dependentOptions) {
        this.dependentOptions = dependentOptions;
    }

    public String getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
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

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "FilterSRO [name=" + name + ", options=" + options + ", dependentOptions=" + dependentOptions + ", dependsOn=" + dependsOn + ", dataUrl=" + dataUrl + ", defaults="
                + defaults + ", type=" + type + ", mandatory=" + mandatory + ", range=" + range + "]";
    }

}
