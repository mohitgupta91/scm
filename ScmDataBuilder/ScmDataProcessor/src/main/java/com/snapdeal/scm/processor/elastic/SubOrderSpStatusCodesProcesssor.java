package com.snapdeal.scm.processor.elastic;

import com.snapdeal.scm.core.dto.impl.SubOrderSpStatusCodesDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticScriptDataDTO;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.core.enums.ElasticScriptKey;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.processor.exception.InsufficientDataException;
import com.snapdeal.scm.processor.exception.InvalidDataException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author prateek
 */
@Service("SubOrderSpStatusCodesProcesssor")
public class SubOrderSpStatusCodesProcesssor extends AbstractElasticProcessor<SubOrderSpStatusCodesDTO> {

    // TODO move this to some other place
    private static Map<String, SubOrderDetailElasticColumn> codeToStatusDateColumn = new HashMap<>();

    static {
        codeToStatusDateColumn.put("AWB_UPLOADED", SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE);
        codeToStatusDateColumn.put("COURIER_RETURNED", SubOrderDetailElasticColumn.SP_COURIER_RETURNED_STATUS_DATE);
        codeToStatusDateColumn.put("RETURN_TRACKING_COMPLETE", SubOrderDetailElasticColumn.SP_RETURN_TRACKING_COMPLETE_STATUS_DATE);
        codeToStatusDateColumn.put("RTO_UNDELIVERED", SubOrderDetailElasticColumn.SP_RTO_UNDELIVERED_STATUS_DATE);
        codeToStatusDateColumn.put("VENDOR_RETURN_CONFIRMED", SubOrderDetailElasticColumn.SP_VENDOR_RETURN_CONFIRMED_STATUS_DATE);
    }

    @Override
    public ElasticDataDTO processRecord(SubOrderSpStatusCodesDTO subOrderSpStatusCodesDTO)
            throws InvalidDataException, InsufficientDataException {
        SubOrderDetailElasticColumn statusColumnName = codeToStatusDateColumn.get(subOrderSpStatusCodesDTO.getStatusCode());
        Optional.ofNullable(statusColumnName).orElseThrow(() -> new InvalidDataException("Status Code : " + subOrderSpStatusCodesDTO.getStatusCode()
                                                                                         + " not supoorted as of now. SubOrder code : " + subOrderSpStatusCodesDTO.getSubOrderCode()));
        ElasticDataDTO elasticDataDTO = new ElasticDataDTO(subOrderSpStatusCodesDTO.getSubOrderCode());
        if (Objects.nonNull(statusColumnName)) {
            addStatusCodeTime(statusColumnName, subOrderSpStatusCodesDTO, elasticDataDTO, subOrderSpStatusCodesDTO.getCreated());
        }
        return elasticDataDTO;
    }

    private void addStatusCodeTime(SubOrderDetailElasticColumn statusColumnName, SubOrderSpStatusCodesDTO subOrderSoiStatusCodesDTO,
                                   ElasticDataDTO elasticDataDTO, Date statusTime) throws InsufficientDataException {
        switch (statusColumnName) {
            case SP_AWB_UPLOADED_STATUS_DATE:
                elasticDataDTO.addDataValue(statusColumnName, statusTime);
                addShippedScript(elasticDataDTO);
                break;
            case SP_COURIER_RETURNED_STATUS_DATE:
            case SP_RETURN_TRACKING_COMPLETE_STATUS_DATE:
            case SP_RTO_UNDELIVERED_STATUS_DATE:
            case SP_VENDOR_RETURN_CONFIRMED_STATUS_DATE:
                addClosedAndRTOScript(subOrderSoiStatusCodesDTO, elasticDataDTO);
                break;
            default:
                break;
        }
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SUB_ORDER_SP_STATUS_CODES;
    }

    private void addShippedScript(ElasticDataDTO elasticDataDTO) {
        ElasticScriptDataDTO elasticScriptDataDTO = new ElasticScriptDataDTO(elasticDataDTO.getSubOrderCode());
        elasticScriptDataDTO.addScript(ElasticScriptKey.ORDER_TO_SHIPPED);
        elasticScriptDataDTO.addScript(ElasticScriptKey.SHIPPED_TO_DELIVERED);
        elasticDataDTO.setElasticScriptDataDTO(elasticScriptDataDTO);
    }

    private void addClosedAndRTOScript(SubOrderSpStatusCodesDTO subOrderSpStatusCodesDTO, ElasticDataDTO elasticDataDTO) {
        ElasticScriptDataDTO elasticScriptDataDTO = new ElasticScriptDataDTO(elasticDataDTO.getSubOrderCode());
        Date                 date                 = subOrderSpStatusCodesDTO.getCreated();
        elasticScriptDataDTO.addScript(ElasticScriptKey.CLOSED_DATE);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.CLOSED_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName(), date);
        elasticScriptDataDTO.addScript(ElasticScriptKey.RTO_DATE);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.RTO_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.RTO_DATE.getColumnName(), date);
        elasticDataDTO.setElasticScriptDataDTO(elasticScriptDataDTO);
    }

    @Override
    protected void mergerElasticScriptDataDTO(ElasticDataDTO elasticDataDTO, Map<String, ElasticScriptDataDTO> elasticScriptMap) {
        ElasticScriptDataDTO elasticScriptDataDTO = elasticDataDTO.getElasticScriptDataDTO();
        if (Objects.isNull(elasticScriptDataDTO)) {
            return;
        }
        ElasticScriptDataDTO previousElasticScriptDataDTO = elasticScriptMap.get(elasticDataDTO.getSubOrderCode());
        if (Objects.isNull(previousElasticScriptDataDTO)) {
            elasticScriptMap.put(elasticDataDTO.getSubOrderCode(), elasticScriptDataDTO);
        } else {
            for (Entry<SubOrderDetailElasticColumn, Object> entrySet : elasticScriptDataDTO.getDataValueMap().entrySet()) {
                switch (entrySet.getKey()) {
                    case RTO_DATE:
                    case CLOSED_DATE:
                        SubOrderDetailElasticColumn key = entrySet.getKey();
                        Date currentDate = (Date) elasticScriptDataDTO.getDataValueMap().get(key);
                        Date previousDate = (Date) previousElasticScriptDataDTO.getDataValueMap().get(key);
                        if (Objects.isNull(previousDate) || currentDate.before(previousDate)) {
                            previousElasticScriptDataDTO.addDataValue(key, entrySet.getValue());
                            previousElasticScriptDataDTO.addParams(key.getColumnName(), entrySet.getValue());
                        }
                        break;
                    default:
                        previousElasticScriptDataDTO.addDataValue(entrySet.getKey(), entrySet.getValue());
                        previousElasticScriptDataDTO.addParams(entrySet.getKey().getColumnName(), entrySet.getValue());
                        break;
                }
            }
            previousElasticScriptDataDTO.addAllScripts(elasticScriptDataDTO.getScripts());
        }
    }
}