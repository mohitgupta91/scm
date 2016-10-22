/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.core.exception;

/**
 * @version 1.0, 23-Apr-2016
 * @author ashwini.kumar
 */
public class FileUploadException extends Exception {

    private String message;

    public FileUploadException(String message) {
        this.message = message;
    }
}
