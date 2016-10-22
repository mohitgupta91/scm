package com.snapdeal.scm.core.mongo.document;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import com.snapdeal.scm.core.utils.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author prateek
 *
 */
@Document(collection = "connected_rad_status")
public class ConnectedRadStatus extends MongoDocument {
	private String subOrderCode;
	private String firstCurrentLocationHub;
	private String courierOriginCity;
	private String courierOriginState;
	private String courierDestinationCity;
	private String courierDestinationState;
	private String courierCode;
	private String courierGroup;
	private String shippingMode;
	private String customerDestinationPincode;

	private Boolean complete = false;

	private Map<String, Date> currentRADCityToDate = new HashMap<>();
	private Set<ConnectedStatusDetails> connectedStatusDetails = new HashSet<>();

	private Date radDate;
	private Date connectedDate;

	public ConnectedRadStatus() {

	}

	public ConnectedRadStatus(String subOrderCode,
			String firstCurrentLocationHub, String courierOriginCity,
			String courierOriginState, String courierDestinationCity,
			String courierDestinationState, String courierCode,
			String courierGroup, String shippingMode) {
		this.subOrderCode = subOrderCode;
		this.firstCurrentLocationHub = firstCurrentLocationHub;
		this.courierOriginCity = courierOriginCity;
		this.courierOriginState = courierOriginState;
		this.courierDestinationCity = courierDestinationCity;
		this.courierDestinationState = courierDestinationState;
		this.courierCode = courierCode;
		this.courierGroup = courierGroup;
		this.shippingMode = shippingMode;
	}

	public ConnectedRadStatus(String subOrderCode) {
		this.subOrderCode = subOrderCode;
	}

	public String getCourierCode() {
		return courierCode;
	}

	public ConnectedRadStatus setCourierCode(String courierCode) {
		if(StringUtils.isNotEmpty(courierCode))
			this.courierCode = courierCode;
		return this;
	}

	public String getSubOrderCode() {
		return subOrderCode;
	}

	public ConnectedRadStatus setSubOrderCode(String subOrderCode) {
		if(StringUtils.isNotEmpty(subOrderCode))
			this.subOrderCode = subOrderCode;
		return  this;
	}

	public String getFirstCurrentLocationHub() {
		return firstCurrentLocationHub;
	}

	public ConnectedRadStatus setFirstCurrentLocationHub(String firstCurrentLocationHub) {
		if(StringUtils.isNotEmpty(firstCurrentLocationHub))
			this.firstCurrentLocationHub = firstCurrentLocationHub;
		return  this;
	}

	public String getCourierOriginCity() {
		return courierOriginCity;
	}

	public ConnectedRadStatus setCourierOriginCity(String courierOriginCity) {
		if(StringUtils.isNotEmpty(courierOriginCity))
			this.courierOriginCity = courierOriginCity;
		return  this;
	}

	public String getCourierOriginState() {
		return courierOriginState;
	}

	public ConnectedRadStatus setCourierOriginState(String courierOriginState) {
		if(StringUtils.isNotEmpty(courierOriginState))
			this.courierOriginState = courierOriginState;
		return  this;
	}

	public String getCourierDestinationCity() {
		return courierDestinationCity;
	}

	public ConnectedRadStatus setCourierDestinationCity(String courierDestinationCity) {
		if(StringUtils.isNotEmpty(courierDestinationCity))
			this.courierDestinationCity = courierDestinationCity;
		return  this;
	}

	public String getCourierDestinationState() {
		return courierDestinationState;
	}

	public ConnectedRadStatus setCourierDestinationState(String courierDestinationState) {
		if(StringUtils.isNotEmpty(courierDestinationState))
			this.courierDestinationState = courierDestinationState;
		return  this;
	}

	public String getCourierGroup() {
		return courierGroup;
	}

	public ConnectedRadStatus setCourierGroup(String courierGroup) {
		if(StringUtils.isNotEmpty(courierGroup))
			this.courierGroup = courierGroup;
		return  this;
	}

	public String getShippingMode() {
		return shippingMode;
	}

	public ConnectedRadStatus setShippingMode(String shippingMode) {
		if(StringUtils.isNotEmpty(shippingMode))
			this.shippingMode = shippingMode;
		return  this;
	}

	public Date getRadDate() {
		return radDate;
	}

	public ConnectedRadStatus setRadDate(Date radDate) {
		if(Objects.nonNull(radDate))
			this.radDate = radDate;
		return  this;
	}

	public Date getConnectedDate() {
		return connectedDate;
	}

	public ConnectedRadStatus setConnectedDate(Date connectedDate) {
		if(Objects.nonNull(connectedDate))
			this.connectedDate = connectedDate;
		return  this;
	}

	public String getCustomerDestinationPincode() {
		return customerDestinationPincode;
	}

	public ConnectedRadStatus setCustomerDestinationPincode(String customerDestinationPincode) {
		if(StringUtils.isNotEmpty(customerDestinationPincode))
			this.customerDestinationPincode = customerDestinationPincode;
		return  this;
	}

	public Map<String, Date> getCurrentRADCityToDate() {
		return currentRADCityToDate;
	}

	public ConnectedRadStatus setCurrentRADCityToDate(Map<String, Date> currentRADCityToDate) {
		if(!CollectionUtils.isEmpty(currentRADCityToDate))
			this.currentRADCityToDate = currentRADCityToDate;
		return  this;
	}

	public ConnectedRadStatus addCurrentRADCityToDate(String currentRADCity, Date date){
		if(StringUtils.isNotEmpty(currentRADCity) && Objects.nonNull(date))
			this.currentRADCityToDate.put(currentRADCity, date);
		return this;
	}
	
	public ConnectedRadStatus addCurrentRADCityToDate(Map<String, Date> currentRADCityToDate) {
		if(!CollectionUtils.isEmpty(currentRADCityToDate))
			this.currentRADCityToDate.putAll(currentRADCityToDate);
		return this;
	}

	public Date getCurrentRADDate(String currentRADCity){
		return this.currentRADCityToDate.get(currentRADCity);
	}

	public Set<ConnectedStatusDetails> getConnectedStatusDetails() {
		return connectedStatusDetails;
	}

	public ConnectedRadStatus setConnectedStatusDetails(Set<ConnectedStatusDetails> connectedStatusDetails) {
		if(!CollectionUtils.isEmpty(connectedStatusDetails))
			this.connectedStatusDetails = connectedStatusDetails;
		return  this;
	}

	public ConnectedRadStatus addConnectedStatusDetail(ConnectedStatusDetails connectedStatusDetail) {
		if(Objects.nonNull(connectedStatusDetail))
			this.connectedStatusDetails.add(connectedStatusDetail);
		return  this;
	}
	
	public ConnectedRadStatus addConnectedStatusDetail(Set<ConnectedStatusDetails> connectedStatusDetails) {
		if(!CollectionUtils.isEmpty(connectedStatusDetails))
			this.connectedStatusDetails.addAll(connectedStatusDetails);
		return  this;
	}

	public Boolean isComplete() {
		return complete;
	}

	public ConnectedRadStatus setComplete(Boolean complete) {
		if(Objects.nonNull(complete))
			this.complete = complete;
		return  this;
	}

	public static class ConnectedStatusDetails implements Serializable{
		private String courierRemarks;
		private String courierStatus;
		private String snapdealStatus;
		private String locationHub;
		private Long statusdate;

		public ConnectedStatusDetails() {
		}

		public ConnectedStatusDetails(String courierRemarks,
				String courierStatus, String snapdealStatus,
				String locationHub, Long statusdate) {
			this.courierRemarks = courierRemarks;
			this.courierStatus = courierStatus;
			this.snapdealStatus = snapdealStatus;
			this.locationHub = locationHub;
			this.statusdate = statusdate;
		}

		public String getCourierRemarks() {
			return courierRemarks;
		}

		public ConnectedStatusDetails setCourierRemarks(String courierRemarks) {
			this.courierRemarks = courierRemarks;
			return this;
		}

		public String getCourierStatus() {
			return courierStatus;
		}

		public ConnectedStatusDetails setCourierStatus(String courierStatus) {
			this.courierStatus = courierStatus;
			return this;
		}

		public String getSnapdealStatus() {
			return snapdealStatus;
		}

		public ConnectedStatusDetails setSnapdealStatus(String snapdealStatus) {
			this.snapdealStatus = snapdealStatus;
			return this;
		}

		public String getLocationHub() {
			return locationHub;
		}

		public ConnectedStatusDetails setLocationHub(String locationHub) {
			this.locationHub = locationHub;
			return this;
		}

		public Long getStatusdate() {
			return statusdate;
		}

		public ConnectedStatusDetails setStatusdate(Long statusdate) {
			this.statusdate = statusdate;
			return this;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((courierRemarks == null) ? 0 : courierRemarks.hashCode());
			result = prime * result
					+ ((courierStatus == null) ? 0 : courierStatus.hashCode());
			result = prime * result
					+ ((locationHub == null) ? 0 : locationHub.hashCode());
			result = prime
					* result
					+ ((snapdealStatus == null) ? 0 : snapdealStatus.hashCode());
			result = prime * result
					+ ((statusdate == null) ? 0 : statusdate.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ConnectedStatusDetails other = (ConnectedStatusDetails) obj;
			if (courierRemarks == null) {
				if (other.courierRemarks != null)
					return false;
			} else if (!courierRemarks.equals(other.courierRemarks))
				return false;
			if (courierStatus == null) {
				if (other.courierStatus != null)
					return false;
			} else if (!courierStatus.equals(other.courierStatus))
				return false;
			if (locationHub == null) {
				if (other.locationHub != null)
					return false;
			} else if (!locationHub.equals(other.locationHub))
				return false;
			if (snapdealStatus == null) {
				if (other.snapdealStatus != null)
					return false;
			} else if (!snapdealStatus.equals(other.snapdealStatus))
				return false;
			if (statusdate == null) {
				if (other.statusdate != null)
					return false;
			} else if (!statusdate.equals(other.statusdate))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "ConnectedStatusDetails [courierRemarks=" + courierRemarks
					+ ", courierStatus=" + courierStatus + ", snapdealStatus="
					+ snapdealStatus + ", locationHub=" + locationHub
					+ ", statusdate=" + statusdate + "]";
		}

	}
}