package com.snapdeal.scm.core.dto.impl;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.NotBlank;

import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;

/**
 * Created by siddhant, prateek  on 2/3/16.
 */
public class LaneGroupDTO extends AbstractStandardDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotBlank
    private String laneGroup;
	private Set<String> lanes = new HashSet<>();

    public LaneGroupDTO() {
	}

	public LaneGroupDTO(String laneGroup, Set<String> lanes) {
		this.laneGroup = laneGroup;
		this.lanes = lanes;
	}

	public String getLaneGroup() {
		return laneGroup;
	}

	public void setLaneGroup(String laneGroup) {
		this.laneGroup = laneGroup;
	}

	public Set<String> getLanes() {
		return lanes;
	}

	public void setLanes(Set<String> lanes) {
		this.lanes = lanes;
	}

	@Override
    public QueryType getQueryType() {
        return QueryType.LANE_GROUP;
    }

	@Override
	public String toString() {
		return "LaneGroupDTO [laneGroup=" + laneGroup + ", lanes=" + lanes
				+ "]";
	}
	
	@Override
	public boolean validateDTO() {
		if(StringUtils.isEmpty(laneGroup))
			return false;
		return true;
	}
}