package com.tan.mapper;

import com.tan.dto.SaveFileDTO;
import com.tan.entity.EntityFile;
import com.tan.entity.EntityResult;
import com.tan.vo.FileListVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapperFile {
    List<FileListVO> list(Integer userId, String fileType, String fileName);

    @Insert("insert into file (file_name, file_type, user_id, file_size, upload_time, file_path) VALUE " +
            "(#{fileName},#{fileType},#{userId},#{fileSize},#{uploadTime},#{filePath})")
    void save(EntityFile entityFile);
}

