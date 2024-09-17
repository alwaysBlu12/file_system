package com.tan.utils;

public class FileUtils {
    public static String convertFileSize(long fileSize) {
        if (fileSize <= 0) {
            return "0 Bytes";
        }
        String[] units = new String[] {"Bytes", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(fileSize) / Math.log10(1024));
        return Math.round((double) fileSize / Math.pow(1024, digitGroups)) + units[digitGroups];
    }

    public static long convertToBytes(String fileSize) {
        if (fileSize == null || fileSize.isEmpty()) {
            return 0;
        }
        String[] units = new String[]{"Bytes", "KB", "MB", "GB", "TB"};
        long multiplier = 1;
        for (int i = 0; i < units.length; i++) {
            if (fileSize.toUpperCase().endsWith(units[i])) {
                multiplier = (long) Math.pow(1024, i);
                break;
            }
        }
        try {
            return Long.parseLong(fileSize.replaceAll("[^0-9.]", "")) * multiplier;
        } catch (NumberFormatException e) {
            return 0; // Or throw an exception, or handle the error as you see fit
        }
    }
}
