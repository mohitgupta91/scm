package com.snapdeal.scm.web.core.mao;

import com.snapdeal.scm.web.core.mongo.documents.FilterOptions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * Created by sahil on 25/2/16.
 */
@Service
public interface FilterOptionsRepository extends MongoRepository<FilterOptions, String> {
    public FilterOptions findByFilterKey(String filterKey);
}
