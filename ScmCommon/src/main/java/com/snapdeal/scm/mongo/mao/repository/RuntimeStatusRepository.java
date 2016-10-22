package com.snapdeal.scm.mongo.mao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.common.domain.mongo.RuntimeStatusKey;
import com.snapdeal.scm.mongo.doc.RuntimeStatus;

/**
 * 
 * @author prateek
 *
 */
public interface RuntimeStatusRepository extends MongoRepository<RuntimeStatus, String> {

	public RuntimeStatus findByRuntimeStatusKey(RuntimeStatusKey runtimeStatusKey);
}
