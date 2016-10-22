package com.snapdeal.scm.processor;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ElasticClientConfiguration : Configuration related to elastic 
 * 
 * @author pranav
 *
 */
@Configuration
public class ScmElasticNodeClientConfiguration implements InitializingBean, DisposableBean {

	private static final Logger LOG = LoggerFactory.getLogger(ScmElasticNodeClientConfiguration.class);

	@Value("${scm.elastic.clustername}")
	private String clusterName;

	@Value("${scm.elastic.path.home}")
	private String nodeClientClusterPathHome;

	@Value("${scm.elastic.host.port}")
	private String elasticHostPort;


	@Value("${scm.elastic.index.name}")
	private String indexName;

	@Value("${scm.elastic.index.name.type}")
	private String indexType;
	
	@Value("${scm.elastic.nodeclient.host:localhost}")
	private String nodeClientHost;	

/*	@Value("${scm.elastic.index.purge.interval}")
	private String purgeInterval;*/

/*	@Value("${scm.elastic.index.ttl.enable}")
	private boolean ttlEnable;
	
	@Value("${scm.elastic.index.purge.size}")
	private int purgeSize;
*/
	
//	@Value("${scm.elastic.index.default.ttl}")
//	private String defaultTTL;

	private Client client;

	@Override
	public void afterPropertiesSet() throws IOException {

		Builder settings = Settings.settingsBuilder()
				.put("discovery.zen.ping.multicast.enabled",false)
				.put("discovery.zen.ping.unicast.hosts",elasticHostPort)
				.put("node.data", false)
				.put("node.client", true)
				.put("path.home", nodeClientClusterPathHome)
				.put("http.enabled", false);
//		        .put("network.host", nodeClientHost);

		Node node = NodeBuilder
				.nodeBuilder()
				.clusterName(clusterName)
				.settings(settings)
				.node();

		client = node.client();

		final IndicesExistsResponse res = client.admin()
				.indices()
				.prepareExists(indexName)
				.execute()
				.actionGet();

		if(!res.isExists()){
			LOG.error("No Elasticsearch index found with name  = "+indexName+". Please create it.");
			throw new RuntimeException("No Elasticsearch index found with name  = "+indexName+". Please create it.");
		}

		/*if (ttlEnable && !res.isExists()) {
			client.close();
			settings.put("indices.ttl.interval", purgeInterval);
			settings.put("indices.ttl.bulk_size", purgeSize);
			node = NodeBuilder
					.nodeBuilder()
					.clusterName(clusterName)
					.settings(settings).node();
			client = node.client();
			CreateIndexResponse indexCreated = client.admin().indices().prepareCreate(indexName).addMapping(indexType, 
					XContentFactory.jsonBuilder()
					.startObject()
					.startObject(indexType)
					.startObject("_ttl").field("enabled", true).field("default", defaultTTL).endObject()
					.endObject()
					.endObject()).execute().actionGet();
			LOG.info(indexType +" : index created at Elastic Serach :" +indexCreated.isAcknowledged());
		}*/
	}

	@Override
	public void destroy() {
		client.close();
	}

	@Bean(name="nodeClient")
	public Client getClient(){
		return client;
	}

	@Bean
	public ObjectMapper getObjectMapper(){
		return new ObjectMapper();
	}
}
