package com.snapdeal.scm.processor.scheduler.impl;

import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.common.domain.mongo.RuntimeStatusKey;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.core.mongo.document.ConnectedRadStatus;
import com.snapdeal.scm.core.mongo.document.ConnectedRadStatus.ConnectedStatusDetails;
import com.snapdeal.scm.core.mongo.document.OriginCityExitMapping;
import com.snapdeal.scm.core.utils.StringUtils;
import com.snapdeal.scm.das.mongo.dao.ConnectedRadStatusRepository;
import com.snapdeal.scm.mongo.doc.RuntimeStatus;
import com.snapdeal.scm.mongo.mao.repository.RuntimeStatusRepository;
import com.snapdeal.scm.processor.IElasticService;
import com.snapdeal.scm.processor.cache.impl.CourierLocationSnapdealLocationMappingCache;
import com.snapdeal.scm.processor.cache.impl.CourierLocationSnapdealLocationMappingCache.SnapdealLocation;
import com.snapdeal.scm.processor.cache.impl.OriginCityExitMappingCache;
import com.snapdeal.scm.processor.cache.impl.PincodeDCMappingCache;
import com.snapdeal.scm.processor.cache.impl.PincodeDCMappingCache.DCDetails;
import com.snapdeal.scm.processor.scheduler.ISubOrderTpProcessorScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author prateek
 */
public class SubOrderTpProcessorScheduler implements ISubOrderTpProcessorScheduler {

    private static final Logger errorLOG = LoggerFactory.getLogger("DATAPROCESSOR-ERROR-LOGGER");
    private static final Logger LOG      = LoggerFactory.getLogger(SubOrderTpProcessorScheduler.class);

    @Autowired
    private ConnectedRadStatusRepository connectedRadStatusRepository;

    @Autowired
    private IElasticService elasticServicce;

    @Value("${tp.processor.scheduler.fixed.delay}")
    private Long delay;

    @Autowired
    private RuntimeStatusRepository runtimeStatusRepository;

    @Override
    @Scheduled(fixedDelayString = "${tp.processor.scheduler.fixed.delay}")
    public void scheduleSubOrderTpProcessor() {
        RuntimeStatus            findByRuntimeStatusKey = runtimeStatusRepository.findByRuntimeStatusKey(RuntimeStatusKey.MONGO_SCHEDULER_LAST_EXECUTION_TIME);
        Date                     lastExecutionTime      = getLastExecutionTime(findByRuntimeStatusKey);
        Date                     currentDate            = Calendar.getInstance().getTime();
        List<ConnectedRadStatus> connectedRadStatuss    = connectedRadStatusRepository.findAllToProcess(lastExecutionTime);
        LOG.info("SubOrderTp Mongo Scheduler... processing : {} records", connectedRadStatuss.size());
        if (CollectionUtils.isEmpty(connectedRadStatuss)) {
            findByRuntimeStatusKey.setValue(currentDate);
            runtimeStatusRepository.save(findByRuntimeStatusKey);
            return;
        }
        List<ElasticDataDTO>      elasticDataDTOs         = new ArrayList<>();
        Collection<MongoDocument> storeConnectedRadStatus = new ArrayList<>();
        for (ConnectedRadStatus connectedRadStatus : connectedRadStatuss) {
            ElasticDataDTO     elasticDataDTO          = new ElasticDataDTO(connectedRadStatus.getSubOrderCode());
            ConnectedRadStatus connectedRad            = new ConnectedRadStatus(connectedRadStatus.getSubOrderCode());
            boolean            setOriginCityState      = setOriginCityState(connectedRadStatus, elasticDataDTO, connectedRad);
            boolean            setDestinationCityState = setDestinationCityState(connectedRadStatus, elasticDataDTO, connectedRad);
            String             destinationCity         = Optional.ofNullable(connectedRadStatus.getCourierDestinationCity()).orElse(connectedRad.getCourierDestinationCity());
            boolean            setRadDate              = setRadDate(connectedRadStatus, elasticDataDTO, connectedRad, destinationCity);
            boolean            setConnectedDate        = setConnectedDate(connectedRadStatus, elasticDataDTO, connectedRad);
            if (setConnectedDate && setDestinationCityState && setOriginCityState && setRadDate) {
                connectedRad.setComplete(true);
            }
            if (elasticDataDTO.getDataValueMap().size() > 0) {
                elasticDataDTOs.add(elasticDataDTO);
                storeConnectedRadStatus.add(connectedRad);
            }
        }
        LOG.info("SubOrderTp Mongo Scheduler storing : {} records in elastic", elasticDataDTOs.size());
        elasticServicce.indexDocuments(elasticDataDTOs);
        connectedRadStatusRepository.upsertAll(storeConnectedRadStatus);
        findByRuntimeStatusKey.setValue(currentDate);
        runtimeStatusRepository.save(findByRuntimeStatusKey);
    }

    private Date getLastExecutionTime(RuntimeStatus findByRuntimeStatusKey) {
        Object lastExecutionTimeObject = findByRuntimeStatusKey.getValue();
        Date   lastExecutionTime       = null;
        if (Objects.isNull(lastExecutionTimeObject)) {
            lastExecutionTime = new Date();
        } else {
            lastExecutionTime = (Date) lastExecutionTimeObject;
        }
        return lastExecutionTime;
    }

    private boolean setConnectedDate(ConnectedRadStatus connectedRadStatus, ElasticDataDTO elasticDataDTO, ConnectedRadStatus connectedRad) {
        boolean isConnectedDateSet = false;
        if (!Objects.isNull(connectedRadStatus.getConnectedDate())) {
            isConnectedDateSet = true;
        } else if (StringUtils.isEmpty(connectedRadStatus.getFirstCurrentLocationHub()) || StringUtils.isEmpty(connectedRadStatus.getCourierGroup())
                   || StringUtils.isEmpty(connectedRadStatus.getShippingMode()) || StringUtils.isEmpty(connectedRadStatus.getCourierOriginCity())) {
            return isConnectedDateSet;
        }
        OriginCityExitMapping originCityExitMapping = CacheManager.getInstance().getCache(OriginCityExitMappingCache.class)
                .getStatusDetails(connectedRadStatus.getCourierGroup(), connectedRadStatus.getShippingMode(),
                                  connectedRadStatus.getCourierOriginCity(), connectedRadStatus.getFirstCurrentLocationHub(),
                                  connectedRadStatus.getCourierDestinationCity(), connectedRadStatus.getCourierDestinationState());
        if (!Objects.isNull(originCityExitMapping)) {
            Long connectedDate = getCurrentConnectedDate(connectedRadStatus, originCityExitMapping);
            if (!Objects.isNull(connectedDate) && (Objects.isNull(connectedRadStatus.getConnectedDate()) || connectedDate.compareTo(connectedRadStatus.getConnectedDate().getTime()) == -1)) {
                elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_CONNECTED_STATUS_DATE, connectedDate);
                connectedRad.setConnectedDate(new Date(connectedDate));
                isConnectedDateSet = true;
            }
        } else {
            errorLOG.error("OriginCityExitMapping found for courierGroup  : {}, shipping Mode : {}, courier Origin City : {}, "
                           + "first locationHub : {}, courier Destination city : {} and courier Destination state : {} is : {}",
                           connectedRadStatus.getCourierGroup(), connectedRadStatus.getShippingMode(),
                           connectedRadStatus.getCourierOriginCity(), connectedRadStatus.getFirstCurrentLocationHub(),
                           connectedRadStatus.getCourierDestinationCity(), connectedRadStatus.getCourierDestinationState(), originCityExitMapping);
        }
        return isConnectedDateSet;
    }

    private Long getCurrentConnectedDate(ConnectedRadStatus connectedRadStatus, OriginCityExitMapping originCityExitMapping) {
        String                      courierRemarks         = originCityExitMapping.getCourierRemarks();
        String                      courierStatus          = originCityExitMapping.getCourierStatus();
        String                      snapdealStatus         = originCityExitMapping.getSnapdealStatus();
        String                      locationHub            = originCityExitMapping.getOriginCityExit();
        Set<ConnectedStatusDetails> connectedStatusDetails = connectedRadStatus.getConnectedStatusDetails();
        Long                        date                   = null;
        for (ConnectedStatusDetails connectedStatusDetail : connectedStatusDetails) {
            if (StringUtils.equalIgnorecase(connectedStatusDetail.getCourierRemarks(), courierRemarks) ||
                StringUtils.equalIgnorecase(connectedStatusDetail.getCourierStatus(), courierStatus) ||
                StringUtils.equalIgnorecase(connectedStatusDetail.getSnapdealStatus(), snapdealStatus)) {
                if (StringUtils.equalIgnorecase(connectedStatusDetail.getLocationHub(), locationHub)) {
                    if (Objects.isNull(date) || connectedStatusDetail.getStatusdate().compareTo(date) == -1) {
                        date = connectedStatusDetail.getStatusdate();
                    }
                }
            }
        }
        ;
        return date;
    }

    private boolean setRadDate(ConnectedRadStatus connectedRadStatus, ElasticDataDTO elasticDataDTO, ConnectedRadStatus connectedRad, String destinationCity) {
        boolean isRadDateSet = false;
        if (!Objects.isNull(connectedRadStatus.getRadDate())) {
            isRadDateSet = true;
        } else if (StringUtils.isEmpty(destinationCity)) {
            return isRadDateSet;
        }
        Date radDate = connectedRadStatus.getCurrentRADDate(destinationCity);
        if (!Objects.isNull(radDate) && (Objects.isNull(connectedRadStatus.getRadDate()) || radDate.before(connectedRadStatus.getRadDate()))) {
            elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_RAD_STATUS_DATE, radDate);
            connectedRad.setRadDate(radDate);
            isRadDateSet = true;
        }
        return isRadDateSet;
    }

    private boolean setDestinationCityState(ConnectedRadStatus connectedRadStatus, ElasticDataDTO elasticDataDTO, ConnectedRadStatus connectedRad) {
        boolean isDestinationCityStateSet = false;
        if (StringUtils.isNotEmpty(connectedRadStatus.getCourierDestinationCity())) {
            isDestinationCityStateSet = true;
        } else if (StringUtils.isNotEmpty(connectedRadStatus.getCourierGroup()) && StringUtils.isNotEmpty(connectedRadStatus.getShippingMode())
                   && StringUtils.isNotEmpty(connectedRadStatus.getCustomerDestinationPincode())) {
            DCDetails mappedDC = CacheManager.getInstance().getCache(PincodeDCMappingCache.class)
                    .getMappedDC(connectedRadStatus.getCustomerDestinationPincode(), connectedRadStatus.getCourierGroup(),
                                 connectedRadStatus.getShippingMode());
            if (!Objects.isNull(mappedDC) && StringUtils.isNotEmpty(mappedDC.getDcCity()) && StringUtils.isNotEmpty(mappedDC.getDcState())) {
                elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.COURIER_DESTINATION_CITY, mappedDC.getDcCity());
                elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.COURIER_DESTINATION_STATE, mappedDC.getDcState());
                connectedRad.setCourierDestinationCity(mappedDC.getDcCity());
                connectedRad.setCourierDestinationState(mappedDC.getDcState());
                isDestinationCityStateSet = true;
            } else {
                errorLOG.error("PincodeDCDetails found for courierGroup  : {}, shipping Mode : {} and customer pincode : {} is : {}",
                               connectedRadStatus.getCourierGroup(), connectedRadStatus.getShippingMode(),
                               connectedRadStatus.getCustomerDestinationPincode(), mappedDC);
            }
        }
        return isDestinationCityStateSet;
    }

    private boolean setOriginCityState(ConnectedRadStatus connectedRadStatus, ElasticDataDTO elasticDataDTO, ConnectedRadStatus connectedRad) {
        boolean isOriginCityStateSet = false;
        if (StringUtils.isNotEmpty(connectedRadStatus.getCourierOriginCity())) {
            isOriginCityStateSet = true;
        } else if (StringUtils.isNotEmpty(connectedRadStatus.getCourierCode()) && StringUtils.isNotEmpty(connectedRadStatus.getFirstCurrentLocationHub())) {
            SnapdealLocation snapdealLocation = CacheManager.getInstance().getCache(CourierLocationSnapdealLocationMappingCache.class)
                    .getSnapdealLocation(connectedRadStatus.getCourierCode(), connectedRadStatus.getFirstCurrentLocationHub());
            if (!Objects.isNull(snapdealLocation) && StringUtils.isNotEmpty(snapdealLocation.getSnapdealLocationCity())
                && StringUtils.isNotEmpty(snapdealLocation.getSnapdealLocationState())) {
                elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.COURIER_ORIGIN_CITY, snapdealLocation.getSnapdealLocationCity());
                elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.COURIER_ORIGIN_STATE, snapdealLocation.getSnapdealLocationState());
                connectedRad.setCourierOriginCity(snapdealLocation.getSnapdealLocationCity());
                connectedRad.setCourierOriginState(snapdealLocation.getSnapdealLocationState());
                isOriginCityStateSet = true;
            } else {
                errorLOG.error("SnapdealLocation found for courierCode  : {} and first locationCode : {} is : {}",
                               connectedRadStatus.getCourierCode(), connectedRadStatus.getFirstCurrentLocationHub(), snapdealLocation);
            }
        }
        return isOriginCityStateSet;
    }
}