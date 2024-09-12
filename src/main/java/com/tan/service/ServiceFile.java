package com.tan.service;

import com.tan.dto.SaveFileDTO;
import com.tan.entity.EntityResult;
import com.tan.entity.PageBean;
import com.tan.vo.FileListVO;

public interface ServiceFile {
    PageBean<FileListVO> list(Integer currentPage, Integer pageSize, String fileType, String fileName);

    /**
     * 存储文件信息
     * @param saveFileDTO
     * @return
     */
    EntityResult save(SaveFileDTO saveFileDTO);
}
