package com.tan.mapper;

import com.tan.entity.EntityFile;
import com.tan.vo.FileListVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MapperFile {
    List<FileListVO> list(Integer userId, String fileType, String fileName,Integer spaceId);

    @Options(useGeneratedKeys = true,keyProperty = "fileId")
    @Insert("insert into file (file_name, file_type, user_id, file_size, update_time,file_path,space_id) VALUE " +
            "(#{fileName},#{fileType},#{userId},#{fileSize},#{updateTime},#{filePath},#{spaceId})")
    void save(EntityFile entityFile);

    @Delete("delete from file where file_id=#{fileId}")
    void deleteById(Integer fileId);

    @Select("select * from file where file_id=#{fileId}")
    EntityFile getById(Integer fileId);


    void update(EntityFile updateFileDTO);

    @Select("select file_type from file where space_id=#{spaceId}")
    List<String> getFileTypes(Integer spaceId);
}

