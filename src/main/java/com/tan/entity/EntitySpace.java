package com.tan.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntitySpace {
    private Integer spaceId;
    private String spaceName;
    private String description;
    private Integer userId;
    private Integer fileCount;
    private Long usedSpace;
    private Long totalSpace;
    private LocalDateTime createTime;
}
