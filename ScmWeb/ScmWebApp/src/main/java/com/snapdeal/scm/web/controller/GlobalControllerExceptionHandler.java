package com.snapdeal.scm.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Harsh Gupta on 29/02/16.
 * This is a global controller advice that will gracefully catch all the exceptions thrown
 * by all the controllers and send a HTTP OK with a valid message.
 */

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    //TODO: Handle exceptions well

	private final static String DEFAULT_ERROR_VIEW ="error";
	private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);	
   
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Exception.class)
    public ModelAndView handleConflict(HttpServletRequest req, Exception e) 
    {
    	LOG.error("exception occured", e);
    	
		ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.addObject("timestamp",new Date());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    	
    }
}
