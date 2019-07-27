package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static LocalDate parseDate(String date){
        return date.equals(" ")?null:LocalDate.parse(date,DATE_FORMATTER);
    }

    public static LocalTime parseTime(String time){
        return time.equals(" ")?null:LocalTime.parse(time,TIME_FORMATTER);
    }
}
