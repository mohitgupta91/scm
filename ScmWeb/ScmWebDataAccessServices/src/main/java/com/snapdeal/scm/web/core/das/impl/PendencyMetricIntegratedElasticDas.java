package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.cache.impl.*;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.web.core.das.ILastMilePieChartElasticDas;
import com.snapdeal.scm.web.core.enums.FilterKey;
import com.snapdeal.scm.web.core.enums.Stage;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.*;

/**
 * @author chitransh
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G8")
public class PendencyMetricIntegratedElasticDas extends PendencyChartElasticDas implements ILastMilePieChartElasticDas {

    public PendencyMetricIntegratedElasticDas() {
        super("G8", 5, SP_AWB_UPLOADED_STATUS_DATE,
              // description of the various legs
              ImmutableMap.<Integer, String>builder()
                      .put(1, "shipped_but_not_connected")
                      .put(2, "connected_but_not_reached_destination")
                      .put(3, "reached_destination_but_not_out_for_delivery")
                      .put(4, "out_for_delivery_but_not_attempted")
                      .put(5, "attempted_but_not_delivered_rto")
                      .build(),

              // column that should exist for a given leg
              ImmutableMap.<Integer, SubOrderDetailElasticColumn>builder()
                      .put(1, SP_AWB_UPLOADED_STATUS_DATE)
                      .put(2, TP_CONNECTED_STATUS_DATE)
                      .put(3, TP_RAD_STATUS_DATE)
                      .put(4, TP_OFD_STATUS_DATE)
                      .put(5, ATTEMPTED_STATUS_DATE)
                      .build(),

              // columns that should not exist for a given leg
              ImmutableMultimap.<Integer, SubOrderDetailElasticColumn>builder()
                      .put(1, TP_CONNECTED_STATUS_DATE)
                      .put(2, TP_RAD_STATUS_DATE)
                      .put(3, TP_OFD_STATUS_DATE)
                      .put(4, ATTEMPTED_STATUS_DATE)
                      .putAll(5, CLOSED_DATE, RTO_DATE)
                      .build(),

              // columns on which the aggregation should take place for a given leg
              ImmutableMultimap.<Integer, SubOrderDetailElasticColumn>builder()
                      .putAll(1, COURIER_TYPE, COURIER_GROUP, SOURCE_STATE, SOURCE_CITY)
                      .putAll(2, COURIER_TYPE, COURIER_GROUP, LANE_TYPE, SOURCE_STATE, DESTINATION_STATE, SOURCE_CITY, DESTINATION_CITY)
                      .putAll(3, COURIER_TYPE, COURIER_GROUP, DESTINATION_STATE, DESTINATION_CITY)
                      .putAll(4, COURIER_TYPE, COURIER_GROUP, DESTINATION_STATE, DESTINATION_CITY)
                      .putAll(5, COURIER_TYPE, COURIER_GROUP, DESTINATION_STATE, DESTINATION_CITY)
                      .build(),

              // caches corresponding to a given leg
              ImmutableMap.<Integer, Class<? extends PriorityMetricsCache>>builder()
                      .put(1, ShippedNotConMetricsCache.class)
                      .put(2, ConNotReachedMetricsCache.class)
                      .put(3, ReachedNotOFDMetricsCache.class)
                      .put(4, OfdNotAttemptMetricsCache.class)
                      .put(5, AttemptNotDelMetricsCache.class)
                      .build());
    }

    @Override
    protected BoolQueryBuilder additionalFilter(BoolQueryBuilder query) {

        List<String> integratedCouriers = CacheManager.getInstance().getCache(CourierGroupCache.class).getListOfIntegrated();
        if (integratedCouriers == null) {
            integratedCouriers = Collections.emptyList();
        }
        return query.must(QueryBuilders.termsQuery(COURIER_GROUP.getColumnName(), integratedCouriers));
    }

    @Override
    protected SubOrderDetailElasticColumn groupByColumnForLevelTwoOnwards(ElasticFilter elasticFilter) {

        if (Stage.FOUR.equals(elasticFilter.getStage())) {
            return LANE;
        }
        FilterKey filterKey = groupByFilterFromChartFilter(elasticFilter);
        if (filterKey != null) {

            return filterKey.getSubOrderDetailElasticColumn();
        }
        return null;
    }
}
