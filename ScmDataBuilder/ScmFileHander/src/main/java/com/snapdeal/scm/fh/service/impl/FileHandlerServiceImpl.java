package com.snapdeal.scm.fh.service.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.core.configuration.ConfigUtils;
import com.snapdeal.scm.core.configuration.Property;
import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.jms.impl.DataProcessorProducer;
import com.snapdeal.scm.core.mongo.document.DtoQueryFieldMap;
import com.snapdeal.scm.core.poller.dto.impl.PollerQueueDto;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;
import com.snapdeal.scm.core.service.IS3StorageService;
import com.snapdeal.scm.fh.cache.impl.QueryDTOFieldMappingCache;
import com.snapdeal.scm.fh.service.IConvertorService;
import com.snapdeal.scm.fh.service.IFileHandlerService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileHandlerServiceImpl: Filer Handler service impl
 * 
 * @author pranav, Ashwini
 */
@Component
public class FileHandlerServiceImpl implements IFileHandlerService {

    private static final Logger   logger   = LoggerFactory.getLogger(FileHandlerServiceImpl.class);
    private static final Logger   errorLOG = LoggerFactory.getLogger("FILEHANDLER-ERROR-LOGGER");

    @Autowired
    private DataProcessorProducer jmsProducer;

    @Autowired
    private IS3StorageService     s3Service;

    @Autowired
    private IConvertorService     convertorService;

    @Override
    public void processFile(PollerQueueDto dto) throws Exception {
        logger.info("<--------------processing file started-------------------> {}", dto.getFilePath());
        QueryType jobName = dto.getQueryType();
        String filePath = dto.getFilePath();
        boolean errorFound = false;
        QueryDTOFieldMappingCache cache = CacheManager.getInstance().getCache(QueryDTOFieldMappingCache.class);
        if (null != cache && cache.mappingExist(jobName)) {
            String dtoClass = cache.getjobMappedDtoClass(jobName);
            List<DtoQueryFieldMap> fieldMapping = cache.getFieldMappingByJobName(jobName);
            S3Object s3Object = s3Service.getObjectFromS3(filePath);
            if (null != s3Object) {
                try {
                    InputStreamReader reader = new InputStreamReader(s3Object.getObjectContent());
                    CSVParser csvParser = new CSVParser(reader, CSVFormat.newFormat(','));
                    List<CSVRecord> csvRecords = csvParser.getRecords();
                    csvParser.close();
                    reader.close();
                    logger.info("Fetched Records from File {} : [{}]", dto.getFilePath(), csvRecords.size());
                    Integer count = 1;
                    Integer recordNumber = 1;

                    List<List<IStandardDTO>> listDTO = new ArrayList<List<IStandardDTO>>();
                    List<IStandardDTO> dataProcessorDtos = new ArrayList<IStandardDTO>();
                    Integer batchSize = ConfigUtils.getIntegerScalar(Property.FILEHANDLER_DTO_BATCHSIZE);
                    logger.info("Start Processdfd");
                    for (CSVRecord csvRecord : csvRecords) {
                        try {
                            IStandardDTO standardDto = processCSVRecord(csvRecord, dtoClass, fieldMapping);
                            dataProcessorDtos.add(standardDto);
                            if (count.equals(batchSize)) {
                            	logger.info("batch...");
                                listDTO.add(dataProcessorDtos);
                                dataProcessorDtos = new ArrayList<IStandardDTO>();
                                count = 0;
                            }
                            count++;
                            recordNumber++;
                        } catch (Exception e) {
                            errorFound = true;
                            logger.error("unable to parse data. data pushed to file-handler-error-log file exception", e);
                            errorLOG.error("{}, {}, {}, {}", dtoClass, recordNumber, csvRecord, e.getMessage());
                        }
                    }
                    logger.info("End Processdfd");
                    if (dataProcessorDtos.size() > 0)
                        listDTO.add(dataProcessorDtos);
                    dataProcessorDtos = null;
                    logger.info("Start Sending");
                    for(List<IStandardDTO> dataProcessorDto : listDTO){
                    	jmsProducer.sendInSync(new ProcessorQueueDto<IStandardDTO>(dataProcessorDto, jobName, filePath));
                    }
                    logger.info("End Sending");
                } catch (IOException e) {
                    logger.error("not able to parse file.", e);
                    errorLOG.error("{}, {} ,{} ", dtoClass, filePath, e.getMessage());
                    throw new RuntimeException("Not able to parse file");
                }
            } else {
                logger.info("unable to read s3Object");
                throw new RuntimeException("unable to get object from S3 Client");
            }
        } else {
            throw new RuntimeException("cache is null or not able to get mapping for jobName" + jobName);
        }
        if (errorFound) {
            errorLOG.error("errorFoundFlag is true for file [{}]", filePath);
        }
        logger.info("<--------------processing file ends---------------> {} ", dto.getFilePath());

    }

    private IStandardDTO processCSVRecord(CSVRecord record, String dtoClass, List<DtoQueryFieldMap> fieldMapping) throws Exception {
        if (record.size() != fieldMapping.size()) {
            throw new Exception("Number of column found in record = " + record.size() + " but expected = " + fieldMapping.size());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (DtoQueryFieldMap fieldDTO : fieldMapping) {
            Object value = convertorService.convertToObject(Class.forName(fieldDTO.getFieldType()), record.get(fieldDTO.getQueryFieldIndex()).trim());
            map.put(fieldDTO.getField(), value);
        }

        if (MapUtils.isNotEmpty(map) && map.size() == fieldMapping.size()) {
            BeanWrapper wrapper = new BeanWrapperImpl(Class.forName(dtoClass));
            wrapper.setPropertyValues(map);
            return (IStandardDTO) wrapper.getWrappedInstance();
        }
        throw new Exception("Unable to convert all data");
    }
}
