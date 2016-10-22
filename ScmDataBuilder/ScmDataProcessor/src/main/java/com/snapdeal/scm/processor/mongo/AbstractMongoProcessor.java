package com.snapdeal.scm.processor.mongo;

import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.dto.validator.IDTOValidator;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;
import com.snapdeal.scm.mongo.mao.MongoRepositoryCustom;
import com.snapdeal.scm.processor.IMongoProcessor;
import com.snapdeal.scm.processor.impl.DataProcessorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 
 * @author prateek
 *
 */
public abstract class AbstractMongoProcessor<T extends IStandardDTO> implements IMongoProcessor<T>{

	private static final Logger LOG = LoggerFactory.getLogger(DataProcessorImpl.class);

	@Autowired
	private IDTOValidator dtoValidator;
	
	@Autowired
	private MongoRepositoryCustom mongoRepositoryCustom;

	@Override
	public void process(ProcessorQueueDto<T> processorQueueDto){
		List<T> invalidDataDtos = new ArrayList<>();
		List<MongoDataDTO> mongoDataDTOs = new ArrayList<>();
		LOG.debug("Data to process : {}", processorQueueDto);
		for(T standardDTO  : processorQueueDto.getDataProcessorDtos()){
			if(!dtoValidator.validate(standardDTO))
				invalidDataDtos.add(standardDTO);
			else {
				Optional.ofNullable(processStandardDTO(standardDTO))
				.ifPresent((mongoDataDTO) -> {
					if(!mergeDataDTO(mongoDataDTO, mongoDataDTOs))
						mongoDataDTOs.add(mongoDataDTO);
				});
/*
				MongoDocument mongoDocument = processRecord(standardDTO);
				if(mongoDocument != null) {
					getMongoRepository().save(mongoDocument);
				}
*/
			}
		}
		if(!mongoDataDTOs.isEmpty()) {
			LOG.debug("Data storing in mongo : {}", mongoDataDTOs);
			mongoRepositoryCustom.upsertAll(mongoDataDTOs);
		}
		if(!invalidDataDtos.isEmpty())
			LOG.error("Data with Invalid values : {}", invalidDataDtos);
	}

	protected boolean mergeDataDTO(MongoDataDTO mongoDataDTO, List<MongoDataDTO> mongoDataDTOs) {
		return false;
	}

	@SuppressWarnings("rawtypes")
	public abstract MongoRepository getMongoRepository() ;

	public abstract MongoDocument processRecord(T standardDTO);
	
	public abstract MongoDataDTO processStandardDTO(T standardDTO);
}
