package com.snapdeal.scm.core.elastic.dto;

import java.util.Date;

import com.snapdeal.scm.web.core.enums.FilterKey;

/**
 * ElasticDateRangeFilter : Elastic Date Range Filter
 * 
 * @author pranav, prateek
 *
 */
public class ElasticDateRangeFilter {
	
	private FilterKey filterKey;
	private Date fromDate;
	private Date toDate;
	private boolean includeUpper;
	private boolean includeLower;
	
	public ElasticDateRangeFilter(){
		
	}
	
	public ElasticDateRangeFilter(FilterKey columnName, Date fromDate,
			Date toDate, boolean includeUpper, boolean includeLower) {
		this.filterKey = columnName;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.includeUpper = includeUpper;
		this.includeLower = includeLower;
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
	@Override
	public String toString() {
		return "ElasticDateRangeFilter [columnName=" + filterKey
				+ ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", includeUpper=" + includeUpper + ", includeLower="
				+ includeLower + "]";
	}

	public FilterKey getFilterKey() {
		return filterKey;
	}

	public void setFilterKey(FilterKey filterKey) {
		this.filterKey = filterKey;
	}
}
