package com.tan.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileListVO {

    private Integer fileId;

    /** 文件名 */
    private String fileName;

    /** 文件类型 */
    private String fileType;

    /** 文件大小 */
    private String fileSize;

    /** 文件路径  做预览功能*/
    private String filePath;

    /** 上传时间 */
    private LocalDateTime updateTime;

}
