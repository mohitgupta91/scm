package com.snapdeal.scm.processor.impl;

import com.snapdeal.scm.core.elastic.dto.ElasticDataDTO;
import com.snapdeal.scm.core.elastic.dto.ElasticScriptDataDTO;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.processor.IElasticService;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService.ScriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * ElasticServiceImpl :elastic index processor
 *
 * @author pranav, prateek
 */
@Service
public class ElasticServiceImpl implements IElasticService {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticServiceImpl.class);

    @Autowired
    @Qualifier("nodeClient")
    private Client client;

    @Value("${scm.elastic.index.name}")
    private String indexName;

    @Value("${scm.elastic.index.name.type}")
    private String indexType;

    @Value("${scm.elastic.retryonconflict}")
    private int retryOnConflict;

    @Override
    public void indexDocuments(Collection<ElasticDataDTO> dtos) {
        if (null == dtos || dtos.size() == 0) {
            return;
        }
        BulkProcessor bulkProcessor = createBulkProcessor();
        LOG.debug("ElasticDataDTO : {}", dtos);
        for (ElasticDataDTO dto : dtos) {
            try {
                Map<String, Object> dataMap = new HashMap<String, Object>();
                dto.getDataValueMap().forEach((schemaKey, value) -> dataMap.put(schemaKey.getColumnName(), value));
                dataMap.put(SubOrderDetailElasticColumn.UPDATED_ON.getColumnName(), new Date());
                Map<String, Object> indexMap = new HashMap<String, Object>();
                indexMap.putAll(dataMap);
                indexMap.put(SubOrderDetailElasticColumn.CREATED_ON.getColumnName(), new Date());
                IndexRequest indexReq = new IndexRequest(indexName, indexType, dto.getSubOrderCode());
                //indexReq.ttl("50s");
                indexReq.source(indexMap);
                UpdateRequest updatReq = new UpdateRequest(indexName, indexType, dto.getSubOrderCode());
                updatReq.doc(dataMap);
                updatReq.upsert(indexReq);
                updatReq.retryOnConflict(retryOnConflict);
                bulkProcessor.add(updatReq);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        bulkProcessor.close();
    }

    @Override
    public void runScript(List<ElasticDataDTO> dtos, String script, Map<String, Object> params) {
        if (null == dtos || dtos.size() == 0) {
        }
        BulkProcessor bulkProcessor = createBulkProcessor();
        LOG.debug("ElasticDataDTO : {}", dtos);
        for (ElasticDataDTO dto : dtos) {
            try {
                UpdateRequest updatReq = new UpdateRequest(indexName, indexType, dto.getSubOrderCode());
                if (null == params) {
                    params = new HashMap<String, Object>();
                }
                Script scriptObj = new Script(script, ScriptType.INLINE, null, params);
                updatReq.script(scriptObj);
                updatReq.retryOnConflict(retryOnConflict);
                bulkProcessor.add(updatReq);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        bulkProcessor.close();
    }

    @Override
    public void runDynamicScript(Collection<ElasticScriptDataDTO> elasticScriptDataDTOs) {
        if (CollectionUtils.isEmpty(elasticScriptDataDTOs)) {
            return;
        }
        BulkProcessor bulkProcessor = createBulkProcessor();
        LOG.debug("ElasticScriptDataDTO : {}", elasticScriptDataDTOs);
        for (ElasticScriptDataDTO dto : elasticScriptDataDTOs) {
            try {
                Map<String, Object> dataMap = new HashMap<>();
                dto.getDataValueMap().forEach((schemaKey, value) -> dataMap.put(schemaKey.getColumnName(), value));
                dataMap.put(SubOrderDetailElasticColumn.UPDATED_ON.getColumnName(), new Date());
                dataMap.put(SubOrderDetailElasticColumn.CREATED_ON.getColumnName(), new Date());

                IndexRequest indexReq = new IndexRequest(indexName, indexType, dto.getSubOrderCode());
                indexReq.source(dataMap);

                UpdateRequest       updatReq = new UpdateRequest(indexName, indexType, dto.getSubOrderCode());
                Map<String, Object> params   = dto.getParams();
                if (null == params) {
                    params = new HashMap<String, Object>();
                }
                params.put("updated", new Date());
                Script scriptObj = new Script(dto.getConcatedScripts() + ";ctx._source.updated_on=updated", ScriptType.INLINE, null, params);
                updatReq.script(scriptObj);
                updatReq.retryOnConflict(retryOnConflict);
                updatReq.upsert(indexReq);
                bulkProcessor.add(updatReq);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        bulkProcessor.close();
    }

    @Override
    public Object getFieldValueBySuborderId(String id, String fieldName) {
        GetResponse response = client.prepareGet(indexName, indexType, id).setFields(fieldName).get();
        if (response != null && response.isExists()) {
            if (response.getField(fieldName) != null) {
                return response.getField(fieldName).getValue();
            }
        }
        return null;
    }

    private BulkProcessor createBulkProcessor() {
        return BulkProcessor.builder(
                client, new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        throw new RuntimeException(failure);
                    }

                })
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .build();
    }
}
