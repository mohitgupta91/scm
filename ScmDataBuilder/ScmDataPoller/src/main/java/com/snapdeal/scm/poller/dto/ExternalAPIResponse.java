package com.snapdeal.scm.poller.dto;

import java.util.List;

import com.snapdeal.scm.core.enums.QueryType;

/**
 * @author gaurav
 *
 */
public class ExternalAPIResponse {
	
	private QueryType queryType;

	private String responseCode;

	private List<String> allOutPaths;

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the allOutPaths
	 */
	public List<String> getAllOutPaths() {
		return allOutPaths;
	}

	/**
	 * @param allOutPaths the allOutPaths to set
	 */
	public void setAllOutPaths(List<String> allOutPaths) {
		this.allOutPaths = allOutPaths;
	}

	/**
	 * @return the queryType
	 */
	public QueryType getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType the queryType to set
	 */
	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExternalAPIResponse [queryType=" + queryType
				+ ", responseCode=" + responseCode + ", allOutPaths="
				+ allOutPaths + "]";
	}

}
