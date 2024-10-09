package com.tan.mapper;

import com.tan.entity.EntityFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MapperGlobal {
    @Select("select * from file")
    List<EntityFile> list();
}
