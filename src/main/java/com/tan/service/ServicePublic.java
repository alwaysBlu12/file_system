package com.tan.service;

import com.tan.entity.EntityResult;

public interface ServicePublic {
    EntityResult sendEmailCode(String email);
}
