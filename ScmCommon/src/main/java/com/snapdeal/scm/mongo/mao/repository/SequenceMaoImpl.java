package com.snapdeal.scm.mongo.mao.repository;

import com.snapdeal.scm.mongo.mao.ISequenceMao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.snapdeal.scm.mongo.doc.SequenceId;
/**
 * SequenceMaoImpl : Sequence Dao
 * @author pranav
 *
 */
@Repository
public class SequenceMaoImpl implements ISequenceMao {

	@Autowired
	private MongoOperations mongoOperation;

	@Override
	public long getNextSequenceId(String key){
		Query query = new Query(Criteria.where("_id").is(key));
		Update update = new Update();
		update.inc("seq", 1);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		SequenceId seqId = 
				mongoOperation.findAndModify(query, update, options, SequenceId.class);
		if (seqId == null) {
			saveNewSequence(key, 100l);
			return 100;
		}
		return seqId.getSeq();
	}

	private void saveNewSequence(String key, long seq){
		SequenceId seqId = new SequenceId(key, seq);
		mongoOperation.save(seqId);
	}

}
