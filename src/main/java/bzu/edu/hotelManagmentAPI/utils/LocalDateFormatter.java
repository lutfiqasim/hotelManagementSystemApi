package bzu.edu.hotelManagmentAPI.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateFormatter {

    static private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    static public LocalDate parseDate(String date) {
        LocalDate dateObj = null;
        try {
            dateObj = LocalDate.parse(date, dateFormatter);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd");
        }
        return dateObj;
    }

    static public LocalDateTime parseDateTime(String dateTime) {
        LocalDateTime dateObj = null;
        try {
            dateObj = LocalDateTime.parse(dateTime, dateFormatter);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd HH:mm");
        }
        return dateObj;
    }
}
