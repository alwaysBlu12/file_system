package com.tan.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String email;
    private String password;
    //这是邮箱验证码
    private String code;
}
