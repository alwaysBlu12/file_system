package com.tan.interceptor;

import com.tan.entity.EntityUser;
import com.tan.utils.UserThreadLocal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断当前用户是否存在
        EntityUser user = UserThreadLocal.get();
        if (user == null) {
            response.setStatus(401);
            return false;
        }
        return true;
    }

}
