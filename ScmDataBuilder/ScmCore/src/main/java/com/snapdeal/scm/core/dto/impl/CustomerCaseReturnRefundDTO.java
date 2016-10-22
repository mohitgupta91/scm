package com.snapdeal.scm.core.dto.impl;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * Created by harshit.nimbark on 5/27/16.
 */
public class CustomerCaseReturnRefundDTO extends AbstractStandardDTO {

    @Order(order = 0)
    @NotBlank
    private String subOrderCode;
    @Order(order = 1)
    @NotBlank
    private String ccOrigin;
    @Order(order = 2)
    @NotBlank
    private String ccLevel_1;
    @Order(order = 3)
    @NotBlank
    private String ccLevel_2;
    @Order(order = 4)
    @NotBlank
    private Date ccCreatedDate;
    @Order(order = 5)
    @NotBlank
    private String ccCloseddate;
    @Order(order = 6)
    @NotBlank
    private String ccOrdercode;
    @Order(order = 7)
    @NotBlank
    private String ccOrderNumber;
    @Order(order = 8)
    @NotBlank
    private String ccCaseNumber;

    @Order(order = 9)
    private String stdhDebitType;
    @Order(order = 10)
    private String stdhDebitStatus;
    @Order(order = 11)
    private Date stdhCreated;
    @Order(order = 12)
    private Date stdhUpdated;

    @Order(order = 13)
    private String srOrderCode;
    @Order(order = 14)
    private Date srCreated;
    @Order(order = 15)
    private Date srUpdated;
    @Order(order = 16)
    private Integer srProcessId;
    @Order(order = 17)
    private String srRequestStatus;

    public CustomerCaseReturnRefundDTO() {
    }

    public CustomerCaseReturnRefundDTO(String subOrderCode, String ccOrigin, String ccLevel_1, String ccLevel_2, Date ccCreatedDate, String ccCloseddate, String ccOrdercode, String ccOrderNumber, String ccCaseNumber, String stdhDebitType, String stdhDebitStatus, Date stdhCreated, Date stdhUpdated, String srOrderCode, Date srCreated, Date srUpdated, Integer srProcessId, String srRequestStatus) {
        this.subOrderCode = subOrderCode;
        this.ccOrigin = ccOrigin;
        this.ccLevel_1 = ccLevel_1;
        this.ccLevel_2 = ccLevel_2;
        this.ccCreatedDate = ccCreatedDate;
        this.ccCloseddate = ccCloseddate;
        this.ccOrdercode = ccOrdercode;
        this.ccOrderNumber = ccOrderNumber;
        this.ccCaseNumber = ccCaseNumber;
        this.stdhDebitType = stdhDebitType;
        this.stdhDebitStatus = stdhDebitStatus;
        this.stdhCreated = stdhCreated;
        this.stdhUpdated = stdhUpdated;
        this.srOrderCode = srOrderCode;
        this.srCreated = srCreated;
        this.srUpdated = srUpdated;
        this.srProcessId = srProcessId;
        this.srRequestStatus = srRequestStatus;
    }

    public String getSubOrderCode() {
        return subOrderCode;
    }

    public void setSubOrderCode(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public String getCcOrigin() {
        return ccOrigin;
    }

    public void setCcOrigin(String ccOrigin) {
        this.ccOrigin = ccOrigin;
    }

    public String getCcLevel_1() {
        return ccLevel_1;
    }

    public void setCcLevel_1(String ccLevel_1) {
        this.ccLevel_1 = ccLevel_1;
    }

    public String getCcLevel_2() {
        return ccLevel_2;
    }

    public void setCcLevel_2(String ccLevel_2) {
        this.ccLevel_2 = ccLevel_2;
    }

    public Date getCcCreatedDate() {
        return ccCreatedDate;
    }

    public void setCcCreatedDate(Date ccCreatedDate) {
        this.ccCreatedDate = ccCreatedDate;
    }

    public String getCcCloseddate() {
        return ccCloseddate;
    }

    public void setCcCloseddate(String ccCloseddate) {
        this.ccCloseddate = ccCloseddate;
    }

    public String getCcOrdercode() {
        return ccOrdercode;
    }

    public void setCcOrdercode(String ccOrdercode) {
        this.ccOrdercode = ccOrdercode;
    }

    public String getCcOrderNumber() {
        return ccOrderNumber;
    }

    public void setCcOrderNumber(String ccOrderNumber) {
        this.ccOrderNumber = ccOrderNumber;
    }

    public String getCcCaseNumber() {
        return ccCaseNumber;
    }

    public void setCcCaseNumber(String ccCaseNumber) {
        this.ccCaseNumber = ccCaseNumber;
    }

    public String getStdhDebitType() {
        return stdhDebitType;
    }

    public void setStdhDebitType(String stdhDebitType) {
        this.stdhDebitType = stdhDebitType;
    }

    public String getStdhDebitStatus() {
        return stdhDebitStatus;
    }

    public void setStdhDebitStatus(String stdhDebitStatus) {
        this.stdhDebitStatus = stdhDebitStatus;
    }

    public Date getStdhCreated() {
        return stdhCreated;
    }

    public void setStdhCreated(Date stdhCreated) {
        this.stdhCreated = stdhCreated;
    }

    public Date getStdhUpdated() {
        return stdhUpdated;
    }

    public void setStdhUpdated(Date stdhUpdated) {
        this.stdhUpdated = stdhUpdated;
    }

    public String getSrOrderCode() {
        return srOrderCode;
    }

    public void setSrOrderCode(String srOrderCode) {
        this.srOrderCode = srOrderCode;
    }

    public Date getSrCreated() {
        return srCreated;
    }

    public void setSrCreated(Date srCreated) {
        this.srCreated = srCreated;
    }

    public Date getSrUpdated() {
        return srUpdated;
    }

    public void setSrUpdated(Date srUpdated) {
        this.srUpdated = srUpdated;
    }

    public Integer getSrProcessId() {
        return srProcessId;
    }

    public void setSrProcessId(Integer srProcessId) {
        this.srProcessId = srProcessId;
    }

    public String getSrRequestStatus() {
        return srRequestStatus;
    }

    public void setSrRequestStatus(String srRequestStatus) {
        this.srRequestStatus = srRequestStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomerCaseReturnRefundDTO{");
        sb.append(subOrderCode).append('\'');
        sb.append(ccOrigin).append('\'');
        sb.append(ccLevel_1).append('\'');
        sb.append(ccLevel_2).append('\'');
        sb.append(ccCreatedDate);
        sb.append(ccCloseddate).append('\'');
        sb.append(ccOrdercode).append('\'');
        sb.append(ccOrderNumber).append('\'');
        sb.append(ccCaseNumber).append('\'');
        sb.append(stdhDebitType).append('\'');
        sb.append(stdhDebitStatus).append('\'');
        sb.append(stdhCreated);
        sb.append(stdhUpdated);
        sb.append(srOrderCode).append('\'');
        sb.append(srCreated);
        sb.append(srUpdated);
        sb.append(srProcessId);
        sb.append(srRequestStatus).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public QueryType getQueryType() {
        return null;
    }

    @Override
    public boolean validateDTO() {
        return false;
    }
}
