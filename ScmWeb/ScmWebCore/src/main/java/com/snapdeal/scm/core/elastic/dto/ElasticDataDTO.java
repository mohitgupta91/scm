package com.snapdeal.scm.core.elastic.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

/**
 * ElasticDataDTO : Elastic data dto
 *
 * @author prateek, pranav
 */

/**
 * @author pranav
 *
 */
public class ElasticDataDTO {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDataDTO.class);

    private String subOrderCode;
    private EnumMap<SubOrderDetailElasticColumn, Object> dataValueMap = new EnumMap<SubOrderDetailElasticColumn, Object>(SubOrderDetailElasticColumn.class);

    public ElasticDataDTO() {
    }

    public ElasticDataDTO(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public ElasticDataDTO(String id, Map<String, Object> sourceAsMap) {
        this.subOrderCode = id;
        for (String colName : sourceAsMap.keySet()) {
            try {
                SubOrderDetailElasticColumn key = SubOrderDetailElasticColumn.valueOf(colName.toUpperCase());
                if (key != null) {
                    this.dataValueMap.put(key, sourceAsMap.get(colName));
                }
            } catch (Exception ex) {
                LOG.error("Unable to parse colName " + colName + " While converting Elastic Dto from Elastic Response Source map, Will escape this value");
            }
        }
    }

    public String getSubOrderCode() {
        return subOrderCode;
    }

    public void setSubOrderCode(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public void addValue(SubOrderDetailElasticColumn schemaKey, Object value) {
        if (value != null) {
            this.dataValueMap.put(schemaKey, value);
        }
    }

    public void addAllDataValues(EnumMap<SubOrderDetailElasticColumn, Object> values) {
        values.forEach((schemaKey, value) -> addValue(schemaKey, value));
    }

    public EnumMap<SubOrderDetailElasticColumn, Object> getDataValueMap() {
        return dataValueMap;
    }

    public void setDataValueMap(EnumMap<SubOrderDetailElasticColumn, Object> dataValueMap) {
        this.dataValueMap = new EnumMap<SubOrderDetailElasticColumn, Object>(SubOrderDetailElasticColumn.class);
        dataValueMap.forEach((schemaKey, value) -> addValue(schemaKey, value));
    }

    @Override
    public String toString() {
        return "ElasticDataDTO [subOrderCode=" + subOrderCode
               + ", dataValueMap=" + dataValueMap + "]";
    }


}
