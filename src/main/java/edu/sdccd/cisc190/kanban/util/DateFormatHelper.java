package edu.sdccd.cisc190.kanban.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatHelper {
    public static String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
        return date.format(formatter);
    }
}
