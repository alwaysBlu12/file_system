package com.tan.interceptor;

import com.tan.entity.EntityUser;
import com.tan.mapper.MapperUser;
import com.tan.utils.JwtUtils;
import com.tan.utils.UserThreadLocal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {

    private MapperUser mapperUser;
    private StringRedisTemplate stringRedisTemplate;

    public MyInterceptor(MapperUser mapperUser, StringRedisTemplate stringRedisTemplate) {
        this.mapperUser = mapperUser;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        log.info("'拦截到了");

        //获取请求头的token
        String token = request.getHeader("Authorization");
        //判断token
        if(Objects.isNull(token)){
            response.setStatus(401);
            throw new RuntimeException("当前未登录");
        }

        //解析token
        Claims claims = JwtUtils.parseJWT(token);
        String username = (String) claims.get("username");
        //获取redis的token
        String redisToken = stringRedisTemplate.opsForValue().get(username);

        if(!redisToken.equals(token)||redisToken==null){
            response.setStatus(401);
            throw new RuntimeException("当前token失效");
        }

        //获取当前用户
        EntityUser user = mapperUser.getUserByUsername(username);
        UserThreadLocal.put(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
