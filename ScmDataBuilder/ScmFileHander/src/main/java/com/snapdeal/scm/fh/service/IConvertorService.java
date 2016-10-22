/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.fh.service;

import java.text.ParseException;

/**
 * @version 1.0, 18-Feb-2016
 * @author ashwini
 */
public interface IConvertorService {

    public <T> T convertToObject(Class<T> clazz, String value) throws ParseException;

}
