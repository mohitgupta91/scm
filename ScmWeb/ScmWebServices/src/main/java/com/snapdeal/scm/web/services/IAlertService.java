package com.snapdeal.scm.web.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.snapdeal.scm.mongo.doc.InstanceGroupingParam;
import com.snapdeal.scm.web.core.dto.AlertDTO;
import com.snapdeal.scm.web.core.dto.AlertGroupLogicDTO;
import com.snapdeal.scm.web.core.dto.AlertInstanceDTO;
import com.snapdeal.scm.web.core.exception.FileUploadException;

/**
 * @author pranav,mohit
 */
public interface IAlertService {

    public AlertInstanceDTO create(AlertInstanceDTO dto) throws IOException, FileUploadException;

    public AlertInstanceDTO edit(AlertInstanceDTO dto,List<Map<String,String>> editedRows,List<String> deletedRowsId)  throws IOException, FileUploadException;

    public List<AlertInstanceDTO> listAlertInstances(String userName);

    public AlertInstanceDTO findByAlertInstanceId(Long id);

    public List<AlertDTO> getAllAlerts();

    public List<AlertGroupLogicDTO> getGroupLogicByAlertId(Long alerTypeId);

    public void delete(Long alertId);

    public List<AlertDTO> listAlerts();

    public AlertDTO getAlertById(Long alertId);
    
    public List<InstanceGroupingParam> getInstanceGroupingParam(Long alertInstanceId);  
}
