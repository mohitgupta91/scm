package com.snapdeal.scm.mongo.mao.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBObject;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.mongo.mao.MongoRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @author prateek
 *
 */
@Service
public class MongoRepositoryImpl implements MongoRepositoryCustom{

	private static final String CREATED = "created";
	private static final String UPDATED = "updated";
	private static final String DOT = ".";

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public void upsert(MongoDataDTO mongoDataDTO) {
		Query queryObject = getQueryObject(mongoDataDTO.getQueryKeyValues());
		Update updateObject = getUpdateObject(mongoDataDTO.getInsertKeyValues());
		mongoOperations.upsert(queryObject, updateObject, mongoDataDTO.getCollectionName().getCollectionsName());
	}

	@Override
	public void upsertAll(List<MongoDataDTO> mongoDataDTOs) {
		String collectionName = mongoDataDTOs.get(0).getCollectionName().getCollectionsName();
		BulkWriteOperation bulkWriteOperation = mongoOperations.getCollection(collectionName).initializeOrderedBulkOperation();
		for(MongoDataDTO mongoDataDTO : mongoDataDTOs){
			DBObject queryDBObject = getQueryDBObject(mongoDataDTO.getQueryKeyValues());
			DBObject updateDBObject = getUpdateDBObject(mongoDataDTO.getInsertKeyValues());
			bulkWriteOperation.find(queryDBObject).upsert().updateOne(updateDBObject);
		}
		if(mongoDataDTOs.size() > 0) {
			BulkWriteResult execute = bulkWriteOperation.execute();
		}
	}

	private Query getQueryObject(Map<String, Object> map) {
		Query query = new Query();
		for(Entry<String, Object> entry : map.entrySet()){
			query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
		}
		return query;
	}

	private DBObject getQueryDBObject(Map<String, Object> map) {
		Query query = getQueryObject(map);
		return query.getQueryObject();
	}

	@SuppressWarnings("unchecked")
	private Update getUpdateObject(Map<String, Object> maps) {
		Update update = new Update();
		update.setOnInsert(CREATED, new Date());
		update.currentDate(UPDATED);
		for(Entry<String, Object> entry : maps.entrySet()){
			Object value = entry.getValue();
			if(value instanceof Map){
				setNotNullMap(update, entry.getKey(), (Map<String, Object>)entry.getValue());
			} else if(value instanceof Set){
				pushNotNullSet(update, entry.getKey(), (Set<Object>)entry.getValue());
			} else if(value instanceof List){
				pushNotNullList(update, entry.getKey(), (List<Object>)entry.getValue());
			} else if(value instanceof Enum){
				setNotNull(update, entry.getKey(), ((Enum) value).name());
			}else  {
				setNotNull(update, entry.getKey(), value);
			}
		}
		return update;
	}

	private void pushNotNullList(Update update, String key, List<Object> listValue) {
		if(CollectionUtils.isEmpty(listValue))
			return;
		Object obj = listValue.iterator().next();
		if(obj instanceof String || obj instanceof Integer 
				|| obj instanceof Long || obj instanceof Float || obj instanceof Double){
			update.pushAll(key, listValue.toArray());
		} else {
			ObjectMapper mapper = new ObjectMapper();
			DBObject[] objects = new DBObject[listValue.size()];
			int  i = 0;
			for(Object object : listValue){
				objects[i++] = mapper.convertValue(object, BasicDBObject.class);
			}
			update.pushAll(key, objects);
		}
	}

	private void pushNotNullSet(Update update, String key, Set<Object> setValue) {
		if(CollectionUtils.isEmpty(setValue))
			return;
		Object obj = setValue.iterator().next();
		if(obj instanceof String || obj instanceof Integer 
				|| obj instanceof Long || obj instanceof Float || obj instanceof Double){
			update.pushAll(key, setValue.toArray());
		} else {
			ObjectMapper mapper = new ObjectMapper();
			DBObject[] objects = new DBObject[setValue.size()];
			int  i = 0;
			for(Object object : setValue){
				objects[i++] = mapper.convertValue(object, BasicDBObject.class);
			}
			update.pushAll(key, objects);
		}
	}

	private void setNotNullMap(Update update, String key, Map<String, Object> map) {
		for(Entry<String, Object> entry : map.entrySet()){
			setNotNull(update, key + DOT + entry.getKey(), entry.getValue());
		}
	}
	private void setNotNull(Update update, String key, Object value){
		Optional.ofNullable(value).ifPresent(val -> update.set(key, val));
	}

	private DBObject getUpdateDBObject(Map<String, Object> maps) {
		return getUpdateObject(maps).getUpdateObject();
	}
}