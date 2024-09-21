package com.tan.mapper;

import com.tan.dto.PageFileDTO;
import com.tan.entity.EntityFile;
import com.tan.vo.FileListVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MapperFile {
    List<FileListVO> list(PageFileDTO pageFileDTO);

    @Options(useGeneratedKeys = true,keyProperty = "fileId")
    @Insert("insert into file (file_name, file_type, user_id, file_size, update_time,file_path,space_id) VALUE " +
            "(#{fileName},#{fileType},#{userId},#{fileSize},#{updateTime},#{filePath},#{spaceId})")
    void save(EntityFile entityFile);


    /**
     * 永久删除
     * @param fileId
     */
    @Delete("delete from file where file_id=#{fileId}")
    void deleteById(Integer fileId);

    @Select("select * from file where file_id=#{fileId}")
    EntityFile getById(Integer fileId);


    void update(EntityFile entityFile);

    @Select("select file_type from file where space_id=#{spaceId} and is_delete=0")
    List<String> getFileTypes(Integer spaceId);

    /**
     * 逻辑删除
     * @param fileId
     */
    @Update("update file set is_delete=1,update_time=now() where file_id=#{fileId}")
    void deleteLogic(Integer fileId);

    @Select("select file_type from file where is_delete=1")
    List<String> getRecycleFileTypes();

    /**
     * 恢复文件
     * @param fileId
     */
    @Update("update file set is_delete=0 where file_id=#{fileId}")
    void recycleFile(Integer fileId);
}

