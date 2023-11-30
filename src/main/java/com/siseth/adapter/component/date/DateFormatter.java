package com.siseth.adapter.component.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class DateFormatter {


    private final static DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static LocalDateTime formatDefault(String date) {
        return date != null ?
                LocalDateTime.parse(date, defaultFormatter) :
                null;
    }

    public static String formatDahua(String localDate) {
        return LocalDateTime
                .of(LocalDate.parse(localDate), LocalTime.of(0,0))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd%20HH:mm:ss"));
    }

    public static String formatDahua(LocalDate localDate) {
        return LocalDateTime
                .of(localDate, LocalTime.of(0,0))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd%20HH:mm:ss"));
    }

}
