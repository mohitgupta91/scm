package com.snapdeal.scm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private static final String           DATE_FORMAT           = "dd/MM/yyyy HH:mm";
    public static final  SimpleDateFormat ELASTIC_DATE_FORMAT   = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static final  SimpleDateFormat MONTH_DAY_YEAR_FORMAT = new SimpleDateFormat("MMM dd, yyyy");


    public static Date getCurrentTime() {
        return new Date();
    }

    public static long getDifferenceInMinutes(Date date1, Date date2) {
        return (date1.getTime() - date2.getTime()) / (1000 * 60);
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(date);
    }

    public static Date subtractFromDate(Date date, Integer quantity, TimeUnit timeUnit) {
        return addToDate(date, -quantity, timeUnit);
    }

    public static Date addToDate(Date date, Integer quantity, TimeUnit timeUnit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (TimeUnit.DAYS.equals(timeUnit)) {
            calendar.add(Calendar.DATE, quantity);
        } else if (TimeUnit.HOURS.equals(timeUnit)) {
            calendar.add(Calendar.HOUR, quantity);
        } else if (TimeUnit.MINUTES.equals(timeUnit)) {
            calendar.add(Calendar.MINUTE, quantity);
        } else if (TimeUnit.SECONDS.equals(timeUnit)) {
            calendar.add(Calendar.SECOND, quantity);
        } else if (TimeUnit.MILLISECONDS.equals(timeUnit)) {
            calendar.add(Calendar.MILLISECOND, quantity);
        } else {
            calendar.add(Calendar.MINUTE, ((Long) timeUnit.toMinutes(quantity)).intValue());
        }
        return calendar.getTime();
    }

    public static boolean isDiffGreaterThan(Date firstDate, Date secondDate, long minutes) {
        return Math.abs(firstDate.getTime() - secondDate.getTime()) > minutes * 60 * 1000;
    }

    public static boolean isDiffGreaterThan(Date firstDate, Date secondDate, long duration, TimeUnit timeUnit) {
        return Math.abs(firstDate.getTime() - secondDate.getTime()) > timeUnit.toMillis(duration);
    }

    public static boolean isDiffLessThan(Date firstDate, Date secondDate, long minutes) {
        return Math.abs(firstDate.getTime() - secondDate.getTime()) < minutes * 60 * 1000;
    }

    public static boolean isDiffLessThan(Date firstDate, Date secondDate, long duration, TimeUnit timeUnit) {
        return Math.abs(firstDate.getTime() - secondDate.getTime()) < timeUnit.toMillis(duration);
    }

    public static boolean isDiffInBetween(Date firstDate, Date secondDate, long startMinutes, long endMinutes) {
        return isDiffGreaterThan(firstDate, secondDate, startMinutes) && isDiffLessThan(firstDate, secondDate, endMinutes);
    }

    public static boolean isDiffInBetween(Date firstDate, Date secondDate, long startDuration, long endDuration, TimeUnit timeUnit) {
        return isDiffGreaterThan(firstDate, secondDate, startDuration, timeUnit) && isDiffLessThan(firstDate, secondDate, endDuration, timeUnit);
    }

    public static String getDateStringFromElastic(String dateFromElastic) throws ParseException {
        return MONTH_DAY_YEAR_FORMAT.format(ELASTIC_DATE_FORMAT.parse(dateFromElastic));
    }
}
