package com.snapdeal.scm.app.service;

import org.springframework.web.multipart.MultipartFile;

import com.snapdeal.scm.core.dto.ServiceResponse;
/**
 * @author siddhant, prateek, chitransh
 */
@SuppressWarnings("rawtypes")
public interface IBulkUploadService {

    void laneGroupBulkUploader(String filePath);

    void metroCitiesBulkUploader(String filePath);

	ServiceResponse shippedNotConnectedMetricsUploader(MultipartFile file);

    ServiceResponse connectedNotReachedMetricsUploader(MultipartFile file);

    ServiceResponse reachedNotOFDMetricsUploader(MultipartFile file);

    ServiceResponse oFDNotAttemptedMetricsUploader(MultipartFile file);

    ServiceResponse attemptedNotDeliveredMetricsUploader(MultipartFile file);

    ServiceResponse attemptedNotDelNonIntgrMetricsUploader(MultipartFile file);

    ServiceResponse shippedNotAttemptNonIntgrMetricsUploader(MultipartFile file);

    ServiceResponse uD1NotUD2MetricsUploader(MultipartFile file);

    ServiceResponse uD2NotUD3MetricsUploader(MultipartFile file);

    ServiceResponse uD3NotUD4MetricsUploader(MultipartFile file);

    ServiceResponse uD4NotDelMetricsUploader(MultipartFile file);

    /**
     * For testing purpose only
     * To create sample data
     */
    void createDummyDataBulkUploader(String filePath);

    void uploadSupcBulkUploader(String supcFilePath, String categoryFilePath, String outputFilePath);

    void elasticDataBulkUploader(String filePath);

}
