package ru.job4j.grabber.utils;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public class HabrCareerDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String dateTime) {
        return LocalDateTime.parse(dateTime, ISO_OFFSET_DATE_TIME);
    }
}
