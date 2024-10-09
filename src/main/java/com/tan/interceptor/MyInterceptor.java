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

import static com.tan.utils.RedisConstants.REDIS_USER;

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
        System.out.println(request.getRequestURI());
        //获取请求头的token
        String token = request.getHeader("Authorization");
        log.info("token:{}", token);
        //判断token
        if(Objects.isNull(token)){
            System.out.println("当前token为空");
            response.setStatus(401);
            response.getWriter().write("xxx");
            return false;
        }

        //解析token
        Claims claims = JwtUtils.parseJWT(token);
        String userId = (String) claims.get("userId");
        //获取redis的token
        String redisToken = stringRedisTemplate.opsForValue().get(REDIS_USER+userId);

        // 判断redisToken是否存在
        if (redisToken == null) {
            response.setStatus(401);
            throw new RuntimeException("当前token失效");
        }

        // 判断redisToken与请求中的token是否一致
        if (!redisToken.equals(token)) {
            response.setStatus(401);
            throw new RuntimeException("当前token失效");
        }

        //获取当前用户
        //修改当前账户名字后,这里就搜不到了
        EntityUser user = mapperUser.getUserById(Integer.valueOf(userId));
        log.info("user:{}", user);
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
