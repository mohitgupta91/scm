/**
 * 
 */
package com.snapdeal.scm.poller.dto;


/**
 * @author gaurav on 22-Feb-2016
 *
 */
public class EndTimeFutureExternalAPIResponse {
	
	ExternalAPIResponse externalAPIResponse;
	
	long endTimestamp;

	/**
	 * @return the externalAPIResponse
	 */
	public ExternalAPIResponse getExternalAPIResponse() {
		return externalAPIResponse;
	}

	/**
	 * @param externalAPIResponse the externalAPIResponse to set
	 */
	public void setExternalAPIResponse(
			ExternalAPIResponse externalAPIResponse) {
		this.externalAPIResponse = externalAPIResponse;
	}

	/**
	 * @return the endTimestamp
	 */
	public long getEndTimestamp() {
		return endTimestamp;
	}

	/**
	 * @param endTimestamp the endTimestamp to set
	 */
	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EndTimeFutureExternalAPIResponse [externalAPIResponse="
				+ externalAPIResponse + ", endTimestamp=" + endTimestamp + "]";
	}
	
	

}
