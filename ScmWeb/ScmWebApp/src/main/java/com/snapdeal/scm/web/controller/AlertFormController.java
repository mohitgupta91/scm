package com.snapdeal.scm.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snapdeal.scm.mongo.doc.InstanceGroupingParam;
import com.snapdeal.scm.web.core.dto.AlertDTO;
import com.snapdeal.scm.web.core.dto.AlertGroupLogicDTO;
import com.snapdeal.scm.web.core.dto.AlertInstanceDTO;
import com.snapdeal.scm.web.core.exception.FileUploadException;
import com.snapdeal.scm.web.core.response.AlertResponse;
import com.snapdeal.scm.web.core.sro.AlertDataSRO;
import com.snapdeal.scm.web.core.sro.AlertGroupLogicDataSRO;
import com.snapdeal.scm.web.core.sro.AlertInstanceDataSRO;
import com.snapdeal.scm.web.core.sro.InstanceGroupingParamSRO;
import com.snapdeal.scm.web.core.validation.SCMError;
import com.snapdeal.scm.web.services.IAlertService;
import com.snapdeal.scm.web.validator.AlertFormValidator;

/**
 * AlertController : Alert Controller
 * 
 * @author pranav,mohit
 */
@Controller
public class AlertFormController {

	private static final Logger LOG = LoggerFactory
			.getLogger(AlertFormController.class);

	@Autowired
	private IAlertService alertService;

	@Value("${scm.sso.enabled}")
	private boolean ssoEnabled;

	@RequestMapping(value = "/data/alerts", method = RequestMethod.GET)
	@ResponseBody
	public AlertResponse<AlertDataSRO> getAllAlert() {
		LOG.info("Serving alerts data");
		return generateAlertResponse(alertService.listAlerts());
	}

	@RequestMapping(value = "/data/alerts/{alertId}", method = RequestMethod.GET)
	@ResponseBody
	public AlertResponse<AlertDataSRO> getAlert(
			@PathVariable("alertId") Long alertId) {
		LOG.info("Serving alerts data");
		AlertDTO alert = alertService.getAlertById(alertId);
		if (alert == null)
			return generateAlertResponse(null);
		List<AlertDTO> alertList = new ArrayList<AlertDTO>();
		alertList.add(alert);
		return generateAlertResponse(alertList);
	}

	@RequestMapping(value = "/data/alerts/{alertId}/grouplogic", method = RequestMethod.GET)
	@ResponseBody
	public AlertResponse<AlertGroupLogicDataSRO> getGroupLogicByAlertId(
			@PathVariable("alertId") Long alertId) {
		LOG.info("Serving group logic for alerts data");
		List<AlertGroupLogicDTO> alertGroupLogics = alertService
				.getGroupLogicByAlertId(alertId);
		return generateAlertGroupLogicResponse(alertGroupLogics);
	}

	@RequestMapping(value = "/data/alerts/{alertId}/instances", method = RequestMethod.POST)
	@ResponseBody
	public AlertResponse<AlertInstanceDataSRO> create(
			@Valid @ModelAttribute AlertInstanceDTO dto,
			@PathVariable("alertId") Long alertId) {
		LOG.info("Serving alerts create : " + dto.toString());
		AlertInstanceDTO alert = null;
		try {
			alert = alertService.create(dto);
		} catch (FileUploadException | IOException e) {
			return generateAlertInstanceResponse(null);
		}
		List<AlertInstanceDTO> alertList = new ArrayList<AlertInstanceDTO>();
		alertList.add(alert);
		return generateAlertInstanceResponse(alertList);
	}

	@RequestMapping(value = "/data/alerts/{alertId}/instances/_edit", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public AlertResponse<AlertInstanceDataSRO> edit(
			@Valid @ModelAttribute AlertInstanceDTO dto,
			@RequestParam(value = "editedRows", required = false) String editedRows,
			@RequestParam(value = "deletedRowsId", required = false) String deletedRows,
			@PathVariable("alertId") Long alertId) throws JsonParseException,
			JsonMappingException, IOException {
		LOG.info("Serving alerts edit : " + dto.toString() + " deletedRows:"
				+ deletedRows + " editedRows: " + editedRows);
		AlertInstanceDTO alert = null;
		List<Map<String, String>> editedRowslist = null;
		List<String> deletedList = null;
		ObjectMapper mapper = new ObjectMapper();
		if (editedRows != null)
			editedRowslist = mapper.readValue(editedRows, List.class);
		if (deletedRows != null)
			deletedList = mapper.readValue(deletedRows, List.class);
		try {
			alert = alertService.edit(dto, editedRowslist, deletedList);
		} catch (FileUploadException | IOException e) {
			return generateAlertInstanceResponse(null);
		}
		if (alert == null)
			return generateAlertInstanceResponse(null);
		List<AlertInstanceDTO> alertList = new ArrayList<AlertInstanceDTO>();
		alertList.add(alert);
		return generateAlertInstanceResponse(alertList);
	}

	@RequestMapping(value = "/data/alerts/{alertId}/instances/{alertInstanceId}", method = RequestMethod.DELETE)
	@ResponseBody
	public AlertResponse<AlertInstanceDataSRO> delete(
			@PathVariable(value = "alertInstanceId") Long alertInstanceId,
			@PathVariable(value = "alertId") Long alertId) {
		LOG.info("Serving alert delete");
		alertService.delete(alertInstanceId);
		return generateAlertInstanceResponse(null);
	}

	@RequestMapping(value = "/data/users/{userId}/alerts/instances", method = RequestMethod.GET)
	@ResponseBody
	public AlertResponse<AlertInstanceDataSRO> list(
			@PathVariable("userId") String userName) {
		LOG.info("Serving alert Listing");
		return generateAlertInstanceResponse(alertService
				.listAlertInstances(userName));
	}

	@RequestMapping(value = "/data/alerts/{alertId}/instances/{alertInstanceId}", method = RequestMethod.GET)
	@ResponseBody
	public AlertResponse<AlertInstanceDataSRO> findByAlertInstanceId(
			@PathVariable(value = "alertId") Long alertId,
			@PathVariable(value = "alertInstanceId") Long alertInstanceId) {
		LOG.info("Serving alert Listing");
		AlertInstanceDTO alert = alertService
				.findByAlertInstanceId(alertInstanceId);
		if (alert == null)
			return generateAlertInstanceResponse(null);
		List<AlertInstanceDTO> alertList = new ArrayList<AlertInstanceDTO>();
		alertList.add(alert);
		return generateAlertInstanceResponse(alertList);
	}

	@RequestMapping(value = "/data/alerts/{alertId}/instances/{alertInstanceId}/filedata", method = RequestMethod.GET)
	@ResponseBody
	public AlertResponse<InstanceGroupingParamSRO> findAlertInstanceGroupingParam(
			@PathVariable(value = "alertId") Long alertId,
			@PathVariable(value = "alertInstanceId") Long alertInstanceId) {
		LOG.info("Serving alert Instance grouping param Listing");
		return generateInstanceGroupingParamResponse(alertService
				.getInstanceGroupingParam(alertInstanceId));
	}

	private AlertResponse<InstanceGroupingParamSRO> generateInstanceGroupingParamResponse(
			List<InstanceGroupingParam> data) {
		AlertResponse<InstanceGroupingParamSRO> response = new AlertResponse<>();
		InstanceGroupingParamSRO instanceGroupingParamSRO = new InstanceGroupingParamSRO();
		List<Map<String, String>> fileData = new ArrayList<>();
		List<SCMError> errors = new ArrayList<SCMError>();
		if (data == null)
			response.setErrors(setNoDataError(errors, "instanceGroupParam"));

		for (InstanceGroupingParam instance : data) {
			Map<String, String> row = new HashMap<>();
			row.putAll(instance.getGroupingParam());
			row.put("rowId", instance.getRowId().toString());
			fileData.add(row);
		}
		instanceGroupingParamSRO.setFileData(fileData);
		response.setCode(HttpStatus.OK.value());
		response.setData(instanceGroupingParamSRO);
		System.out.println(instanceGroupingParamSRO);
		return response;
	}

	@InitBinder
	protected void initWebCommonBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.addValidators(new AlertFormValidator());
	}

	private AlertResponse<AlertDataSRO> generateAlertResponse(
			List<AlertDTO> data) {
		AlertResponse<AlertDataSRO> response = new AlertResponse<AlertDataSRO>();
		AlertDataSRO alertDataSRO = new AlertDataSRO();
		List<SCMError> errors = new ArrayList<SCMError>();
		if (data == null)
			response.setErrors(setNoDataError(errors, "alert"));
		alertDataSRO.setAlerts(data);
		response.setCode(HttpStatus.OK.value());
		response.setData(alertDataSRO);
		return response;
	}

	private AlertResponse<AlertInstanceDataSRO> generateAlertInstanceResponse(
			List<AlertInstanceDTO> data) {
		AlertResponse<AlertInstanceDataSRO> response = new AlertResponse<AlertInstanceDataSRO>();
		AlertInstanceDataSRO alertInstanceSRO = new AlertInstanceDataSRO();
		List<SCMError> errors = new ArrayList<SCMError>();
		if (data == null)
			response.setErrors(setNoDataError(errors, "alertInstance"));
		alertInstanceSRO.setAlertInstanceList(data);
		response.setCode(HttpStatus.OK.value());
		response.setData(alertInstanceSRO);
		return response;
	}

	private AlertResponse<AlertGroupLogicDataSRO> generateAlertGroupLogicResponse(
			List<AlertGroupLogicDTO> data) {
		AlertResponse<AlertGroupLogicDataSRO> response = new AlertResponse<AlertGroupLogicDataSRO>();
		AlertGroupLogicDataSRO groupLogicSRO = new AlertGroupLogicDataSRO();
		List<SCMError> errors = new ArrayList<SCMError>();
		if (data == null)
			response.setErrors(setNoDataError(errors, "alertGroupLogic"));
		groupLogicSRO.setAlertGroupLogics(data);
		response.setCode(HttpStatus.OK.value());
		response.setData(groupLogicSRO);
		return response;
	}

	private List<SCMError> setNoDataError(List<SCMError> errors,
			String fieldName) {

		SCMError error = new SCMError();
		error.setCode(HttpStatus.OK.value());
		error.setDescription(HttpStatus.OK.name());
		error.setFieldName(fieldName);
		error.setMessage("No Data Found");
		errors.add(error);

		return errors;
	}
}
