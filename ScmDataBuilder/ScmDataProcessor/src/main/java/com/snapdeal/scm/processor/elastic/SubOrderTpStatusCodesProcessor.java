package com.snapdeal.scm.processor.elastic;

import com.google.common.collect.Lists;
import com.snapdeal.base.utils.CollectionUtils;
import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.cache.impl.CourierGroupCache;
import com.snapdeal.scm.cache.impl.CourierGroupCache.CourierDetail;
import com.snapdeal.scm.cache.impl.PincodeMasterCache;
import com.snapdeal.scm.cache.impl.PincodeMasterCache.PincodeDetail;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.SubOrderTpStatusCodesDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticScriptDataDTO;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.core.enums.ElasticScriptKey;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.mongo.document.ConnectedRadStatus;
import com.snapdeal.scm.core.mongo.document.ConnectedRadStatus.ConnectedStatusDetails;
import com.snapdeal.scm.core.utils.StringUtils;
import com.snapdeal.scm.das.mongo.dao.ConnectedRadStatusRepository;
import com.snapdeal.scm.processor.IElasticService;
import com.snapdeal.scm.processor.cache.impl.CourierLocationSnapdealLocationMappingCache;
import com.snapdeal.scm.processor.cache.impl.CourierLocationSnapdealLocationMappingCache.SnapdealLocation;
import com.snapdeal.scm.processor.exception.InsufficientDataException;
import com.snapdeal.scm.processor.exception.InvalidDataException;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author prateek
 */
@Service("SubOrderTpStatusCodesProcessor")
public class SubOrderTpStatusCodesProcessor extends AbstractElasticProcessor<SubOrderTpStatusCodesDTO> {

    @Autowired
    private IElasticService elasticService;

    @Autowired
    private ConnectedRadStatusRepository connectedRadStatusRepository;

    private static final String CTD  = "CTD";
    private static final String MISC = "MISC";

    // TODO move this to some other place
    private static Map<String, SubOrderDetailElasticColumn> codeToStatusDateColumn                = new HashMap<>();
    private static Set<String>                              integratedCourierWithDirectStatus     = new HashSet<>();
    private static Set<String>                              integratedCourierWithCalculatedStatus = new HashSet<>();
    private static Set<String>                              otherThanCONNECTEDAndRADStatuss       = new HashSet<>();

    static {
        codeToStatusDateColumn.put("OFD", SubOrderDetailElasticColumn.TP_OFD_STATUS_DATE);
        codeToStatusDateColumn.put("UDL", SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE);
        codeToStatusDateColumn.put("DEL", SubOrderDetailElasticColumn.TP_DEL_STATUS_DATE);
        codeToStatusDateColumn.put("STKU", SubOrderDetailElasticColumn.TP_RAD_STATUS_DATE);
        codeToStatusDateColumn.put("GTPS", SubOrderDetailElasticColumn.TP_CONNECTED_STATUS_DATE);
        integratedCourierWithDirectStatus.add("GO_JAVAS");
        integratedCourierWithDirectStatus.add("XPRESSBEES");
        integratedCourierWithDirectStatus.add("JV_EXPRESS");
        integratedCourierWithCalculatedStatus.add("BLUEDART");
        integratedCourierWithCalculatedStatus.add("ECOMM_EXP");
        integratedCourierWithCalculatedStatus.add("VULCAN");
        integratedCourierWithCalculatedStatus.add("VELEX");
        otherThanCONNECTEDAndRADStatuss.add("OFD");
        otherThanCONNECTEDAndRADStatuss.add("UDL");
        otherThanCONNECTEDAndRADStatuss.add("DEL");
        otherThanCONNECTEDAndRADStatuss.add("STKU");
        otherThanCONNECTEDAndRADStatuss.add("GTPS");
    }

    @Override
    public ElasticDataDTO processRecord(SubOrderTpStatusCodesDTO subOrderTpStatusCodesDTO)
            throws InvalidDataException, InsufficientDataException {
        String                      statuscode       = subOrderTpStatusCodesDTO.getNewStatusCode();
        SubOrderDetailElasticColumn statusColumnName = codeToStatusDateColumn.get(statuscode);
        ElasticDataDTO              elasticDataDTO   = new ElasticDataDTO(subOrderTpStatusCodesDTO.getSubOrderCode());
        Date                        statusTime       = subOrderTpStatusCodesDTO.getNewStatusDate();
        if (Objects.nonNull(statusColumnName)) {
            addStatusCodeTime(statusColumnName, subOrderTpStatusCodesDTO, elasticDataDTO, statusTime);
        }
        elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_CURRENT_STATUS, subOrderTpStatusCodesDTO.getCurrentStatus());
        elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_CURRENT_STATUS_DATE, subOrderTpStatusCodesDTO.getCurrentStatusDate());
        elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.RTO_REASON, StringUtils.isEmpty(subOrderTpStatusCodesDTO.getRejectCode()) ? MISC : subOrderTpStatusCodesDTO.getRejectCode());//TODO: verify the default value for Reject code; currently set to MISC
        if (isIntegratedCourierWIthCalculatedStatus(subOrderTpStatusCodesDTO.getCourierCode()) && !otherThanCONNECTEDAndRADStatuss.contains(statuscode)) {
            saveInMongo(subOrderTpStatusCodesDTO, elasticDataDTO);
        }
        return elasticDataDTO;
    }

    private void addStatusCodeTime(SubOrderDetailElasticColumn statusColumnName, SubOrderTpStatusCodesDTO subOrderTpStatusCodesDTO,
                                   ElasticDataDTO elasticDataDTO, Date statusTime) throws InsufficientDataException {
        switch (statusColumnName) {
            case TP_FIRST_UDL_STATUS_DATE:
                addUDLScript(subOrderTpStatusCodesDTO, elasticDataDTO);
                break;
            case TP_DEL_STATUS_DATE:
                addDELAndClosedScript(subOrderTpStatusCodesDTO, elasticDataDTO);
                elasticDataDTO.addDataValue(statusColumnName, statusTime);
                break;
            case TP_RAD_STATUS_DATE:
                if (isIntegratedCourierWithDirectStatus(subOrderTpStatusCodesDTO.getCourierCode())) {
                    addRADScript(subOrderTpStatusCodesDTO, elasticDataDTO);
                }
                break;
            case TP_CONNECTED_STATUS_DATE:
                if (isIntegratedCourierWithDirectStatus(subOrderTpStatusCodesDTO.getCourierCode())) {
                    addConnectedScript(subOrderTpStatusCodesDTO, elasticDataDTO);
                }
                break;
            case TP_OFD_STATUS_DATE:
                addOFDScript(subOrderTpStatusCodesDTO, elasticDataDTO);
                break;
            default:
        }
    }

    private boolean isIntegratedCourierWIthCalculatedStatus(String courierCode) throws InsufficientDataException {
        CourierDetail courierCodeToDetail = CacheManager.getInstance().getCache(CourierGroupCache.class).getCourierCodeToDetail(courierCode);
        if (Objects.isNull(courierCodeToDetail) || StringUtils.isEmpty(courierCodeToDetail.getCourierGroup())) {
            throw new InsufficientDataException("CourierDetail is not found for courierCode =" + courierCode);
        }
        if (integratedCourierWithCalculatedStatus.stream().anyMatch((courierGrp) ->
                                                                            courierGrp.equalsIgnoreCase(courierCodeToDetail.getCourierGroup()))) {
            return true;
        }
        return false;
    }

    private boolean isIntegratedCourierWithDirectStatus(String courierCode) throws InsufficientDataException {
        CourierDetail courierCodeToDetail = CacheManager.getInstance().getCache(CourierGroupCache.class).getCourierCodeToDetail(courierCode);
        if (Objects.isNull(courierCodeToDetail) || StringUtils.isEmpty(courierCodeToDetail.getCourierGroup())) {
            throw new InsufficientDataException("CourierDetail is not found for courierCode =" + courierCode);
        }
        if (integratedCourierWithDirectStatus.stream().anyMatch((courierGrp) ->
                                                                        courierGrp.equalsIgnoreCase(courierCodeToDetail.getCourierGroup()))) {
            return true;
        }
        return false;
    }

    private void saveInMongo(SubOrderTpStatusCodesDTO subOrderTpStatusCodesDTO, ElasticDataDTO elasticDataDTO) throws InsufficientDataException, InvalidDataException {
        String oldStatus           = subOrderTpStatusCodesDTO.getOldStatusCode();
        String currentLocationCode = subOrderTpStatusCodesDTO.getCurrentLocationCode();
        String courierCode         = subOrderTpStatusCodesDTO.getCourierCode();

        ConnectedRadStatus connectedRadStatus = new ConnectedRadStatus(subOrderTpStatusCodesDTO.getSubOrderCode());
        setCourierOriginAnadDestinationHub(elasticDataDTO, subOrderTpStatusCodesDTO.getOriginHubLocationPincode(), subOrderTpStatusCodesDTO.getDestinationHubLocationPincode(), connectedRadStatus);
        // TODO check with Gurwant
        setFirstCourierLocationHub(oldStatus, currentLocationCode, connectedRadStatus);
        setCourierCodeAndGroup(courierCode, connectedRadStatus);
        setCurrentRADLocationCityAndDate(subOrderTpStatusCodesDTO.getCurrentLocationPincode(), courierCode, currentLocationCode, subOrderTpStatusCodesDTO.getNewStatusDate(), connectedRadStatus);
        setCurentConnectedDetailAndDate(subOrderTpStatusCodesDTO, connectedRadStatus);
        connectedRadStatusRepository.upsert(connectedRadStatus);
    }

    private void setFirstCourierLocationHub(String oldStatus, String currentLocationCode, ConnectedRadStatus connectedRadStatus) {
        if (StringUtils.isNotEmpty(oldStatus) && oldStatus.equalsIgnoreCase(CTD) && StringUtils.isNotEmpty(currentLocationCode)) {
            connectedRadStatus.setFirstCurrentLocationHub(currentLocationCode);
        }
    }

    private void setCourierCodeAndGroup(String courierCode, ConnectedRadStatus connectedRadStatus) throws InsufficientDataException {
        if (StringUtils.isEmpty(courierCode)) {
            return;
        }
        connectedRadStatus.setCourierCode(courierCode);
        CourierDetail courierDetails = CacheManager.getInstance().getCache(CourierGroupCache.class).
                getCourierCodeToDetail(courierCode);
        if (Objects.isNull(courierDetails) || StringUtils.isEmpty(courierDetails.getCourierGroup())) {
            throw new InsufficientDataException("CourierDetail is not found for courierCode = " + courierCode);
        }
        connectedRadStatus.setCourierGroup(courierDetails.getCourierGroup());
    }

    private void setCurentConnectedDetailAndDate(SubOrderTpStatusCodesDTO subOrderTpStatusCodesDTO, ConnectedRadStatus connectedRadStatus) {
        connectedRadStatus.addConnectedStatusDetail(new ConnectedStatusDetails()
                                                            .setCourierRemarks(subOrderTpStatusCodesDTO.getRemarks())
                                                            .setCourierStatus(subOrderTpStatusCodesDTO.getCourierStatus())
                                                            .setSnapdealStatus(subOrderTpStatusCodesDTO.getNewStatusCode())
                                                            .setLocationHub(subOrderTpStatusCodesDTO.getCurrentLocationCode())
                                                            .setStatusdate(subOrderTpStatusCodesDTO.getNewStatusDate().getTime()));
    }

    private void setCurrentRADLocationCityAndDate(String currentLocationPincode, String courierCode,
                                                  String currentLocationCode, Date locationDate, ConnectedRadStatus connectedRadStatus) throws InvalidDataException, InsufficientDataException {
        if (StringUtils.isNotEmpty(currentLocationPincode)) {
            PincodeDetail pincodeDetail = CacheManager.getInstance().getCache(PincodeMasterCache.class).getPincodeDetail(currentLocationPincode);
            Optional.ofNullable(pincodeDetail).orElseThrow(() -> new InsufficientDataException("pincode detail is null for pincode : " + currentLocationPincode));
            Date previousDate = connectedRadStatus.getCurrentRADDate(pincodeDetail.getCity());
            if (Objects.isNull(previousDate) || locationDate.before(previousDate)) {
                connectedRadStatus.addCurrentRADCityToDate(pincodeDetail.getCity(), locationDate);
            }
        } else if (StringUtils.isNotEmpty(currentLocationCode)) {
            SnapdealLocation snapdealLocation = CacheManager.getInstance().getCache(CourierLocationSnapdealLocationMappingCache.class)
                    .getSnapdealLocation(courierCode, currentLocationCode);
            Optional.ofNullable(snapdealLocation).orElseThrow(() -> new InsufficientDataException("CourierLocationSnapdealLocationMapping detail is null for courierCode : "
                                                                                                  + courierCode + ", locationCode : " + currentLocationCode));
            Date previousDate = connectedRadStatus.getCurrentRADDate(snapdealLocation.getSnapdealLocationCity());
            if (Objects.isNull(previousDate) || locationDate.before(previousDate)) {
                connectedRadStatus.addCurrentRADCityToDate(snapdealLocation.getSnapdealLocationCity(), locationDate);
            }
            // Check with Gurwant
            //		} else {
            //			throw new InvalidDataException("Both current Location pincode and current Location  code is null.. Believe me not acceptable");
        }
    }

    private void setCourierOriginAnadDestinationHub(ElasticDataDTO elasticDataDTO, String originHubPincode, String destinationHubPincode, ConnectedRadStatus connectedRadStatus) throws InsufficientDataException {
        if (StringUtils.isNotEmpty(originHubPincode)) {
            PincodeDetail sourceHub = CacheManager.getInstance().getCache(PincodeMasterCache.class).getPincodeDetail(originHubPincode);
            Optional.ofNullable(sourceHub).orElseThrow(() -> new InsufficientDataException("pincode detail is null for pincode : " + originHubPincode));
            connectedRadStatus.setCourierOriginState(sourceHub.getState());
            elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.COURIER_ORIGIN_STATE, sourceHub.getState());
            connectedRadStatus.setCourierOriginCity(sourceHub.getCity());
            elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.COURIER_ORIGIN_CITY, sourceHub.getCity());
        }
        if (StringUtils.isNotEmpty(destinationHubPincode)) {
            PincodeDetail destinationHub = CacheManager.getInstance().getCache(PincodeMasterCache.class).getPincodeDetail(destinationHubPincode);
            Optional.ofNullable(destinationHub).orElseThrow(() -> new InsufficientDataException("pincode detail is null for pincode : " + destinationHubPincode));
            connectedRadStatus.setCourierDestinationState(destinationHub.getState());
            elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.COURIER_DESTINATION_STATE, destinationHub.getState());
            connectedRadStatus.setCourierDestinationCity(destinationHub.getCity());
            elasticDataDTO.addDataValue(SubOrderDetailElasticColumn.COURIER_DESTINATION_CITY, destinationHub.getCity());
        }
    }

    private void addDELAndClosedScript(SubOrderTpStatusCodesDTO subOrderTpStatusCodesDTO, ElasticDataDTO elasticDataDTO) {
        Date                 date                 = subOrderTpStatusCodesDTO.getNewStatusDate();
        ElasticScriptDataDTO elasticScriptDataDTO = new ElasticScriptDataDTO(elasticDataDTO.getSubOrderCode());
        elasticScriptDataDTO.addScript(ElasticScriptKey.ORDER_TO_DELIVERED);
        elasticScriptDataDTO.addScript(ElasticScriptKey.SHIPPED_TO_DELIVERED);
        elasticScriptDataDTO.addScript(ElasticScriptKey.ATTEMPTED);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE.getColumnName(), date);
        elasticScriptDataDTO.addScript(ElasticScriptKey.CLOSED_DATE);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE.getColumnName(), date);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.CLOSED_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.CLOSED_DATE.getColumnName(), date);
        elasticDataDTO.setElasticScriptDataDTO(elasticScriptDataDTO);
    }

    private void addUDLScript(SubOrderTpStatusCodesDTO subOrderTpStatusCodesDTO, ElasticDataDTO elasticDataDTO) {
        Date                 date                 = subOrderTpStatusCodesDTO.getNewStatusDate();
        ElasticScriptDataDTO elasticScriptDataDTO = new ElasticScriptDataDTO(elasticDataDTO.getSubOrderCode());
        elasticScriptDataDTO.addScript(ElasticScriptKey.ATTEMPTED);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE.getColumnName(), date);
        elasticScriptDataDTO.addScript(ElasticScriptKey.UDL);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.ATTEMPTED_STATUS_DATE.getColumnName(), date);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE.getColumnName(), Lists.newArrayList(date));
        elasticDataDTO.setElasticScriptDataDTO(elasticScriptDataDTO);
    }

    private void addConnectedScript(SubOrderTpStatusCodesDTO subOrderTpStatusCodesDTO, ElasticDataDTO elasticDataDTO) {
        Date                 date                 = subOrderTpStatusCodesDTO.getNewStatusDate();
        ElasticScriptDataDTO elasticScriptDataDTO = new ElasticScriptDataDTO(elasticDataDTO.getSubOrderCode());
        elasticScriptDataDTO.addScript(ElasticScriptKey.CONNECTED);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_CONNECTED_STATUS_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.TP_CONNECTED_STATUS_DATE.getColumnName(), date);
        elasticDataDTO.setElasticScriptDataDTO(elasticScriptDataDTO);
    }

    private void addOFDScript(SubOrderTpStatusCodesDTO subOrderTpStatusCodesDTO, ElasticDataDTO elasticDataDTO) {
        Date                 date                 = subOrderTpStatusCodesDTO.getNewStatusDate();
        ElasticScriptDataDTO elasticScriptDataDTO = new ElasticScriptDataDTO(elasticDataDTO.getSubOrderCode());
        elasticScriptDataDTO.addScript(ElasticScriptKey.OFD);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_OFD_STATUS_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.TP_OFD_STATUS_DATE.getColumnName(), date);
        elasticDataDTO.setElasticScriptDataDTO(elasticScriptDataDTO);
    }

    private void addRADScript(SubOrderTpStatusCodesDTO subOrderTpStatusCodesDTO, ElasticDataDTO elasticDataDTO) {
        Date                 date                 = subOrderTpStatusCodesDTO.getNewStatusDate();
        ElasticScriptDataDTO elasticScriptDataDTO = new ElasticScriptDataDTO(elasticDataDTO.getSubOrderCode());
        elasticScriptDataDTO.addScript(ElasticScriptKey.RAD);
        elasticScriptDataDTO.addDataValue(SubOrderDetailElasticColumn.TP_RAD_STATUS_DATE, date);
        elasticScriptDataDTO.addParams(SubOrderDetailElasticColumn.TP_RAD_STATUS_DATE.getColumnName(), date);
        elasticDataDTO.setElasticScriptDataDTO(elasticScriptDataDTO);
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SUB_ORDER_TP_STATUS_CODES;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void mergerElasticScriptDataDTO(ElasticDataDTO elasticDataDTO, Map<String, ElasticScriptDataDTO> elasticScriptMap) {
        ElasticScriptDataDTO elasticScriptDataDTO = elasticDataDTO.getElasticScriptDataDTO();
        if (Objects.isNull(elasticScriptDataDTO)) {
            return;
        }
        ElasticScriptDataDTO previousElasticScriptDataDTO = elasticScriptMap.get(elasticDataDTO.getSubOrderCode());
        if (Objects.isNull(previousElasticScriptDataDTO)) {
            elasticScriptMap.put(elasticDataDTO.getSubOrderCode(), elasticScriptDataDTO);
        } else {
            EnumMap<SubOrderDetailElasticColumn, Object> currentDataMapValue = elasticScriptDataDTO.getDataValueMap();
            for (Entry<SubOrderDetailElasticColumn, Object> entrySet : currentDataMapValue.entrySet()) {
                Object                      value = entrySet.getValue();
                SubOrderDetailElasticColumn key   = entrySet.getKey();
                switch (entrySet.getKey()) {
                    case TP_CONNECTED_STATUS_DATE:
                    case CLOSED_DATE:
                    case TP_OFD_STATUS_DATE:
                    case TP_RAD_STATUS_DATE:
                    case ATTEMPTED_STATUS_DATE: {
                        Date currentDate  = (Date) value;
                        Date previousDate = (Date) previousElasticScriptDataDTO.getDataValueMap().get(key);
                        if (Objects.isNull(previousDate) || currentDate.before(previousDate)) {
                            previousElasticScriptDataDTO.addDataValue(key, value);
                            previousElasticScriptDataDTO.addParams(key.getColumnName(), value);
                        }
                    }
                    break;
                    case TP_FIRST_UDL_STATUS_DATE: {
                        List<Date> currentDates  = (List<Date>) elasticScriptDataDTO.getParams().get(key);
                        Date       currentDate   = currentDates.get(0);
                        List<Date> previousDates = (List<Date>) previousElasticScriptDataDTO.getParams().get(key);
                        if (CollectionUtils.isEmpty(previousDates)) {
                            previousElasticScriptDataDTO.addDataValue(key, value);
                            previousElasticScriptDataDTO.addParams(key.getColumnName(), currentDates);
                        } else {
                            boolean isAdded = false;
                            for (Date date : previousDates) {
                                if (DateUtils.isSameDay(date, currentDate)) {
                                    isAdded = true;
                                    if (currentDate.before(date)) {
                                        previousDates.add(previousDates.indexOf(date), currentDate);
                                        previousDates.remove(date);
                                    }
                                    break;
                                }
                                if (date.after(currentDate)) {
                                    isAdded = true;
                                    previousDates.add(previousDates.indexOf(date), currentDate);
                                    break;
                                }
                            }
                            if (!isAdded) {
                                previousDates.add(currentDate);
                            }
                            if (previousDates.size() > 4) {
                                previousDates.remove(4);
                            }
                        }
                        List<SubOrderDetailElasticColumn> columnNames = new ArrayList<>();
                        columnNames.add(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE);
                        columnNames.add(SubOrderDetailElasticColumn.TP_SECOND_UDL_STATUS_DATE);
                        columnNames.add(SubOrderDetailElasticColumn.TP_THIRD_UDL_STATUS_DATE);
                        columnNames.add(SubOrderDetailElasticColumn.TP_FOURTH_UDL_STATUS_DATE);
                        for (int i = 0; i < previousDates.size(); i++) {
                            previousElasticScriptDataDTO.addDataValue(columnNames.get(i), previousDates.get(i));
                        }
                    }
                    break;
                    default:
                        previousElasticScriptDataDTO.addDataValue(key, value);
                        previousElasticScriptDataDTO.addParams(key.getColumnName(), value);
                        break;
                }
            }
            previousElasticScriptDataDTO.addAllScripts(elasticScriptDataDTO.getScripts());
        }
    }

    @Override
    protected void mergeMongoDataDTO(ElasticDataDTO elasticDataDTO, Map<String, MongoDocument> mongoDataMap) {
        ConnectedRadStatus mongoDocument = (ConnectedRadStatus) elasticDataDTO.getMongoDocument();
        if (Objects.isNull(mongoDocument)) {
            return;
        }
        ConnectedRadStatus previousMongoDocument = (ConnectedRadStatus) mongoDataMap.get(elasticDataDTO.getSubOrderCode());
        if (previousMongoDocument != null) {
            previousMongoDocument.setCourierCode(mongoDocument.getCourierCode())
                    .setCourierGroup(mongoDocument.getCourierGroup())
                    .setCourierOriginCity(mongoDocument.getCourierOriginCity())
                    .setCourierOriginState(mongoDocument.getCourierOriginState())
                    .setCourierDestinationCity(mongoDocument.getCourierDestinationCity())
                    .setCourierDestinationState(mongoDocument.getCourierDestinationState())
                    .setFirstCurrentLocationHub(mongoDocument.getFirstCurrentLocationHub())
                    .addCurrentRADCityToDate(mongoDocument.getCurrentRADCityToDate())
                    .addConnectedStatusDetail(mongoDocument.getConnectedStatusDetails());
        } else {
            mongoDataMap.put(elasticDataDTO.getSubOrderCode(), mongoDocument);
        }
    }

    @Override
    protected void storeInMongo(Collection<MongoDocument> connectedRadStatus) {
        connectedRadStatusRepository.upsertAll(connectedRadStatus);
    }
}