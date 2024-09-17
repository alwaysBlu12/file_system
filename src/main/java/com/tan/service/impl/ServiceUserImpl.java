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
import com.tan.vo.UserListVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.*;

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
        stringRedisTemplate.opsForValue().set(REDIS_USER + user.getUserId(), token);
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

//        //查询该用户
//        EntityUser user =  mapperUser.getUserByEmail(email);
//        //存在
//        if (Objects.nonNull(user)){
//            return EntityResult.error("当前用户已经存在");
//        }

        //这里为了测试,一个邮箱可以注册多个

        //查询该用户
        EntityUser user =  mapperUser.getUserByUsername(username);
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
        createSpace(user1.getUserId());

        return EntityResult.success();

    }

    /**
     * 创建个人空间
     * @param userId
     */
    private void createSpace(Integer userId) {
        //此时需要为用户生成一个默认大小为100Mb的存储空间-->
        EntitySpace space = new EntitySpace().builder()
                .fileCount(0)
                //统一单位是bit,100MB=1,000,000bit
                .totalSpace(1000000L)
                .usedSpace(0L)
                .userId(userId)
                .build();
        mapperSpace.create(space);
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
        List<EntityUser> list = mapperUser.list();
        //List<UserListVO> userListVOS = new ArrayList<>();
//        for (EntityUser entityUser : list) {
//            //获取userId
//            Integer userId = entityUser.getUserId();
//            //获取空间信息
//            EntitySpace entitySpace = mapperSpace.getByUserId(userId);
//            UserListVO userListVO = new UserListVO();
//            BeanUtils.copyProperties(entityUser,userListVO);
//
//            //计算
//            Long totalSpace = entitySpace.getTotalSpace();
//            Long usedSpace = entitySpace.getUsedSpace();
//
//            userListVO.setPercent(1.0*usedSpace/totalSpace*100);
//            userListVO.setFileCount(entitySpace.getFileCount());
//            userListVOS.add(userListVO);
//
//        }
        return EntityResult.success(list);
    }

    /**
     * 添加用户
     * @param saveUserDTO
     * @return
     */
    @Override
    public EntityResult save(@RequestBody SaveUserDTO saveUserDTO) {
        EntityUser user = new EntityUser();
        BeanUtils.copyProperties(saveUserDTO,user);
        mapperUser.add(user);
        //也需要分配一个空间
        createSpace(user.getUserId());
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
