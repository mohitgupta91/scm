package com.snapdeal.scm.core.elastic.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * AggResult : Resultset
 * 
 * @author pranav, prateek, vinay
 *
 */
@SuppressWarnings("unchecked")
public class AggResult<T> {
	private T key;
	private Map<String, Double> dataValue = new HashMap<String, Double>();

	private List<AggResult<String>> aggResults;

	public AggResult(T key) {
		if(key instanceof DateTime){
			DateTime dt= (DateTime)key;
			DateTime today = new DateTime().withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("IST"))).withMillis(dt.getMillis());
			this.key = (T) today;
		}else
			this.key=key;

	}
	public AggResult(T key, Map<String, Double> dataValue) {
		if(key instanceof DateTime){
			DateTime dt= (DateTime)key;
			DateTime today = new DateTime().withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("IST"))).withMillis(dt.getMillis());
			this.key = (T) today;
		}else
			this.key=key;
		this.dataValue = dataValue;
	}
	public T getKey() {
		return key;
	}
	public void setKey(T key) {
		if(key instanceof DateTime){
			DateTime dt= (DateTime)key;
			DateTime today = new DateTime().withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("IST"))).withMillis(dt.getMillis());
			this.key = (T) today;
		}else
			this.key=key;
	}
	public Map<String, Double> getDataValue() {
		return dataValue;
	}
	public void setDataValue(Map<String, Double> dataValue) {
		this.dataValue = dataValue;
	}
	public void addDataValue(String dataKey, Double dataValue) {
		this.dataValue.put(dataKey, dataValue);
	}

	public List<AggResult<String>> getAggResults() {
		return aggResults;
	}

	public void setAggResults(List<AggResult<String>> aggResults) {
		this.aggResults = aggResults;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("AggResult{");
		sb.append("key=").append(key);
		sb.append(", dataValue=").append(dataValue);
		sb.append(", aggResults=").append(aggResults);
		sb.append('}');
		return sb.toString();
	}
}
