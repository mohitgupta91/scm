package com.snapdeal.scm.mongo.doc;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;

/**
 * Created by siddhant, prateek on 2/3/16.
 */
@Document(collection = "lane_group")
public class LaneGroup extends MongoDocument {

    private String laneGroup;
    private Set<String> lanes = new HashSet<String>();

    public LaneGroup() {
    }

    public LaneGroup(String laneGroup) {
        this.laneGroup = laneGroup;
    }

    public LaneGroup(String laneGroup, Set<String> lanes) {
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

    public void setLane(Set<String> lane) {
        this.lanes = lane;
    }

    @Override
    public String toString() {
        return "LaneGroup{" +
                "laneKey='" + laneGroup + '\'' +
                ", lanes=" + lanes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaneGroup laneGroup = (LaneGroup) o;
        if (laneGroup != null ? !laneGroup.equals(laneGroup.laneGroup) : laneGroup.laneGroup != null) return false;
        return !(lanes != null ? !lanes.equals(laneGroup.lanes) : laneGroup.lanes != null);
    }

    @Override
    public int hashCode() {
        int result = laneGroup != null ? laneGroup.hashCode() : 0;
        result = 31 * result + (lanes != null ? lanes.hashCode() : 0);
        return result;
    }
}