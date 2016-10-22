package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.mao.ScmJmsPropertyMongoRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty;

/**
 * ScmJmsPropertyMongoRepository : Mongo Repository
 * 
 * @author pranav
 *
 */
public interface ScmJmsPropertyMongoRepository extends MongoRepository<ScmJmsMachineProperty, String>, ScmJmsPropertyMongoRepositoryCustom{
	
	public ScmJmsMachineProperty findById(String id);
}
