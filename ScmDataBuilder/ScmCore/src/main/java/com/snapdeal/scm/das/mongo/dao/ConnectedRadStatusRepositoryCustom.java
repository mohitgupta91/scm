package com.snapdeal.scm.das.mongo.dao;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.mongo.document.ConnectedRadStatus;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author prateek
 *
 */
public interface ConnectedRadStatusRepositoryCustom {

	public void upsert(ConnectedRadStatus connectedRadStatus);
	
	public void upsertAll(Collection<MongoDocument> connectedRadStatus);
	
	public List<ConnectedRadStatus> findAllToProcess(Date time);
}
