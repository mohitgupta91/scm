package com.snapdeal.scm.app.web;

import com.snapdeal.scm.app.service.IBulkUploadService;
import com.snapdeal.scm.core.dto.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author siddhant, prateek, chitransh
 */
@RestController
@RequestMapping(value = "/bulkUpload")
public class BulkUploadController {

    @Autowired
    IBulkUploadService bulkUploadService;

    @RequestMapping(value = "/laneGroup", method = RequestMethod.POST)
    @ResponseBody
    public void laneGroupBulkUploader(@RequestBody String filePath) {
        bulkUploadService.laneGroupBulkUploader(filePath);
    }

    @RequestMapping(value = "/metrocities", method = RequestMethod.POST)
    @ResponseBody
    public void metroCitiesBulkUploader(@RequestBody String filePath) {
        bulkUploadService.metroCitiesBulkUploader(filePath);
    }

    @RequestMapping(value = "/shippedNotConnectedMetrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse shippedNotConnectedMetricsBulkUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.shippedNotConnectedMetricsUploader(file);
    }

    @RequestMapping(value = "/connectedNotReachedMetrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse connectedNotReachedMetricsBulkUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.connectedNotReachedMetricsUploader(file);
    }

    @RequestMapping(value = "/reachedNotOutForDeliveryMetrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse reachedNotOutForDeliveryMetricsBulkUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.reachedNotOFDMetricsUploader(file);
    }

    @RequestMapping(value = "/outForDeliveryNotAttemptedMetrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse outForDeliveryNotAttemptedMetricsBulkUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.oFDNotAttemptedMetricsUploader(file);
    }

    @RequestMapping(value = "/attemptedNotDeliveredMetrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse attemptedNotDeliveredMetricsBulkUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.attemptedNotDeliveredMetricsUploader(file);
    }

    @RequestMapping(value = "/attemptedNotDelNonIntgrMetrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse attemptedNotDelNonIntgrMetricsUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.attemptedNotDelNonIntgrMetricsUploader(file);
    }

    @RequestMapping(value = "/shippedNotAttemptNonIntgrMetrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse shippedNotAttemptNonIntgrMetricsUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.shippedNotAttemptNonIntgrMetricsUploader(file);
    }

    @RequestMapping(value = "/uD1NotUD2Metrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse uD1NotUD2MetricsUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.uD1NotUD2MetricsUploader(file);
    }

    @RequestMapping(value = "/uD2NotUD3Metrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse uD2NotUD3MetricsUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.uD2NotUD3MetricsUploader(file);
    }

    @RequestMapping(value = "/uD3NotUD4Metrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse uD3NotUD4MetricsUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.uD3NotUD4MetricsUploader(file);
    }

    @RequestMapping(value = "/uD4NotDelMetrics", method = RequestMethod.POST, consumes = {"multipart/mixed"})
    @ResponseBody
    public ServiceResponse uD4NotDelMetricsUploader(@RequestBody MultipartFile file) {
        return bulkUploadService.uD4NotDelMetricsUploader(file);
    }

    /**
     * For testing purpose only To create sample data
     */
    @RequestMapping(value = "/uploadsupc", method = RequestMethod.POST)
    @ResponseBody
    public void uploadSupcBulkUploader(@RequestBody Map<String, String> files) {
        bulkUploadService.uploadSupcBulkUploader(files.get("mto"), files.get("category"), files.get("output"));
    }

    @RequestMapping(value = "/elasticdummydata", method = RequestMethod.POST)
    @ResponseBody
    public void elasticDataBulkUploader(@RequestBody String filePath) {
        bulkUploadService.elasticDataBulkUploader(filePath);
    }
}