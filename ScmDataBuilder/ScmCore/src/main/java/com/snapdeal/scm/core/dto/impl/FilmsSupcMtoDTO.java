package com.snapdeal.scm.core.dto.impl;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * @author prateek
 */
public class FilmsSupcMtoDTO extends AbstractStandardDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 5552730986227479222L;

    @Order(order = 0)
    @NotBlank
    private String supc;
    @Order(order = 1)
    @NotBlank
    private String mto;

    public FilmsSupcMtoDTO() {
    }

    public FilmsSupcMtoDTO(String supc, String mto) {
        this.supc = supc;
        this.mto = mto;
    }

    public String getSupc() {
        return supc;
    }

    public void setSupc(String supc) {
        this.supc = supc;
    }

    public String getMto() {
        return mto;
    }

    public void setMto(String mto) {
        this.mto = mto;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.FILMS_SUPC_MTO;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FilmsSupcMtoDTO [");
        builder.append(supc).append(StringUtils.COMMA).append(mto);
        return builder.toString();
    }
    
    @Override
	public boolean validateDTO() {
		if(StringUtils.isEmpty(supc) || StringUtils.isEmpty(mto))
			return false;
		return true;
	}
}
