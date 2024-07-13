package com.tan.mapper;

import com.tan.dto.SaveUserDTO;
import com.tan.dto.UpdateUserDTO;
import com.tan.entity.EntityUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MapperUser {

    @Insert("insert into user (username, email, password, avatar_url, update_time) value (#{username},#{email},#{password},#{avatarUrl},now())")
    void save(EntityUser user1);

    @Select("select * from user where username = #{username}")
    EntityUser getUserByUsername(String username);

    @Select("select * from user")
    List<EntityUser> list();

    @Insert("insert into user (username,email,update_time) value (#{username},#{email},now())")
    void add(SaveUserDTO saveUserDTO);

    @Update("update user set username=#{username},email=#{email},update_time=now() where user_id = #{userId}")
    void update(UpdateUserDTO updateUserDTO);

    @Select("select * from user where user_id=#{userId}")
    EntityUser getUserById(Integer userId);
    @Delete("delete from user where user_id=#{id}")
    void delete(Integer id);
}
