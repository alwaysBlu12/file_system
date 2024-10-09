package com.tan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
    private String fileSize;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /** 文件路径 */
    private String filePath;

    /**
     * 空间id
     */
    private Integer spaceId;

    /**
     * 0代表没有删除,1代表删除
     */
    private Integer isDelete;

    private List<String> suggestion;

    public EntityFile() {
    }

    public EntityFile(EntityFile entityFile) {
        this.fileId = entityFile.getFileId();
        this.fileName = entityFile.getFileName();
        this.fileType = entityFile.getFileType();
        this.userId = entityFile.getUserId();
        this.fileSize = entityFile.getFileSize();
        this.updateTime = entityFile.getUpdateTime();
        this.filePath = entityFile.getFilePath();
        this.spaceId = entityFile.getSpaceId();
        this.isDelete = entityFile.getIsDelete();
        this.suggestion = Arrays.asList(this.fileName,this.fileType);
    }
}
