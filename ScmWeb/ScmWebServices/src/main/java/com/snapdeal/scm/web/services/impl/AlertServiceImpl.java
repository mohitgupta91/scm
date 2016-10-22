package com.snapdeal.scm.web.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.snapdeal.scm.mongo.mao.ISequenceMao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.snapdeal.sacs.base.sro.v2.UserSRO;
import com.snapdeal.scm.enums.SequenceKey;
import com.snapdeal.scm.mongo.dao.AlertGroupLogicRepository;
import com.snapdeal.scm.mongo.dao.AlertInstanceRepository;
import com.snapdeal.scm.mongo.dao.AlertRepository;
import com.snapdeal.scm.mongo.dao.InstanceGroupingParamRepository;
import com.snapdeal.scm.mongo.doc.Alert;
import com.snapdeal.scm.mongo.doc.AlertGroupLogic;
import com.snapdeal.scm.mongo.doc.AlertInstance;
import com.snapdeal.scm.mongo.doc.FileValuesDTO;
import com.snapdeal.scm.mongo.doc.InstanceGroupingParam;
import com.snapdeal.scm.web.core.dto.AlertDTO;
import com.snapdeal.scm.web.core.dto.AlertGroupLogicDTO;
import com.snapdeal.scm.web.core.dto.AlertInstanceDTO;
import com.snapdeal.scm.web.core.exception.FileUploadException;
import com.snapdeal.scm.web.core.utils.ExcelReader;
import com.snapdeal.scm.web.services.IAlertService;
import org.springframework.util.StringUtils;

/**
 * AlertServiceImpl
 *
 * @author pranav, Ashwini, Mohit
 */
@Service
public class AlertServiceImpl implements IAlertService {

    @Autowired
    private AlertInstanceRepository alertInstanceRepo;

    @Autowired
    private AlertGroupLogicRepository alertGroupLogicRepo;

    @Autowired
    private AlertRepository alertRepo;

    @Autowired
    private ISequenceMao sequenceDao;

    @Autowired
    private InstanceGroupingParamRepository instanceParamRepo;

    private static final String instanceGroupLogicRowId = "rowId";

    @Override
    public AlertInstanceDTO create(AlertInstanceDTO dto) throws IOException, FileUploadException {
        AlertInstance entity = convertToAlertFromAlertDto(new AlertInstance(sequenceDao.getNextSequenceId(SequenceKey.ALERT_INSTANCE.name())), dto);
        entity.setCreated(new Date());
        entity.setUpdated(new Date());
        entity.setCreatedBy(StringUtils.isEmpty(dto.getCreatedBy()) ? getCurrentUserName() : dto.getCreatedBy());
        entity.setUpdatedBy(StringUtils.isEmpty(dto.getUpdatedBy()) ? getCurrentUserName() : dto.getUpdatedBy());
        List<InstanceGroupingParam> groupingParam = null;
        if (dto.getFile() != null) {
            FileValuesDTO fileValueDTO = ExcelReader.readExcelFile(dto.getFile());
            entity.setFileHeader(fileValueDTO.getHeader());
            groupingParam = fileValueDTO.getHeaderDataMap().stream().map(map -> {
                InstanceGroupingParam instanceParam = new InstanceGroupingParam(sequenceDao.getNextSequenceId(SequenceKey.ALERT_INSTANCE_FILE_ROW.name()), entity.getAlertInstanceId(), map);
                return instanceParam;
            }).collect(Collectors.toList());
            instanceParamRepo.save(groupingParam);
        }
        alertInstanceRepo.save(entity);

        dto.setAlertInstanceId(entity.getAlertInstanceId());
        return dto;
    }

    @Override
    public AlertInstanceDTO edit(AlertInstanceDTO dto, List<Map<String, String>> editedRows, List<String> deletedRowsID) throws IOException, FileUploadException {
        AlertInstance entity = alertInstanceRepo.findByAlertInstanceId(dto.getAlertInstanceId());
        entity = convertToAlertFromAlertDto(entity, dto);
        List<InstanceGroupingParam> groupingParam = null;
        if (dto.getFile() != null) {
            FileValuesDTO fileValueDTO = ExcelReader.readExcelFile(dto.getFile());
            entity.setFileHeader(fileValueDTO.getHeader());
            groupingParam = fileValueDTO.getHeaderDataMap().stream().map(map -> {
                InstanceGroupingParam instanceParam = new InstanceGroupingParam(sequenceDao.getNextSequenceId(SequenceKey.ALERT_INSTANCE.name()), dto.getAlertInstanceId(), map);
                return instanceParam;
            }).collect(Collectors.toList());

            List<InstanceGroupingParam> persistedInstanceGroupingParam = instanceParamRepo.findByAlertInstanceId(entity.getAlertInstanceId());
            instanceParamRepo.delete(persistedInstanceGroupingParam);
            instanceParamRepo.save(groupingParam);
        } else {
            if (editedRows != null && editedRows.size() > 0) {
                for (Map<String, String> instanceParamMap : editedRows) {
                    InstanceGroupingParam persistedInstanceParam = instanceParamRepo.findByRowId(Long.parseLong(instanceParamMap.get(instanceGroupLogicRowId)));
                    instanceParamMap.remove(instanceGroupLogicRowId);
                    persistedInstanceParam.setGroupingParam(instanceParamMap);
                    instanceParamRepo.save(persistedInstanceParam);
                }
            }
            if (deletedRowsID != null && deletedRowsID.size() > 0) {
                for (String rowId : deletedRowsID) {
                    InstanceGroupingParam savedInstanceParam = instanceParamRepo.findByRowId(Long.parseLong(rowId));
                    instanceParamRepo.delete(savedInstanceParam);
                }
            }
        }
        entity.setUpdatedBy(getCurrentUserName());
        entity.setUpdated(new Date());
        alertInstanceRepo.save(entity);
        return new AlertInstanceDTO(entity);
    }


    @Override
    public void delete(Long alertInstanceId) {
        AlertInstance alert = alertInstanceRepo.findByAlertInstanceId(alertInstanceId);
        List<InstanceGroupingParam> groupingParam = instanceParamRepo.findByAlertInstanceId(alertInstanceId);
        instanceParamRepo.delete(groupingParam);
        alertInstanceRepo.delete(alert);
    }

    @Override
    public List<AlertInstanceDTO> listAlertInstances(String userName) {
        List<AlertInstance> alerts = alertInstanceRepo.findByCreatedBy(userName);
        List<AlertInstanceDTO> dtos = new ArrayList<AlertInstanceDTO>();
        for (AlertInstance alert : alerts) {
            dtos.add(new AlertInstanceDTO(alert));
        }
        return dtos;
    }

    @Override
    public AlertInstanceDTO findByAlertInstanceId(Long id) {
        AlertInstance alert = alertInstanceRepo.findByAlertInstanceId(id);
        if (alert != null)
            return new AlertInstanceDTO(alert);
        else
            return null;
    }

    @Override
    public List<AlertDTO> getAllAlerts() {
        List<Alert> alerts = alertRepo.findAll();
        List<AlertDTO> dtos = new ArrayList<AlertDTO>();
        for (Alert alert : alerts) {
            dtos.add(new AlertDTO(alert));
        }
        return dtos;
    }

    @Override
    public List<AlertGroupLogicDTO> getGroupLogicByAlertId(Long alerId) {
        List<AlertGroupLogicDTO> groupLogic = new ArrayList<AlertGroupLogicDTO>();
        List<AlertGroupLogic> list = alertGroupLogicRepo.findByAlertId(alerId);
        for (AlertGroupLogic alertTypeGroupLogic : list) {
            groupLogic.add(new AlertGroupLogicDTO(alertTypeGroupLogic));
        }
        return groupLogic;
    }

    @Override
    public List<AlertDTO> listAlerts() {
        List<Alert> alertList = alertRepo.findAll();
        List<AlertDTO> dtos = new ArrayList<AlertDTO>();
        for (Alert alert : alertList) {
            dtos.add(new AlertDTO(alert));
        }
        if (dtos.size() == 0)
            return null;
        return dtos;
    }

    @Override
    public AlertDTO getAlertById(Long alertId) {
        Alert alert = alertRepo.findByAlertId(alertId);
        if (alert != null)
            return new AlertDTO(alert);
        else
            return null;
    }

    @Override
    public List<InstanceGroupingParam> getInstanceGroupingParam(Long alertInstanceId) {
//		Pageable pageRequest = new PageRequest(pageNumber, pageSize);
        return instanceParamRepo.findByAlertInstanceId(alertInstanceId);
    }

    private String getCurrentUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserSRO) {
            UserSRO user = (UserSRO) principal;
            return user.getEmail();
        } else if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            return user.getUsername();
        }
        return null;
    }

    private AlertInstance convertToAlertFromAlertDto(AlertInstance alert, AlertInstanceDTO dto) {
        alert.setAlertId(dto.getAlertId());
        alert.setAlertTitle(dto.getAlertTitle());
        alert.setCurrentDateRangeEnd(dto.getCurrentDateRangeEnd());
        alert.setCurrentDateRangeStart(dto.getCurrentDateRangeStart());
        alert.setEmailId(dto.getEmailId());
        alert.setHistoricalDateRangeEnd(dto.getHistoricalDateRangeEnd());
        alert.setHistoricalDateRangeStart(dto.getHistoricalDateRangeStart());
        alert.setOperator(dto.getOperator());
        alert.setValue(dto.getValue());
        alert.setRuleTime(dto.getRuleTime());
        alert.setGroupLogicName(dto.getGroupLogicName());
        alert.setAlertInstanceTitle(dto.getAlertInstanceTitle());
        alert.setFileHeader(dto.getFileHeader());
        return alert;
    }
}
