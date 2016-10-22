package com.snapdeal.scm.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.snapdeal.scm.web.core.response.UserResponse;
import com.snapdeal.scm.web.services.IUserService;

/***
 * 
 * @author mohit
 *
 */

@RestController
@RequestMapping("/data/users")
public class UserDetailsController {

	@Autowired
	IUserService userService;
	
	@Value("${scm.sso.enabled}")
	private boolean ssoEnabled;

	@RequestMapping(value = "/current", produces = "application/json")
	@ResponseBody
   	public UserResponse getUserDetails()
	{
		if(!ssoEnabled){
		return userService.giveTestPermissions();
		}
		return userService.getUserDetails();
	}

}
