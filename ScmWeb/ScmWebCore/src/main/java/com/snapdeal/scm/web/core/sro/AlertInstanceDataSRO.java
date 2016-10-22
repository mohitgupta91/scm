package com.snapdeal.scm.web.core.sro;

import java.util.ArrayList;
import java.util.List;

import com.snapdeal.scm.web.core.dto.AlertInstanceDTO;

/***
 * 
 * @author mohit
 *
 */
public class AlertInstanceDataSRO{

	private List<AlertInstanceDTO> alertInstanceList= new ArrayList<AlertInstanceDTO>();

	public List<AlertInstanceDTO> getAlertInstanceList() {
		return alertInstanceList;
	}

	public void setAlertInstanceList(List<AlertInstanceDTO> alertInstanceList) {
		this.alertInstanceList = alertInstanceList;
	}
	
}
