package com.snapdeal.scm.web.core.sro;

import java.util.ArrayList;
import java.util.List;

import com.snapdeal.scm.web.core.dto.AlertGroupLogicDTO;

/**
 * 
 * @author mohit
 *
 *
 */
public class AlertGroupLogicDataSRO {

	private List<AlertGroupLogicDTO> alertGroupLogics=new ArrayList<AlertGroupLogicDTO>();

	public List<AlertGroupLogicDTO> getAlertGroupLogics() {
		return alertGroupLogics;
	}

	public void setAlertGroupLogics(List<AlertGroupLogicDTO> alertGroupLogics) {
		this.alertGroupLogics = alertGroupLogics;
	}
}
