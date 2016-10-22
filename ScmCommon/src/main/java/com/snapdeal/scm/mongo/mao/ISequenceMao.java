package com.snapdeal.scm.mongo.mao;

/**
 * SequenceDao
 * 
 * @author pranav
 *
 */
public interface ISequenceMao {
	long getNextSequenceId(String key);
}