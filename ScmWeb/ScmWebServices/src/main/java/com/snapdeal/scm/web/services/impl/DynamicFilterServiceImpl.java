/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.snapdeal.scm.cache.impl.FulfillmentProviderCache;
import com.snapdeal.scm.cache.impl.SuperCategoryCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.scm.web.core.enums.FilterKey;
import com.snapdeal.scm.web.core.utils.Constants;
import com.snapdeal.scm.web.dto.FilterDataDTO;
import com.snapdeal.scm.web.services.IDynamicFilterService;
import com.snapdeal.scm.cache.CacheManager;
import com.snapdeal.scm.cache.impl.CourierGroupCache;
import com.snapdeal.scm.web.services.cache.impl.LaneGroupCache;
import com.snapdeal.scm.cache.impl.PincodeMasterCache;

/**
 * @version 1.0, 25-Feb-2016
 * @author ashwini, prateek
 */
@Service("DynamicFilterServiceImpl")
public class DynamicFilterServiceImpl implements IDynamicFilterService {

	@Autowired
	List<IDynamicFilter> dynamicFilter;

	Map<String, IDynamicFilter> dynamicFiltertMap = new HashMap<String, IDynamicFilter>();

	@PostConstruct
	public void constructDynaminFilterMap() {
		dynamicFilter.forEach(filter -> dynamicFiltertMap.put(filter.getKey(), filter));
	}

	@Override
	public FilterDataDTO getFilterData(String filterKey) {
		return getFilterDataDTO(filterKey, false);
	}

	@Override
	public FilterDataDTO getFilterDataByUrlFilterKey(String urlFilterKey) {
		return getFilterDataDTO(urlFilterKey, true);
	}

	private FilterDataDTO getFilterDataDTO(String filterKey, boolean isUrlRequest) {
		return Optional.ofNullable(dynamicFiltertMap.get(filterKey)).map(filter -> filter.getFilterDataDTO(isUrlRequest)).orElse(null);
	}
}

interface IDynamicFilter {
	FilterDataDTO getFilterDataDTO(boolean isUrlRequest);

	String getKey();

	default String getUrl() {
		return Constants.FILTER_URL + getKey();
	}
}

@Service("sourceCityFilter")
class SourceCityFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		if (!isUrlRequest)
			dto.setUrl(getUrl());
		else {
			Map<String, List<String>> filterDataMap = CacheManager.getInstance().getCache(PincodeMasterCache.class).getMapOfCities();
			dto.setFilterDataMap(filterDataMap);
		}
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.SOURCE_CITY.getFilterKey();
	}
}

@Service("destinationCityFilter")
class DestinationCityFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		if (!isUrlRequest)
			dto.setUrl(getUrl());
		else {
			Map<String, List<String>> filterDataMap = CacheManager.getInstance().getCache(PincodeMasterCache.class).getMapOfCities();
			dto.setFilterDataMap(filterDataMap);
		}
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.DESTINATION_CITY.getFilterKey();
	}
}

@Service("sourceStateFilter")
class SourceStateFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		List<String> filterDataList = CacheManager.getInstance().getCache(PincodeMasterCache.class).getListOfStates();
		dto.setFilterDataList(filterDataList);
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.SOURCE_STATE.getFilterKey();
	}
}

@Service("destinationStateFilter")
class DestinationStateFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		List<String> filterDataList = CacheManager.getInstance().getCache(PincodeMasterCache.class).getListOfStates();
		dto.setFilterDataList(filterDataList);
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.DESTINATION_STATE.getFilterKey();
	}
}

@Service("CourierGroupFilter")
class CourierGroupFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		List<String> filterDataList = CacheManager.getInstance().getCache(CourierGroupCache.class).getListOfCourierGroup();
		dto.setFilterDataList(filterDataList);
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.COURIER_GROUP.getFilterKey();
	}
}

@Service("CourierTypeFilter")
class CourierTypeFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		List<String> filterDataList = CacheManager.getInstance().getCache(CourierGroupCache.class).getListOfCourierType();
		dto.setFilterDataList(filterDataList);
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.COURIER_TYPE.getFilterKey();
	}
}

@Service("DestinationRegionFilter")
class DestinationRegionFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		List<String> filterDataList = CacheManager.getInstance().getCache(PincodeMasterCache.class).getListOfRegions();
		dto.setFilterDataList(filterDataList);
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.DESTINATION_REGION.getFilterKey();
	}
}

@Service("DestinationTierFilter")
class DestinationTierFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		List<String> filterDataList = CacheManager.getInstance().getCache(PincodeMasterCache.class).getListOfTiers();
		dto.setFilterDataList(filterDataList);
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.DESTINATION_TIER.getFilterKey();
	}
}

@Service("laneFilter")
class LaneFilter implements IDynamicFilter {
	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		if (!isUrlRequest)
			dto.setUrl(getUrl());
		else {
			Map<String, List<String>> laneGroupLanesMap = CacheManager.getInstance().getCache(LaneGroupCache.class).getLaneGroupLanesMap();
			dto.setFilterDataMap(laneGroupLanesMap);
		}
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.LANE.getFilterKey();
	}
}

@Service("laneGroupFilter")
class LaneGroupFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		List<String> laneGroups = CacheManager.getInstance().getCache(LaneGroupCache.class).getLanegroups();
		dto.setFilterDataList(laneGroups);
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.LANE_GROUP.getFilterKey();
	}
}

@Service("SuperCategoryFilter")
class SuperCategoryFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		List<String> filterDataList = CacheManager.getInstance().getCache(SuperCategoryCache.class).getAllSuperCategories();
		dto.setFilterDataList(filterDataList);
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.SUPER_CATEGORY.getFilterKey();
	}
}

@Service("CourierCodeFilter")
class CourierCodeFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		List<String> filterDataList = CacheManager.getInstance().getCache(CourierGroupCache.class).getAllCourierCode();
		dto.setFilterDataList(filterDataList);
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.COURIER_CODE.getFilterKey();
	}
}

@Service("FulfillmentCenterFilter")
class FulfillmentCenterFilter implements IDynamicFilter {

	@Override
	public FilterDataDTO getFilterDataDTO(boolean isUrlRequest) {
		FilterDataDTO dto = new FilterDataDTO();
		List<String> filterDataList = CacheManager.getInstance().getCache(FulfillmentProviderCache.class).getAllFulfillmentCenter();
		dto.setFilterDataList(filterDataList);
		return dto;
	}

	@Override
	public String getKey() {
		return FilterKey.FULFILLMENT_CENTER.getFilterKey();
	}
}