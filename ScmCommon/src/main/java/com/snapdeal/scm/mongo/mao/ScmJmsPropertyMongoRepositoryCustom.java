package com.snapdeal.scm.mongo.mao;

import com.snapdeal.scm.mongo.doc.JmsDetails;
import com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty;

/**
 * ScmJmsPropertyMongoRepositoryCustom : Custom repo for scm jms document
 * 
 * @author pranav
 *
 */
public interface ScmJmsPropertyMongoRepositoryCustom {
	
	public ScmJmsMachineProperty findByCritria(String hostName);

	public JmsDetails findJmsConf();

	public void saveJmsDetails(JmsDetails details);
	
	public void deleteJmsDetails();
}
