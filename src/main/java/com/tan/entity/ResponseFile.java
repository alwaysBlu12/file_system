package com.tan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFile {
    private String fileName;
    private String filePath;
    private String fileType;
    private String fileSize;
}
