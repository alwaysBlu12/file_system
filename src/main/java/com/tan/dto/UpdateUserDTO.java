package com.tan.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private Integer userId;
    private String username;
    private String email;
}
