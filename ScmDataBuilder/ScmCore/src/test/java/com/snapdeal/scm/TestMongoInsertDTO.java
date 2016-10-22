/**
 *  Copyright 2016 Jasper Infotech (P) Limited . All Rights Reserved.
 *  JASPER INFOTECH PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.snapdeal.scm;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.snapdeal.scm.core.annotation.Order;
import com.snapdeal.scm.core.enums.QueryType;
import com.snapdeal.scm.core.mongo.document.DtoQueryFieldMap;
import com.snapdeal.scm.core.mongo.document.QueryDTOFieldMapping;
import com.snapdeal.scm.das.mongo.dao.QueryDTOFieldMappingRepository;

/**
 * @version 1.0, 24-Feb-2016
 * @author ashwini
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestScmDataJpaApplication.class)
@EnableMongoAuditing
public class TestMongoInsertDTO {

	@Autowired
	private QueryDTOFieldMappingRepository repo;

	@Before
	public void setUp() {
		repo.deleteAll();
		List<Class<?>> classes = ClassFinder.find("com.snapdeal.scm.core.dto.impl");

		classes.forEach(clazz -> {
			QueryDTOFieldMapping mapping = new QueryDTOFieldMapping();
			Object obj;
			try {
				obj = clazz.newInstance();

				mapping.setJobClass(clazz.getName());
				Object invoke = obj.getClass().getDeclaredMethod("getQueryType").invoke(obj);
				mapping.setJobName((QueryType) invoke);
				List<DtoQueryFieldMap> list = new ArrayList<>();
				for (Field field : obj.getClass().getDeclaredFields()) {
					Order order = field.getAnnotation(Order.class);
					if (null != order) {
						DtoQueryFieldMap map = new DtoQueryFieldMap();
						map.setField(field.getName());
						map.setFieldType(field.getType().getName());
						map.setQueryFieldIndex(order.order());
						list.add(map);
					}
				}
				mapping.setFieldMapping(list);
				mapping.setCreated(new Date());
				mapping.setUpdated(new Date());
				repo.save(mapping);
			} catch (Exception e) {
				System.out.println("Not convet for clazz" + clazz);
			}
		});

	}

	@Test
	public void test() {
		repo.findAll().forEach(dto -> System.out.println(dto));
	}
}

class ClassFinder {

	private static final char PKG_SEPARATOR = '.';

	private static final char DIR_SEPARATOR = '/';

	private static final String CLASS_FILE_SUFFIX = ".class";

	private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

	public static List<Class<?>> find(String scannedPackage) {
		String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
		URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
		if (scannedUrl == null) {
			throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
		}
		File scannedDir = new File(scannedUrl.getFile());
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (File file : scannedDir.listFiles()) {
			classes.addAll(find(file, scannedPackage));
		}
		return classes;
	}

	private static List<Class<?>> find(File file, String scannedPackage) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String resource = scannedPackage + PKG_SEPARATOR + file.getName();
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				classes.addAll(find(child, resource));
			}
		} else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
			int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
			String className = resource.substring(0, endIndex);
			try {
				classes.add(Class.forName(className));
			} catch (ClassNotFoundException ignore) {
			}
		}
		return classes;
	}

}