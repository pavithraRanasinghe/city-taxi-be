package com.esoft.citytaxi.util;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return null;
        }
        int lastIndexOfDot = originalFilename.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            // No extension found
            return "";
        }
        return originalFilename.substring(lastIndexOfDot + 1);
    }
}
