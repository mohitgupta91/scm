/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.fh.service.impl;

import java.text.ParseException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.snapdeal.scm.core.utils.StringUtils;
import com.snapdeal.scm.fh.service.IConvertorService;

/**
 * @version 1.0, 18-Feb-2016
 * @author ashwini
 */
@Service
public class ConvertorService implements IConvertorService {

    @Value("${allowed.date.formats}")
    private String          allowedDateFormats;
    private static String[] ALLOWED_DATE_FORMATS;

    @PostConstruct
    void initialize() {
        ALLOWED_DATE_FORMATS = (String[]) StringUtils.split(allowedDateFormats).toArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convertToObject(Class<T> clazz, String value) throws ParseException {
        if (StringUtils.isEmpty(value) || StringUtils.NULL.equalsIgnoreCase(value)) {
            return null;
        }
        if (clazz.equals(Integer.class)) {
            return (T) Integer.valueOf(value);
        }
        if (clazz.equals(Date.class)) {
            try {
                return (T) new Date(Long.valueOf(value));
            } catch (NumberFormatException ne) {
                return (T) DateUtils.parseDate(value, ALLOWED_DATE_FORMATS);
            }
        }
        if (clazz.equals(String.class)) {
            return (T) value;
        }
        if (clazz.equals(Float.class)) {
            return (T) Float.valueOf(value);
        }
        if (clazz.equals(Double.class)) {
            return (T) Double.valueOf(value);
        }
        if (clazz.equals(Boolean.class)) {
            if (value.equals("1") || value.equalsIgnoreCase("true")) {
                return (T) Boolean.TRUE;
            } else if (value.equals("0") || value.equalsIgnoreCase("false")) {
                return (T) Boolean.FALSE;
            }
        }
        throw new ParseException("unable to parse string [" + value + "] to class " + clazz, 0);
    }
}
