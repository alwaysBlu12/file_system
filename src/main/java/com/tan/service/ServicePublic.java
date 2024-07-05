package com.tan.service;

import com.tan.utils.EntityResult;

public interface ServicePublic {
    EntityResult sendEmailCode(String email);
}
