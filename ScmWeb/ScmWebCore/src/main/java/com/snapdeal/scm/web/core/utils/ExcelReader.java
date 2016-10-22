/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm.web.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.snapdeal.scm.mongo.doc.FileValuesDTO;
import com.snapdeal.scm.web.core.exception.FileUploadException;

/**
 * @version 1.0, 20-Apr-2016
 * @author ashwini
 */
public class ExcelReader {

	public static FileValuesDTO readExcelFile(MultipartFile file)
			throws IOException, FileUploadException {

		FileValuesDTO fileValue = new FileValuesDTO();

		InputStream inputStream = file.getInputStream();

		Workbook workbook = getWorkbook(inputStream, file.getOriginalFilename());
		Sheet firstSheet = workbook.getSheetAt(0);

		Iterator<Row> iterator = firstSheet.iterator();

		if (!iterator.hasNext()) {
			throw new FileUploadException("Empty File Uploded");
		}

		List<String> headers = new ArrayList<String>();
		iterator.next().forEach(cell -> headers.add(cell.getStringCellValue()));
		fileValue.setHeader(headers);
		List<Map<String, String>> headerDataMap = new ArrayList<Map<String, String>>();
		int size = fileValue.getHeader().size();

		while (iterator.hasNext()) {
			Map<String, String> dataMap = new HashMap<String, String>();
			Row row = iterator.next();
			if (row.getPhysicalNumberOfCells() == 0) {
				continue;
			}
			for (int i = 0; i < size; i++) {
				switch (row.getCell(i).getCellType()) {
				case Cell.CELL_TYPE_NUMERIC:
					dataMap.put(fileValue.getHeader().get(i), new Double(row
							.getCell(i).getNumericCellValue()).toString());
					break;
				case Cell.CELL_TYPE_STRING:
					dataMap.put(fileValue.getHeader().get(i), row.getCell(i)
							.getStringCellValue());
					break;
				}
			}
			headerDataMap.add(dataMap);
		}

		if (CollectionUtils.isEmpty(headerDataMap)) {
			throw new FileUploadException("No date in Uploded File");
		}

		fileValue.setHeaderDataMap(headerDataMap);
		workbook.close();
		inputStream.close();

		return fileValue;
	}

	private static Workbook getWorkbook(InputStream inputStream, String filename)
			throws IOException {
		Workbook workbook = null;

		if (filename.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else if (filename.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException(
					"The specified file is not Excel file");
		}

		return workbook;
	}

}
