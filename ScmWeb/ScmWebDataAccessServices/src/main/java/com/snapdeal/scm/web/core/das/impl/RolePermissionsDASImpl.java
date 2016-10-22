package com.snapdeal.scm.web.core.das.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.base.utils.CollectionUtils;
import com.snapdeal.scm.web.core.das.IRolePermissionsDAS;
import com.snapdeal.scm.web.core.mao.RolePermissionsRepository;
import com.snapdeal.scm.web.core.mongo.documents.RolePermissions;

/**
 * Created by Harsh Gupta on 25/02/16.
 */

@Service("rolePermissionsDAS")
public class RolePermissionsDASImpl implements IRolePermissionsDAS {

    @Autowired
    RolePermissionsRepository rolePermissionsRepository;

    @Override
    public Set<String> getAllPermissionsForRoles(List<String> roles) {

        if(CollectionUtils.isEmpty(roles)){
            return Collections.emptySet();
        }
        Set<String> permissions = new HashSet<>();

        for(String role : roles){
            List<RolePermissions> rolePermissions = rolePermissionsRepository.findByRole(role);
            if(!CollectionUtils.isEmpty(rolePermissions)){
                for(RolePermissions rolePermissionEntry : rolePermissions){
                    permissions.addAll(rolePermissionEntry.getPermissions());
                }
            }
        }
        return permissions;
    }
}