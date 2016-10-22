package com.snapdeal.scm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
/**
 * Common Mongo Connection setting options
 * 
 * @author pranav
 *
 */
@Configuration
public class SCMMongoConfiguration {

	@Value("${scm.mongodb.socketTimeoutMS}")
	private int socketTimeout;

	@Value("${scm.mongodb.connectionTimeoutMS}")
	private int connectionTimeout;
	
	@Value("${scm.mongodb.connectionPerHost}")
	private int connectionPerHost;

	public @Bean MongoClientOptions mongoClientOptionSettings(){
		MongoClientOptions options = MongoClientOptions.builder()
				.socketTimeout(socketTimeout)
				.connectTimeout(connectionTimeout)
				.connectionsPerHost(connectionPerHost)
				.readPreference(ReadPreference.secondaryPreferred())
				.build();
		return options;
	}
}
