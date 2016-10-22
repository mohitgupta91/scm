/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.das.mongo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping;

/**
 * @version 1.0, 17-Feb-2016
 * @author ashwini
 */
public interface QueryDTOFieldMappingRepository extends MongoRepository<QueryDTOFieldMapping, String> {

}
