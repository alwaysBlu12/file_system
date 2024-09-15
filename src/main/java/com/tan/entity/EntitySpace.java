package com.tan.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntitySpace {
    private Integer spaceId;

    private Integer userId;

    private Integer fileCount;

    private Long usedSpace;

    private Long totalSpace;
}
