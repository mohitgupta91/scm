package com.snapdeal.scm.mongo.mao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.snapdeal.scm.mongo.doc.MetroCity;

/**
 * 
 * @author prateek
 *
 */
public interface MetroCitiesRepository extends MongoRepository<MetroCity,String> {

	MetroCity findByCity(String cityState);

}