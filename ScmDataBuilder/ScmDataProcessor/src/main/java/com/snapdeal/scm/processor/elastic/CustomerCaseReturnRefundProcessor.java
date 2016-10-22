package com.snapdeal.scm.processor.elastic;

import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.core.dto.impl.CustomerCaseReturnRefundDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.enums.ComplaintResolution;
import com.snapdeal.scm.processor.cache.impl.ComplaintTypeCache;
import com.snapdeal.scm.processor.exception.InsufficientDataException;
import com.snapdeal.scm.processor.exception.InvalidDataException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by harshit.nimbark on 5/27/16.
 */
@Service("CustomerCaseReturnRefundProcessor")
public class CustomerCaseReturnRefundProcessor extends AbstractElasticProcessor<CustomerCaseReturnRefundDTO> {

    @Override
    public QueryType getQueryType() {
        return QueryType.CUSTOMER_CASE_RETURN_REFUND;
    }

    @Override
    public ElasticDataDTO processRecord(CustomerCaseReturnRefundDTO customerCaseReturnRefundDTO) throws InvalidDataException, InsufficientDataException {

        if (customerCaseReturnRefundDTO.getCcLevel_1() == null || customerCaseReturnRefundDTO.getCcCreatedDate() == null || customerCaseReturnRefundDTO.getCcOrigin() == null) {
            throw new InvalidDataException("Customer case data is incomplete.");
        }

        ComplaintTypeCache complaintTypeCache = CacheManager.getInstance().getCache(ComplaintTypeCache.class);
        if (complaintTypeCache == null) {
            throw new InsufficientDataException("ComplaintTypeCache is null. will not save CustomerCaseReturnRefund details in Elastic");
        }

        ElasticDataDTO dto = new ElasticDataDTO(customerCaseReturnRefundDTO.getSubOrderCode());
        dto.addDataValue(SubOrderDetailElasticColumn.COMPLAINT_CATEGORY, complaintTypeCache.getCodeByComplaintType(customerCaseReturnRefundDTO.getCcLevel_1()));
        Date stdhCreated = customerCaseReturnRefundDTO.getStdhCreated();
        Date srCreated   = customerCaseReturnRefundDTO.getSrCreated();
        if (stdhCreated != null) {
            dto.addDataValue(SubOrderDetailElasticColumn.COMPLAINT_RESOLUTION, ComplaintResolution.REFUNDED.getValue());
        } else if (srCreated != null) {
            dto.addDataValue(SubOrderDetailElasticColumn.COMPLAINT_RESOLUTION, ComplaintResolution.RETURNED.getValue());
        } else {
            dto.addDataValue(SubOrderDetailElasticColumn.COMPLAINT_RESOLUTION, ComplaintResolution.PENDING.getValue());
        }
        dto.addDataValue(SubOrderDetailElasticColumn.COMPLAINT_ORIGIN, customerCaseReturnRefundDTO.getCcOrigin());
        dto.addDataValue(SubOrderDetailElasticColumn.CC_CREATED_DATE, customerCaseReturnRefundDTO.getCcCreatedDate());
        dto.addDataValue(SubOrderDetailElasticColumn.CC_CLOSED_DATE, customerCaseReturnRefundDTO.getCcCloseddate());

        return dto;
    }
}
