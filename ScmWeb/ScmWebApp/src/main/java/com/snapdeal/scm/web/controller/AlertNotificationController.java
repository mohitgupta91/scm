package com.snapdeal.scm.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.scm.web.core.dto.AlertNotificationUpdateDTO;
import com.snapdeal.scm.web.core.response.AlertResponse;
import com.snapdeal.scm.web.core.sro.AlertNotificationSRO;
import com.snapdeal.scm.web.services.IAlertNotificationService;
import com.snapdeal.scm.web.validator.NotificationRequestValidator;

/**
 * AlertNotificationController: Notification Controller
 * 
 * @author pranav, Ashwini
 */
@Controller
@RequestMapping("/data")
public class AlertNotificationController {

    private static final Logger       LOG = LoggerFactory.getLogger(AlertNotificationController.class);

    @Autowired
    private IAlertNotificationService service;

    @RequestMapping(value = "/notifications/{notificationId}", method = RequestMethod.POST)
    @ResponseBody
    public void update(@Valid @RequestBody AlertNotificationUpdateDTO dto,
    		@PathVariable("notificationId") Long notificationId) {
        LOG.info("request to update Notification : [{}]", dto);
        service.updateNotification(dto);
    }

    @RequestMapping(value = "/notifications", method = RequestMethod.GET)
    @ResponseBody
    public AlertResponse<List<AlertNotificationSRO>> list() {
        AlertResponse<List<AlertNotificationSRO>> resp = new AlertResponse<List<AlertNotificationSRO>>();
        List<AlertNotificationSRO> list = service.listAlertNotification();
        resp.setCode(HttpStatus.OK.value());
        resp.setData(list);
        return resp;
    }

    @RequestMapping(value = "/users/{userId}/notifications", method = RequestMethod.GET)
    @ResponseBody
    public AlertResponse<List<AlertNotificationSRO>> list(@PathVariable("userId") String userName) {
    	AlertResponse<List<AlertNotificationSRO>> resp = new AlertResponse<List<AlertNotificationSRO>>();
        List<AlertNotificationSRO> list = service.listAlertNotification(userName);
        resp.setCode(HttpStatus.OK.value());
        resp.setData(list);
        return resp;
    }

    @RequestMapping(value = "/notifications/_view", method = RequestMethod.GET)
    @ResponseBody
    public AlertResponse<AlertNotificationSRO> view(@RequestParam(value = "notificationId", required = true) Long notificationId) {
    	AlertResponse<AlertNotificationSRO> resp = new AlertResponse<AlertNotificationSRO>();
        AlertNotificationSRO notification = service.viewNotification(notificationId);
        resp.setCode(HttpStatus.OK.value());
        resp.setData(notification);
        return null;
    }

    @InitBinder
    protected void initWebCommonBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.addValidators(new NotificationRequestValidator());
    }

}
