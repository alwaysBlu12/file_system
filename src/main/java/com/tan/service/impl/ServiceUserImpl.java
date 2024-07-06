package com.tan.service.impl;

import com.tan.dto.LoginDTO;
import com.tan.dto.RegisterDTO;
import com.tan.entity.EntityUser;
import com.tan.mapper.MapperUser;
import com.tan.service.ServiceUser;
import com.tan.entity.EntityResult;
import com.tan.utils.JwtUtils;
import com.tan.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Slf4j
@Service
public class ServiceUserImpl implements ServiceUser {
    
    @Autowired
    private MapperUser mapperUser;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    /**
     * 登录
     * @param loginDTO
     * @return
     */
    @Override
    public EntityResult login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        //判断该用户是否存在
        EntityUser user = mapperUser.getUserByUsername(username);
        if (Objects.isNull(user)) {
            return EntityResult.error("当前用户不存在");
        }
        //密码是否正确
        if(!Md5Util.getMD5String(loginDTO.getPassword()).equals(user.getPassword())){
            return EntityResult.error("密码错误");
        }
        //验证码是否正确
        String captcha = stringRedisTemplate.opsForValue().get("captcha");

        if (!captcha.equals(loginDTO.getImageCode())){
            return EntityResult.error("验证码错误");
        }

        //登录成功,生成token
        Map<String, Object> claims  =new HashMap<>();
        claims.put("username", username);
        claims.put("userId", user.getUserId().toString());
        claims.put("email", user.getEmail());


        String token = JwtUtils.generateJwt(claims);

        //存入redis
        stringRedisTemplate.opsForValue().set(username, token);

        return EntityResult.success(token);
    }

    /**
     * 注册
     * @param registerDTO
     * @return
     */
    @Override
    public EntityResult register(RegisterDTO registerDTO) {

        String username = registerDTO.getUsername();

        //查询该用户
        EntityUser user =  mapperUser.getUserByUsername(username);
        //存在
        if (Objects.nonNull(user)){
            return EntityResult.error("当前用户已经存在");
        }
        
        //两次密码
        if (!registerDTO.getPassword().equals(registerDTO.getRePassword())){
            return EntityResult.error("两次密码不一致");
        }
        
        //不存在
        //校验验证码
        String code = registerDTO.getCode();

        String redisCode = stringRedisTemplate.opsForValue().get(registerDTO.getEmail());
        
        if (!code.equals(redisCode)){
            return EntityResult.error("验证码不正确");
        }
        
        //注册
        
        //加密
        String md5String = Md5Util.getMD5String(registerDTO.getPassword());
        registerDTO.setPassword(md5String);

        EntityUser user1 = new EntityUser();
        BeanUtils.copyProperties(registerDTO,user1);

        user1.setPassword(md5String);
        user1.setAvatarUrl("default");

        mapperUser.save(user1);

        return EntityResult.success();

    }
}
