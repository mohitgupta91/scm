package com.snapdeal.scm.das.mongo.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBObject;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.mongo.document.ConnectedRadStatus;
import com.snapdeal.scm.mongo.mao.repository.ScmJmsPropertyMongoRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @author prateek
 *
 */
@Component
public class ConnectedRadStatusRepositoryImpl implements ConnectedRadStatusRepositoryCustom{

	private static final Logger logger = LoggerFactory.getLogger(ScmJmsPropertyMongoRepositoryImpl.class);

	private static final String CONNECTED_RAD_STATUS = "connected_rad_status";
	private static final String SUBORDER_CODE = "subOrderCode";
	private static final String CREATED = "created";
	private static final String UPDATED = "updated";
	private static final String FIRST_CURRENT_LOCATION_HUB = "firstCurrentLocationHub";
	private static final String COURIER_ORIGIN_CITY = "courierOriginCity";
	private static final String COURIER_ORIGIN_STATE = "courierOriginState";
	private static final String COURIER_DESTINATION_CITY = "courierDestinationCity";
	private static final String COURIER_DESTINATION_STATE = "courierDestinationState";
	private static final String COURIER_CODE = "courierCode";
	private static final String COURIER_GROUP = "courierGroup";
	private static final String SHIPPING_MODE = "shippingMode";
	private static final String CUSTOMER_DESTINATION_PINCODE = "customerDestinationPincode";
	private static final String COMPLETE = "complete";
	private static final String RAD_DATE = "radDate";
	private static final String CONNECTED_DATE = "connectedDate";
	private static final String CURRENT_RAD_STATUS_DATE_DOT = "currentRADCityToDate.";
	private static final String CURRENT_CONNECTED_STATUS_DETAILS = "connectedStatusDetails";

	@Autowired
	private MongoOperations mongoOperations;

	@Autowired
	private ConnectedRadStatusRepository connectedRadStatusRepository;

	@Override
	public List<ConnectedRadStatus> findAllToProcess(Date time) {
		Query query = new Query();
		query.addCriteria(Criteria.where(UPDATED).gte(time)).
		addCriteria(Criteria.where(COMPLETE).ne(true));
		return mongoOperations.find(query, ConnectedRadStatus.class);
	}

	@Override
	public void upsert(ConnectedRadStatus connectedRadStatus) {
		Query queryObject = getQueryObject(connectedRadStatus.getSubOrderCode());
		Update updateObject = getUpdateObject(connectedRadStatus);
		mongoOperations.upsert(queryObject, updateObject, ConnectedRadStatus.class);
	}

	@Override
	public void upsertAll(Collection<MongoDocument> connectedRadStatus) {
		BulkWriteOperation bulkWriteOperation = mongoOperations.getCollection(CONNECTED_RAD_STATUS).initializeOrderedBulkOperation();
		for(MongoDocument connectedRad : connectedRadStatus){
			DBObject queryDBObject = getQueryDBObject(((ConnectedRadStatus)connectedRad).getSubOrderCode());
			DBObject updateDBObject = getUpdateDBObject((ConnectedRadStatus)connectedRad);
			bulkWriteOperation.find(queryDBObject).upsert().updateOne(updateDBObject);
		}
		if(connectedRadStatus.size() > 0) {
			BulkWriteResult execute = bulkWriteOperation.execute();
		}
	}

	private Query getQueryObject(String subOrderCode) {
		return new Query().addCriteria(Criteria.where(SUBORDER_CODE).is(subOrderCode));
	}

	private DBObject getQueryDBObject(String subOrderCode) {
		Query query = getQueryObject(subOrderCode);
		return query.getQueryObject();
	}

	private Update getUpdateObject(ConnectedRadStatus connectedRadStatus) {
		Update update = new Update();
		update.setOnInsert(CREATED, new Date());
		update.currentDate(UPDATED);
		// or use reflection here
		setNotNull(update, FIRST_CURRENT_LOCATION_HUB, connectedRadStatus.getFirstCurrentLocationHub());
		setNotNull(update, COURIER_ORIGIN_CITY, connectedRadStatus.getCourierOriginCity());
		setNotNull(update, COURIER_ORIGIN_STATE, connectedRadStatus.getCourierOriginState());
		setNotNull(update, COURIER_DESTINATION_CITY, connectedRadStatus.getCourierDestinationCity());
		setNotNull(update, COURIER_DESTINATION_STATE, connectedRadStatus.getCourierDestinationState());
		setNotNull(update, COURIER_CODE, connectedRadStatus.getCourierCode());
		setNotNull(update, COURIER_GROUP, connectedRadStatus.getCourierGroup());
		setNotNull(update, SHIPPING_MODE, connectedRadStatus.getShippingMode());
		setNotNull(update, CUSTOMER_DESTINATION_PINCODE, connectedRadStatus.getCustomerDestinationPincode());
		setNotNull(update, COMPLETE, connectedRadStatus.isComplete());
		setNotNull(update, RAD_DATE, connectedRadStatus.getRadDate());
		setNotNull(update, CONNECTED_DATE, connectedRadStatus.getConnectedDate());
		for(Entry<String, Date> entrySet : connectedRadStatus.getCurrentRADCityToDate().entrySet())
			setNotNull(update, CURRENT_RAD_STATUS_DATE_DOT+entrySet.getKey(), entrySet.getValue());
		pushNotNull(update, CURRENT_CONNECTED_STATUS_DETAILS, connectedRadStatus.getConnectedStatusDetails());
		return update;
	}

	private void pushNotNull(Update update, String key, Set<? extends Object> listValue) {
		int size = listValue.size();
		if(size == 0)
			return;
		ObjectMapper mapper = new ObjectMapper();
		DBObject[] objects = new DBObject[size];
		int  i = 0;
		for(Object object : listValue){
			objects[i++] = mapper.convertValue(object, BasicDBObject.class);
		}
		update.pushAll(key, objects);
	}

	private void setNotNull(Update update, String key, Object value){
		Optional.ofNullable(value).ifPresent(val -> update.set(key, val));
	}

	private DBObject getUpdateDBObject(ConnectedRadStatus connectedRadStatus) {
		return getUpdateObject(connectedRadStatus).getUpdateObject();
	}
}
