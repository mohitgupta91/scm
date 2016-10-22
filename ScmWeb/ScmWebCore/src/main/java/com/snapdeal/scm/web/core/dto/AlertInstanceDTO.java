package com.snapdeal.scm.web.core.dto;

import java.util.List;

import com.snapdeal.scm.utils.DateUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snapdeal.scm.mongo.doc.AlertInstance;

/**
 * AlertDTO : Alert Form Data
 *
 * @author pranav, Mohit, Ashwini
 */
public class AlertInstanceDTO {

    private Long alertInstanceId;
    private Long alertId;
    private String alertTitle;
    private String alertInstanceTitle;
    private String groupLogicName;
    private String operator;
    private Float value;
    private Integer historicalDateRangeStart;
    private Integer historicalDateRangeEnd;
    private Integer currentDateRangeStart;
    private Integer currentDateRangeEnd;
    private String emailId;
    private Integer ruleTime;
    private List<String> fileHeader;
    @JsonIgnore
    private MultipartFile file;
    @CreatedBy
    private String createdBy;
    private String updatedBy;
    private String createdOn;
    private String updatedOn;

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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public AlertInstanceDTO() {

    }

    public AlertInstanceDTO(AlertInstance alert) {
        this.setAlertInstanceId(alert.getAlertInstanceId());
        this.setAlertId(alert.getAlertId());
        this.currentDateRangeEnd = alert.getCurrentDateRangeEnd();
        this.currentDateRangeStart = alert.getCurrentDateRangeStart();
        this.emailId = alert.getEmailId();
        this.groupLogicName = alert.getGroupLogicName();
        this.historicalDateRangeEnd = alert.getHistoricalDateRangeEnd();
        this.historicalDateRangeStart = alert.getHistoricalDateRangeStart();
        this.operator = alert.getOperator();
        this.ruleTime = alert.getRuleTime();
        this.setValue(alert.getValue());
        this.createdBy = alert.getCreatedBy();
        this.updatedBy = alert.getUpdatedBy();
        this.setAlertTitle(alert.getAlertTitle());
        this.alertInstanceTitle = alert.getAlertInstanceTitle();
        this.createdOn = DateUtils.convertDateToString(alert.getCreated());
        this.updatedOn = DateUtils.convertDateToString(alert.getUpdated());
        this.createdBy = alert.getCreatedBy();
        this.updatedBy = alert.getUpdatedBy();
        this.fileHeader = alert.getFileHeader();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Integer getRuleTime() {
        return ruleTime;
    }

    public void setRuleTime(Integer ruleTime) {
        this.ruleTime = ruleTime;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getGroupLogicName() {
        return groupLogicName;
    }

    public void setGroupLogicName(String groupLogicName) {
        this.groupLogicName = groupLogicName;
    }

    public Integer getHistoricalDateRangeStart() {
        return historicalDateRangeStart;
    }

    public void setHistoricalDateRangeStart(Integer historicalDateRangeStart) {
        this.historicalDateRangeStart = historicalDateRangeStart;
    }

    public Integer getHistoricalDateRangeEnd() {
        return historicalDateRangeEnd;
    }

    public void setHistoricalDateRangeEnd(Integer historicalDateRangeEnd) {
        this.historicalDateRangeEnd = historicalDateRangeEnd;
    }

    public Integer getCurrentDateRangeStart() {
        return currentDateRangeStart;
    }

    public void setCurrentDateRangeStart(Integer currentDateRangeStart) {
        this.currentDateRangeStart = currentDateRangeStart;
    }

    public Integer getCurrentDateRangeEnd() {
        return currentDateRangeEnd;
    }

    public void setCurrentDateRangeEnd(Integer currentDateRangeEnd) {
        this.currentDateRangeEnd = currentDateRangeEnd;
    }

    public Long getAlertInstanceId() {
        return alertInstanceId;
    }

    public void setAlertInstanceId(Long alertInstanceId) {
        this.alertInstanceId = alertInstanceId;
    }

    public String getAlertTitle() {
        return alertTitle;
    }

    public void setAlertTitle(String alertTitle) {
        this.alertTitle = alertTitle;
    }

    @Override
    public String toString() {
        return "AlertInstanceDTO [alertInstanceId=" + alertInstanceId + ", alertId=" + alertId + ", alertTitle=" + alertTitle + ", groupLogicName=" + groupLogicName
                + ", operator=" + operator + ", value=" + value + ", historicalDateRangeStart=" + historicalDateRangeStart + ", historicalDateRangeEnd=" + historicalDateRangeEnd
                + ", currentDateRangeStart=" + currentDateRangeStart + ", currentDateRangeEnd=" + currentDateRangeEnd + ", emailId=" + emailId + ", ruleTime=" + ruleTime
                + ", file=" + file + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + "]";
    }

    public String getAlertInstanceTitle() {
        return alertInstanceTitle;
    }

    public void setAlertInstanceTitle(String alertInstanceTitle) {
        this.alertInstanceTitle = alertInstanceTitle;
    }

    public List<String> getFileHeader() {
        return fileHeader;
    }

    public void setFileHeader(List<String> fileHeader) {
        this.fileHeader = fileHeader;
    }
}
