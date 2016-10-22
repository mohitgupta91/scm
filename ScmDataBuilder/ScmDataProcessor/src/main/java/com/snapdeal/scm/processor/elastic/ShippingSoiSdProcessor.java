package com.snapdeal.scm.processor.elastic;

import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.cache.impl.FulfillmentProviderCache;
import com.snapdeal.scm.cache.impl.MetroCityCache;
import com.snapdeal.scm.cache.impl.PincodeMasterCache;
import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.dto.impl.ShippingSoiSdDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticScriptDataDTO;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.core.enums.ElasticScriptKey;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.mongo.document.ConnectedRadStatus;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;
import com.snapdeal.scm.core.utils.CommonUtils;
import com.snapdeal.scm.core.utils.StringUtils;
import com.snapdeal.scm.das.mongo.dao.ConnectedRadStatusRepository;
import com.snapdeal.scm.mongo.doc.SupcDetails;
import com.snapdeal.scm.mongo.mao.repository.SupcDetailsRepository;
import com.snapdeal.scm.processor.cache.impl.SupcCache;
import com.snapdeal.scm.processor.exception.InsufficientDataException;
import com.snapdeal.scm.processor.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * Query: ""
 * <p>
 * Key : {@link SubOrderDetailElasticColumn#SUB_ORDER_CODE}
 * <p>
 * Data : {@link SubOrderDetailElasticColumn#ORDER_CREATED_DATE}, {@link SubOrderDetailElasticColumn#LANE_TYPE},
 * {@linkSchemaKey#SUPER_CATEGORY}, {@linkSchemaKey#MTO}
 *
 * @author prateek
 */
@Service("ShippingSoiSdProcessor")
public class ShippingSoiSdProcessor extends AbstractElasticProcessor<ShippingSoiSdDTO> {

    @Autowired
    private SupcDetailsRepository supcDetailsRepository;

    @Autowired
    private ConnectedRadStatusRepository connectedRadStatusRepository;

    // TODO move to some configuration
    // Fulfillment Model
    private static final String FC_VOI  = "FC_VOI";
    private static final String ONESHIP = "ONESHIP";
    private static final String OCPLUS  = "OCPLUS";

    // TODO move to some configuration
    // Lane Type
    private static final String SAME_CITY = "same-city";
    private static final String METRO     = "metro";
    private static final String Zone      = "zone";
    private static final String ROI       = "roi";

    private static final String METRO_CITY     = "metro";
    private static final String NON_METRO_CITY = "non-metro";

    private static final String MTO     = "mto";
    private static final String NON_MTO = "non-mto";

    private static final String SJ = "SJ";

    @Override
    public ElasticDataDTO processRecord(ShippingSoiSdDTO shippingSoiSdDTO) throws InvalidDataException, InsufficientDataException {
        String                           centerCode                 = shippingSoiSdDTO.getCenterCode();
        String                           sourcePincode              = getSourcePincode(shippingSoiSdDTO.getFullfilmentModel(), centerCode, shippingSoiSdDTO.getVendorCode());
        String                           customerDestinationPincode = shippingSoiSdDTO.getCustomerDestinationPincode();
        PincodeMasterCache.PincodeDetail sourceDetail               = CacheManager.getInstance().getCache(PincodeMasterCache.class).getPincodeDetail(sourcePincode);
        PincodeMasterCache.PincodeDetail destinationDetail          = CacheManager.getInstance().getCache(PincodeMasterCache.class).getPincodeDetail(customerDestinationPincode);
        String                           laneType                   = getLaneType(sourceDetail, destinationDetail, sourcePincode, customerDestinationPincode);
        SupcDetails                      supcDetails                = CacheManager.getInstance().getCache(SupcCache.class).getSupcDetailsFromGuavacache(shippingSoiSdDTO.getSupc());
        if (supcDetails == null) {
            throw new InsufficientDataException("Supc detail is not found for supc:" + shippingSoiSdDTO.getSupc());
        }
        String superCategory = supcDetails.getSuperCategory();
        String mto           = supcDetails.getMto();
        if (superCategory == null || mto == null) {
            throw new InsufficientDataException("Super category = " + superCategory + " or mto = " + mto + " is null for supc:" + shippingSoiSdDTO.getSupc());
        }
        String metro = NON_METRO_CITY;
        if (CacheManager.getInstance().getCache(MetroCityCache.class).isMetroCity(destinationDetail.getCity())) {
            metro = METRO_CITY;
        }
        if (mto.equalsIgnoreCase(MTO)) {
            mto = MTO;
        } else {
            mto = NON_MTO;
        }
        ElasticDataDTO eLasticSearchDTO = createELasticSearchDTO(shippingSoiSdDTO.getSubOrderCode(), centerCode, shippingSoiSdDTO.getOrderCreatedDate(), laneType, superCategory, mto, sourceDetail, destinationDetail,
                                                                 shippingSoiSdDTO.getCenterCode(), shippingSoiSdDTO.getFullfilmentModel(), metro, shippingSoiSdDTO.getOnHold(), shippingSoiSdDTO.getItemPrice(), shippingSoiSdDTO.getPaymentMode());
        setDestinationPincodeInMongo(customerDestinationPincode, shippingSoiSdDTO.getSubOrderCode(), eLasticSearchDTO);
        return eLasticSearchDTO;
    }

    private void setDestinationPincodeInMongo(String customerDestinationPincode, String subOrderCode, ElasticDataDTO eLasticSearchDTO) {
        ConnectedRadStatus connectedRadStatus = new ConnectedRadStatus(subOrderCode);
        connectedRadStatus.setCustomerDestinationPincode(customerDestinationPincode);
        eLasticSearchDTO.setMongoDocument(connectedRadStatus);
    }

    private ElasticDataDTO createELasticSearchDTO(String subOrderCode, String centerCode, Date orderCreatedDate, String laneType, String superCategory, String mto, PincodeMasterCache.PincodeDetail sourceDetail,
                                                  PincodeMasterCache.PincodeDetail destinationDetail, String fpCode, String fulfillmentModel, String metro, Boolean onHold, Double itemPrice, String paymentMode) {
        ElasticDataDTO dto = new ElasticDataDTO(subOrderCode);
        dto.addDataValue(SubOrderDetailElasticColumn.ORDER_CREATED_DATE, orderCreatedDate);
        if (orderCreatedDate != null) {
            addOrderCreatedScript(dto);
        }
        dto.addDataValue(SubOrderDetailElasticColumn.CENTER_CODE, centerCode);
        dto.addDataValue(SubOrderDetailElasticColumn.LANE_TYPE, laneType);
        dto.addDataValue(SubOrderDetailElasticColumn.SUPER_CATEGORY, superCategory);
        dto.addDataValue(SubOrderDetailElasticColumn.MTO, mto);
        dto.addDataValue(SubOrderDetailElasticColumn.SOURCE_CITY, sourceDetail.getCity());
        dto.addDataValue(SubOrderDetailElasticColumn.SOURCE_STATE, sourceDetail.getState());
        dto.addDataValue(SubOrderDetailElasticColumn.SOURCE_ZONE, sourceDetail.getZone());
        // TODO Currently not used but may be used in future
        // dto.addValue(SubOrderDetailElasticColumn.SOURCE_NODE, sourceDetail);
        dto.addDataValue(SubOrderDetailElasticColumn.SOURCE_TIER, sourceDetail.getZone());
        dto.addDataValue(SubOrderDetailElasticColumn.SOURCE_REGION, sourceDetail.getBdeZone());
        dto.addDataValue(SubOrderDetailElasticColumn.DESTINATION_CITY, destinationDetail.getCity());
        dto.addDataValue(SubOrderDetailElasticColumn.DESTINATION_STATE, destinationDetail.getState());
        dto.addDataValue(SubOrderDetailElasticColumn.DESTINATION_ZONE, destinationDetail.getZone());
        // TODO Currently not used but may be used in future
        // dto.addValue(SubOrderDetailElasticColumn.DESTINATION_NODE, destinationDetail);
        dto.addDataValue(SubOrderDetailElasticColumn.DESTINATION_TIER, destinationDetail.getZone());
        dto.addDataValue(SubOrderDetailElasticColumn.DESTINATION_REGION, destinationDetail.getBdeZone());
        String lane = CommonUtils.generateLane(sourceDetail.getCity(), destinationDetail.getCity());
        dto.addDataValue(SubOrderDetailElasticColumn.LANE, lane);
        if (StringUtils.isNotEmpty(fpCode) && ONESHIP.equalsIgnoreCase(fulfillmentModel) && fpCode.toUpperCase().startsWith(SJ)) {
            ;
        }
        fulfillmentModel = OCPLUS;
        dto.addDataValue(SubOrderDetailElasticColumn.FULFILLMENT_MODEL, fulfillmentModel);
        dto.addDataValue(SubOrderDetailElasticColumn.METRO, metro);
        dto.addDataValue(SubOrderDetailElasticColumn.ON_HOLD, onHold);
        dto.addDataValue(SubOrderDetailElasticColumn.ITEM_PRICE, itemPrice);
        dto.addDataValue(SubOrderDetailElasticColumn.PAYMENT_MODE, paymentMode);
        return dto;
    }

    /**
     * @param sourceDetail
     * @param destinationDetail
     * @return String laneType, return null if sourceDetail or destinationDetail is null
     * @throws InsufficientDataException
     */
    private String getLaneType(PincodeMasterCache.PincodeDetail sourceDetail, PincodeMasterCache.PincodeDetail destinationDetail, String sourcePincode, String destinationPincode) throws InsufficientDataException {
        String laneType = ROI;
        try {
            if (sourceDetail == null || destinationDetail == null) {
                throw new Exception();
            }
            if (sourceDetail.getScCity().equals(destinationDetail.getScCity())) {
                laneType = SAME_CITY;
            } else if (sourceDetail.getScZone().equalsIgnoreCase(destinationDetail.getScZone()) && sourceDetail.getScZone().equalsIgnoreCase(METRO)) {
                laneType = METRO;
            } else if (sourceDetail.getBdeZone().equalsIgnoreCase(destinationDetail.getBdeZone())) {
                laneType = Zone;
            }
        } catch (Throwable e) {
            throw new InsufficientDataException("sourceDetail :" + sourceDetail + "or destinationDetail :" + destinationDetail + " is null for sourcePincode :" + sourcePincode
                                                + ", destinationPincode :" + destinationPincode);
        }
        return laneType;
    }

    /**
     * return source pincode (either from centerCode or vendor code) based on fullfilmentModel
     *
     * @param fullfilmentModel
     * @param centerCode
     * @param vendorCode
     * @return String pincode
     * @throws InvalidDataException
     * @throws InsufficientDataException
     */
    private String getSourcePincode(String fullfilmentModel, String centerCode, String vendorCode) throws InvalidDataException, InsufficientDataException {
        String pincode = null;
        if (FC_VOI.equalsIgnoreCase(fullfilmentModel) || ONESHIP.equalsIgnoreCase(fullfilmentModel) || OCPLUS.equalsIgnoreCase(fullfilmentModel)) {
            Optional.ofNullable(centerCode).orElseThrow(() -> new InvalidDataException("center code is null for fullfillmentModel :" + fullfilmentModel));
            pincode = CacheManager.getInstance().getCache(FulfillmentProviderCache.class).getCenterCodeToPincodeMapping(centerCode);
        } else {
            Optional.ofNullable(vendorCode).orElseThrow(() -> new InvalidDataException("vendor code is null for fullfillmentModel :" + fullfilmentModel));
            pincode = CacheManager.getInstance().getCache(FulfillmentProviderCache.class).getVendorCodeToPincodeMapping(vendorCode);
        }
        return Optional.ofNullable(pincode).orElseThrow(() -> new InsufficientDataException(
                "pincode is null for fulfillmentModel :" + fullfilmentModel + ", center code :" + centerCode + " and  vendor code :" + vendorCode));
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SHIPPING_SOI_SD;
    }

    @Override
    protected void preProcess(ProcessorQueueDto<ShippingSoiSdDTO> processorQueueDto) {
        Set<String> supcs = new HashSet<>();
        processorQueueDto.getDataProcessorDtos().forEach(standardDTO -> supcs.add(((ShippingSoiSdDTO) standardDTO).getSupc()));
        CacheManager.getInstance().getCache(SupcCache.class).addAllSupcDetailsInGuavaCache(supcDetailsRepository.findByAllSupc(supcs));
    }

    private void addOrderCreatedScript(ElasticDataDTO elasticDataDTO) {
        ElasticScriptDataDTO elasticScriptDataDTO = new ElasticScriptDataDTO(elasticDataDTO.getSubOrderCode());
        elasticScriptDataDTO.addScript(ElasticScriptKey.ORDER_TO_DELIVERED);
        elasticScriptDataDTO.addScript(ElasticScriptKey.ORDER_TO_SHIPPED);
        elasticDataDTO.setElasticScriptDataDTO(elasticScriptDataDTO);
    }

    @Override
    protected void mergeMongoDataDTO(ElasticDataDTO elasticDataDTO, Map<String, MongoDocument> mongoDataMap) {
        ConnectedRadStatus mongoDocument = (ConnectedRadStatus) elasticDataDTO.getMongoDocument();
        if (Objects.isNull(mongoDocument)) {
            return;
        }
        ConnectedRadStatus previousMongoDocument = (ConnectedRadStatus) mongoDataMap.get(elasticDataDTO.getSubOrderCode());
        if (Objects.nonNull(previousMongoDocument)) {
            String pincode = mongoDocument.getCustomerDestinationPincode();
            if (StringUtils.isNotEmpty(pincode)) {
                previousMongoDocument.setCustomerDestinationPincode(pincode);
            }
        } else {
            mongoDataMap.put(elasticDataDTO.getSubOrderCode(), mongoDocument);
        }
    }

    @Override
    protected void storeInMongo(Collection<MongoDocument> connectedRadStatus) {
        connectedRadStatusRepository.upsertAll(connectedRadStatus);
    }
}