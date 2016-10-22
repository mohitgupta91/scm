use scm;
db.metrics_info.remove({});
db.metrics_info.insert({
    "metricsId": 1,
    "title": "Average O2D Performance Delivered",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.O2DMetricElasticDas",
    "usedForPieChart": false
    });

db.metrics_info.insert({
    "metricsId": 2,
    "title": "Average O2D Performance Shipped",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.O2SMetricElasticDas",
    "usedForPieChart": false
    });
db.metrics_info.insert({
    "metricsId": 3,
    "title": "Delivery Adherance",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.DeliveredEDDRMetricElasticDas",
    "usedForPieChart": false
    });
db.metrics_info.insert({
    "metricsId": 4,
    "title": "Attempt Adherance",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.AttemptedEDDRMetricElasticDas",
    "usedForPieChart": false
    });
db.metrics_info.insert({
    "metricsId": 5,
    "title": "Percentage Delivered Performance",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.DeliveredPerformanceMetricElasticDas",
    "usedForPieChart": false
    });
db.metrics_info.insert({
    "metricsId": 6,
    "title": "S2D Pendency Trend",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.S2DPendencyTrendMetricElasticDas",
    "usedForPieChart": false
    });
db.metrics_info.insert({
    "metricsId": 7,
    "title": "Percentage Attempt Performance",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.AttemptedPerformanceMetricElasticDas",
    "usedForPieChart": false
    });
db.metrics_info.insert({
    "metricsId": 8,
    "title": "S2D Pendency For Integrated Couriers",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.PendencyMetricIntegratedElasticDas",
    "usedForPieChart": true
    });
db.metrics_info.insert({
    "metricsId": 9,
    "title": "S2D Pendency For Non Integrated Couriers",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.PendencyMetricNonIntegratedElasticDas",
    "usedForPieChart": true
    });
db.metrics_info.insert({
    "metricsId": 10,
    "title": "UD Monitoring",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.UndeliveredPendencyMetricElasticDas",
    "usedForPieChart": true
    });
db.metrics_info.insert({
    "metricsId": 38,
    "title": "RTO Count",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.RTOCountMetricElasticDas",
    "usedForPieChart": false
    });
db.metrics_info.insert({
    "metricsId": 39,
    "title": "RTO Status Percentage",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.RTOPercentageMetricElasticDas",
    "usedForPieChart": false
    });
db.metrics_info.insert({
    "metricsId": 40,
    "title": "RTO and SHP Pendency",
    "implClass" : "com.snapdeal.scm.web.core.das.impl.RTOPendencyElasticDas",
    "usedForPieChart": true
    });
