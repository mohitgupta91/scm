package com.snapdeal.scm.web.core.sro;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Harsh Gupta on 27/02/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class OptionSRO implements Serializable {

    private static final long serialVersionUID = -7191710901031348258L;

    private String key;
    private String value;

    public OptionSRO() {
    }

    public OptionSRO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OptionSRO optionSRO = (OptionSRO) o;

        if (key != null ? !key.equals(optionSRO.key) : optionSRO.key != null)
            return false;
        return value != null ? value.equals(optionSRO.value) : optionSRO.value == null;

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OptionSRO{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }
}
