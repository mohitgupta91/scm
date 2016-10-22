package com.snapdeal.scm.web.services;

import com.snapdeal.scm.web.core.response.UserResponse;

/***
 * 
 * @author mohit
 *
 */
public interface IUserService {

	UserResponse getUserDetails();

	UserResponse giveTestPermissions();

}
