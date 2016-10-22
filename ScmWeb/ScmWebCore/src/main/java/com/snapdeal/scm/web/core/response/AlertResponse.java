package com.snapdeal.scm.web.core.response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
/**
 * 
 * @author mohit
 *
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertResponse<T> extends BaseResponse<T>{

	private static final long serialVersionUID = 1L;

}
