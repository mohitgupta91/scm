package com.snapdeal.scm.processor.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.SCMConfiguration;
import com.snapdeal.scm.cache.ICacheLoadService;
import com.snapdeal.scm.core.dto.IStandardDTO;
import com.snapdeal.scm.core.dto.impl.CamsSupcSuperCategoryDTO;
import com.snapdeal.scm.core.dto.impl.CenterMasterDTO;
import com.snapdeal.scm.core.dto.impl.CourierGroupDTO;
import com.snapdeal.scm.core.dto.impl.FilmsSupcMtoDTO;
import com.snapdeal.scm.core.dto.impl.PincodeMasterDTO;
import com.snapdeal.scm.core.dto.impl.ShippingSoiSdDTO;
import com.snapdeal.scm.core.dto.impl.ShippingSoiSoidDTO;
import com.snapdeal.scm.core.dto.impl.ShippingSoiSpDTO;
import com.snapdeal.scm.core.dto.impl.SubOrderSoiStatusCodesDTO;
import com.snapdeal.scm.core.dto.impl.SubOrderSpStatusCodesDTO;
import com.snapdeal.scm.core.dto.impl.SubOrderTpStatusCodesDTO;
import com.snapdeal.scm.core.dto.impl.VendorContactDTO;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.processor.dto.impl.ProcessorQueueDto;
import com.snapdeal.scm.processor.IDataProcessor;

/**
 * 
 * @author prateek
 *
 */
@SpringApplicationConfiguration(SCMConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDataProcessor {

	@Autowired
	IDataProcessor dataProcessorImpl;
	
	@Autowired
	ICacheLoadService cacheLoadService;

	/**
	 * stored in mongo
	 */
	@Test
	public void test1CamsSupcSuperCategory() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		standardDTOs.add(new CamsSupcSuperCategoryDTO("supc1", "superCategory1"));
		process(QueryType.CAMS_SUPC_SUPER_CATEGORY, standardDTOs);
	}

	/**
	 * stored in mongo
	 */
	@Test
	public void test2FilmsSupcMto() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		standardDTOs.add(new FilmsSupcMtoDTO("supc1", "mto1"));
		process(QueryType.FILMS_SUPC_MTO, standardDTOs);
	}

	/**
	 * stored in mongo
	 */
	@Test
	public void test3VendorContact() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		standardDTOs.add(new VendorContactDTO("pincode1", "vendorCode1"));
		standardDTOs.add(new VendorContactDTO("destinationPincode1", "vendorCode2"));
		standardDTOs.add(new VendorContactDTO("sourcePincode1", "vendorCode3ss"));
		process(QueryType.VENDOR_CONTACT, standardDTOs);
	}

	/**
	 * stored in mongo
	 */
	@Test
	public void test4CenterMaster() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		standardDTOs.add(new CenterMasterDTO("centerCode1", "name1", true, "type1", 
				"destinationPincode1", "city1", "state2", new Date(), "scoreCity1", "scoreTier1", "scorestate2", "logiopsCity1", "logiopsstate2"
				, "logiopsZone1", "bdeZone1", "scCity1", "scstate2", "scZone1", "shipNearZone1", "centerType1"));
		standardDTOs.add(new CenterMasterDTO("centerCode2", "name1", true, "type1",
				"sourcePincode1", "city1", "state2", new Date(), "scoreCity1", "scoreTier1", "scorestate2", "logiopsCity1", "logiopsstate2"
				, "logiopsZone1", "bdeZone1", "scCity1", "scstate2", "scZone1", "shipNearZone1", "centerType1"));
		standardDTOs.add(new CenterMasterDTO("centerCode3", "name1", true, "type1",
				"pincode1", "city1", "state2", new Date(), "scoreCity1", "scoreTier1", "scorestate2", "logiopsCity1", "logiopsstate2"
				, "logiopsZone1", "bdeZone1", "scCity1", "scstate2", "scZone1", "shipNearZone1", "centerType1"));
		process(QueryType.CENTER_MASTER, standardDTOs);
	}

	/**
	 * stored in mongo
	 */
	@Test
	public void test5CourierGroup() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		standardDTOs.add(new CourierGroupDTO(1,1,"name1", "courierCode1", "shippingModeCode1", "transitTypeCode1", "courierType1"
				, "courierGroup1", "wmsRlCode", false));
		process(QueryType.COURIER_GROUP, standardDTOs);
	}

	/**
	 * stored in mongo
	 */
	@Test
	public void test6PincodeMaster() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		standardDTOs.add(new PincodeMasterDTO("destinationPincode1", "city1", "state2", "zone1", "bdeZone1", "scCity1", "scstate2","scZone1", "ecomCity1",
				"ecomstate2", "dvlCity1", "dvlstate2", "bdCort1", "bdstate2", "gjCity1", "gjstate2", "redexCity1", "redexstate2"));
		standardDTOs.add(new PincodeMasterDTO("sourcePincode1", "city1", "state2", "zone1", "bdeZone1", "scCity1", "scstate2","scZone1", "ecomCity1",
				"ecomstate2", "dvlCity1", "dvlstate2", "bdCort1", "bdstate2", "gjCity1", "gjstate2", "redexCity1", "redexstate2"));
		standardDTOs.add(new PincodeMasterDTO("pincode1", "city1", "state2", "zone1", "bdeZone1", "scCity1", "scstate2","scZone1", "ecomCity1",
				"ecomstate2", "dvlCity1", "dvlstate2", "bdCort1", "bdstate2", "gjCity1", "gjstate2", "redexCity1", "redexstate2"));
		process(QueryType.PINCODE_MASTER, standardDTOs);
	}

	@Test
	public void test1000ReloadCache() {
		cacheLoadService.loadAll(true);
	}
	
	/**
	 * output : [ElasticDataDTO [subOrderCode:z,16:Air]]
	 */
	@Test
	public void testShippingSoiSoid() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		standardDTOs.add(new ShippingSoiSoidDTO("subOrderCodeAB", "SHIPPING_MODE", "Air"));
		process(QueryType.SHIPPING_SOI_SOID, standardDTOs);
	}
	/**
	 * output : [ElasticDataDTO [subOrderCode:subOrderCodeAB,18:courierGroup1,19:courierType1,20:Wed Feb 24 17:34:49 IST 2016,21:Wed Feb 24 17:34:49 IST 2016]]
	 */
	@Test
	public void testShippingSoiSp() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		standardDTOs.add(new ShippingSoiSpDTO("subOrderCodeAB", "courierCode1", new Date(), new Date()));
		process(QueryType.SHIPPING_SOI_SP, standardDTOs);
	}

	/**
	 * output : [ElasticDataDTO [subOrderCode:subOrderCodeAB,1:Wed Feb 24 17:33:12 IST 2016,2:Same City,3:superCategory1,4:mto1,5:scCity1-,6:scstate2,7:scZone1
	 * ,9:scZone1,10:scCity1-,11:scstate2,12:scZone1,14:scZone1,15:scCity1-::scCity1-]]
	 */
	@Test
	public void testShippingSoiSd() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 9, 5);
		standardDTOs.add(new ShippingSoiSdDTO("subOrderCodeAB", "supc1", "destinationPincode1", "centerCode1", "FC_VOI", cal.getTime(), "vendorCode1", true));
		process(QueryType.SHIPPING_SOI_SD, standardDTOs);
	}

	/**
	 * output : [ElasticDataDTO [subOrderCode:subOrderCodeAB,22:Wed Feb 24 17:35:09 IST 2016]]
	 */
	@Test
	public void testSubOrderSoiStatusCodes() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		standardDTOs.add(new SubOrderSoiStatusCodesDTO("subOrderCodeAB", "RTN", new Date()));
		process(QueryType.SUB_ORDER_SOI_STATUS_CODES, standardDTOs);
	}

	/**
	 * output : [ElasticDataDTO [subOrderCode:subOrderCodeAB,26:Wed Feb 24 17:35:27 IST 2016]]
	 */
	@Test
	public void testSubOrderSpStatusCodes() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 9, 7);
		standardDTOs.add(new SubOrderSpStatusCodesDTO("subOrderCodeAB", "AWB_UPLOADED", cal.getTime()));
		process(QueryType.SUB_ORDER_SP_STATUS_CODES, standardDTOs);
	}

	/**
	 * output : [ElasticDataDTO [subOrderCode:subOrderCodeAB,33:Wed Feb 24 17:35:45 IST 2016]]
	 */
	@Test
	public void testSubOrderTpStatusCodes() {
		List<IStandardDTO> standardDTOs = new ArrayList<IStandardDTO>();
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 9, 9);
		standardDTOs.add(new SubOrderTpStatusCodesDTO("subOrderCodeAB", "DEL", cal.getTime(), "currentLocation", "currentLocationPincode", "remarks", "courierStatus", "originHubLocationPincode", "destinationHubLocationPincode", "DEL", cal.getTime()));
		standardDTOs.add(new SubOrderTpStatusCodesDTO("subOrderCodeAB", "UDL", cal.getTime(), "currentLocation", "currentLocationPincode", "remarks", "courierStatus", "originHubLocationPincode", "destinationHubLocationPincode", "UDL", cal.getTime()));
		process(QueryType.SUB_ORDER_TP_STATUS_CODES, standardDTOs);
	}

//	@Autowired
//	ScmPropertyRepository scmPropertiesRepository;
//	@Test
//	public void A(){
//		Map<String, String> map = new HashMap<String, String>();
//		try {
//			File fXmlFile = new File("/home/prateek/state_cde.xml");
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(fXmlFile);
//			NodeList nList = doc.getElementsByTagName("values");
//			for (int temp = 0; temp < nList.getLength(); temp++) {
//				Node nNode = nList.item(temp);
//				Element eElement = (Element) nNode;
//				map.put(eElement.getAttribute("key"),eElement.getAttribute("value"));
//			} 
//		}catch (Exception e) {
//		}
//		ScmProperty scmProperty = new ScmProperty(Property.STATE_CODE.getName(), map);
//		scmPropertiesRepository.save(scmProperty);
//	}

	private void process(QueryType queryType, List<IStandardDTO> standardDTOs) {
		ProcessorQueueDto processorQueueDto = new ProcessorQueueDto();
		processorQueueDto.setQueryType(queryType);
		processorQueueDto.setFilePath("filePath");
		processorQueueDto.setDataProcessorDtos(standardDTOs);
		try {
			dataProcessorImpl.process(processorQueueDto);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}