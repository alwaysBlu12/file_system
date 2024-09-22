package com.tan.dto;

import lombok.Data;

@Data
public class ResetPwdDTO {

    private String username;
    private String password;
    private String email;
    private String code;

}
