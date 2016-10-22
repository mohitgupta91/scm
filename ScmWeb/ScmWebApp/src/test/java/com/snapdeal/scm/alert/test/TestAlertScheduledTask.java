/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.alert.test;

import com.snapdeal.scm.alerts.GroupLogic;
import com.snapdeal.scm.mongo.dao.AlertInstanceRepository;
import com.snapdeal.scm.mongo.doc.AlertInstance;
import com.snapdeal.scm.web.core.dto.AlertInstanceDTO;
import com.snapdeal.scm.web.core.exception.FileUploadException;
import com.snapdeal.scm.web.services.IAlertService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.alerts.task.AlertScheduledTask;
import com.snapdeal.scm.web.ConfigurationScmWebApp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0, 08-Apr-2016
 * @author ashwini
 */
@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
public class TestAlertScheduledTask {

    @Autowired
    private AlertScheduledTask task;

    @Autowired
    private IAlertService alertService;

    private static Long alertInstanceId;

    @Before
    public void setup() throws IOException, FileUploadException {
        AlertInstanceDTO dto = new AlertInstanceDTO();
        dto.setOperator(">");
        dto.setEmailId("ashwini.kumar@snapdeal.com");
        dto.setFile(null);
        dto.setValue(new Float(0.0));
        dto.setRuleTime(new Integer(12));
        dto.setAlertId(alertService.getAllAlerts().get(0).getAlertId());
        dto.setGroupLogicName(GroupLogic.OVERALL.name());
        dto.setHistoricalDateRangeStart(new Integer(86));
        dto.setHistoricalDateRangeEnd(new Integer(90));
        dto.setCurrentDateRangeStart(new Integer(86));
        dto.setCurrentDateRangeEnd(new Integer(86));
        dto.setAlertTitle(alertService.getAllAlerts().get(0).getTitle());
        dto.setAlertInstanceTitle("test");
        dto.setFileHeader(null);
        dto.setCreatedBy("test");
        dto.setUpdatedBy("test");
        alertInstanceId = alertService.create(dto).getAlertInstanceId();
    }

    @Test
    public void alertJob() {
        task.alertJob();
    }

    @After
    public void destroy(){
        alertService.delete(alertInstanceId);
    }
}
