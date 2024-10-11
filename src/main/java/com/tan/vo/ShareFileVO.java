package com.tan.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShareFileVO {

    private String fileName;
    private String filePath;
    private String fileSize;
    private String fileType;
    private Integer fileId;
    private String shareId;
    private Integer userId;
    private String shareUser;
    private LocalDateTime expiryTime;

}
