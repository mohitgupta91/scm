package com.snapdeal.scm.mongo.doc;

import org.springframework.data.mongodb.core.mapping.Document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.common.domain.mongo.RuntimeStatusKey;

/**
 * 
 * @author prateek
 *
 */
@Document(collection = "runtime_status")
public class RuntimeStatus extends MongoDocument{

	private RuntimeStatusKey runtimeStatusKey;
	private Object value;
	public RuntimeStatus() {
	}
	public RuntimeStatus(RuntimeStatusKey runtimeStatusKey, Object value) {
		this.runtimeStatusKey = runtimeStatusKey;
		this.value = value;
	}
	public RuntimeStatusKey getRuntimeStatusKey() {
		return runtimeStatusKey;
	}
	public void setRuntimeStatusKey(RuntimeStatusKey runtimeStatusKey) {
		this.runtimeStatusKey = runtimeStatusKey;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
