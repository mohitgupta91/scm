/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.scm.web.core.mao.MetricDetailsRepository;
import com.snapdeal.scm.web.core.mongo.documents.FilterOptions;
import com.snapdeal.scm.web.core.mongo.documents.MetricDetails;
import com.snapdeal.scm.web.core.sro.FilterSRO;
import com.snapdeal.scm.web.core.utils.Constants;
import com.snapdeal.scm.web.core.utils.StringUtils;
import com.snapdeal.scm.web.dto.FilterDataDTO;
import com.snapdeal.scm.web.services.IFilterOptionService;
import com.snapdeal.scm.web.services.IFilterService;

/**
 * @version 1.0, 25-Feb-2016
 * @author ashwini, prateek
 */
@Service
public class FilterServiceImpl implements IFilterService {

    @Autowired
    private DynamicFilterServiceImpl filterFactory;

    @Autowired
    private IFilterOptionService foService;

    @Autowired
    MetricDetailsRepository metricDetailsRepository;

    @Override
    public List<FilterSRO> getFilterForMatricByid(String maticId) {
        MetricDetails filters = metricDetailsRepository.findByMetricID(maticId);

        List<FilterSRO> filterSROs = new ArrayList<FilterSRO>();
        if (null != filters) {
            filters.getFilterKey().forEach((filterKey, mandatory) -> {
                FilterSRO sro = convertToFilterSRO(filterKey);
                sro.setMandatory(mandatory);
                filterSROs.add(sro);
            });
        }
        return filterSROs;
    }

    @Override
    public FilterSRO getFilterByKey(String urlFilterKey) {
        FilterOptions filter = foService.getFiltersBykey(urlFilterKey);
        if (filter == null)
            return null;
        FilterSRO filterSRO = new FilterSRO();
        filterSRO.setName(urlFilterKey);
        filterSRO.setDependsOn(filter.getDependsOn());
        filterSRO.setType(filter.getType());
        filterSRO.setDefaults(filter.getDefaults());
        filterSRO.setRange(filter.getRange());
        FilterDataDTO dto = filterFactory.getFilterDataByUrlFilterKey(urlFilterKey);
        if (CollectionUtils.isNotEmpty(dto.getFilterDataList())) {
            filterSRO.setOptions(dto.getFilterDataList());
        } else if (MapUtils.isNotEmpty(dto.getFilterDataMap())) {
            filterSRO.setDependentOptions(dto.getFilterDataMap());
        }
        return filterSRO;
    }

    private FilterSRO convertToFilterSRO(String filterKey) {
        FilterOptions filter = foService.getFiltersBykey(filterKey);
        if (null != filter) {
            FilterSRO filterSRO = new FilterSRO();
            filterSRO.setName(filterKey);
            filterSRO.setDependsOn(filter.getDependsOn());
            filterSRO.setType(filter.getType());
            filterSRO.setDefaults(filter.getDefaults());
            filterSRO.setRange(filter.getRange());
            if (!filter.getType().equalsIgnoreCase(Constants.DATE)) {
                if (CollectionUtils.isNotEmpty(filter.getOptions())) {
                    filterSRO.setOptions(filter.getOptions());
                } else if (MapUtils.isNotEmpty(filter.getOptionMapping())) {
                    filterSRO.setDependentOptions(filter.getOptionMapping());
                } else {
                    FilterDataDTO dto = filterFactory.getFilterData(filterKey);
                    if (StringUtils.isNotEmpty(dto.getUrl())) {
                        filterSRO.setDataUrl(dto.getUrl());
                    } else if (CollectionUtils.isNotEmpty(dto.getFilterDataList())) {
                        filterSRO.setOptions(dto.getFilterDataList());
                    } else if (MapUtils.isNotEmpty(dto.getFilterDataMap())) {
                        filterSRO.setDependentOptions(dto.getFilterDataMap());
                    }
                }
            }
            return filterSRO;
        }
        return null;
    }
}
