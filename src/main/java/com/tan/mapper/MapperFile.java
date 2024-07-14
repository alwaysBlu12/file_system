package com.tan.mapper;

import com.tan.vo.FileListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapperFile {
    List<FileListVO> list(Integer userId, String fileType, String fileName);
}

