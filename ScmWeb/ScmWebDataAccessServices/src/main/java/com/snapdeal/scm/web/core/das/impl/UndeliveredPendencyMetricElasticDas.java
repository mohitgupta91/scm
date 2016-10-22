package com.snapdeal.scm.web.core.das.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.snapdeal.scm.cache.impl.*;
import com.snapdeal.scm.core.elastic.dto.ElasticFilter;
import com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn;
import com.snapdeal.scm.web.core.das.ILastMilePieChartElasticDas;
import com.snapdeal.scm.web.core.enums.FilterKey;
import com.snapdeal.scm.web.core.enums.Stage;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Service;

import static com.snapdeal.scm.core.elastic.dto.SubOrderDetailElasticColumn.*;

/**
 * @author chitransh
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Service("G10")
public class UndeliveredPendencyMetricElasticDas extends PendencyChartElasticDas implements ILastMilePieChartElasticDas {

    public UndeliveredPendencyMetricElasticDas() {
        super("G10", 4, ATTEMPTED_STATUS_DATE,
              // description of the various legs
              ImmutableMap.<Integer, String>builder()
                      .put(1, "ud1_but_not_ud2_del_rto")
                      .put(2, "ud2_but_not_ud3_del_rto")
                      .put(3, "ud3_but_not_ud4_del_rto")
                      .put(4, "ud4_but_not__del_rto")
                      .build(),

              // column that should exist for a given leg
              ImmutableMap.<Integer, SubOrderDetailElasticColumn>builder()
                      .put(1, TP_FIRST_UDL_STATUS_DATE)
                      .put(2, TP_SECOND_UDL_STATUS_DATE)
                      .put(3, TP_THIRD_UDL_STATUS_DATE)
                      .put(4, TP_FOURTH_UDL_STATUS_DATE)
                      .build(),

              // columns that should not exist for a given leg
              ImmutableMultimap.<Integer, SubOrderDetailElasticColumn>builder()
                      .putAll(1, ImmutableList.of(TP_SECOND_UDL_STATUS_DATE, TP_DEL_STATUS_DATE, RTO_DATE))
                      .putAll(2, ImmutableList.of(TP_THIRD_UDL_STATUS_DATE, TP_DEL_STATUS_DATE, RTO_DATE))
                      .putAll(3, ImmutableList.of(TP_FOURTH_UDL_STATUS_DATE, TP_DEL_STATUS_DATE, RTO_DATE))
                      .putAll(4, ImmutableList.of(TP_DEL_STATUS_DATE, RTO_DATE))
                      .build(),

              // columns on which the aggregation should take place for a given leg
              ImmutableMultimap.<Integer, SubOrderDetailElasticColumn>builder()
                      .putAll(1, COURIER_TYPE, COURIER_GROUP, SOURCE_STATE, SOURCE_CITY)
                      .putAll(2, COURIER_TYPE, COURIER_GROUP, LANE_TYPE, SOURCE_STATE, DESTINATION_STATE, SOURCE_CITY, DESTINATION_CITY)
                      .putAll(3, COURIER_TYPE, COURIER_GROUP, DESTINATION_STATE, DESTINATION_CITY)
                      .putAll(4, COURIER_TYPE, COURIER_GROUP, DESTINATION_STATE, DESTINATION_CITY)
                      .build(),


              // caches corresponding to a given leg
              ImmutableMap.<Integer, Class<? extends PriorityMetricsCache>>builder()
                      .put(1, UD1NotUD2MetricsCache.class)
                      .put(2, UD2NotUD3MetricsCache.class)
                      .put(3, UD3NotUD4MetricsCache.class)
                      .put(4, UD4NotDelMetricsCache.class)
                      .build());
    }

    @Override
    protected BoolQueryBuilder additionalFilter(BoolQueryBuilder query) {
        return query;
    }

    @Override
    protected SubOrderDetailElasticColumn groupByColumnForLevelTwoOnwards(ElasticFilter elasticFilter) {

        if (Stage.THREE.equals(elasticFilter.getStage())) {
            return DESTINATION_CITY;
        }
        FilterKey filterKey = groupByFilterFromChartFilter(elasticFilter);
        if (filterKey != null) {

            return filterKey.getSubOrderDetailElasticColumn();
        }
        return null;
    }
}
