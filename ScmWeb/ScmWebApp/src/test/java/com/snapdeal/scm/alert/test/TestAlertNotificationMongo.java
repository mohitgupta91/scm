package com.snapdeal.scm.alert.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.enums.SequenceKey;
import com.snapdeal.scm.mongo.dao.AlertNotificationRepository;
import com.snapdeal.scm.mongo.mao.ISequenceMao;
import com.snapdeal.scm.mongo.doc.AlertNotification;
import com.snapdeal.scm.mongo.doc.AlertNotification.NotificationStatus;
import com.snapdeal.scm.mongo.doc.NotificationActivity;
import com.snapdeal.scm.web.ConfigurationScmWebApp;

/**
 * 
 * @author mohit
 */
@SpringApplicationConfiguration(ConfigurationScmWebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest
public class TestAlertNotificationMongo{

	@Autowired
	AlertNotificationRepository notificationRepo;
	
	@Autowired
	private ISequenceMao sequenaceDao;
	
	@Before
	public void setUp(){
		notificationRepo.deleteAll();
		AlertNotification notification = new AlertNotification();
		notification.setAlertId(233);
		notification.setAlertCreatedBy("admin");
		notification.setAssignedTo("admin1");
		notification.setCreated(new Date());
		notification.setCurrentStatus(NotificationStatus.ASSIGNED);
		notification.getDetails().add("abcd");
		notification.setUpdated(new Date());
		notification.setNotificationId(sequenaceDao.getNextSequenceId(SequenceKey.ALERT_NOTIFICATION.name()));
		notification.setInstanceId(173);
		notificationRepo.save(notification);
		
		AlertNotification notification2 = new AlertNotification();
		notification2.setAlertId(233);
		notification2.setAlertCreatedBy("admin1");
		notification2.setAssignedTo("admin");
		notification2.setCreated(new Date());
		notification2.setCurrentStatus(NotificationStatus.ASSIGNED);
		notification2.getDetails().add("abcd2");
		notification2.setUpdated(new Date());
		notification2.setNotificationId(sequenaceDao.getNextSequenceId(SequenceKey.ALERT_NOTIFICATION.name()));
		notification2.setInstanceId(173);
		
//		List<NotificationActivity> list2 = new ArrayList<NotificationActivity>();
//		NotificationActivity activity2 = new NotificationActivity();
//		activity2.setComment("commemt1");
//		activity2.setStatus(NotificationStatus.SUBMITTED);
//		activity2.setUpdatedBy("admin");
//		activity2.setUpdatedOn(new Date());
//		list2.add(activity2);
//		notification2.setNotificationActivity(list2);
		notificationRepo.save(notification2);
	
		AlertNotification notification3 = new AlertNotification();
		notification3.setAlertId(233);
		notification3.setAlertCreatedBy("admin");
		notification3.setAssignedTo("admin1");
		notification3.setCreated(new Date());
		notification3.setCurrentStatus(NotificationStatus.SUBMITTED);
		notification3.getDetails().add("abcd3");
		notification3.setUpdated(new Date());
		notification3.setNotificationId(sequenaceDao.getNextSequenceId(SequenceKey.ALERT_NOTIFICATION.name()));
		notification3.setInstanceId(173);
		
		List<NotificationActivity> list3 = new ArrayList<NotificationActivity>();
		NotificationActivity activity3 = new NotificationActivity();
		activity3.setComment("commemt1");
		activity3.setStatus(NotificationStatus.SUBMITTED);
		activity3.setUpdatedBy("admin1");
		activity3.setUpdatedOn(new Date());
		list3.add(activity3);
		
		
		notification3.setNotificationActivity(list3);
		notificationRepo.save(notification3);
	
	}
	
	@Test
	public void alertNotificationTest(){
		System.out.println(notificationRepo.findAll().get(0));
	}
}
