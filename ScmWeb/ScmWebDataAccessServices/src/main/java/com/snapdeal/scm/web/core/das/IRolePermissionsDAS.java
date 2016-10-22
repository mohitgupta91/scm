package com.snapdeal.scm.web.core.das;

import java.util.List;
import java.util.Set;

/**
 * Created by Harsh Gupta on 25/02/16.
 */
public interface IRolePermissionsDAS {

    Set<String> getAllPermissionsForRoles(List<String> roles);

}
