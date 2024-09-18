package com.tan.dto;

import lombok.Data;

@Data
public class UpdatePwdDTO {

    private String rawPassword;

    private String newPassword;

    private String rePassword;

}
