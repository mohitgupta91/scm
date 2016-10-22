package com.snapdeal.scm.processor.elastic;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.dto.validator.IDTOValidator;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticScriptDataDTO;
import com.snapdeal.scm.core.jms.impl.DataProcessorProducer;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;
import com.snapdeal.scm.processor.IElasticProcessor;
import com.snapdeal.scm.processor.IElasticService;
import com.snapdeal.scm.processor.exception.InsufficientDataException;
import com.snapdeal.scm.processor.exception.InvalidDataException;
import com.snapdeal.scm.processor.impl.DataProcessorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import javax.jms.JMSException;
import java.util.*;

/**
 * Abstract class to process {@link ProcessorQueueDto}. {@link ProcessorQueueDto} contain {@link ProcessorQueueDto#getQueryType()} and 
 * {@link ProcessorQueueDto#getDataProcessorDtos()}.
 * 
 * <p> Processing of {@link ProcessorQueueDto#getDataProcessorDtos()} may produce 3 type of output.
 * <p> 1. DTO with invalid values (i.e data which throw {@link InvalidDataException}) --> ignore
 * <p> 2. DTO for which data is insufficient in mongo to process (i.e data which throw {@link InsufficientDataException}) --> send back
 * again to processor jms queue to reprocess.
 * <p> 3. DTO with sufficient data --> stored in elastic
 * 
 * @author prateek
 *
 */
public abstract class AbstractElasticProcessor<T extends IStandardDTO> implements IElasticProcessor<T>{

	private static final Logger LOG = LoggerFactory.getLogger(DataProcessorImpl.class);
	private static final Logger errorLOG = LoggerFactory.getLogger("DATAPROCESSOR-ERROR-LOGGER");

	@Value("${scm.processor.retry.count}")
	private double retryCount;

	@Value("${scm.processor.retry.delay}")
	private double retryDelay;

	@Autowired
	private IElasticService elasticServicce;

	@Autowired
	private DataProcessorProducer dataProcessorProducer;

	@Autowired
	private IDTOValidator dtoValidator;

	@Override
	public void process(ProcessorQueueDto<T> processorQueueDto) throws JMSException {
		ProcessorQueueDto<T> insufficientDataDto = new ProcessorQueueDto<>(processorQueueDto.getQueryType(), processorQueueDto.getFilePath());
		List<T> invalidDataDtos = new ArrayList<>();
		Map<String, ElasticDataDTO> elasticMap  = new HashMap<>();
		Map<String, ElasticScriptDataDTO> elasticScriptMap  = new HashMap<>();
		Map<String, MongoDocument> mongoDataMap  = new HashMap<>();
		preProcess(processorQueueDto);
		for(T standardDTO : processorQueueDto.getDataProcessorDtos()){
			if(!dtoValidator.validate(standardDTO)){
				invalidDataDtos.add(standardDTO);
				continue;
			}
			try {
				ElasticDataDTO elasticDataDTO = processRecord(standardDTO);
				postProcessRecord(elasticDataDTO, elasticMap, elasticScriptMap, mongoDataMap);
			} catch (InvalidDataException invalidDataException){
				errorLOG.error(invalidDataException.getMessage());
				invalidDataDtos.add(standardDTO);
			} catch (InsufficientDataException insufficientDataException) {
				errorLOG.error(insufficientDataException.getMessage());
				insufficientDataDto.addDataProcessorDto(standardDTO);
			}
		}
		postProcess();
		if(!invalidDataDtos.isEmpty())
			errorLOG.error("Data with Invalid values, Total DTO size : {}, DTOs :{}", invalidDataDtos.size(), invalidDataDtos);
		// Storing in elastic Search
		storeInElasticSearch(elasticMap.values());
		// Storing in mongo
		storeInMongo(mongoDataMap.values());
		// Compute data
		computeData(elasticScriptMap.values());
		// sending in processor Queue
		retryProcess(insufficientDataDto, processorQueueDto.getRetryCounter());
	}

	protected void storeInMongo(Collection<MongoDocument> values) {
		
	}

	protected void postProcessRecord(ElasticDataDTO elasticDataDTO, Map<String, ElasticDataDTO> elasticMap,
			Map<String, ElasticScriptDataDTO> elasticScriptMap, Map<String, MongoDocument> mongoDataMap) {
		mergeElasticDataDTO(elasticDataDTO, elasticMap);
		mergerElasticScriptDataDTO(elasticDataDTO, elasticScriptMap);
		mergeMongoDataDTO(elasticDataDTO, mongoDataMap);
	}

	protected void mergeElasticDataDTO(ElasticDataDTO elasticDataDTO, Map<String, ElasticDataDTO> elasticMap) {
		if(CollectionUtils.isEmpty(elasticDataDTO.getDataValueMap())) {
			return;
		}
		ElasticDataDTO previousElasticDataDTO = elasticMap.get(elasticDataDTO.getSubOrderCode());
		if(previousElasticDataDTO != null){
			previousElasticDataDTO.addAllDataValues(elasticDataDTO.getDataValueMap());
		} else {
			elasticMap.put(elasticDataDTO.getSubOrderCode(), elasticDataDTO);
		}
	}

	protected void mergerElasticScriptDataDTO(ElasticDataDTO elasticDataDTO, Map<String, ElasticScriptDataDTO> elasticScriptMap) {
		ElasticScriptDataDTO elasticScriptDataDTO = elasticDataDTO.getElasticScriptDataDTO();
		if(Objects.isNull(elasticScriptDataDTO)){
			return;
		}
		ElasticScriptDataDTO previousElasticScriptDataDTO = elasticScriptMap.get(elasticDataDTO.getSubOrderCode());
		if(previousElasticScriptDataDTO != null){
			previousElasticScriptDataDTO.addAllScripts(elasticScriptDataDTO.getScripts());
			previousElasticScriptDataDTO.addAllParams(elasticScriptDataDTO.getParams());
			previousElasticScriptDataDTO.addAllDataValues(elasticScriptDataDTO.getDataValueMap());
		} else {
			elasticScriptMap.put(elasticDataDTO.getSubOrderCode(), elasticScriptDataDTO);
		}
	}

	protected void mergeMongoDataDTO(ElasticDataDTO elasticDataDTO, Map<String, MongoDocument> mongoDataMap) {
	}

	private void computeData(Collection<ElasticScriptDataDTO> collection) {
		if(CollectionUtils.isEmpty(collection))
			LOG.info("No data found to Store in Elastic Search using script");
		else {
			LOG.info("Data storing in elastic using script : {}", collection);
			elasticServicce.runDynamicScript(collection);
		}
	}

	private void retryProcess(ProcessorQueueDto<T> insufficientDataDto, Double previousRetryCount) throws JMSException {
		if(insufficientDataDto==null || insufficientDataDto.isEmpty()){
			LOG.info("No data found to send in Processor Queue");
		}else{
			if(previousRetryCount == retryCount)
				LOG.info("No of retry limit complete Total DTO Size : {}",insufficientDataDto.size());
			else {
				Double newCount = previousRetryCount + 1;
				insufficientDataDto.setRetryCounter(newCount);
				LOG.info("Data sending again in processor jms queue for reprocessing, Total DTO Size : {}", insufficientDataDto.size());
				dataProcessorProducer.sendInSync(insufficientDataDto, getDelay(newCount));
			}
		}
	}

	private long getDelay(Double newCount) {
		return (long)(Math.pow(retryDelay/1000, newCount) * 1000);
	}

	private void storeInElasticSearch(Collection<ElasticDataDTO> elasticDataDTOs) {
		if(CollectionUtils.isEmpty(elasticDataDTOs))
			LOG.info("No data found to Store in Elastic Search");
		else {
			LOG.info("Data storing in elastic : {}", elasticDataDTOs);
			elasticServicce.indexDocuments(elasticDataDTOs);
		}			
	}

	protected void preProcess(ProcessorQueueDto<T> processorQueueDto){
	}

	protected void postProcess() {
	}

	public abstract ElasticDataDTO processRecord(T standardDTO) throws InvalidDataException, InsufficientDataException;
}
