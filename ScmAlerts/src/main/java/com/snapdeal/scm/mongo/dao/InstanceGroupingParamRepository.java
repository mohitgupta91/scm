/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.mongo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.mongo.doc.InstanceGroupingParam;

/**
 * @version 1.0, 23-Apr-2016
 * @author ashwini.kumar , mohit
 */
public interface InstanceGroupingParamRepository extends MongoRepository<InstanceGroupingParam, String> {
	Page<InstanceGroupingParam> findByAlertInstanceId(Long alertInstanceId,Pageable pageable);
	
	List<InstanceGroupingParam> findByAlertInstanceId(Long alertInstanceId);
	
	InstanceGroupingParam findByRowId(Long rowId);

}
