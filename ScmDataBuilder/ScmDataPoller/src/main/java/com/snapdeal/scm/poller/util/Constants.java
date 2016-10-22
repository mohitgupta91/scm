/**
 * 
 */
package com.snapdeal.scm.poller.util;

/**
 * @author gaurav
 *
 */
public class Constants {

	private Constants() {}
	
	public static final String OK = "OK";
	
	public static final String DIRECTORY_SEPARATOR = "/";
	
	public static final String CRC_PATTERN_STRING = ".*?/\\.$|.*?/\\.\\.$|.*crc|.*_SUCCESS.*";
	
	public static final String S3_STRING = "s3://%s%s";
	
	public static final String API_URL_STR = "externalAPIURL";
	
	public static final String DELAY_STORAGE = "delayStorage";
	
	public static final String REST_CONNECT_TIMEOUT = "restConnectTimeout";
	
	public static final String REST_READ_TIMEOUT = "restReadTimeout";
}
