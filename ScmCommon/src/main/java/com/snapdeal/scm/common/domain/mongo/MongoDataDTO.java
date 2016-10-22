package com.snapdeal.scm.common.domain.mongo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author prateek
 *
 */
@SuppressWarnings("unchecked")
public class MongoDataDTO {

	private MongoCollectionName collectionName;
	private Map<String, Object> queryKeyValues = new HashMap<>();
	private Map<String, Object> insertKeyValues = new HashMap<>();
	
	public MongoDataDTO() {
	}
	
	public MongoDataDTO(MongoCollectionName collectionName) {
		this.collectionName = collectionName;
	}

	public void addQueryKeyValue(String key, Object value){
		this.queryKeyValues.put(key, value);
	}
	
	public void addInsertKeyObjectValue(String key, Object value){
		this.insertKeyValues.put(key, value);
	}
	
	public void addInsertKeyMapValue(String key, Map<String, Object> mapValue){
		this.insertKeyValues.put(key, mapValue);
	}
	
	public void addInsertKeySetValue(String key, Set<Object> listValue){
		this.insertKeyValues.put(key, listValue);
	}
	
	public void addInMapValueOfInsertKey(String insertKey, String insertMapKey, Object insertMapValue){
		Object object = this.insertKeyValues.computeIfAbsent(insertKey, (val) -> new HashMap<String, Object>());
		if(object instanceof Map){
			((Map<String, Object>)object).put(insertMapKey, insertMapValue);
		}
	}
	
	public void addInSetValueOfInsertKey(String insertKey, Object insertListValue){
		Object object = this.insertKeyValues.computeIfAbsent(insertKey, (val) -> new HashSet<Object>());
		if(object instanceof Set){
			((Set<Object>)object).add(insertListValue);
		}
	}
	
	public MongoCollectionName getCollectionName() {
		return collectionName;
	}
	
	public void setCollectionName(MongoCollectionName collectionName) {
		this.collectionName = collectionName;
	}
	
	public Map<String, Object> getQueryKeyValues() {
		return queryKeyValues;
	}
	
	public void setQueryKeyValues(Map<String, Object> queryKeyValues) {
		this.queryKeyValues = queryKeyValues;
	}
	
	public Map<String, Object> getInsertKeyValues() {
		return insertKeyValues;
	}
	
	public void setInsertKeyValues(Map<String, Object> insertKeyValues) {
		this.insertKeyValues = insertKeyValues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((collectionName == null) ? 0 : collectionName.hashCode());
		result = prime * result
				+ ((queryKeyValues == null) ? 0 : queryKeyValues.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MongoDataDTO other = (MongoDataDTO) obj;
		if (collectionName != other.collectionName)
			return false;
		if (queryKeyValues == null) {
			if (other.queryKeyValues != null)
				return false;
		} else if (!queryKeyValues.equals(other.queryKeyValues))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MongoDataDTO [collectionName=" + collectionName
				+ ", queryKeyValues=" + queryKeyValues + ", insertKeyValues="
				+ insertKeyValues + "]";
	}
}