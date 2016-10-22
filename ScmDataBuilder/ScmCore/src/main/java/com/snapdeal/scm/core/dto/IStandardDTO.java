package com.snapdeal.scm.core.dto;

import java.io.Serializable;

import com.snapdeal.scm.core.enums.QueryType;

/**
 * Created by vinay on 11/2/16.
 */
public interface IStandardDTO extends Serializable{

	public QueryType getQueryType();

	public boolean validateDTO();

}
