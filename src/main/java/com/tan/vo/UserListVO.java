package com.tan.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserListVO {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /** 密码 */
    /**
     * @JsonIgnore 注解的作用是告诉 Jackson JSON 处理库在序列化对象到 JSON 时忽略特定的字段。
     * 这个注解不会影响数据库查询操作，
     * 它只作用于序列化过程，即当你的后端服务准备将对象转换为 JSON 格式以响应前端请求时。
     */
    @JsonIgnore
    private String password;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 权限
     **/
    private Integer isAdmin;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private double percent;

    private Integer fileCount;
}
