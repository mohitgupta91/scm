package com.snapdeal.scm.core.utils;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author prateek
 *
 */
public class StringUtils {

	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	public static final char DASH = '-';
	public static final char COMMA = ',';
	public static final String UNDERSCORE = "_";
	public static final String COLON = ":";
	public static final String DOUBLE_COLON = "::";
	public static final String NULL = "null";

	public static boolean isEmpty(String value) {
		//		return Optional.ofNullable(value).filter(val -> val.equals(EMPTY_STRING)).isPresent();
		if(value == null || value.equals(EMPTY_STRING))
			return true;
		return false;
	}
	
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	public static List<String> split(final String input, final String regex) {
		return Arrays.asList(input.split(regex));
	}

	public static List<String> split(final String input) {
		return split(input, ",");
	}
	
	public static boolean equalIgnorecase(final String key1, final String key2) {
		if(isEmpty(key1) || isEmpty(key2))
			return false;
		return key1.equalsIgnoreCase(key2);
	}

	public static String removeNonWordChars(String input) {
		StringBuilder output = new StringBuilder();
		char[] cinput = input.toCharArray();
		for (char element : cinput) {
			if (Character.isLetterOrDigit(element)) {
				output.append(element);
			}
		}
		return output.toString();
	}

}
