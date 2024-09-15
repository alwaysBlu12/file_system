package com.tan.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.tan.dto.LoginDTO;
import com.tan.dto.RegisterDTO;
import com.tan.dto.SaveUserDTO;
import com.tan.dto.UpdateUserDTO;
import com.tan.entity.EntitySpace;
import com.tan.entity.EntityUser;
import com.tan.mapper.MapperSpace;
import com.tan.mapper.MapperUser;
import com.tan.service.ServiceUser;
import com.tan.entity.EntityResult;
import com.tan.utils.JwtUtils;
import com.tan.utils.Md5Util;
import com.tan.utils.UserThreadLocal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.tan.utils.RedisConstants.REDIS_USER;

@Slf4j
@Service
public class ServiceUserImpl implements ServiceUser {
    
    @Autowired
    private MapperUser mapperUser;

    @Resource
    private MapperSpace mapperSpace;
    
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

        if (!captcha.equals(loginDTO.getCode())){
            return EntityResult.error("验证码错误");
        }

        //登录成功,生成token
        Map<String, Object> claims  =new HashMap<>();
        claims.put("username", username);
        claims.put("userId", user.getUserId().toString());
        claims.put("email", user.getEmail());


        String token = JwtUtils.generateJwt(claims);

        //存入redis
        stringRedisTemplate.opsForValue().set(REDIS_USER + user.getUserId(), token,1000, TimeUnit.MINUTES);
//        log.info("登录成功:{}",token);
        return EntityResult.success(token);
    }

    /**
     * 注册
     * @param registerDTO
     * @return
     */
    @Override
    public EntityResult register(RegisterDTO registerDTO) {


        String email = registerDTO.getEmail();
        String username = registerDTO.getUsername();

        //查询该用户
        EntityUser user =  mapperUser.getUserByEmail(email);
        //存在
        if (Objects.nonNull(user)){
            return EntityResult.error("当前用户已经存在");
        }
        //查询该用户
        user =  mapperUser.getUserByUsername(username);
        //存在
        if (Objects.nonNull(user)){
            return EntityResult.error("当前用户名已经存在");
        }


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
        user1.setUpdateTime(LocalDateTime.now());

        mapperUser.save(user1);
        //返回主键值
        //log.info("返回主键:{}",user1.getUserId());
        //此时需要为用户生成一个默认大小为1GB的存储空间
        EntitySpace space = new EntitySpace().builder()
                .fileCount(0)
                .totalSpace(1L)
                .usedSpace(0L)
                .userId(user1.getUserId())
                .build();
        mapperSpace.create(space);

        return EntityResult.success();

    }

    /**
     * 获取用户信息
     * @return
     */
    @Override
    public EntityResult getUserInfo() {
        //直接返回userHold
        EntityUser user = UserThreadLocal.get();
        return EntityResult.success(user);
    }

    /**
     * 获取用户列表
     * @return
     */
    @Override
    public EntityResult list() {
        //管理员可以看到全部的用户-->普通用户只能看到自己的
        EntityUser user = UserThreadLocal.get();
//        if(user.getIsAdmin().equals(1)){
//            log.info("is_admin:{}",user.getIsAdmin());
//            return EntityResult.success(mapperUser.list());
//        }else{
//            List<EntityUser> list = new ArrayList<>();
//            list.add(user);
//            return EntityResult.success(list);
//        }
        return EntityResult.success(mapperUser.list());
    }

    /**
     * 添加用户
     * @param saveUserDTO
     * @return
     */
    @Override
    public EntityResult save(@RequestBody SaveUserDTO saveUserDTO) {
        mapperUser.add(saveUserDTO);
        return EntityResult.success();
    }

    /**
     * 更新用户信息
     * @param updateUserDTO
     * @return
     */
    @Override
    public EntityResult update(UpdateUserDTO updateUserDTO) {
        mapperUser.update(updateUserDTO);
        return EntityResult.success();
    }

    /**
     * 删除用户
     *
     *
     * 管理员能删所有的
     *
     * 普通用户只能删除自己的
     * @param id
     * @return
     */
    @Override
    public EntityResult deleteById(Integer id) {

        //获取当前用户id
        EntityUser user = UserThreadLocal.get();
        Integer userId = user.getUserId();


        //判断是否是管理员
        Integer isAdmin = user.getIsAdmin();

        if(isAdmin.equals(1)||userId.equals(id)){
            //删除用户还得把redis信息删除了
            String redisToken = stringRedisTemplate.opsForValue().get("user" + id);
            if(redisToken != null){
                stringRedisTemplate.delete(REDIS_USER+id);
                mapperUser.delete(id);
                return EntityResult.success("none");
            }
            mapperUser.delete(id);
            return EntityResult.success();
        }else{
            return EntityResult.error("没有权限");
        }


    }

    /**
     * 退出登录
     * @return
     */
    @Override
    public EntityResult logout() {
        //获取当前用户
        EntityUser user = UserThreadLocal.get();
        //删除redis
        log.info("rediscode:{}",REDIS_USER+user.getUserId());
        stringRedisTemplate.delete(REDIS_USER+user.getUserId());

        UserThreadLocal.remove();
        return EntityResult.success();
    }
}
