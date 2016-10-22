package com.snapdeal.scm.core.dto.impl;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.utils.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author prateek
 */
public class SubOrderTpStatusCodesDTO extends AbstractStandardDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 3401531257854427493L;

    @Order(order = 0)
    @NotBlank
    private String subOrderCode;
    @Order(order = 1)
    private String newStatusCode;
    @Order(order = 2)
    @NotNull
    private Date newStatusDate;
    @Order(order = 3)
    private String currentLocationCode;//can be null
    @Order(order = 4)
    @NotNull
    private String currentLocationPincode;
    @Order(order = 5)
    @NotNull
    private String remarks;
    @Order(order = 6)
    @NotNull
    private String courierStatus;
    @Order(order = 7)
    @NotNull
    private String originHubLocationPincode;
    @Order(order = 8)
    @NotNull
    private String destinationHubLocationPincode;
    @Order(order = 9)
    @NotNull
    private String currentStatus;
    @Order(order = 10)
    @NotNull
    private Date currentStatusDate;
    @Order(order = 11)
    private String rejectCode;
    @Order(order = 12)
    private String oldStatusCode;
    @Order(order = 13)
    private String courierCode;
    @Order(order = 14)
    private String description;




    public SubOrderTpStatusCodesDTO() {
    }

    public SubOrderTpStatusCodesDTO(String subOrderCode, String newStatusCode, Date newStatusDate, String currentLocationCode, String currentLocationPincode, String remarks, String courierStatus, String originHubLocationPincode, String destinationHubLocationPincode) {
        this.subOrderCode = subOrderCode;
        this.newStatusCode = newStatusCode;
        this.newStatusDate = newStatusDate;
        this.currentLocationCode = currentLocationCode;
        this.currentLocationPincode = currentLocationPincode;
        this.remarks = remarks;
        this.courierStatus = courierStatus;
        this.originHubLocationPincode = originHubLocationPincode;
        this.destinationHubLocationPincode = destinationHubLocationPincode;
    }

    public SubOrderTpStatusCodesDTO(String subOrderCode, String newStatusCode, Date newStatusDate, String currentLocationCode, String currentLocationPincode, String remarks, String courierStatus, String originHubLocationPincode, String destinationHubLocationPincode, String currentStatus, Date currentStatusDate) {
        this.subOrderCode = subOrderCode;
        this.newStatusCode = newStatusCode;
        this.newStatusDate = newStatusDate;
        this.currentLocationCode = currentLocationCode;
        this.currentLocationPincode = currentLocationPincode;
        this.remarks = remarks;
        this.courierStatus = courierStatus;
        this.originHubLocationPincode = originHubLocationPincode;
        this.destinationHubLocationPincode = destinationHubLocationPincode;
        this.currentStatus = currentStatus;
        this.currentStatusDate = currentStatusDate;
    }

    public SubOrderTpStatusCodesDTO(String subOrderCode, String newStatusCode, Date newStatusDate, String currentLocationCode, String currentLocationPincode, String remarks, String courierStatus, String originHubLocationPincode, String destinationHubLocationPincode, String currentStatus, Date currentStatusDate, String rejectCode) {
        this.subOrderCode = subOrderCode;
        this.newStatusCode = newStatusCode;
        this.newStatusDate = newStatusDate;
        this.currentLocationCode = currentLocationCode;
        this.currentLocationPincode = currentLocationPincode;
        this.remarks = remarks;
        this.courierStatus = courierStatus;
        this.originHubLocationPincode = originHubLocationPincode;
        this.destinationHubLocationPincode = destinationHubLocationPincode;
        this.currentStatus = currentStatus;
        this.currentStatusDate = currentStatusDate;
        this.rejectCode = rejectCode;
    }

    public SubOrderTpStatusCodesDTO(String subOrderCode, String newStatusCode, Date newStatusDate, String currentLocationCode, String currentLocationPincode, String remarks, String courierStatus, String originHubLocationPincode, String destinationHubLocationPincode, String currentStatus, Date currentStatusDate, String rejectCode, String oldStatusCode, String courierCode) {
        this.subOrderCode = subOrderCode;
        this.newStatusCode = newStatusCode;
        this.newStatusDate = newStatusDate;
        this.currentLocationCode = currentLocationCode;
        this.currentLocationPincode = currentLocationPincode;
        this.remarks = remarks;
        this.courierStatus = courierStatus;
        this.originHubLocationPincode = originHubLocationPincode;
        this.destinationHubLocationPincode = destinationHubLocationPincode;
        this.currentStatus = currentStatus;
        this.currentStatusDate = currentStatusDate;
        this.rejectCode = rejectCode;
        this.oldStatusCode = oldStatusCode;
        this.courierCode = courierCode;
    }

    public SubOrderTpStatusCodesDTO(String subOrderCode, String newStatusCode, Date newStatusDate, String currentLocationCode, String currentLocationPincode, String remarks, String courierStatus, String originHubLocationPincode, String destinationHubLocationPincode, String currentStatus, Date currentStatusDate, String rejectCode, String oldStatusCode, String courierCode, String description) {
        this.subOrderCode = subOrderCode;
        this.newStatusCode = newStatusCode;
        this.newStatusDate = newStatusDate;
        this.currentLocationCode = currentLocationCode;
        this.currentLocationPincode = currentLocationPincode;
        this.remarks = remarks;
        this.courierStatus = courierStatus;
        this.originHubLocationPincode = originHubLocationPincode;
        this.destinationHubLocationPincode = destinationHubLocationPincode;
        this.currentStatus = currentStatus;
        this.currentStatusDate = currentStatusDate;
        this.rejectCode = rejectCode;
        this.oldStatusCode = oldStatusCode;
        this.courierCode = courierCode;
        this.description = description;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSubOrderCode() {
        return subOrderCode;
    }

    public void setSubOrderCode(String subOrderCode) {
        this.subOrderCode = subOrderCode;
    }

    public String getNewStatusCode() {
        return newStatusCode;
    }

    public void setNewStatusCode(String newStatusCode) {
        this.newStatusCode = newStatusCode;
    }

    public Date getNewStatusDate() {
        return newStatusDate;
    }

    public void setNewStatusDate(Date newStatusDate) {
        this.newStatusDate = newStatusDate;
    }

    public String getCurrentLocationCode() {
        return currentLocationCode;
    }

    public void setCurrentLocationCode(String currentLocationCode) {
        this.currentLocationCode = currentLocationCode;
    }

    public String getCurrentLocationPincode() {
        return currentLocationPincode;
    }

    public void setCurrentLocationPincode(String currentLocationPincode) {
        this.currentLocationPincode = currentLocationPincode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCourierStatus() {
        return courierStatus;
    }

    public void setCourierStatus(String courierStatus) {
        this.courierStatus = courierStatus;
    }

    public String getOriginHubLocationPincode() {
        return originHubLocationPincode;
    }

    public void setOriginHubLocationPincode(String originHubLocationPincode) {
        this.originHubLocationPincode = originHubLocationPincode;
    }

    public String getDestinationHubLocationPincode() {
        return destinationHubLocationPincode;
    }

    public void setDestinationHubLocationPincode(String destinationHubLocationPincode) {
        this.destinationHubLocationPincode = destinationHubLocationPincode;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Date getCurrentStatusDate() {
        return currentStatusDate;
    }

    public void setCurrentStatusDate(Date currentStatusDate) {
        this.currentStatusDate = currentStatusDate;
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(String rejectCode) {
        this.rejectCode = rejectCode;
    }

    public String getOldStatusCode() {
        return oldStatusCode;
    }

    public void setOldStatusCode(String oldStatusCode) {
        this.oldStatusCode = oldStatusCode;
    }

    public String getCourierCode() {
        return courierCode;
    }

    public void setCourierCode(String courierCode) {
        this.courierCode = courierCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubOrderTpStatusCodesDTO{");
        sb.append("subOrderCode='").append(subOrderCode).append('\'');
        sb.append(", newStatusCode='").append(newStatusCode).append('\'');
        sb.append(", newStatusDate=").append(newStatusDate);
        sb.append(", currentLocationCode='").append(currentLocationCode).append('\'');
        sb.append(", currentLocationPincode='").append(currentLocationPincode).append('\'');
        sb.append(", remarks='").append(remarks).append('\'');
        sb.append(", courierStatus='").append(courierStatus).append('\'');
        sb.append(", originHubLocationPincode='").append(originHubLocationPincode).append('\'');
        sb.append(", destinationHubLocationPincode='").append(destinationHubLocationPincode).append('\'');
        sb.append(", currentStatus='").append(currentStatus).append('\'');
        sb.append(", currentStatusDate=").append(currentStatusDate);
        sb.append(", rejectCode='").append(rejectCode).append('\'');
        sb.append(", oldStatusCode='").append(oldStatusCode).append('\'');
        sb.append(", courierCode='").append(courierCode).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SUB_ORDER_TP_STATUS_CODES;
    }
    
    @Override
   	public boolean validateDTO() {
        if (StringUtils.isEmpty(subOrderCode) || StringUtils.isEmpty(newStatusCode) || newStatusDate == null)
            return false;
   		return true;
   	}
}
