package com.bigbro.wwooss.v1.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtil {

    public static final String SIMPLE_FORMAT_DATE_TIME = "yyyyMMddHHmmss";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String YEAR_MONTH_DAY_FORMAT = "yyyy-MM-dd";
    public static final String MONTH_DAY_WEEK_FORMAT_KO = "MM월 dd일 (E)";

    public static LocalDateTime parseStringToDate(String pattern, String strDate) {
        return LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern(pattern).withLocale(Locale.forLanguageTag("ko-KR")));
    }

    public static String getCurrentDate(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
}
