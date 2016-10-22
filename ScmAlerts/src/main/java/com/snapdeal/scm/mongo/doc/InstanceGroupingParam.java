/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.mongo.doc;

import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;

/**
 * @version 1.0, 23-Apr-2016
 * @author ashwini.kumar
 */
@Document(collection="alert_instance_grouping_param")
public class InstanceGroupingParam extends MongoDocument{

    private Long                rowId;
    private Long                alertInstanceId;
    private Map<String, String> groupingParam;

    public InstanceGroupingParam() {
    }

    public InstanceGroupingParam(Long alertInstanceId, Map<String, String> groupingParam) {
        this.alertInstanceId = alertInstanceId;
        this.groupingParam = groupingParam;
    }

    public InstanceGroupingParam(Long rowId, Long alertInstanceId, Map<String, String> groupingParam) {
        super();
        this.rowId = rowId;
        this.alertInstanceId = alertInstanceId;
        this.groupingParam = groupingParam;
    }

    public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	public Long getAlertInstanceId() {
        return alertInstanceId;
    }

    public void setAlertInstanceId(Long alertInstanceId) {
        this.alertInstanceId = alertInstanceId;
    }

    public Map<String, String> getGroupingParam() {
        return groupingParam;
    }

    public void setGroupingParam(Map<String, String> groupingParam) {
        this.groupingParam = groupingParam;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InstanceGroupingParam [instanceParamId=");
        builder.append(rowId);
        builder.append(", alertInstanceId=");
        builder.append(alertInstanceId);
        builder.append(", groupingParam=");
        builder.append(groupingParam);
        builder.append("]");
        return builder.toString();
    }
}
