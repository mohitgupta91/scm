package com.snapdeal.scm.mongo.mao.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.snapdeal.scm.mongo.mao.ScmJmsPropertyMongoRepositoryCustom;
import com.snapdeal.scm.mongo.doc.JmsDetails;
import com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty;

/**
 * ScmJmsPropertyMongoRepositoryImpl : Custom Query Implementation for scm jms conf
 * 
 * @author pranav
 *
 */
@Component
public class ScmJmsPropertyMongoRepositoryImpl implements ScmJmsPropertyMongoRepositoryCustom{

	private static final Logger logger = LoggerFactory.getLogger(ScmJmsPropertyMongoRepositoryImpl.class);

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public ScmJmsMachineProperty findByCritria(String hostName){
		Query query = new Query();
		query.addCriteria(Criteria.
				where("machineHostName").is(hostName));
		logger.debug("Query = "+ query);
		return mongoOperations.findOne(query, ScmJmsMachineProperty.class);
	}
	
	@Override
	public JmsDetails findJmsConf(){
		return mongoOperations.findAll(JmsDetails.class).get(0);
	}
	
	@Override
	public void saveJmsDetails(JmsDetails details){
		mongoOperations.save(details);
	}

	@Override
	public void deleteJmsDetails() {
		mongoOperations.dropCollection(JmsDetails.class);
	}
}
