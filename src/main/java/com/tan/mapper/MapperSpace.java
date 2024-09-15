package com.tan.mapper;

import com.tan.entity.EntitySpace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MapperSpace {
    @Insert("insert into user_file_space (user_id, file_count, used_space, total_space) value " +
            "(#{userId},#{fileCount},#{usedSpace},#{totalSpace})")
    void create(EntitySpace entitySpace);
}
