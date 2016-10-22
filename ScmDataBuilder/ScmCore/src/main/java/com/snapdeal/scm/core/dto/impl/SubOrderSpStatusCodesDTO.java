package com.snapdeal.scm.core.dto.impl;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * @author prateek
 */
public class SubOrderSpStatusCodesDTO extends AbstractStandardDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -1555882367287468899L;

    @Order(order = 0)
    @NotBlank
    private String subOrderCode;
    @Order(order = 1)
    private String statusCode;
    @Order(order = 2)
    @NotNull
    private Date   created;

    public SubOrderSpStatusCodesDTO() {
    }

    public SubOrderSpStatusCodesDTO(String subOrderCode, String statusCode, Date created) {
        this.subOrderCode = subOrderCode;
        this.statusCode = statusCode;
        this.created = created;
    }

    public String getSubOrderCode() {
        return subOrderCode;
    }

    public void setSubOrderCode(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SubOrderSpStatusCodesDTO [").append(subOrderCode).append(", ").append(statusCode).append(", ").append(created).append("]");
        return builder.toString();
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SUB_ORDER_SP_STATUS_CODES;
    }
    
    @Override
   	public boolean validateDTO() {
   		if(StringUtils.isEmpty(subOrderCode) || StringUtils.isEmpty(statusCode)|| created == null)
   			return false;
   		return true;
   	}
}
