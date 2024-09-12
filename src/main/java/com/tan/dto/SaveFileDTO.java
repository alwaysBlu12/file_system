package com.tan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SaveFileDTO {

    /** 文件名 */
    private String fileName;

    /** 文件类型 */
    private String fileType;

    /** 文件大小 */
    private String fileSize;

    /** 文件路径 */
    private String filePath;
}
