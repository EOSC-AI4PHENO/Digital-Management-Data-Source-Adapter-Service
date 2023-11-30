package com.siseth.adapter.constant;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime moveOfOffsetLocalDateTime(LocalDateTime localDateTime) {
        ZoneOffset offset = getLocalDateTimeZoneOffset(localDateTime);
        return localDateTime.plusSeconds(offset.getTotalSeconds());
    }

    public static OffsetDateTime translateDate(LocalDateTime localDateTime) {
        return localDateTime.atOffset(getLocalDateTimeZoneOffset(localDateTime));
    }

    public static ZoneOffset getLocalDateTimeZoneOffset(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneOffset.systemDefault()).getOffset();
    }

    public static String getLocalDateInStringFormat(LocalDateTime date) {
        return date.format(dateTimeFormatter);
    }

    public static String getOffDateInStringFormat(OffsetDateTime date) {
        return date.format(dateTimeFormatter);
    }

    public static String calculateDuration(LocalDateTime valueStartDate, LocalDateTime valueEndDate) {
        Duration duration = Duration.between(valueStartDate, valueEndDate);
        long hours = duration.toHours();
        long minutes = (duration.toMinutes() % 60);
        long seconds = (duration.toSeconds() % 60);
        if (hours > 99)
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
