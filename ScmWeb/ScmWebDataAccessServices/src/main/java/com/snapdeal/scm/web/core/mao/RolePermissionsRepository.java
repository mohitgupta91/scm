package com.snapdeal.scm.web.core.mao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.web.core.mongo.documents.RolePermissions;

/**
 * Created by Harsh Gupta on 25/02/16.
 */
public interface RolePermissionsRepository extends MongoRepository<RolePermissions,String> {

    List<RolePermissions> findByRole(String role);
}
