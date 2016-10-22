package com.snapdeal.scm.mongo.doc;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.mongodb.core.mapping.Document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;

/**
 * Alert : Hold records of all alerts created from UI.
 * 
 * @author pranav
 */
@Document(collection = "alert_instance")
public class AlertInstance extends MongoDocument {

    @NotNull
    private Long         alertInstanceId;
    @NotNull
    private Long         alertId;
    private String       alertTitle;
    private String       alertInstanceTitle;
    @NotNull
    private String       groupLogicName;
    private String       operator;
    private float        value;
    private int          historicalDateRangeStart;
    private int          historicalDateRangeEnd;
    private int          currentDateRangeStart;
    private int          currentDateRangeEnd;
    private String       emailId;
    private int          ruleTime;
    private List<String> fileHeader;
    @CreatedBy
    private String       createdBy;
    private String       updatedBy;

    public AlertInstance(long alertInstanceId) {
        this.alertInstanceId = alertInstanceId;
    }

    public Long getAlertInstanceId() {
        return alertInstanceId;
    }

    public void setAlertInstanceId(Long alertInstanceId) {
        this.alertInstanceId = alertInstanceId;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    public String getAlertInstanceTitle() {
        return alertInstanceTitle;
    }

    public void setAlertInstanceTitle(String alertInstanceTitle) {
        this.alertInstanceTitle = alertInstanceTitle;
    }

    public String getGroupLogicName() {
        return groupLogicName;
    }

    public void setGroupLogicName(String groupLogicName) {
        this.groupLogicName = groupLogicName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getHistoricalDateRangeStart() {
        return historicalDateRangeStart;
    }

    public void setHistoricalDateRangeStart(int historicalDateRangeStart) {
        this.historicalDateRangeStart = historicalDateRangeStart;
    }

    public int getHistoricalDateRangeEnd() {
        return historicalDateRangeEnd;
    }

    public void setHistoricalDateRangeEnd(int historicalDateRangeEnd) {
        this.historicalDateRangeEnd = historicalDateRangeEnd;
    }

    public int getCurrentDateRangeStart() {
        return currentDateRangeStart;
    }

    public void setCurrentDateRangeStart(int currentDateRangeStart) {
        this.currentDateRangeStart = currentDateRangeStart;
    }

    public int getCurrentDateRangeEnd() {
        return currentDateRangeEnd;
    }

    public void setCurrentDateRangeEnd(int currentDateRangeEnd) {
        this.currentDateRangeEnd = currentDateRangeEnd;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getRuleTime() {
        return ruleTime;
    }

    public void setRuleTime(int ruleTime) {
        this.ruleTime = ruleTime;
    }

    public List<String> getFileHeader() {
        return fileHeader;
    }

    public void setFileHeader(List<String> fileHeader) {
        this.fileHeader = fileHeader;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AlertInstance [alertInstanceId=");
        builder.append(alertInstanceId);
        builder.append(", alertId=");
        builder.append(alertId);
        builder.append(", alertTitle=");
        builder.append(alertTitle);
        builder.append(", alertInstanceTitle=");
        builder.append(alertInstanceTitle);
        builder.append(", groupLogicName=");
        builder.append(groupLogicName);
        builder.append(", operator=");
        builder.append(operator);
        builder.append(", value=");
        builder.append(value);
        builder.append(", historicalDateRangeStart=");
        builder.append(historicalDateRangeStart);
        builder.append(", historicalDateRangeEnd=");
        builder.append(historicalDateRangeEnd);
        builder.append(", currentDateRangeStart=");
        builder.append(currentDateRangeStart);
        builder.append(", currentDateRangeEnd=");
        builder.append(currentDateRangeEnd);
        builder.append(", emailId=");
        builder.append(emailId);
        builder.append(", ruleTime=");
        builder.append(ruleTime);
        builder.append(", fileHeader=");
        builder.append(fileHeader);
        builder.append(", createdBy=");
        builder.append(createdBy);
        builder.append(", updatedBy=");
        builder.append(updatedBy);
        builder.append("]");
        return builder.toString();
    }

}
