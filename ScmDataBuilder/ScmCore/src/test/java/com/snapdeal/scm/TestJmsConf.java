package com.snapdeal.scm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.mongo.mao.repository.ScmJmsPropertyMongoRepository;
import com.snapdeal.scm.mongo.doc.JmsDetails;
import com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty;

/**
 * TestJmsConf : Test the JMS Configuration for Pushing up initial Data
 * 
 * @author pranav
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestScmDataJpaApplication.class)
@EnableMongoAuditing
public class TestJmsConf {

    @Autowired
    private ApplicationContext            context;

    @Autowired
    private ScmJmsPropertyMongoRepository repo;

    @Before
    public void setUp() throws Exception {
        repo.deleteAll();
        repo.deleteJmsDetails();
        ScmJmsMachineProperty scmJmsProperty = new ScmJmsMachineProperty("data-localhost");
        JmsDetails details = new JmsDetails("tcp://localhost:61616", "admin", "admin", "data-poller", "data-processor", "alerts");
        scmJmsProperty.setMaxRedeliveryAttempt(0);
        scmJmsProperty.setRedeliveryDelayInMS(10000);
        scmJmsProperty.setDataPoller(true);
        scmJmsProperty.setFileHandler(true);
        scmJmsProperty.setFhConcurrency("5-10");
        scmJmsProperty.setProcessorConcurrency("5-10");
        scmJmsProperty.setDataProcessor(false);
        scmJmsProperty.setProcessorScheduler(false);
        scmJmsProperty.setAlertConsumer(false);
        scmJmsProperty.setAlertProducer(false);
        scmJmsProperty.setAlertsConcurrency("1-2");
        repo.saveJmsDetails(details);
        repo.save(scmJmsProperty);

        scmJmsProperty = new ScmJmsMachineProperty("web-localhost");
        scmJmsProperty.setMaxRedeliveryAttempt(0);
        scmJmsProperty.setRedeliveryDelayInMS(10000);
        scmJmsProperty.setDataPoller(false);
        scmJmsProperty.setFileHandler(false);
        scmJmsProperty.setFhConcurrency("5-10");
        scmJmsProperty.setProcessorConcurrency("5-10");
        scmJmsProperty.setDataProcessor(false);
        scmJmsProperty.setAlertConsumer(false);
        scmJmsProperty.setAlertProducer(false);
        scmJmsProperty.setAlertsConcurrency("1-2");
        repo.save(scmJmsProperty);
    }

    @Test
    public void testScmJmsConf() throws Exception {
        ScmJmsMachineProperty jmsConf = repo.findByCritria("localhost");
        System.out.println("\n\n" + jmsConf + "\n\n");
    }
}
