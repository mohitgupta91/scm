/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.dto;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0, 25-Feb-2016
 * @author ashwini
 */
public class FilterDataDTO {

    private Map<String, List<String>> filterDataMap;
    private List<String>              filterDataList;
    private String                    url;

    public Map<String, List<String>> getFilterDataMap() {
        return filterDataMap;
    }

    public void setFilterDataMap(Map<String, List<String>> filterDataMap) {
        this.filterDataMap = filterDataMap;
    }

    public List<String> getFilterDataList() {
        return filterDataList;
    }

    public void setFilterDataList(List<String> filterDataList) {
        this.filterDataList = filterDataList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FilterDataDTO [filterDataMap=").append(filterDataMap).append(", filterDataList=").append(filterDataList).append(", url=").append(url).append("]");
        return builder.toString();
    }

}
