package edu.sdccd.cisc190.kanban.util;

import java.nio.file.Path;

public class FileHelper {
    private static final long KILO = 1024; // Use 10
    private static final String[] UNITS = new String[]{"Bytes", "KB", "MB", "GB", "TB"};

    public static String getHumanReadableSize(final long size) {
        int unitIndex = size == 0 ? 0 : (int) ( Math.log(size) / Math.log(KILO) );
        double relativeSize = size / Math.pow(KILO, unitIndex);

        if (unitIndex == 0) {
            return String.format("%.0f %s", relativeSize, UNITS[0]);
        } else {
            return String.format("%.1f %s", relativeSize, UNITS[unitIndex]);
        }
    }

    public static boolean isFileAnImage(final Path filePath) {
        final String fileName = filePath.getFileName().toString();
        final String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

        return switch (fileExtension.toLowerCase()) {
            case "png", "jpg", "jpeg", "gif", "bmp" -> true;
            default -> false;
        };
    }
}
