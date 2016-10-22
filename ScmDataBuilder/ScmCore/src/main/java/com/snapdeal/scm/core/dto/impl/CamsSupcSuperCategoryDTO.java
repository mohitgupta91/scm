package com.snapdeal.scm.core.dto.impl;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * @author prateek
 */
public class CamsSupcSuperCategoryDTO extends AbstractStandardDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -6412923770726227377L;

    @Order(order = 0)
    @NotBlank
    private String supc;
    @Order(order = 1)
    @NotBlank
    private String superCategory;

    public CamsSupcSuperCategoryDTO() {
    }

    public CamsSupcSuperCategoryDTO(String supc, String superCategory) {
        this.supc = supc;
        this.superCategory = superCategory;
    }

    public String getSupc() {
        return supc;
    }

    public void setSupc(String supc) {
        this.supc = supc;
    }

    public String getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(String superCategory) {
        this.superCategory = superCategory;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CamsSupcSuperCategoryDTO [");
        builder.append(supc).append(StringUtils.COMMA).append(superCategory);
        return builder.toString();
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.CAMS_SUPC_SUPER_CATEGORY;
    }
    
    @Override
	public boolean validateDTO() {
		if(StringUtils.isEmpty(supc) || StringUtils.isEmpty(superCategory))
			return false;
		return true;
	}

}
