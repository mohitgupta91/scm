package com.snapdeal.scm.mongo.doc;

import com.snapdeal.scm.common.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * @author chitransh
 */
@Document(collection = "metrics_info")
public class MetricsInfo extends MongoDocument {

    @NotNull
    private Integer metricsId;
    private String title;
    private String implClass;
    private boolean usedForPieChart;

    public Integer getMetricsId() {
        return metricsId;
    }

    public void setMetricsId(Integer metricsId) {
        this.metricsId = metricsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImplClass() {
        return implClass;
    }

    public void setImplClass(String implClass) {
        this.implClass = implClass;
    }

    public boolean isUsedForPieChart() {
        return usedForPieChart;
    }

    public void setUsedForPieChart(boolean usedForPieChart) {
        this.usedForPieChart = usedForPieChart;
    }
}
