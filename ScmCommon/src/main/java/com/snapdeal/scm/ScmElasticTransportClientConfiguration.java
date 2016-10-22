package com.snapdeal.scm;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ScmElasticTransportClientConfiguration :  Hold the elastic configuration
 * 
 * @author pranav
 *
 */
@Configuration
public class ScmElasticTransportClientConfiguration {
	
	private static final Logger LOG = LoggerFactory.getLogger(ScmElasticTransportClientConfiguration.class);

	@Value("${scm.elastic.clustername}")
	private String clusterName;

	@Value("${scm.elastic.host.port}")
	private String elasticHostPort;

	@Bean(name="transportClient")
	public Client afterPropertiesSet() throws Exception {
		Client client=null;
		try { 
			Settings settings = Settings.settingsBuilder() 
					.put("cluster.name",clusterName)
					.put("client.transport.sniff", false)
					.put("node.data",false)
					.put("node.local",false)
					.put("client.transport.ping_timeout", 20, TimeUnit.SECONDS).build();
			TransportClient tClient = TransportClient.builder().settings(settings).build(); 
			String[] nodes = elasticHostPort.split(","); 
			for (String node : nodes) { 
				String[] hostPort = node.split(":"); 
				if(hostPort.length!=2){
					throw new RuntimeException("Elastic Transport Client Issue : HostName and Port not provided correctly");
				}
				tClient.addTransportAddress(new InetSocketTransportAddress( 
						InetAddress.getByName(hostPort[0]), Integer.parseInt(hostPort[1]))); 
			} 
			client= tClient; 
		} catch (Exception e) { 
			LOG.error("Error in initializing elastic transport client !",e);
			throw e;
		} 
		return client;
	}
}
