package com.snapdeal.scm.web.core.response;

import java.util.List;

import com.snapdeal.scm.web.core.sro.SearchResponseDataSRO;
import com.snapdeal.scm.web.core.validation.SCMError;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author harsh
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse extends BaseResponse<SearchResponseDataSRO> {

    public SearchResponse(int code, SearchResponseDataSRO dataSRO,
    		 List<SCMError> errors, String message) {
    	super(code, message, errors, dataSRO);
	}
    
    public SearchResponse(){
    	
    }

	private static final long serialVersionUID = -7191710901031348258L;
}
