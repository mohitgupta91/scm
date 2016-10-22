package com.snapdeal.scm.core.processor.dto.impl;

import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.enums.QueryType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author prateek
 *
 */
public class ProcessorQueueDto<T extends IStandardDTO> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5196390201147805842L;
	@NotEmpty
	List<T> dataProcessorDtos = new  ArrayList<>();
	@NotNull
	QueryType queryType;
	String filePath;
	// Used for ElasticProcessor.
	Double retryCounter = new Double(0);
	
	public ProcessorQueueDto() {
		
	}

	public ProcessorQueueDto(List<T> dataProcessorDtos,
			QueryType queryType, String filePath) {
		this.dataProcessorDtos = dataProcessorDtos;
		this.queryType = queryType;
		this.filePath = filePath;
	}
	
	public ProcessorQueueDto(QueryType queryType) {
		this.queryType = queryType;
	}

	public ProcessorQueueDto(QueryType queryType, String filePath) {
		this.queryType = queryType;
		this.filePath = filePath;
	}

	public List<T> getDataProcessorDtos() {
		return dataProcessorDtos;
	}

	public void setDataProcessorDtos(List<T> dataProcessorDtos) {
		this.dataProcessorDtos = dataProcessorDtos;
	}

	public QueryType getQueryType() {
		return queryType;
	}

	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}
	
	public void addDataProcessorDto(T dataProcessorDto) {
		dataProcessorDtos.add(dataProcessorDto);
	}
	
	public boolean isEmpty(){
		return (dataProcessorDtos == null || dataProcessorDtos.isEmpty());
	}
	
	public int size() {
		if(isEmpty())
			return 0;
		return dataProcessorDtos.size();
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Double getRetryCounter() {
		return retryCounter;
	}

	public void setRetryCounter(Double retryCounter) {
		this.retryCounter = retryCounter;
	}

	@Override
	public String toString() {
		return "ProcessorQueueDto [dataProcessorDtos=" + dataProcessorDtos
				+ ", queryType=" + queryType + ", filePath=" + filePath
				+ ", retryCounter=" + retryCounter + "]";
	}
}
