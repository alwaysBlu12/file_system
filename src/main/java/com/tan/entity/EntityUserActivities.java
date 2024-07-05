package com.tan.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户活动实体类
 */
@Data
public class EntityUserActivities {
    /** 活动ID */
    private Integer activityId;

    /** 用户ID */
    private Integer userId;

    /** 活动类型 */
    private String activityType;

    /** 文件ID */
    private Integer fileId;

    /** 活动时间 */
    private LocalDateTime activityTime;

    /** 活动结果 */
    private String activityResult;
}
