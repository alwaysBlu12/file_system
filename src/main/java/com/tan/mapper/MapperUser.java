package com.tan.mapper;

import com.tan.entity.EntityUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.GetMapping;

@Mapper
public interface MapperUser {

    @Insert("insert into user (username, email, password, avatar_url, update_time) value (#{username},#{email},#{password},#{avatarUrl},now())")
    void save(EntityUser user1);

    @Select("select * from user where username = #{username}")
    EntityUser getUserByUsername(String username);
}
