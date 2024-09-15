package com.tan.mapper;

import com.tan.dto.SaveFileDTO;
import com.tan.dto.UpdateFileDTO;
import com.tan.entity.EntityFile;
import com.tan.entity.EntityResult;
import com.tan.vo.FileListVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MapperFile {
    List<FileListVO> list(Integer userId, String fileType, String fileName);

    @Insert("insert into file (file_name, file_type, user_id, file_size, upload_time, file_path) VALUE " +
            "(#{fileName},#{fileType},#{userId},#{fileSize},#{uploadTime},#{filePath})")
    void save(EntityFile entityFile);

    @Delete("delete from file where file_id=#{fileId}")
    void deleteById(Integer fileId);

    @Select("select * from file where file_id=#{fileId} and user_id=#{userId}")
    EntityFile getById(Integer fileId,Integer userId);

    void update(UpdateFileDTO updateFileDTO);
}

