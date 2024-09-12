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
}
