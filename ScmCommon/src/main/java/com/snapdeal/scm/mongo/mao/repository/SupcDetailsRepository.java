package com.snapdeal.scm.mongo.mao.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.snapdeal.scm.mongo.doc.SupcDetails;

/**
 * 
 * @author prateek
 *
 */
public interface SupcDetailsRepository extends MongoRepository<SupcDetails, String>, SupcDetailsRepositoryCustom{

	public SupcDetails findBySupc(String supc);

	@Query(value = "{ 'supc' : {$in : ?0 } }")
	public List<SupcDetails> findByAllSupc(Set<String> supcs);

}
