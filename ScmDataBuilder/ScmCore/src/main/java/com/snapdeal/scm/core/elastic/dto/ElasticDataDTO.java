package com.snapdeal.scm.core.elastic.dto;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.utils.StringUtils;

import java.util.EnumMap;
import java.util.Map.Entry;

/**
 * ElasticDataDTO : Elastic data dto
 *
 * @author prateek, pranav
 */
public class ElasticDataDTO {

    private String subOrderCode;
    private EnumMap<SubOrderDetailElasticColumn, Object> dataValueMap = new EnumMap<SubOrderDetailElasticColumn, Object>(SubOrderDetailElasticColumn.class);
    private ElasticScriptDataDTO elasticScriptDataDTO;
    private MongoDocument        mongoDocument;

    public ElasticDataDTO(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public String getSubOrderCode() {
        return subOrderCode;
    }

    public void setSubOrderCode(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public void addDataValue(SubOrderDetailElasticColumn schemaKey, Object value) {
        if (value != null) {
            this.dataValueMap.put(schemaKey, value);
        }
    }

    public void addAllDataValues(EnumMap<SubOrderDetailElasticColumn, Object> values) {
        values.forEach((schemaKey, value) -> addDataValue(schemaKey, value));
    }

    public EnumMap<SubOrderDetailElasticColumn, Object> getDataValueMap() {
        return dataValueMap;
    }

    public void setDataValueMap(EnumMap<SubOrderDetailElasticColumn, Object> dataValueMap) {
        this.dataValueMap = new EnumMap<SubOrderDetailElasticColumn, Object>(SubOrderDetailElasticColumn.class);
        dataValueMap.forEach((schemaKey, value) -> addDataValue(schemaKey, value));
    }

    public boolean isContainInDataValueMap(SubOrderDetailElasticColumn subOrderDetailElasticColumn) {
        return dataValueMap.containsKey(subOrderDetailElasticColumn);
    }

    public ElasticScriptDataDTO getElasticScriptDataDTO() {
        return elasticScriptDataDTO;
    }

    public void setElasticScriptDataDTO(ElasticScriptDataDTO elasticScriptDataDTO) {
        this.elasticScriptDataDTO = elasticScriptDataDTO;
    }

    public MongoDocument getMongoDocument() {
        return mongoDocument;
    }

    public void setMongoDocument(MongoDocument mongoDocument) {
        this.mongoDocument = mongoDocument;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ElasticDataDTO [subOrderCode").append(StringUtils.COLON).append(subOrderCode);
        builder.append(StringUtils.COMMA).append(" dataValueMap").append(StringUtils.COLON).append("{");
        for (Entry<SubOrderDetailElasticColumn, Object> entrySet : dataValueMap.entrySet()) {
            builder.append(entrySet.getKey().ordinal()).append(StringUtils.COLON)
                    .append(entrySet.getValue()).append(StringUtils.COMMA);
        }
        builder.append("}").append("]");
        return builder.toString();
    }
}