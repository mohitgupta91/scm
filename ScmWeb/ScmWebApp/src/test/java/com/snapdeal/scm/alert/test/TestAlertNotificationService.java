package com.snapdeal.scm.alert.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.web.ConfigurationScmWebApp;
import com.snapdeal.scm.web.core.dto.AlertNotificationUpdateDTO;
import com.snapdeal.scm.web.services.IAlertNotificationService;

/**
 * Created by ashwini on 13/4/16.
 */

@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
public class TestAlertNotificationService {

    @Autowired
    private IAlertNotificationService service;

    @Test
    public void TestUpdateNotification() {
        AlertNotificationUpdateDTO dto = new AlertNotificationUpdateDTO();
        dto.setComment("none your business");
        dto.setNotificationId(107);
        dto.setUpdatedBy("Why");
        dto.setStatus("CLOSED");
        service.updateNotification(dto);
    }

    @Test
    public void TestListAlertNotification() {
        System.out.println(service.listAlertNotification());
    }

    @Test
    public void TestListAlertNotificationByUserName() {
        System.out.println(service.listAlertNotification("any One"));
    }
}
