package com.snapdeal.scm.web.core.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author prateek, vinay, chitransh
 */
public class StringUtils {

    public static final String EMPTY_STRING = "";
    public static final char   DASH         = '-';
    public static final char   COMMA        = ',';
    public static final String UNDERSCORE   = "_";
    public static final String  COMMA_STRING = ",";

    public static void main(String[] args) {
        System.out.println(isEmpty("a"));
    }

    public static boolean isEmpty(String value) {
        //		return Optional.ofNullable(value).filter(val -> val.equals(EMPTY_STRING)).isPresent();
        if (value == null || value.trim().equals(EMPTY_STRING))
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
        return split(input, COMMA_STRING);
    }

    public static String getTextOrNull(String input){
        String trimmedString;
        if (input == null || EMPTY_STRING.equals(trimmedString = input.trim())) return null;
        return trimmedString;
    }
}
