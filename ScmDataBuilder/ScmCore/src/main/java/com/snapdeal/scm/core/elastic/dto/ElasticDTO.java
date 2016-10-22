package com.snapdeal.scm.core.elastic.dto;

import java.util.ArrayList;
import java.util.List;

import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;

/**
 * contain List both 
 * successful {@link ElasticDataDTO} to be stored in elastic search
 * failed {@link ProcessorQueueDto} to send back in Processor Queue
 * 
 * @author prateek
 *
 */
public class ElasticDTO {

	List<ElasticDataDTO> elasticDataDtos = new ArrayList<>();
	ProcessorQueueDto processorQueueDto;
	
	public ElasticDTO(QueryType queryType) {
			this.processorQueueDto = new ProcessorQueueDto(queryType);
		}
	
	public ElasticDTO(List<ElasticDataDTO> elasticDataDtos,
		ProcessorQueueDto sendToProcessorQueueDto) {
		this.elasticDataDtos = elasticDataDtos;
		this.processorQueueDto = sendToProcessorQueueDto;
	}

	public List<ElasticDataDTO> getElasticDataDtos() {
		return elasticDataDtos;
	}

	public void setElasticDataDtos(List<ElasticDataDTO> elasticDataDtos) {
		this.elasticDataDtos = elasticDataDtos;
	}
	
	public void addElasticDataDto(ElasticDataDTO elasticDataDto) {
		this.elasticDataDtos.add(elasticDataDto);
	}

	public ProcessorQueueDto getProcessorQueueDto() {
		return processorQueueDto;
	}

	public void setProcessorQueueDto(ProcessorQueueDto processorQueueDto) {
		this.processorQueueDto = processorQueueDto;
	}
	
	public void addStandardDtoInProcessorQueueDto(IStandardDTO standardDTO) {
		this.processorQueueDto.addDataProcessorDto(standardDTO);
	}
	
}
