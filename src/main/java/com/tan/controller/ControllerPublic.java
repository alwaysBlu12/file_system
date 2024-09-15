package com.tan.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.tan.service.ServicePublic;
import com.tan.entity.EntityResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 公共Controller
 */
@Slf4j
@RestController
@RequestMapping("/public")
public class ControllerPublic {

    @Autowired
    private ServicePublic servicePublic;

    @GetMapping("/captcha")
    public EntityResult generateCaptcha() {
        return servicePublic.getCaptcha();
    }

    @GetMapping("/email-code")
    public EntityResult generateEmailCode(String email) {
        return servicePublic.sendEmailCode(email);
    }


}
