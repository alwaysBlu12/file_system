package com.tan.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class PageFileDTO {
    private Integer currentPage;
    private Integer pageSize;
    private String fileType;
    private String fileName;
    private Integer spaceId;
    private Integer isDelete;
}
