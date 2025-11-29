package edu.sdccd.cisc190.kanban.util;

public class FileHelper {
    private static final long KILO = 1024;
    private static final String[] UNITS = new String[]{"Bytes", "KiB", "MiB", "GiB", "TiB"};

    public static String getHumanReadableSize(final long size) {
        int unitIndex = size == 0 ? 0 : (int) ( Math.log(size) / Math.log(KILO) );
        double relativeSize = size / Math.pow(KILO, unitIndex);

        if (unitIndex == 0) {
            return String.format("%.0f %s", relativeSize, UNITS[0]);
        } else {
            return String.format("%.1f %s", relativeSize, UNITS[unitIndex]);
        }
    }
}
