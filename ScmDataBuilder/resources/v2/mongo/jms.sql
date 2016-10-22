use scm;
db.jms_details.remove({});
db.scm_jms_machine_property.remove({});
db.scm_jms_machine_property.insert({
	"_class" : "com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty",
	"machineHostName" : "data-localhost",
	"fhConcurrency" : "5-10",
	"processorConcurrency" : "5-10",
	"alertsConcurrency" : "1-2",
	"isDataPoller" : true,
	"isFileHandler" : true,
	"isDataProcessor" : true,
	"isAlertConsumer" : false,
	"isAlertProducer" : false,
	"isProcessorScheduler" : true,
	"maxRedeliveryAttempt" : 0,
	"redeliveryDelayInMS" : NumberLong(10000),
	"created" : new Date(),
	"updated" : new Date()
});

db.scm_jms_machine_property.insert({
	"_class" : "com.snapdeal.scm.mongo.doc.ScmJmsMachineProperty",
	"machineHostName" : "web-localhost",
	"fhConcurrency" : "5-10",
	"processorConcurrency" : "5-10",
	"alertsConcurrency" : "1-2",
	"isDataPoller" : false,
	"isFileHandler" : false,
	"isDataProcessor" : false,
	"isAlertConsumer" : false,
	"isAlertProducer" : false,
	"isProcessorScheduler" : false,
	"maxRedeliveryAttempt" : 0,
	"redeliveryDelayInMS" : NumberLong(10000),
	"created" : new Date(),
	"updated" : new Date()
});


db.jms_details.insert({
	"_class" : "com.snapdeal.scm.mongo.doc.JmsDetails",
	"jmsBrokerUrl" : "tcp://localhost:61616",
	"uname" : "admin",
	"pwd" : "admin",
	"destPollerToFH" : "data-poller",
	"destFHToProcessor" : "data-processor",
	"destAlerts" : "alerts",
	"created" : new Date(),
	"updated" : new Date()
});
