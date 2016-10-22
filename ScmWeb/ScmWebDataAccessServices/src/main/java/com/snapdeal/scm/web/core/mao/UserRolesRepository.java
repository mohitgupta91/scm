package com.snapdeal.scm.web.core.mao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.web.core.mongo.documents.RolePermissions;;

public interface UserRolesRepository extends MongoRepository<RolePermissions, String>{

	
}
