package com.tan.service;

import com.tan.entity.EntityResult;

public interface ServicePublic {
    EntityResult sendEmailCode(String email);

    /**
     * 图形验证码
     * @return
     */
    EntityResult getCaptcha();
}
