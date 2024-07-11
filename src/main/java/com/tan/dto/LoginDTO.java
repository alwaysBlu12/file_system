package com.tan.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;
    //这是图形验证码
    private String code;
}
