package com.tan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class EntityUser {
    /** 用户ID */
    private Integer userId;

    /** 用户名 */
    private String username;

    /** 邮箱 */
    private String email;

    /** 密码 */
    @JsonIgnore
    private String password;

    /** 头像URL */
    private String avatarUrl;

    /**权限**/
    private Integer isAdmin;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
