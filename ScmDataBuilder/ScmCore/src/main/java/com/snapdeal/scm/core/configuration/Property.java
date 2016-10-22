package com.snapdeal.scm.core.configuration;

/**
 * 
 * @author prateek
 *
 */
public enum Property {

	AUTO_CACHE_RELOAD_CRON ("auto.cache.reload.cron","0 0 0/6 * * ?" ),
	STATE_CODE("state.code", null),
	FILEHANDLER_DTO_BATCHSIZE("filehandler.dto.batchsize", "100"),
	SUPC_CACHE_TTL_SECOND("supc.cache.ttl_second", "86400"),
	SUPC_CACHE_SIZE("supc.cache.size", "10000"),
	METRO("metro", "");

	private String name;
	private String value;

	private Property(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue(){
		return value;
	}
}
