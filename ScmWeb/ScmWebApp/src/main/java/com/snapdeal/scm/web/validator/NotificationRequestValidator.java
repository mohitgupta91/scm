/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.snapdeal.scm.mongo.doc.AlertNotification.NotificationStatus;
import com.snapdeal.scm.web.core.dto.AlertNotificationUpdateDTO;

/**
 * @version 1.0, 14-Apr-2016
 * @author ashwini
 */
public class NotificationRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return AlertNotificationUpdateDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        AlertNotificationUpdateDTO dto = (AlertNotificationUpdateDTO) object;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "notificationId", "No notificationId given");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "comment", "No comment given");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "updatedBy", "No updated By");
        if (null != dto.getStatus()) {
            try {
                NotificationStatus.valueOf(dto.getStatus());
            } catch (Exception e) {
                errors.reject("Incorrect Notification Status");
            }
        }
    }
}
