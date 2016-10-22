package com.snapdeal.scm.web.core.sro;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * Created by Harsh Gupta on 27/02/16.
 */

@JsonSerialize(using = PointSerializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PointSRO implements Serializable{

	private static final long serialVersionUID = -7191710901031348258L;

	private String x;

	private Double y;

	public PointSRO() {
	}

	public PointSRO(String x, Double y) {
		this.x = x;
		this.y = y;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}


	public void setY(Double y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PointSRO pointSRO = (PointSRO) o;

		if (x != null ? !x.equals(pointSRO.x) : pointSRO.x != null) return false;
		return y != null ? y.equals(pointSRO.y) : pointSRO.y == null;

	}

	@Override
	public int hashCode() {
		int result = x != null ? x.hashCode() : 0;
		result = 31 * result + (y != null ? y.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "PointSRO [x=" + x + ", y=" + y + "]";
	}
}
