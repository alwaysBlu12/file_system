package com.tan.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String rePassword;
    private String email;
    private String code;
}
