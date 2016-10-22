/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.snapdeal.scm.web.core.response.BaseResponse;
import com.snapdeal.scm.web.core.sro.FilterSRO;
import com.snapdeal.scm.web.core.utils.Constants;
import com.snapdeal.scm.web.core.utils.StringUtils;
import com.snapdeal.scm.web.core.validation.SCMError;
import com.snapdeal.scm.web.services.IFilterService;

/**
 * @version 1.0, 25-Feb-2016
 * @author ashwini, prateek
 */
@Controller
public class FilterController {

    private static final Logger LOG            = LoggerFactory.getLogger(FilterController.class);
    public static final String  METRIC_ID_URL  = "/data/metrics/{module}/{submudule}/{metricId}/filter";
    public static final String  FILTER_KEY_URL = "/data/" + Constants.FILTER_URL + "{filterKey}";

    @Autowired
    private IFilterService filterService;

    @RequestMapping(METRIC_ID_URL)
    public @ResponseBody BaseResponse<List<FilterSRO>> getFilters(@NotNull @PathVariable(value = "module") String module,
            @NotNull @PathVariable(value = "submudule") String submudule, @NotNull @PathVariable(value = "metricId") String metricId) {
        BaseResponse<List<FilterSRO>> resp = new BaseResponse<List<FilterSRO>>();
        resp.setCode(HttpStatus.OK.value());
        if (StringUtils.isNotEmpty(metricId)) {
            List<FilterSRO> filterData = filterService.getFilterForMatricByid(metricId);
            resp.setData(filterData);
            return resp;
        }
        List<SCMError> errors = new ArrayList<SCMError>();
        SCMError error = new SCMError();
        error.setCode(HttpStatus.BAD_REQUEST.value());
        error.setDescription(HttpStatus.BAD_REQUEST.name());
        error.setFieldName("metricId");
        error.setMessage("metric Id Found null in request parameters.");
        errors.add(error);
        resp.setErrors(errors);
        return resp;
    }

    @RequestMapping(FILTER_KEY_URL)
    public @ResponseBody BaseResponse<List<FilterSRO>> getFilterByKey(@NotNull @PathVariable(value = "filterKey") String filterKey) {
        BaseResponse<List<FilterSRO>> resp = new BaseResponse<>();
        resp.setCode(HttpStatus.OK.value());
        if (StringUtils.isNotEmpty(filterKey)) {
            FilterSRO filterData = filterService.getFilterByKey(filterKey);
            List<FilterSRO> sros = new ArrayList<>();
            sros.add(filterData);
            resp.setData(sros);
            return resp;
        }
        List<SCMError> errors = new ArrayList<SCMError>();
        SCMError error = new SCMError();
        error.setCode(HttpStatus.BAD_REQUEST.value());
        error.setDescription(HttpStatus.BAD_REQUEST.name());
        error.setFieldName("filterKey");
        error.setMessage("filterKey Found null in request parameters.");
        errors.add(error);
        resp.setErrors(errors);
        return resp;
    }

}
