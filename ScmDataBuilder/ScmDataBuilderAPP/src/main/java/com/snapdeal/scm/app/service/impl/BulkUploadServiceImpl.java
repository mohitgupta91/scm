package com.snapdeal.scm.app.service.impl;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.snapdeal.scm.app.service.IBulkUploadService;
import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.dto.ServiceResponse;
import com.snapdeal.scm.core.dto.impl.LaneGroupDTO;
import com.snapdeal.scm.core.dto.impl.MetroCityDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;
import com.snapdeal.scm.core.utils.CommonUtils;
import com.snapdeal.scm.core.utils.StringUtils;
import com.snapdeal.scm.core.utils.excel.ExcelParser;
import com.snapdeal.scm.core.utils.excel.ExcelParserResponse;
import com.snapdeal.scm.mongo.doc.*;
import com.snapdeal.scm.mongo.mao.repository.*;
import com.snapdeal.scm.processor.IDataProcessor;
import com.snapdeal.scm.processor.IElasticService;
import com.snapdeal.scm.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.jms.JMSException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author siddhant, prateek, chitransh
 */
@Service("bulkUploadServiceImpl")
@SuppressWarnings("rawtypes")
public class BulkUploadServiceImpl implements IBulkUploadService {

    private static final String MTO            = "mto";
    private static final String ROI            = "roi";
    private static final String Zone           = "zone";
    private static final String METRO          = "metro";
    private static final String NON_MTO        = "non-mto";
    private static final String METRO_CITY     = "metro";
    private static final String SAME_CITY      = "same-city";
    private static final String NON_METRO_CITY = "non-metro";

    private static final Logger     LOG      = LoggerFactory.getLogger(BulkUploadServiceImpl.class);
    private static final Logger     errorLOG = LoggerFactory.getLogger("DATAPROCESSOR-ERROR-LOGGER");
    private              DateFormat format   = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);

    @Autowired
    private IElasticService elasticServicce;

    @Autowired
    private IDataProcessor dataProcessorImpl;

    @Autowired
    private SupcDetailsRepository supcDetailsRepository;

    @Autowired
    private ShippedNotConMetricsRepository shippedNotConMetricsRepository;

    @Autowired
    private ConNotReachedMetricsRepository conNotReachedMetricsRepository;

    @Autowired
    private ReachedNotOFDMetricsRepository reachedNotOFDMetricsRepository;

    @Autowired
    private OfdNotAttemptMetricsRepository ofdNotAttemptMetricsRepository;

    @Autowired
    private AttemptNotDelMetricsRepository attemptNotDelMetricsRepository;

    @Autowired
    private AttemptNotDelNonIntgrMetricsRepository attemptNotDelNonIntgrMetricsRepository;

    @Autowired
    private ShipNotAttemptNonIntgrMetricsRepository shipNotAttemptNonIntgrMetricsRepository;

    @Autowired
    private UD1NotUD2MetricsRepository ud1NotUD2MetricsRepository;

    @Autowired
    private UD2NotUD3MetricsRepository ud2NotUD3MetricsRepository;

    @Autowired
    private UD3NotUD4MetricsRepository ud3NotUD4MetricsRepository;

    @Autowired
    private UD4NotDelMetricsRepository ud4NotDelMetricsRepository;

    public static void main(String[] args) {
        new BulkUploadServiceImpl().uploadSupcBulkUploader("/home/prateek/git/scm-control-tower/A.csv", "/home/prateek/git/scm-control-tower/B.csv", "/home/prateek/git/scm-control-tower/D.csv");
    }

    @Override
    public void laneGroupBulkUploader(String filePath) {
        List<IStandardDTO> laneGroupDTOList  = getLaneGroupDTOsFromFile(filePath);
        ProcessorQueueDto  processorQueueDto = new ProcessorQueueDto(QueryType.LANE_GROUP);
        processorQueueDto.setDataProcessorDtos(laneGroupDTOList);
        processorQueueDto.setFilePath(filePath);
        try {
            dataProcessorImpl.process(processorQueueDto);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private List<IStandardDTO> getLaneGroupDTOsFromFile(String filePath) {
        CSVReader          reader           = null;
        List<IStandardDTO> laneGroupDTOList = new ArrayList<>();
        try {
            //Get the CSVReader instance with specifying the delimiter to be used
            reader = new CSVReader(new FileReader(filePath), StringUtils.COMMA);
            String[] nextLine;
            //Read one line at a time
            Map<String, Set<String>> laneGroups = new HashMap<>();
            while ((nextLine = reader.readNext()) != null) {
                Set<String> lanes = laneGroups.get(nextLine[0]);
                if (lanes == null) {
                    lanes = new HashSet<String>();
                    laneGroups.put(nextLine[0], lanes);
                }
                lanes.add(CommonUtils.generateLane(nextLine[1], nextLine[2], nextLine[3], nextLine[4]));
            }
            for (Entry<String, Set<String>> laneGrp : laneGroups.entrySet()) {
                laneGroupDTOList.add(new LaneGroupDTO(laneGrp.getKey(), laneGrp.getValue()));
            }
        } catch (Exception e) {
            errorLOG.error("", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                errorLOG.error("", e);
            }
        }
        return laneGroupDTOList;
    }

    @Override
    public void metroCitiesBulkUploader(String filePath) {
        List<IStandardDTO> metroCityDTOList  = getMetroCityDTOsFromFile(filePath);
        ProcessorQueueDto  processorQueueDto = new ProcessorQueueDto(QueryType.METRO_CITY);
        processorQueueDto.setDataProcessorDtos(metroCityDTOList);
        processorQueueDto.setFilePath(filePath);
        try {
            dataProcessorImpl.process(processorQueueDto);
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public ServiceResponse shippedNotConnectedMetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to shipped but not connected metrics");
        return metricsUploader(file, ShippedNotConMetrics.class, shippedNotConMetricsRepository);
    }

    @Override
    public ServiceResponse connectedNotReachedMetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to connected but not reached metrics");
        return metricsUploader(file, ConNotReachedMetrics.class, conNotReachedMetricsRepository);
    }

    @Override
    public ServiceResponse reachedNotOFDMetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to reached but not out for delivery metrics");
        return metricsUploader(file, ReachedNotOFDMetrics.class, reachedNotOFDMetricsRepository);
    }

    @Override
    public ServiceResponse oFDNotAttemptedMetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to out for delivery but not attempted metrics");
        return metricsUploader(file, OfdNotAttemptMetrics.class, ofdNotAttemptMetricsRepository);
    }

    @Override
    public ServiceResponse attemptedNotDeliveredMetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to attempted but not delivered metrics");
        return metricsUploader(file, AttemptNotDelMetrics.class, attemptNotDelMetricsRepository);
    }

    @Override
    public ServiceResponse attemptedNotDelNonIntgrMetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to attempted but not delivered metrics for non-integrated couriers");
        return metricsUploader(file, AttemptNotDelNonIntgrMetrics.class, attemptNotDelNonIntgrMetricsRepository);
    }

    @Override
    public ServiceResponse shippedNotAttemptNonIntgrMetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to shipped but not attempted metrics for non-integrated couriers");
        return metricsUploader(file, ShipNotAttemptNonIntgrMetrics.class, shipNotAttemptNonIntgrMetricsRepository);
    }

    @Override
    public ServiceResponse uD1NotUD2MetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to undelivered 1 but not undelivered 2 metrics");
        return metricsUploader(file, UD1NotUD2Metrics.class, ud1NotUD2MetricsRepository);
    }

    @Override
    public ServiceResponse uD2NotUD3MetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to undelivered 2 but not undelivered 3 metrics");
        return metricsUploader(file, UD2NotUD3Metrics.class, ud2NotUD3MetricsRepository);
    }

    @Override
    public ServiceResponse uD3NotUD4MetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to undelivered 3 but not undelivered 4 metrics");
        return metricsUploader(file, UD3NotUD4Metrics.class, ud3NotUD4MetricsRepository);
    }

    @Override
    public ServiceResponse uD4NotDelMetricsUploader(MultipartFile file) {
        LOG.info("Attempting to read excel sheet corresponding to undelivered 4 but not delivered metrics");
        return metricsUploader(file, UD4NotDelMetrics.class, ud4NotDelMetricsRepository);
    }

    private <T, K extends Serializable> ServiceResponse<List<T>> metricsUploader(MultipartFile file, Class<T> document,
                                                                                 MongoRepository<T, K> repository) {

        ServiceResponse<List<T>> response = new ServiceResponse<>();
        try {
            ExcelParser            excelParser    = new ExcelParser(file.getInputStream());
            ExcelParserResponse<T> parserResponse = excelParser.parse(document);

            if (!parserResponse.isSuccessful()) {
                String error = parserResponse.getError();
                LOG.info("Unable to parse the input stream. Encountered error: {}", error);
                response.addError(error);
                return response;
            }

            List<T> records = parserResponse.getRecords();
            LOG.info("Read {} records from the excel stream. Now, attempting to persist in the mongo...", records.size());
            records = repository.insert(records);
            LOG.info("Persisted {} records successfully in mongo", records.size());

            response.setSuccessful(true);
            response.setData(records);

        } catch (IOException e) {
            response.addError("IO Exception while reading the input stream" + e.getMessage());
            LOG.info("IO Exception while reading the input stream: {}", e);
        } catch (Exception e) {
            response.addError("Encountered exception while processing the excel file: " + e.getMessage());
            LOG.info("Encountered exception while processing the excel file: {}", e);
        }
        return response;
    }

    private List<IStandardDTO> getMetroCityDTOsFromFile(String filePath) {
        CSVReader          reader           = null;
        List<IStandardDTO> metroCityDTOList = new ArrayList<>();
        try {
            //Get the CSVReader instance with specifying the delimiter to be used
            reader = new CSVReader(new FileReader(filePath), StringUtils.COMMA);
            String[] nextLine = null;
            //Read one line at a time
            while ((nextLine = reader.readNext()) != null) {
                MetroCityDTO metroCityDTO = new MetroCityDTO();
                List<String> tokens       = new ArrayList<>();
                Collections.addAll(tokens, nextLine);
                metroCityDTO.setCity(tokens.get(0));
                metroCityDTO.setState(tokens.get(1));
                metroCityDTOList.add(metroCityDTO);
            }
        } catch (Exception e) {
            errorLOG.error("", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                errorLOG.error("", e);
            }
        }
        return metroCityDTOList;
    }

    /**
     * For testing purpose only
     * To create sample data
     */

    @Override
    public void createDummyDataBulkUploader(String filePath) {
        CSVReader reader = null;
        //		List<IStandardDTO> metroCityDTOList = new ArrayList<>();
        //		String[] ALLOWED_DATE_FORMATS = new String[]{"dd/mm/yyyy hh:mm:ss"};
        List<SupcDetails> findAll     = supcDetailsRepository.findAll();
        Set<String>       categorySet = new HashSet<String>();
        Set<String>       mtoSet      = new HashSet<String>();
        LOG.error("Start Processing Supc.................");
        for (SupcDetails supcDetails : findAll) {
            if (supcDetails.getSuperCategory() != null) {
                categorySet.add(supcDetails.getSupc());
            }
            if (supcDetails.getMto() != null) {
                mtoSet.add(supcDetails.getSupc());
            }
        }
        try {
            //Get the CSVReader instance with specifying the delimiter to be used
            reader = new CSVReader(new FileReader(filePath), StringUtils.COMMA);
            String[] nextLine;
            //Read one line at a time
            List<String> shippingSoiSdDTOs         = new ArrayList<String>();
            List<String> shippingSoiSoidDTOs       = new ArrayList<String>();
            List<String> shippingSoiSpDTOs         = new ArrayList<String>();
            List<String> subOrderSpStatusCodesDTOs = new ArrayList<String>();
            List<String> subOrderTpStatusCodesDTOs = new ArrayList<String>();
            List<String> camsSupcSuperCategoryDTOs = new ArrayList<String>();
            List<String> filmsSupcMtoDTOs          = new ArrayList<String>();
            while ((nextLine = reader.readNext()) != null) {
                List<String> tokens = new ArrayList<>();
                Collections.addAll(tokens, nextLine);
                shippingSoiSdDTOs.add(org.apache.commons.lang.StringUtils.join(new String[]{tokens.get(0), tokens.get(1), tokens.get(2),
                                                                                            tokens.get(3), tokens.get(4), tokens.get(5), tokens.get(6).substring(0, tokens.get(6).lastIndexOf("-"))}, ","));
                shippingSoiSoidDTOs.add(org.apache.commons.lang.StringUtils.join(new String[]{tokens.get(0), "SHIPPING_MODE", tokens.get(7)}, ","));
                shippingSoiSoidDTOs.add(org.apache.commons.lang.StringUtils.join(new String[]{tokens.get(0), "DELIVERY_TYPE", tokens.get(8)}, ","));
                shippingSoiSpDTOs.add(org.apache.commons.lang.StringUtils.join(new String[]{tokens.get(0), tokens.get(9), tokens.get(10), tokens.get(11)}, ","));
                subOrderSpStatusCodesDTOs.add(org.apache.commons.lang.StringUtils.join(new String[]{tokens.get(0), "AWB_UPLOADED", tokens.get(12)}, ","));
                subOrderTpStatusCodesDTOs.add(org.apache.commons.lang.StringUtils.join(new String[]{tokens.get(0), "UDL", tokens.get(13)}, ","));
                subOrderTpStatusCodesDTOs.add(org.apache.commons.lang.StringUtils.join(new String[]{tokens.get(0), "DEL", tokens.get(14)}, ","));
                if (!categorySet.contains(tokens.get(1))) {
                    camsSupcSuperCategoryDTOs.add(org.apache.commons.lang.StringUtils.join(new String[]{tokens.get(1), tokens.get(15)}, ","));
                    categorySet.add(tokens.get(1));
                }
                if (!mtoSet.contains(tokens.get(1))) {
                    filmsSupcMtoDTOs.add(org.apache.commons.lang.StringUtils.join(new String[]{tokens.get(1), tokens.get(16)}, ","));
                    mtoSet.add(tokens.get(1));
                }
            }
            String directory = filePath.substring(0, filePath.lastIndexOf("/") + 1);
            Path   soiSd     = Paths.get(directory + "soiSd.csv");
            Files.write(soiSd, shippingSoiSdDTOs, Charset.defaultCharset());
            Path soiSoid = Paths.get(directory + "soiSoid.csv");
            Files.write(soiSoid, shippingSoiSoidDTOs, Charset.defaultCharset());
            Path soiSp = Paths.get(directory + "soiSp.csv");
            Files.write(soiSp, shippingSoiSpDTOs, Charset.defaultCharset());
            Path spStatus = Paths.get(directory + "spStatus.csv");
            Files.write(spStatus, subOrderSpStatusCodesDTOs, Charset.defaultCharset());
            Path tpStatus = Paths.get(directory + "tpStatus.csv");
            Files.write(tpStatus, subOrderTpStatusCodesDTOs, Charset.defaultCharset());
            Path superCategory = Paths.get(directory + "superCategory.csv");
            Files.write(superCategory, camsSupcSuperCategoryDTOs, Charset.defaultCharset());
            Path mto = Paths.get(directory + "mto.csv");
            Files.write(mto, filmsSupcMtoDTOs, Charset.defaultCharset());
        } catch (Exception e) {
            errorLOG.error("", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                errorLOG.error("", e);
            }
        }
    }

    @Override
    public void elasticDataBulkUploader(String filePath) {
        CSVReader            reader          = null;
        List<ElasticDataDTO> elasticDataDTOs = new ArrayList<>();
        try {
            //Get the CSVReader instance with specifying the delimiter to be used
            reader = new CSVReader(new FileReader(filePath), StringUtils.COMMA);
            String[] nextLine = null;
            //Read one line at a time
            while ((nextLine = reader.readNext()) != null) {
                List<String> tokens = new ArrayList<>();
                Collections.addAll(tokens, nextLine);
                ElasticDataDTO elasticDataDTO = new ElasticDataDTO(tokens.get(0));
                try {
                    Date orderCreatedDate = format.parse(tokens.get(1));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.ORDER_CREATED_DATE, orderCreatedDate);
                    try {
                        elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.EXPECTED_DELIVERY_DATE_RANGE_START, format.parse(tokens.get(2)));
                        elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.EXPECTED_DELIVERY_DATE, format.parse(tokens.get(3)));
                    } catch (Exception e) {
                        errorLOG.error("IGNORING EXPECTED DELIVERY DATE: " + tokens);
                    }
                    Date uploadedDate = format.parse(tokens.get(4));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.SP_AWB_UPLOADED_STATUS_DATE, uploadedDate);
                    Date udlDate = null;
                    try {
                        udlDate = format.parse(tokens.get(5));
                        elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE, udlDate);
                    } catch (Exception e) {
                        errorLOG.error("IGNORING UDL DATE: " + tokens);
                    }
                    try {
                        elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_RTI_STATUS_DATE, format.parse(tokens.get(6)));
                    } catch (Exception e) {
                        errorLOG.error("IGNORING RTI DATE: " + tokens);
                    }
                    Date deliveredDate = format.parse(tokens.get(7));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE, deliveredDate);
                    String metro = NON_METRO_CITY;
                    if (tokens.get(8).equalsIgnoreCase(METRO_CITY)) {
                        metro = METRO_CITY;
                    }
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.METRO, metro);
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.FULFILLMENT_MODEL, tokens.get(9));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.SHIPPING_MODE, tokens.get(10));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.DELIVERY_TYPE, tokens.get(11));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.COURIER_GROUP, tokens.get(12));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.COURIER_TYPE, tokens.get(13));
                    String laneType = ROI;
                    if (tokens.get(14).equalsIgnoreCase(Zone)) {
                        laneType = Zone;
                    } else if (tokens.get(14).equalsIgnoreCase("Same City")) {
                        laneType = SAME_CITY;
                    } else if (tokens.get(14).equalsIgnoreCase(METRO)) {
                        laneType = METRO;
                    }
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.LANE_TYPE, laneType);
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.SUPER_CATEGORY, tokens.get(15));
                    String mto = NON_MTO;
                    if (tokens.get(16).equalsIgnoreCase(MTO)) {
                        mto = MTO;
                    }
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.MTO, mto);
                    String sourceCity = CommonUtils.getCityStateMapping(tokens.get(17), tokens.get(18));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.SOURCE_CITY, sourceCity);
                    String sourceState = CommonUtils.getFormattedValue(tokens.get(18));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.SOURCE_STATE, sourceState);
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.SOURCE_ZONE, tokens.get(19));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.SOURCE_TIER, tokens.get(20));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.SOURCE_REGION, tokens.get(21));
                    String destinationCity = CommonUtils.getCityStateMapping(tokens.get(22), tokens.get(23));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.DESTINATION_CITY, destinationCity);
                    String destinationState = CommonUtils.getFormattedValue(tokens.get(23));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.DESTINATION_STATE, destinationState);
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.DESTINATION_ZONE, tokens.get(24));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.DESTINATION_TIER, tokens.get(25));
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.DESTINATION_REGION, tokens.get(26));

                    String lane = CommonUtils.generateLane(sourceCity, destinationCity);
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.LANE, lane);

                    Date attempted = udlDate;
                    if (udlDate == null) {
                        attempted = deliveredDate;
                    }
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE, attempted);

                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.ORDER_TO_DELIVERED, DateUtils.getDifferenceInMinutes(deliveredDate, orderCreatedDate) / 60);
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.ORDER_TO_SHIPPED, DateUtils.getDifferenceInMinutes(uploadedDate, orderCreatedDate) / 60);
                    elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.SHIPPED_TO_DELIVERED, DateUtils.getDifferenceInMinutes(deliveredDate, uploadedDate) / 60);
                } catch (Exception e) {
                    errorLOG.error("IGNORING COMPLETE: " + tokens);
                }
                elasticDataDTOs.add(elasticDataDTO);
            }
            LOG.info("Total Size: " + elasticDataDTOs.size());
        } catch (Exception e) {
            errorLOG.error("", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                errorLOG.error("", e);
            }
        }
        elasticServicce.indexDocuments(elasticDataDTOs);
    }

    public void uploadSupcBulkUploader(String mtoFilePath, String categoryFilePath, String outputFilePath) {
        CSVReader                mtoReader      = null;
        CSVReader                categoryReader = null;
        CSVWriter                csvOutput      = null;
        Map<String, SupcDetails> supc           = new HashMap<String, SupcDetails>();
        try {
            //Get the CSVReader instance with specifying the delimiter to be used
            mtoReader = new CSVReader(new FileReader(mtoFilePath), StringUtils.COMMA);
            categoryReader = new CSVReader(new FileReader(categoryFilePath), StringUtils.COMMA);
            csvOutput = new CSVWriter(new FileWriter(outputFilePath, true));
            String[] nextLine = null;
            //Read one line at a time
            List<String> tokens = new ArrayList<>();
            while ((nextLine = mtoReader.readNext()) != null) {
                tokens.clear();
                Collections.addAll(tokens, nextLine);
                SupcDetails supcdetails = new SupcDetails(tokens.get(0));
                if (supc.get(tokens.get(0)) != null) {
                    errorLOG.error("DUPLICATE RECORD : supc = " + tokens.get(0) + ", mto = " + tokens.get(1));
                    continue;
                }
                String mto = MTO;
                if (!tokens.get(1).equalsIgnoreCase("MTO")) {
                    mto = NON_MTO;
                }
                supcdetails.setMto(mto);
                supc.put(tokens.get(0), supcdetails);
            }
            errorLOG.error("MTO DONE");
            int      i   = 0;
            int      j   = 0;
            String[] str = new String[3];
            str[0] = "supc";
            str[1] = "mto";
            str[2] = "superCategory";
            csvOutput.writeNext(str);
            while ((nextLine = categoryReader.readNext()) != null) {
                tokens.clear();
                Collections.addAll(tokens, nextLine);
                SupcDetails supcdetails = supc.remove(tokens.get(0));
                if (supcdetails == null) {
                    supcdetails = new SupcDetails(tokens.get(0));
                    supcdetails.setMto(NON_MTO);
                }
                supcdetails.setSuperCategory(tokens.get(1));
                str[0] = supcdetails.getSupc();
                str[1] = supcdetails.getMto();
                str[2] = supcdetails.getSuperCategory();
                csvOutput.writeNext(str);
                if (i++ == 100000) {
                    csvOutput.flush();
                    errorLOG.error("CATEGORY DONE : " + j++);
                    i = 0;
                }
            }
            errorLOG.error("CATEGORY DONE : " + j++);
            if (supc != null && supc.values() != null) {
                for (SupcDetails sup : supc.values()) {
                    str[0] = sup.getSupc();
                    str[1] = sup.getMto();
                    str[2] = "";
                    csvOutput.writeNext(str);
                }
            }
            csvOutput.flush();
            errorLOG.error("FLUSH DONE : " + j++);

        } catch (Exception e) {
            errorLOG.error("", e);
        } finally {
            try {
                mtoReader.close();
                categoryReader.close();
                csvOutput.close();
            } catch (IOException e) {
                errorLOG.error("", e);
            }
        }
    }
}