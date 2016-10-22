package com.snapdeal.scm.alert.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.alerts.type.S2dHistoricalComparisonOverallAlert;
import com.snapdeal.scm.enums.SequenceKey;
import com.snapdeal.scm.mongo.dao.AlertGroupLogicRepository;
import com.snapdeal.scm.mongo.dao.AlertInstanceRepository;
import com.snapdeal.scm.mongo.dao.AlertRepository;
import com.snapdeal.scm.mongo.mao.ISequenceMao;
import com.snapdeal.scm.mongo.dao.InstanceGroupingParamRepository;
import com.snapdeal.scm.mongo.doc.Alert;
import com.snapdeal.scm.mongo.doc.AlertGroupLogic;
import com.snapdeal.scm.mongo.doc.AlertInstance;
import com.snapdeal.scm.mongo.doc.InstanceGroupingParam;
import com.snapdeal.scm.web.ConfigurationScmWebApp;

/**
 * TestAlertMongo
 * 
 * @author pranav, Ashwini, mohit
 */
@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
public class TestAlertMongo {

    @Autowired
    private AlertRepository alertRepo;

    @Autowired
    private AlertInstanceRepository alertInstanceRepo;

    @Autowired
    private AlertGroupLogicRepository alertGroupLogicRepo;

    @Autowired
    private ISequenceMao sequenaceDao;

    @Autowired
    private InstanceGroupingParamRepository instanceGroupingRepo;

    private Long alertInstance1, alertInstance2, alertId;

    @Before
    public void testData() {

        alertRepo.deleteAll();
        Alert alert = new Alert(sequenaceDao.getNextSequenceId(SequenceKey.ALERT.name()));
        alert.setCreated(new Date());
        alert.setImplClass(S2dHistoricalComparisonOverallAlert.class.getName());
        alert.setTitle("s2d_comparison");
        alert.setUpdated(new Date());
        alertRepo.save(alert);
        alertId = alert.getAlertId();

        Alert alert2 = new Alert(sequenaceDao.getNextSequenceId(SequenceKey.ALERT.name()));
        alert2.setCreated(new Date());
        alert2.setImplClass(S2dHistoricalComparisonOverallAlert.class.getName());
        alert2.setTitle("o2d_comparison");
        alert2.setUpdated(new Date());
        alertRepo.save(alert2);

        alertGroupLogicRepo.deleteAll();
        AlertGroupLogic alertLogic = new AlertGroupLogic(alert.getAlertId(), new Date(), new Date(), false, "overall");
        alertGroupLogicRepo.save(alertLogic);
        alertLogic = new AlertGroupLogic(alert.getAlertId(), new Date(), new Date(), true, "Lane");
        alertGroupLogicRepo.save(alertLogic);

        alertInstanceRepo.deleteAll();
        AlertInstance alertInstance = new AlertInstance(sequenaceDao.getNextSequenceId(SequenceKey.ALERT_INSTANCE.name()));
        alertInstance.setAlertTitle(alert.getTitle());
        alertInstance.setAlertId(alert.getAlertId());
        alertInstance.setCreated(new Date());
        alertInstance.setCurrentDateRangeEnd(0);
        alertInstance.setCurrentDateRangeStart(30);
        alertInstance.setEmailId("emailId");
        alertInstance.setHistoricalDateRangeEnd(2);
        alertInstance.setHistoricalDateRangeStart(30);
        alertInstance.setOperator("=");
        alertInstance.setRuleTime(14);
        alertInstance.setUpdated(new Date());
        alertInstance.setValue(5);
        alertInstance.setGroupLogicName("overall");
        alertInstance.setCreatedBy("admin");
        alertInstanceRepo.save(alertInstance);
        alertInstance1 = alertInstance.getAlertId();

        AlertInstance alert1 = new AlertInstance(sequenaceDao.getNextSequenceId(SequenceKey.ALERT_INSTANCE.name()));
        alert1.setRuleTime(15);
        alert1.setValue(5);
        alert1.setAlertId(alert2.getAlertId());
        alert1.setAlertTitle(alert2.getTitle());
        alert1.setGroupLogicName("lane");
        alert1.setCurrentDateRangeEnd(0);
        alert1.setCurrentDateRangeStart(30);
        alert1.setHistoricalDateRangeStart(30);
        alert1.setHistoricalDateRangeEnd(2);
        alert1.setOperator("=");
        alert1.setUpdated(new Date());
        alert1.setValue(5);
        alert1.setCreatedBy("admin");
        alert1.setCreated(new Date());
        InstanceGroupingParam instanceParam = new InstanceGroupingParam();
        instanceParam.setAlertInstanceId(alert1.getAlertInstanceId());
        instanceParam.setRowId(sequenaceDao.getNextSequenceId(SequenceKey.ALERT_INSTANCE_FILE_ROW.name()));
        Map<String,String> groupingParam = new HashMap<>();
        groupingParam.put("destinationCity", "NCR");
        groupingParam.put("sourceCity", "Delhi");
        groupingParam.put("sourceState", "Delhi");
        groupingParam.put("destinationState", "Delhi");
        groupingParam.put("value", "5");
        instanceParam.setGroupingParam(groupingParam);
        instanceGroupingRepo.save(instanceParam);
        alertInstanceRepo.save(alert1);
        alertInstance2 = alert1.getAlertId();
    }

    @Test
    public void testAlertById() {
        System.out.println(alertRepo.findOne("570b615c44ae3f59bb161fce"));
    }

    @Test
    public void testAlert() {
        System.out.println(alertInstanceRepo.findByAlertId(alertInstance1));
    }

    @Test
    public void testAlert2() {
        System.out.println(alertInstanceRepo.findByAlertId(alertInstance2));
    }

    @Test
    public void testAlertType() {
        System.out.println(alertRepo.findByAlertId(alertId));
    }

    @Test
    public void testAlertTypeGroupLogic() {
        System.out.println(alertGroupLogicRepo.findByAlertId(alertId));
    }

}
