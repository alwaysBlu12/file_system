package com.tan.dto;

import lombok.Data;

@Data
public class CreateShareDTO {

    private Integer fileId;
    private Integer expiryTime;
    private String filePassword;
}
