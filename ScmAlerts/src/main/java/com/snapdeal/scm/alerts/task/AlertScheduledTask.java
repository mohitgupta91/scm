/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.alerts.task;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.snapdeal.scm.alerts.jms.AlertProducer;
import com.snapdeal.scm.mongo.dao.AlertInstanceRepository;
import com.snapdeal.scm.mongo.doc.AlertInstance;

/**
 * @version 1.0, 06-Apr-2016
 * @author ashwini
 */
public class AlertScheduledTask {

    private static final Logger  logger = LoggerFactory.getLogger(AlertScheduledTask.class);

    @Autowired
    private AlertInstanceRepository repo;

    @Autowired
    private AlertProducer        alertProducer;

    @Scheduled(cron = "${alert.task.cron}")
    public void alertJob() {
        DateTime currentTime = new DateTime();
        List<AlertInstance> alerts = repo.findByTimeBetween(currentTime.getHourOfDay());
        logger.info("Alerts to be Q for processing : " + alerts);
        alerts.forEach(alert -> {
            alertProducer.send(alert.getAlertId());
        });
    }
}