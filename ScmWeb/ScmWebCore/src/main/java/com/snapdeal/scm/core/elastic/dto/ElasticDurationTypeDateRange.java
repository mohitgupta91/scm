package com.snapdeal.scm.core.elastic.dto;

import java.util.Date;

import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
/**
 * ElasticDurationTypeDateRange :Date range for data display
 * 
 * @author pranav
 *
 */
public class ElasticDurationTypeDateRange {
	private String columnName;
	private Date fromDate;
	private Date toDate;
	private boolean includeUpper;
	private boolean includeLower;
	private DateHistogramInterval dateHistogramInterval = DateHistogramInterval.DAY;
	
	public ElasticDurationTypeDateRange(String columnName, Date fromDate,
			Date toDate, boolean includeUpper, boolean includeLower, DateHistogramInterval dateHistogramInterval) {
		this.columnName = columnName;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.includeUpper = includeUpper;
		this.includeLower = includeLower;
		this.dateHistogramInterval=dateHistogramInterval;
	}
	
	public ElasticDurationTypeDateRange(){
		
	}

	public ElasticDurationTypeDateRange(Date dStart, Date dEnd,
			DateHistogramInterval interval, boolean includeUpper,
			boolean includeLower) {
		this.fromDate = dStart;
		this.toDate = dEnd;
		this.dateHistogramInterval=interval;
		this.includeUpper = includeUpper;
		this.includeLower = includeLower;
	}

	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public boolean isIncludeUpper() {
		return includeUpper;
	}
	public void setIncludeUpper(boolean includeUpper) {
		this.includeUpper = includeUpper;
	}
	public boolean isIncludeLower() {
		return includeLower;
	}
	public void setIncludeLower(boolean includeLower) {
		this.includeLower = includeLower;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public DateHistogramInterval getDateHistogramInterval() {
		return dateHistogramInterval;
	}

	public void setDateHistogramInterval(DateHistogramInterval dateHistogramInterval) {
		this.dateHistogramInterval = dateHistogramInterval;
	}

	@Override
	public String toString() {
		return "ElasticDurationTypeDateRange [columnName=" + columnName
				+ ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", includeUpper=" + includeUpper + ", includeLower="
				+ includeLower + ", dateHistogramInterval="
				+ dateHistogramInterval + "]";
	}
}
