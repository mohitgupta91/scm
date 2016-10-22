/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.mongo.doc;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0, 20-Apr-2016
 * @author ashwini
 */
public class FileValuesDTO {
    List<String>              header;
    List<Map<String, String>> headerDataMap;

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<Map<String, String>> getHeaderDataMap() {
        return headerDataMap;
    }

    public void setHeaderDataMap(List<Map<String, String>> headerDataMap) {
        this.headerDataMap = headerDataMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FileValues [header=");
        builder.append(header);
        builder.append(", headerDataMap=");
        builder.append(headerDataMap);
        builder.append("]");
        return builder.toString();
    }

}
