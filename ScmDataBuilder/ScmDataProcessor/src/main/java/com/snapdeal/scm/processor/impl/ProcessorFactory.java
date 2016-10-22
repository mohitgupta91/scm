package com.snapdeal.scm.processor.impl;

import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.processor.IElasticProcessor;
import com.snapdeal.scm.processor.IMongoProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author prateek
 */
@Service("ProcessorFactory")
public class ProcessorFactory {

    @Autowired
    List<IElasticProcessor> elasticProcessors;
    @Autowired
    List<IMongoProcessor> mongoProcessors;

    Map<QueryType, IElasticProcessor<IStandardDTO>> elasticProcessorsMap = new EnumMap<>(QueryType.class);
    Map<QueryType, IMongoProcessor<IStandardDTO>> mongoProcessorsMap = new EnumMap<>(QueryType.class);

    @PostConstruct
    public void createProcessorMap() {
        elasticProcessors.forEach(elasticProcessor -> elasticProcessorsMap.put(elasticProcessor.getQueryType(), elasticProcessor));
        mongoProcessors.forEach(mongoProcessor -> mongoProcessorsMap.put(mongoProcessor.getQueryType(), mongoProcessor));
    }

    public void addElasticProcessor(QueryType jobName, IElasticProcessor<IStandardDTO> elasticProcessor) {
        elasticProcessorsMap.put(jobName, elasticProcessor);
    }

    public IElasticProcessor<IStandardDTO> getElasticProcessor(QueryType jobName) {
        return elasticProcessorsMap.get(jobName);
    }
    
    public void addMongoProcessor(QueryType jobName, IMongoProcessor<IStandardDTO> mongoProcessor) {
    	mongoProcessorsMap.put(jobName, mongoProcessor);
    }

    public IMongoProcessor<IStandardDTO> getMongoProcessor(QueryType jobName) {
        return mongoProcessorsMap.get(jobName);
    }
}
