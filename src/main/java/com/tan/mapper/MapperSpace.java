package com.tan.mapper;

import com.tan.entity.EntitySpace;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MapperSpace {
    @Insert("insert into space (user_id, file_count, used_space, total_space) value " +
            "(#{userId},#{fileCount},#{usedSpace},#{totalSpace})")
    void create(EntitySpace entitySpace);

    @Select("select * from space where space_id=#{spaceId}")
    EntitySpace getBySpacecId(Integer spaceId);


    void update(EntitySpace fileSize);

    @Select("select * from space where user_id=#{userId}")
    List<EntitySpace> list(Integer userId);

    @Select("select * from space where space_id=#{spaceId}")
    EntitySpace getBySpaceId(Integer spaceId);

    @Insert("insert into space (space_name, description, user_id, file_count, used_space, total_space, create_time) value " +
            "(#{spaceName},#{description},#{userId},#{fileCount},#{usedSpace},#{totalSpace},#{createTime})")
    void save(EntitySpace entitySpace);

    /**
     * 当前空间文件数-1
     * @param currentSpaceId
     */
    @Update("update space set file_count=file_count-1,used_space=used_space-#{fileByte} where space_id=#{currentSpaceId}")
    void subFileCountAndSpace(Integer currentSpaceId,Long fileByte);

    /**
     * 当前空间文件数-1
     * @param updateSpaceId
     */
    @Update("update space set file_count=file_count+1,used_space=used_space+#{fileByte} where space_id=#{updateSpaceId}")
    void addFileCountAndSpace(Integer updateSpaceId,Long fileByte);

    @Delete("delete from space where space_id=#{spaceId}")
    void deleteById(Integer spaceId);
}
