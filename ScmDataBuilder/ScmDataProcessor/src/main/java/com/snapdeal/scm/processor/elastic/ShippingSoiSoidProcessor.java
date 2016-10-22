package com.snapdeal.scm.processor.elastic;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.ShippingSoiSoidDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.mongo.document.ConnectedRadStatus;
import com.snapdeal.scm.core.utils.StringUtils;
import com.snapdeal.scm.das.mongo.dao.ConnectedRadStatusRepository;
import com.snapdeal.scm.processor.exception.InsufficientDataException;
import com.snapdeal.scm.processor.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <p> Query: ""
 * <p> Key  : {@link SubOrderDetailElasticColumn#SUB_ORDER_CODE}
 * <p> Data : {@link SubOrderDetailElasticColumn#SHIPPING_MODE}, {@link SubOrderDetailElasticColumn#DELIVERY_TYPE}
 *
 * @author prateek
 */
@Service("ShippingSoiSoidProcessor")
public class ShippingSoiSoidProcessor extends AbstractElasticProcessor<ShippingSoiSoidDTO> {

    @Autowired
    private ConnectedRadStatusRepository connectedRadStatusRepository;

    // TODO move to some static place
    // Attribute Name
    private static String SHIPPING_MODE = "SHIPPING_MODE";
    private static String DELIVERY_TYPE = "DELIVERY_TYPE";

    @Override
    public ElasticDataDTO processRecord(ShippingSoiSoidDTO shippingSoiSoidDTO)
            throws InvalidDataException, InsufficientDataException {
        SubOrderDetailElasticColumn schemaKey = getSchemaKey(shippingSoiSoidDTO.getAttributeName());
        Optional.ofNullable(schemaKey).orElseThrow(() -> new InvalidDataException(shippingSoiSoidDTO.getAttributeName()));
        ElasticDataDTO dto = new ElasticDataDTO(shippingSoiSoidDTO.getSubOrderCode());
        dto.addDataValue(schemaKey, shippingSoiSoidDTO.getAttributeValue());
        saveShippingModeInMongo(shippingSoiSoidDTO, schemaKey, dto);
        return dto;
    }

    private void saveShippingModeInMongo(ShippingSoiSoidDTO shippingSoiSoidDTO, SubOrderDetailElasticColumn schemaKey, ElasticDataDTO dto) {
        if (!schemaKey.getColumnName().equalsIgnoreCase(SHIPPING_MODE)) {
            return;
        }
        ConnectedRadStatus connectedRadStatus = new ConnectedRadStatus();
        connectedRadStatus.setSubOrderCode(shippingSoiSoidDTO.getSubOrderCode());
        connectedRadStatus.setShippingMode(shippingSoiSoidDTO.getAttributeValue());
        dto.setMongoDocument(connectedRadStatus);
    }

    private SubOrderDetailElasticColumn getSchemaKey(String attributeName) {
        SubOrderDetailElasticColumn schemaKey = null;
        if (attributeName.equalsIgnoreCase(SHIPPING_MODE)) {
            schemaKey = SubOrderDetailElasticColumn.SHIPPING_MODE;
        } else if (attributeName.equalsIgnoreCase(DELIVERY_TYPE)) {
            schemaKey = SubOrderDetailElasticColumn.DELIVERY_TYPE;
        }
        return schemaKey;
    }

    @Override
    protected void mergeMongoDataDTO(ElasticDataDTO elasticDataDTO, Map<String, MongoDocument> mongoDataMap) {
        ConnectedRadStatus mongoDocument = (ConnectedRadStatus) elasticDataDTO.getMongoDocument();
        if (Objects.isNull(mongoDocument)) {
            return;
        }
        ConnectedRadStatus previousMongoDocument = (ConnectedRadStatus) mongoDataMap.get(elasticDataDTO.getSubOrderCode());
        if (Objects.nonNull(previousMongoDocument)) {
            String shippingMode = mongoDocument.getShippingMode();
            if (StringUtils.isNotEmpty(shippingMode)) {
                previousMongoDocument.setShippingMode(shippingMode);
            }
        } else {
            mongoDataMap.put(elasticDataDTO.getSubOrderCode(), mongoDocument);
        }
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SHIPPING_SOI_SOID;
    }

    @Override
    protected void storeInMongo(Collection<MongoDocument> connectedRadStatus) {
        connectedRadStatusRepository.upsertAll(connectedRadStatus);
    }
}
