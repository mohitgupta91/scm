package com.snapdeal.scm.alerts.type;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.snapdeal.scm.mongo.doc.AlertInstance;
/**
 * 
 * @author mohit
 *
 *
 */
@Component
public class S2DDailyAvgNumberAlert extends BaseAlert{

	private static final Logger logger = LoggerFactory.getLogger(S2DDailyAvgNumberAlert.class);

	private Map<String, String> groupLogicToElasticColMap= new HashMap<String, String>();
	
	private static final String S2D_DAILY_AVG = "daily_s2d_avg";
	
	@Override
	public void process(AlertInstance alert) {
		logger.info("Processing S2D Average Number alert : "+alert.getAlertInstanceTitle());
	}

	static enum GroupLogic{
		OVERALL("overall"), LANE_TYPE("lanetype"),COURIER("courier"),LANE_COURIER("lane_courier");
		String name;
		GroupLogic(String name){
			this.name=name;
		}
	}
}
