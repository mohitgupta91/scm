package com.snapdeal.scm.mongo.mao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.mongo.doc.LaneGroup;

/**
 * Created by siddhant, prateek on 2/3/16.
 */
public interface LaneGroupRepository extends MongoRepository<LaneGroup,String> {

     LaneGroup findByLaneGroup(String laneGroup);
}
