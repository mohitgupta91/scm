package com.snapdeal.scm.core.elastic.dto;

import com.snapdeal.scm.core.enums.ElasticScriptKey;
import com.snapdeal.scm.core.utils.ElasticScriptsUtil;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author prateek
 */
public class ElasticScriptDataDTO {

    private String subOrderCode;
    private EnumMap<SubOrderDetailElasticColumn, Object> dataValueMap = new EnumMap<SubOrderDetailElasticColumn, Object>(SubOrderDetailElasticColumn.class);
    private Set<ElasticScriptKey>                        scripts      = new HashSet<>();
    private Map<String, Object>                          params       = new HashMap<>();

    public ElasticScriptDataDTO(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public String getSubOrderCode() {
        return subOrderCode;
    }

    public void setSubOrderCode(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public void setScripts(Set<ElasticScriptKey> scripts) {
        this.scripts = scripts;
    }

    public Set<ElasticScriptKey> getScripts() {
        return scripts;
    }

    public void addScript(ElasticScriptKey script) {
        this.scripts.add(script);
    }

    public void addAllScripts(Set<ElasticScriptKey> scripts) {
        this.scripts.addAll(scripts);
    }

    public String getConcatedScripts() {
        if (CollectionUtils.isEmpty(scripts)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        scripts.forEach(script -> builder.append(ElasticScriptsUtil.getElasticScript(script)).append(";"));
        return builder.toString();
    }

    public EnumMap<SubOrderDetailElasticColumn, Object> getDataValueMap() {
        return dataValueMap;
    }

    public void setDataValueMap(EnumMap<SubOrderDetailElasticColumn, Object> dataValueMap) {
        this.dataValueMap = dataValueMap;
    }

    public void addDataValue(SubOrderDetailElasticColumn schemaKey, Object value) {
        if (value != null) {
            this.dataValueMap.put(schemaKey, value);
        }
    }

    public void addAllDataValues(EnumMap<SubOrderDetailElasticColumn, Object> values) {
        values.forEach((schemaKey, value) -> addDataValue(schemaKey, value));
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void addAllParams(Map<String, Object> params) {
        params.forEach((schemaKey, value) -> addParams(schemaKey, value));
    }

    public void addParams(String key, Object value) {
        this.params.put(key, value);
    }

    @Override
    public String toString() {
        return "ElasticScriptDataDTO [subOrderCode=" + subOrderCode
               + ", dataValueMap=" + dataValueMap + ", scripts=" + scripts
               + ", params=" + params + "]";
    }
}
