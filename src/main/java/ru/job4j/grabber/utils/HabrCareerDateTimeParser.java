package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public class HabrCareerDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String dateTime) {
        LocalDateTime parsedDate = LocalDateTime.parse(dateTime, ISO_OFFSET_DATE_TIME);
        return parsedDate;
    }
}
