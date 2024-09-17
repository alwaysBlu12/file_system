package com.tan.dto;

import lombok.Data;


@Data
public class UpdateFileDTO {

    /** 文件ID */
    private Integer fileId;

    /** 文件名 */
    private String fileName;


    /** 空间id */
    private Integer spaceId;

}
