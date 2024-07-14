package com.tan.service;

import com.tan.entity.PageBean;
import com.tan.vo.FileListVO;

public interface ServiceFile {
    PageBean<FileListVO> list(Integer currentPage, Integer pageSize, String fileType, String fileName);
}
