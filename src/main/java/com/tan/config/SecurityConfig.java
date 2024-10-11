package com.tan.config;

import com.tan.interceptor.LoginInterceptor;
import com.tan.interceptor.RefreshInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by TanLiangJie
 * Time:2024/5/8 下午7:04
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {


    /**
     * 这里需要将两个变量传入拦截器中,不知道为啥在拦截器中自动注入不了
     */

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private MapperUser mapperUser;

    @Autowired
    private RefreshInterceptor refreshInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/login",
                        "/public/**",
                        "/email-code",
                        "/user/register",
                        "/getImage/**", //访问静态资源
                        "/user/resetPwd",
                        "/share/**"
                        ).order(1);
        registry.addInterceptor(refreshInterceptor)
                .excludePathPatterns("/public/**")
                .addPathPatterns("/**")
                .order(0);
    }

}
