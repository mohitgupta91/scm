package com.snapdeal.scm.web.core.sro;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author mohit
 *
 */
public class InstanceGroupingParamSRO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Map<String,String>> fileData;
	
	public List<Map<String, String>> getFileData() {
		return fileData;
	}

	public void setFileData(List<Map<String, String>> fileData) {
		this.fileData = fileData;
	}

	@Override
	public String toString(){
		return "fileData ["+fileData+"]";
	}
}
