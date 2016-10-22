package com.snapdeal.scm.web.core.sro;

import java.util.ArrayList;
import java.util.List;

import com.snapdeal.scm.web.core.dto.AlertDTO;

public class AlertDataSRO {
	
	private List<AlertDTO> alerts= new ArrayList<AlertDTO>();

	public List<AlertDTO> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<AlertDTO> alerts) {
		this.alerts = alerts;
	}
}
