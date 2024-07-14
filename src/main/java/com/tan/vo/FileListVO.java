package com.tan.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileListVO {

    /** 文件名 */
    private String fileName;

    /** 文件类型 */
    private String fileType;

    /** 文件大小 */
    private Long fileSize;

    /** 上传时间 */
    private LocalDateTime uploadTime;

}
