package com.tan.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文件实体类
 */
@Data
public class EntityFile {
    /** 文件ID */
    private Integer fileId;

    /** 文件名 */
    private String fileName;

    /** 文件类型 */
    private String fileType;

    /** 上传者ID */
    private Integer userId;

    /** 文件大小 */
    private Long fileSize;

    /** 上传时间 */
    private LocalDateTime uploadTime;

    /** 文件路径 */
    private String filePath;
}
