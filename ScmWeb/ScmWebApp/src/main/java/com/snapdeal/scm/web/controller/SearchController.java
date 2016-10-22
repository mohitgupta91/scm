package com.snapdeal.scm.web.controller;

import com.google.common.collect.Lists;
import com.snapdeal.scm.web.core.enums.Metric;
import com.snapdeal.scm.web.core.request.SearchRequest;
import com.snapdeal.scm.web.core.response.SearchResponse;
import com.snapdeal.scm.web.services.IMetricDataService;
import com.snapdeal.scm.web.services.impl.DataValidationException;
import com.snapdeal.scm.web.validator.SearchRequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay, Harsh
 */

@Controller
@RequestMapping(SearchController.URL)
public class SearchController {

    public static final String URL = "/data/metrics";

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    IMetricDataService metricDataService;

    @RequestMapping(value = "/{module}/{submodule}/{metricID}/chartdata", method = RequestMethod.POST)
    public
    @ResponseBody
    SearchResponse getChartData(@Valid @RequestBody SearchRequest request,
                                @PathVariable("module") String module, @PathVariable("submodule") String submodule, @PathVariable("metricID") String metricID) {
        LOG.info("Search request : " + request);
        SearchResponse response = null;
        try {
            Metric metric = Metric.fromURL(module, submodule, metricID);
            if (metric == null) throw new DataValidationException("Invalid module-submodule-metricID combo provided.");
            response = metricDataService.getLevelData(request, metric);
        } catch (DataValidationException ex) {
            LOG.error("Search Error : ", ex);
            response = new SearchResponse();
            response.setCode(ex.getErrorCode());
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            LOG.error("Search Error : ", ex);
            response = new SearchResponse();
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(ex.getMessage());
        }
        LOG.info("Search response : " + response);
        return response;
    }

    @InitBinder
    protected void initWebCommonBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.setValidator(new SearchRequestValidator());
    }
}
