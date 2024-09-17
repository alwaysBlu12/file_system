package com.tan.mapper;

import com.tan.entity.EntitySpace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MapperSpace {
    @Insert("insert into space (user_id, file_count, used_space, total_space) value " +
            "(#{userId},#{fileCount},#{usedSpace},#{totalSpace})")
    void create(EntitySpace entitySpace);

    @Select("select * from space where space_id=#{spaceId}")
    EntitySpace getBySpacecId(Integer spaceId);

    @Update("update space set file_count=#{fileCount},used_space=#{usedSpace} where user_id=#{userId}")
    void update(EntitySpace fileSize);

    @Select("select * from space where user_id=#{userId}")
    List<EntitySpace> list(Integer userId);

    @Select("select * from space where space_id=#{spaceId}")
    EntitySpace getBySpaceId(Integer spaceId);

    @Insert("insert into space (name, description, user_id, file_count, used_space, total_space, create_time) value " +
            "(#{name},#{description},#{userId},#{fileCount},#{usedSpace},#{totalSpace},#{createTime})")
    void save(EntitySpace entitySpace);
}
