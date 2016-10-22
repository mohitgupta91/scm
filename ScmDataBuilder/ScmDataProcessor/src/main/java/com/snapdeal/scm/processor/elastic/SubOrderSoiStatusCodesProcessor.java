package com.snapdeal.scm.processor.elastic;

import com.snapdeal.scm.core.dto.impl.SubOrderSoiStatusCodesDTO;
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
@Service("SubOrderSoiStatusCodesProcessor")
public class SubOrderSoiStatusCodesProcessor extends AbstractElasticProcessor<SubOrderSoiStatusCodesDTO> {

    // TODO move this to some other place
    private static Map<String, SubOrderDetailElasticColumn> codeToStatusDateColumn = new HashMap<>();

    static {
        codeToStatusDateColumn.put("PLI", SubOrderDetailElasticColumn.SOI_PLI_STATUS_DATE);
        codeToStatusDateColumn.put("PLC", SubOrderDetailElasticColumn.SOI_PLC_STATUS_DATE);
        codeToStatusDateColumn.put("PDI", SubOrderDetailElasticColumn.SOI_PDI_STATUS_DATE);
        codeToStatusDateColumn.put("PDC", SubOrderDetailElasticColumn.SOI_PDC_STATUS_DATE);
        codeToStatusDateColumn.put("SDH", SubOrderDetailElasticColumn.SOI_SDH_STATUS_DATE);
    }

    @Override
    public ElasticDataDTO processRecord(SubOrderSoiStatusCodesDTO subOrderSoiStatusCodesDTO)
            throws InvalidDataException, InsufficientDataException {
        SubOrderDetailElasticColumn statusColumnName = codeToStatusDateColumn.get(subOrderSoiStatusCodesDTO.getStatusCode());
        Optional.ofNullable(statusColumnName).orElseThrow(() -> new InvalidDataException("Status Code : " + subOrderSoiStatusCodesDTO.getStatusCode()
                                                                                         + " not supoorted as of now. SubOrder code : " + subOrderSoiStatusCodesDTO.getSubOrderCode()));
        ElasticDataDTO elasticDataDTO = new ElasticDataDTO(subOrderSoiStatusCodesDTO.getSubOrderCode());
        if (Objects.nonNull(statusColumnName)) {
            addStatusCodeTime(statusColumnName, subOrderSoiStatusCodesDTO, elasticDataDTO, subOrderSoiStatusCodesDTO.getCreated());
        }
        return elasticDataDTO;
    }

    private void addStatusCodeTime(SubOrderDetailElasticColumn statusColumnName, SubOrderSoiStatusCodesDTO subOrderSoiStatusCodesDTO,
                                   ElasticDataDTO elasticDataDTO, Date statusTime) throws InsufficientDataException {
        switch (statusColumnName) {
            case SOI_PLI_STATUS_DATE:
            case SOI_PLC_STATUS_DATE:
            case SOI_PDI_STATUS_DATE:
            case SOI_PDC_STATUS_DATE:
            case SOI_SDH_STATUS_DATE:
                addCLOSEDAndLostAndDamagedScript(subOrderSoiStatusCodesDTO, elasticDataDTO);
                break;
            default:
                break;
        }
    }

    private void addCLOSEDAndLostAndDamagedScript(SubOrderSoiStatusCodesDTO subOrderSoiStatusCodesDTO, ElasticDataDTO elasticDataDTO) {
        ElasticScriptDataDTO elasticScriptDataDTO = new ElasticScriptDataDTO(elasticDataDTO.getSubOrderCode());
        elasticScriptDataDTO.addScript(ElasticScriptKey.LOST_AND_DAMAGED_DATE);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.LOST_AND_DAMAGED_DATE, subOrderSoiStatusCodesDTO.getStatusCode());
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.LOST_AND_DAMAGED_DATE.getColumnName(), subOrderSoiStatusCodesDTO.getStatusCode());
        elasticScriptDataDTO.addScript(ElasticScriptKey.CLOSED_DATE);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.CLOSED_DATE, subOrderSoiStatusCodesDTO.getStatusCode());
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName(), subOrderSoiStatusCodesDTO.getStatusCode());
        elasticDataDTO.setElasticScriptDataDTO(elasticScriptDataDTO);
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SUB_ORDER_SOI_STATUS_CODES;
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
                    case CLOSED_DATE:
                    case LOST_AND_DAMAGED_DATE:
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
