package com.snapdeal.scm.processor.test;

import com.snapdeal.scm.SCMConfiguration;
import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.processor.IElasticService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * @author pranav
 */
@SpringApplicationConfiguration(SCMConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestElastic {

    @Autowired
    IElasticService elasticService;


    @Test
    public void dynamicScript1() {
        String              string        = "ctx._source.tp_second_udl_status_date = tp_udl_status_date;";
        String              dynamicScript = string;
        Map<String, Object> params        = new HashMap<String, Object>();
        params.put(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE.getColumnName(), new Date());
        List<ElasticDataDTO> dtos = new ArrayList<ElasticDataDTO>();
        ElasticDataDTO       dto  = new ElasticDataDTO("aaaa");
        dto.addDataValue(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE, new Date());
        dtos.add(dto);
//		elasticService.runDynamicScript(dtos , dynamicScript, params);
    }

    @Test
    public void dynamicScript2() {
        String              string        = "ctx._source.tp_second_udl_status_date = tp_udl_status_date;";
        String              dynamicScript = string;
        Map<String, Object> params        = new HashMap<String, Object>();
        params.put(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE.getColumnName(), new Date());
        List<ElasticDataDTO> dtos = new ArrayList<ElasticDataDTO>();
        ElasticDataDTO       dto  = new ElasticDataDTO("aaaa");
        dto.addDataValue(SubOrderDetailElasticColumn.TP_FIRST_UDL_STATUS_DATE, new Date());
        dtos.add(dto);
//		elasticService.runDynamicScript(dtos , dynamicScript, params);
    }

    /**
     * Test Elastic Storage
     */
    @Test
    public void testElastic_1() {
        List<ElasticDataDTO>                         dtos         = new ArrayList<ElasticDataDTO>();
        ElasticDataDTO                               dto          = new ElasticDataDTO("supc_test_elastic");
        EnumMap<SubOrderDetailElasticColumn, Object> dataValueMap = dto.getDataValueMap();
        dataValueMap.put(SubOrderDetailElasticColumn.COURIER_TYPE, "test_courier");
        dataValueMap.put(SubOrderDetailElasticColumn.SOURCE_CITY, "test_city");
        dtos.add(dto);
        //Index Document
        elasticService.indexDocuments(dtos);

        //First Level script run
        String script1 = "if( ctx._source.containsKey(\"destination_node\") ){ ctx._source.source_city += newCity; } "
                         + "else { ctx._source.ifFailed = oldCity; }";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("newCity", "NEWDELHI");
        params.put("oldCity", "DELHI");
        elasticService.runScript(dtos, script1, params);

        //Second script run
        String script2 = "ctx._source.source_city = newCity";
        elasticService.runScript(dtos, script2, params);

        //Add new column
        dataValueMap.put(SubOrderDetailElasticColumn.DESTINATION_NODE, "new column added");
        elasticService.indexDocuments(dtos);

        //run the first level again with the hofe of passing if condition as we have added destination_code
        elasticService.runScript(dtos, script1, params);

        String script3 = "if( ctx._source.containsKey('created_on') && ctx._source.containsKey('updated_on') ){"
                         + "def duration = groovy.time.TimeCategory.minus(new java.util.Date(ctx._source.updated_on), "
                         + "new java.util.Date(ctx._source.created_on));"
                         + "ctx._source.dateDiff = duration.days;"
                         + "}";

        elasticService.runScript(dtos, script3, params);
    }

    @Test
    public void testElastic_3() {
        List<ElasticDataDTO> dtos = new ArrayList<ElasticDataDTO>();
        dtos.add(new ElasticDataDTO("supc_test_elastic"));
        dtos.add(new ElasticDataDTO("subOrderCode1"));
        String script3 = "if( ctx._source.containsKey('created_on') && ctx._source.containsKey('updated_on') ){"
                         + "def duration = groovy.time.TimeCategory.minus(new java.util.Date(ctx._source.updated_on), "
                         + "new java.util.Date(ctx._source.created_on));"
                         + "ctx._source.dateDiff = duration.days;"
                         + "}";
        elasticService.runScript(dtos, script3, null);
    }

    @Test
    public void testElastic_2() {
        List<ElasticDataDTO> dtos = new ArrayList<ElasticDataDTO>();
        ElasticDataDTO       dto  = new ElasticDataDTO("indexForSearch");
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.DESTINATION_STATE, "Delhi");
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.COURIER_GROUP, "CG");
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.COURIER_TYPE, "CT");
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.DESTINATION_CITY, "Delhi");
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.DESTINATION_TIER, "MTO");
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.LANE_TYPE, "SameCity");
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.MTO, "MTO");
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.SHIPPING_MODE, "air");
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.SOURCE_CITY, "Delhi");
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.SOI_PDC_STATUS_DATE, new Date());
        dto.getDataValueMap().put(SubOrderDetailElasticColumn.SOI_PDI_STATUS_DATE, new Date());
        dtos.add(dto);
        elasticService.indexDocuments(dtos);
    }

}
