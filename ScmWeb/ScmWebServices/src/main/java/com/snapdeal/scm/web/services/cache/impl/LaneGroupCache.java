package com.snapdeal.scm.web.services.cache.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.snapdeal.scm.cache.CacheKey;
import com.snapdeal.scm.cache.ICache;
import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.scm.cache.Cache;
import com.snapdeal.scm.mongo.mao.repository.LaneGroupRepository;
import com.snapdeal.scm.mongo.doc.LaneGroup;

/**
 * Created by siddhant, prateek on 3/3/16.
 */
@Cache(cacheKey= CacheKey.LANE_GROUP, MIN_REPEAT_TIME_IN_HOUR=24)
public class LaneGroupCache implements ICache {

    @Autowired
    LaneGroupRepository laneGroupRepository;
    Map<String, List<String>> laneGroupLanesMap = new HashMap<>();
    List<String> lanegroups = new ArrayList<String>();

    public Map<String, List<String>> getLaneGroupLanesMap() {
        return laneGroupLanesMap;
    }

    public void setLaneGroupLanesMap(Map<String, List<String>> laneGroupLanesMap) {
        this.laneGroupLanesMap = laneGroupLanesMap;
    }

    private void addLaneGroupLanes(String laneGroup, Set<String> lanes) {
    	List<String> list = new ArrayList<String>();
    	list.addAll(lanes);
        laneGroupLanesMap.put(laneGroup,list);
        lanegroups.add(laneGroup);
    }
    
    public List<String> getLanegroups() {
		return lanegroups;
	}

	public void setLanegroups(List<String> lanegroups) {
		this.lanegroups = lanegroups;
	}

	@Override
    public void load() {
        for(LaneGroup laneGroup : laneGroupRepository.findAll()) {
            if(laneGroup.getLaneGroup() != null && laneGroup.getLanes() != null) {
                addLaneGroupLanes(laneGroup.getLaneGroup(),laneGroup.getLanes());
            }
        }
    }
}