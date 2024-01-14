package com.bigbro.wwooss.v1.utils;

import com.bigbro.wwooss.v1.dto.WeekInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Component
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

    /**
     * N주차, N요일의 일수 구하기
     *
     * @param year : 검색 연도
     * @param month : 검색 월
     * @param weekOfMonth : N 주차
     * @param dayOfWeek : 일 ~ 토 [ 1 ~ 7 ]
     * @return N주차 N요일의 며칠
     */
    public Calendar getDayAtWeekOfMonth(int year, int month, int weekOfMonth, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // month 0 부터 시작
        calendar.set(Calendar.WEEK_OF_MONTH, weekOfMonth);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        return calendar;
    }

    /**
     * 날짜 N 주차 구하기
     *
     * @param date : 검색 날짜
     * @return year, month, weekOfMonth
     */
    public WeekInfo getCurrentWeekOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1; // calendar에서의 월은 0부터 시작
        int day = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);

        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(4);

        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

        // 첫 주에 해당하지 않는 주의 경우 전 달 마지막 주차로 계산
        if (weekOfMonth == 0) {
            calendar.add(Calendar.DATE, -day); // 전 달의 마지막 날 기준
            return getCurrentWeekOfMonth(calendar.getTime());
        }

        // 마지막 주차의 경우
        if (weekOfMonth == calendar.getActualMaximum(Calendar.WEEK_OF_MONTH)) {
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE)); // 이번 달의 마지막 날
            int lastDaysDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 이번 달 마지막 날의 요일

            // 마지막 날이 월~수 사이이면 다음달 1주차로 계산
            if (lastDaysDayOfWeek >= Calendar.MONDAY && lastDaysDayOfWeek <= Calendar.WEDNESDAY) {
                calendar.add(Calendar.DATE, 1); // 마지막 날 + 1일 => 다음달 1일
                return getCurrentWeekOfMonth(calendar.getTime());
            }
        }
        return WeekInfo.of(year, month, weekOfMonth);
    }

    public Date convertToDate(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }
}
