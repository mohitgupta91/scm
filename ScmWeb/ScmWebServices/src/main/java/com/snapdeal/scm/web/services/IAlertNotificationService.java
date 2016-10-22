/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.services;

import java.util.List;

import com.snapdeal.scm.web.core.dto.AlertNotificationUpdateDTO;
import com.snapdeal.scm.web.core.sro.AlertNotificationSRO;

/**
 * @version 1.0, 13-Apr-2016
 * @author ashwini
 */
public interface IAlertNotificationService {

    public void updateNotification(AlertNotificationUpdateDTO dto);

    public List<AlertNotificationSRO> listAlertNotification();

    public AlertNotificationSRO viewNotification(Long notificationId);

    public List<AlertNotificationSRO> listAlertNotification(String userName);

}
