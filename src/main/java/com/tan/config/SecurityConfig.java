package com.tan.config;

import com.tan.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by TanLiangJie
 * Time:2024/5/8 下午7:04
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())
                .excludePathPatterns(
                        "/user/login",
                        "/captcha",
                        "/code"
                        );
    }

}
