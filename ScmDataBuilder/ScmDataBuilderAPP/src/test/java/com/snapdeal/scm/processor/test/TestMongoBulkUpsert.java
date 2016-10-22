package com.snapdeal.scm.processor.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.SCMConfiguration;
import com.snapdeal.scm.common.domain.mongo.MongoCollectionName;
import com.snapdeal.scm.common.domain.mongo.MongoDataDTO;
import com.snapdeal.scm.core.mongo.document.ConnectedRadStatus;
import com.snapdeal.scm.das.mongo.dao.ConnectedRadStatusRepository;
import com.snapdeal.scm.mongo.mao.MongoRepositoryCustom;

/**
 * 
 * @author prateek
 *
 */
@SpringApplicationConfiguration(SCMConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestMongoBulkUpsert {

	@Autowired
	private ConnectedRadStatusRepository connectedRadStatusRepository;
	
	@Autowired
	private MongoRepositoryCustom mongoRepositoryCustom;

	@Test
	public  void testConnectedBulkUpsert(){
		List<ConnectedRadStatus> connectedRadStatuss = new ArrayList<>();
		for(int i = 0 ; i <4 ; i++){
			ConnectedRadStatus connectedRadStatus = new ConnectedRadStatus("subOrderCode" + i);
			connectedRadStatus.setCourierCode("myCourier1"+i);
			connectedRadStatus.setFirstCurrentLocationHub("firstLocationHub"+i);
			connectedRadStatus.addCurrentRADCityToDate("abc"+i, new Date());
			connectedRadStatus.addCurrentRADCityToDate("xyz"+i, new Date());
			connectedRadStatuss.add(connectedRadStatus);
		}
		connectedRadStatusRepository.upsert(connectedRadStatuss.get(0));
	}
	
	@Test
	public  void testBulkUpsert(){
		List<MongoDataDTO> mongoDataDTOs = new ArrayList<>();
		for(int i = 0 ; i <4 ; i++){
			MongoDataDTO mongoDataDTO = new MongoDataDTO(MongoCollectionName.CONNECTED_RAD_STATUS);
			mongoDataDTO.addQueryKeyValue("subOrderCode", "subOrderCode"+i);
			mongoDataDTO.addInsertKeyObjectValue("courierGroup", "myCourier"+i);
			mongoDataDTO.addInsertKeyObjectValue("firstCurrentLocationHub", "firstLocationHub"+i);
			mongoDataDTO.addInMapValueOfInsertKey("currentRADCityToDate", "123"+i, new Date());
			mongoDataDTO.addInMapValueOfInsertKey("currentRADCityToDate", "xyza"+i, new Date());
			mongoDataDTOs.add(mongoDataDTO);
		}
		mongoRepositoryCustom.upsertAll(mongoDataDTOs);
	}
}
