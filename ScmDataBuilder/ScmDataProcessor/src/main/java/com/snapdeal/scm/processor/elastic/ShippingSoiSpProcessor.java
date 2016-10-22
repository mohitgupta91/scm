package com.snapdeal.scm.processor.elastic;

import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.cache.impl.CourierGroupCache;
import com.snapdeal.scm.core.dto.impl.ShippingSoiSpDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;
import com.snapdeal.scm.processor.exception.InsufficientDataException;
import com.snapdeal.scm.processor.exception.InvalidDataException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p> Query: ""
 * <p> Key  : {@link SubOrderDetailElasticColumn#SUB_ORDER_CODE}
 * <p> Data : {@link SubOrderDetailElasticColumn#COURIER_GROUP}, {@link SubOrderDetailElasticColumn#COURIER_TYPE},
 * {@link SubOrderDetailElasticColumn#EXPECTED_DELIVERY_DATE}, {@link SubOrderDetailElasticColumn#EXPECTED_DELIVERY_DATE_RANGE_START}
 *
 * @author prateek
 */
@Service("ShippingSoiSpProcessor")
public class ShippingSoiSpProcessor extends AbstractElasticProcessor<ShippingSoiSpDTO> {

    @Override
    public ElasticDataDTO processRecord(ShippingSoiSpDTO shippingSoiSpDTO)
            throws InvalidDataException, InsufficientDataException {
        String courierCode       = shippingSoiSpDTO.getCourierCode();
        Date   expectedStartDate = shippingSoiSpDTO.getExpectedDeliveryDateRangeStart();
        Date   expectedEnddate   = shippingSoiSpDTO.getExpectedDeliveryDate();
        if (StringUtils.isEmpty(courierCode) && expectedStartDate == null && expectedEnddate == null) {
            throw new InvalidDataException("Value of all courierCode, expectedStartDate and expectedEndDate is null");
        }
        if (StringUtils.isEmpty(courierCode)) {
            return createElasticDataDTO(shippingSoiSpDTO, null, null);
        }
        CourierGroupCache.CourierDetail courierDetail = CacheManager.getInstance().getCache(CourierGroupCache.class).
                getCourierCodeToDetail(courierCode);
        if (courierDetail == null) {
            throw new InsufficientDataException("CourierDetail is not found for courierCode =" + courierCode);
        }
        String courierGroup = courierDetail.getCourierGroup();
        String courierType  = courierDetail.getCourierType();
        if (courierGroup == null || courierType == null) {
            throw new InsufficientDataException("courierGroup = " + courierGroup + " or courierType = "
                                                + courierType + "is null for courier code = " + courierCode);
        }
        return createElasticDataDTO(shippingSoiSpDTO, courierGroup, courierType);
    }

    private ElasticDataDTO createElasticDataDTO(ShippingSoiSpDTO shippingSoiSpDTO, String courierGroup, String courierType) throws InvalidDataException {
        ElasticDataDTO dto = new ElasticDataDTO(shippingSoiSpDTO.getSubOrderCode());
        dto.addDataValue(SubOrderDetailElasticColumn.COURIER_CODE, shippingSoiSpDTO.getCourierCode());
        dto.addDataValue(SubOrderDetailElasticColumn.COURIER_GROUP, courierGroup);
        dto.addDataValue(SubOrderDetailElasticColumn.COURIER_TYPE, courierType);
        dto.addDataValue(SubOrderDetailElasticColumn.EXPECTED_DELIVERY_DATE, shippingSoiSpDTO.getExpectedDeliveryDate());
        dto.addDataValue(SubOrderDetailElasticColumn.EXPECTED_DELIVERY_DATE_RANGE_START, shippingSoiSpDTO.getExpectedDeliveryDateRangeStart());
        return dto;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SHIPPING_SOI_SP;
    }
}
